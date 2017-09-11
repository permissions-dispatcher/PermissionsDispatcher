package permissions.dispatcher.processor.impl.kotlin

import com.squareup.kotlinpoet.ARRAY
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.KotlinFile
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.processor.KtProcessorUnit
import permissions.dispatcher.processor.RequestCodeProvider
import permissions.dispatcher.processor.RuntimePermissionsElement
import permissions.dispatcher.processor.util.FILE_COMMENT
import permissions.dispatcher.processor.util.addFunctions
import permissions.dispatcher.processor.util.addProperties
import permissions.dispatcher.processor.util.addTypes
import permissions.dispatcher.processor.util.pendingRequestFieldName
import permissions.dispatcher.processor.util.permissionFieldName
import permissions.dispatcher.processor.util.permissionRequestTypeName
import permissions.dispatcher.processor.util.permissionValue
import permissions.dispatcher.processor.util.requestCodeFieldName
import permissions.dispatcher.processor.util.simpleString
import permissions.dispatcher.processor.util.varargsKtParametersCodeBlock
import permissions.dispatcher.processor.util.withCheckMethodName
import java.util.*
import javax.lang.model.element.ExecutableElement

/**
 * Base class for [KtProcessorUnit] implementations.
 * <p>
 * This generates the parts of code independent from specific permission method signatures for different target objects.
 */
abstract class KotlinBaseProcessorUnit : KtProcessorUnit {

    protected val PERMISSION_UTILS = ClassName.bestGuess("permissions.dispatcher.PermissionUtils")
    private val BUILD = ClassName.bestGuess("android.os.Build")
    private val MANIFEST_WRITE_SETTING = "android.permission.WRITE_SETTINGS"
    private val MANIFEST_SYSTEM_ALERT_WINDOW = "android.permission.SYSTEM_ALERT_WINDOW"
    private val INT_ARRAY = ClassName("kotlin", "IntArray")
    private val ADD_WITH_CHECK_BODY_MAP = hashMapOf(MANIFEST_SYSTEM_ALERT_WINDOW to SystemAlertWindowHelper(), MANIFEST_WRITE_SETTING to WriteSettingsHelper())

    /* Begin abstract */

    abstract fun addRequestPermissionsStatement(builder: FunSpec.Builder, targetParam: String = "this", permissionField: String, requestCodeField: String)

    abstract fun addShouldShowRequestPermissionRationaleCondition(builder: FunSpec.Builder, permissionField: String, isPositiveCondition: Boolean = true)

    abstract fun getActivityName(): String

    override fun createFile(rpe: RuntimePermissionsElement, requestCodeProvider: RequestCodeProvider): KotlinFile {
        return KotlinFile.builder(rpe.packageName, rpe.generatedClassName)
                .addFileComment(FILE_COMMENT)
                .addFileAnnotation(createFileAnnotation(rpe))
                .addProperties(createProperties(rpe.needsElements, requestCodeProvider))
                .addFunctions(createWithCheckFuns(rpe))
                .addFunctions(createPermissionHandlingFuns(rpe))
                .addTypes(createPermissionRequestClasses(rpe))
                .build()
    }

    /* Begin private */

    private fun createFileAnnotation(rpe: RuntimePermissionsElement): AnnotationSpec =
            AnnotationSpec.builder(JvmName::class)
                    .addMember("name", "\"${rpe.generatedClassName}\"")
                    .build()

    private fun createProperties(needsElements: List<ExecutableElement>, requestCodeProvider: RequestCodeProvider): List<PropertySpec> {
        val properties: ArrayList<PropertySpec> = arrayListOf()
        // The Set of annotated elements needs to be ordered
        // in order to achieve Deterministic, Reproducible Builds
        needsElements.sortedBy { it.simpleString() }.forEach {
            // For each method annotated with @NeedsPermission, add REQUEST integer and PERMISSION String[] fields
            properties.add(createRequestCodeProperty(it, requestCodeProvider.nextRequestCode()))
            properties.add(createPermissionProperty(it))
            if (it.parameters.isNotEmpty()) {
                properties.add(createPendingRequestProperty(it))
            }
        }
        return properties
    }

    private fun createRequestCodeProperty(e: ExecutableElement, index: Int): PropertySpec {
        return PropertySpec.builder(requestCodeFieldName(e), Int::class.java, KModifier.PRIVATE)
                .initializer("%L", index)
                .build()
    }

    private fun createPermissionProperty(e: ExecutableElement): PropertySpec {
        val permissionValue: List<String> = e.getAnnotation(NeedsPermission::class.java).permissionValue()
        val formattedValue: String = permissionValue.joinToString(
                separator = ", ",
                transform = { "\"$it\"" }
        )
        val parameterType = ParameterizedTypeName.get(ARRAY, ClassName("kotlin", "String"))
        return PropertySpec.builder(permissionFieldName(e), parameterType, KModifier.PRIVATE)
                .initializer("%N", "arrayOf($formattedValue)")
                .build()
    }

    private fun createPendingRequestProperty(e: ExecutableElement): PropertySpec {
        return PropertySpec
                .builder(pendingRequestFieldName(e), ClassName.bestGuess("permissions.dispatcher.GrantableRequest"), KModifier.PRIVATE)
                .build()
    }

    private fun createWithCheckFuns(rpe: RuntimePermissionsElement): List<FunSpec> {
        val methods: ArrayList<FunSpec> = arrayListOf()
        rpe.needsElements.forEach {
            // For each @NeedsPermission method, create the "WithCheck" equivalent
            methods.add(createWithCheckFun(rpe, it))
        }
        return methods
    }

    private fun createWithCheckFun(rpe: RuntimePermissionsElement, method: ExecutableElement): FunSpec {
        val builder = FunSpec.builder(withCheckMethodName(method))
                .addTypeVariables(rpe.ktTypeVariables)
                .receiver(rpe.ktTypeName)

        // If the method has parameters, add those as well
        method.parameters.forEach {
            builder.addParameter(it.simpleString(), it.asType().asTypeName())
        }

        // Delegate method body generation to implementing classes
        addWithCheckBody(builder, method, rpe)
        return builder.build()
    }

    fun addWithCheckBody(builder: FunSpec.Builder, needsMethod: ExecutableElement, rpe: RuntimePermissionsElement) {
        // Create field names for the constants to use
        val requestCodeField = requestCodeFieldName(needsMethod)
        val permissionField = permissionFieldName(needsMethod)

        // if maxSdkVersion is lower than os level does nothing
        val maxSdkVersion = needsMethod.getAnnotation(NeedsPermission::class.java).maxSdkVersion
        if (maxSdkVersion > 0) {
            builder.beginControlFlow("if (%T.VERSION.SDK_INT > %L)", BUILD, maxSdkVersion)
                    .addCode(CodeBlock.builder()
                            .add("%N(", needsMethod.simpleString())
                            .add(varargsKtParametersCodeBlock(needsMethod))
                            .addStatement(")")
                            .addStatement("return")
                            .build())
                    .endControlFlow()
        }

        // Add the conditional for when permission has already been granted
        val needsPermissionParameter = needsMethod.getAnnotation(NeedsPermission::class.java).value[0]
        val activity = getActivityName()
        ADD_WITH_CHECK_BODY_MAP[needsPermissionParameter]?.addHasSelfPermissionsCondition(builder, activity, permissionField)
                ?: builder.beginControlFlow("if (%T.hasSelfPermissions(%N, *%N))", PERMISSION_UTILS, activity, permissionField)
        builder.addCode(CodeBlock.builder()
                .add("%N(", needsMethod.simpleString())
                .add(varargsKtParametersCodeBlock(needsMethod))
                .addStatement(")")
                .build()
        )
        builder.nextControlFlow("else")

        // Add the conditional for "OnShowRationale", if present
        val onRationale = rpe.findOnRationaleForNeeds(needsMethod)
        val hasParameters = needsMethod.parameters.isNotEmpty()
        if (hasParameters) {
            // If the method has parameters, precede the potential OnRationale call with
            // an instantiation of the temporary Request object
            val varargsCall = CodeBlock.builder()
                    .add("%N = %N(this, ",
                            pendingRequestFieldName(needsMethod),
                            permissionRequestTypeName(rpe, needsMethod)
                    )
                    .add(varargsKtParametersCodeBlock(needsMethod))
                    .addStatement(")")
            builder.addCode(varargsCall.build())
        }
        if (onRationale != null) {
            addShouldShowRequestPermissionRationaleCondition(builder, permissionField)
            if (hasParameters) {
                // For methods with parameters, use the PermissionRequest instantiated above
                builder.addStatement("%N(%N)", onRationale.simpleString(), pendingRequestFieldName(needsMethod))
            } else {
                // Otherwise, create a new PermissionRequest on-the-fly
                builder.addStatement("%N(%N(this))", onRationale.simpleString(), permissionRequestTypeName(rpe, needsMethod))
            }
            builder.nextControlFlow("else")
        }

        // Add the branch for "request permission"
        ADD_WITH_CHECK_BODY_MAP[needsPermissionParameter]?.addRequestPermissionsStatement(builder, activity, requestCodeField)
                ?: addRequestPermissionsStatement(builder = builder, permissionField = permissionField, requestCodeField = requestCodeField)
        if (onRationale != null) {
            builder.endControlFlow()
        }
        builder.endControlFlow()
    }

    private fun createPermissionHandlingFuns(rpe: RuntimePermissionsElement): List<FunSpec> {
        val methods: ArrayList<FunSpec> = arrayListOf()

        if (hasNormalPermission(rpe)) {
            methods.add(createPermissionResultFun(rpe))
        }

        if (hasSystemAlertWindowPermission(rpe) || hasWriteSettingPermission(rpe)) {
            methods.add(createOnActivityResultFun(rpe))
        }

        return methods
    }

    private fun createOnActivityResultFun(rpe: RuntimePermissionsElement): FunSpec {
        val requestCodeParam = "requestCode"
        val grantResultsParam = "grantResults"
        val builder = FunSpec.builder("onActivityResult")
                .addTypeVariables(rpe.ktTypeVariables)
                .addParameter(requestCodeParam, INT)

        builder.beginControlFlow("when (%N)", requestCodeParam)
        for (needsMethod in rpe.needsElements) {
            val needsPermissionParameter = needsMethod.getAnnotation(NeedsPermission::class.java).value[0]
            if (!ADD_WITH_CHECK_BODY_MAP.containsKey(needsPermissionParameter)) {
                continue
            }

            builder.addCode("%N ->\n", requestCodeFieldName(needsMethod))

            addResultCaseBody(builder, needsMethod, rpe, grantResultsParam)
        }

        builder.endControlFlow()
        return builder.build()
    }

    private fun createPermissionResultFun(rpe: RuntimePermissionsElement): FunSpec {
        val requestCodeParam = "requestCode"
        val grantResultsParam = "grantResults"
        val builder = FunSpec.builder("onRequestPermissionsResult")
                .addTypeVariables(rpe.ktTypeVariables)
                .receiver(rpe.ktTypeName)
                .addParameter(requestCodeParam, INT)
                .addParameter(grantResultsParam, INT_ARRAY)

        // For each @NeedsPermission method, add a switch case
        builder.beginControlFlow("when (%N)", requestCodeParam)
        for (needsMethod in rpe.needsElements) {
            val needsPermissionParameter = needsMethod.getAnnotation(NeedsPermission::class.java).value[0]
            if (ADD_WITH_CHECK_BODY_MAP.containsKey(needsPermissionParameter)) {
                continue
            }

            builder.addCode("%N ->\n", requestCodeFieldName(needsMethod))
            // Delegate switch-case generation to implementing classes
            addResultCaseBody(builder, needsMethod, rpe, grantResultsParam)
        }

        // Add the default case
        builder.endControlFlow()
        return builder.build()
    }


    private fun addResultCaseBody(builder: FunSpec.Builder, needsMethod: ExecutableElement, rpe: RuntimePermissionsElement, grantResultsParam: String) {

        // just workaround, see https://github.com/hotchemi/PermissionsDispatcher/issues/45
        val onDenied: ExecutableElement? = rpe.findOnDeniedForNeeds(needsMethod)
        val hasDenied = onDenied != null
        val needsPermissionParameter = needsMethod.getAnnotation(NeedsPermission::class.java).value[0]
        val permissionField = permissionFieldName(needsMethod)

        // Add the conditional for "permission verified"
        val activity = getActivityName()
        ADD_WITH_CHECK_BODY_MAP[needsPermissionParameter]?.addHasSelfPermissionsCondition(builder, activity, permissionField)
                ?: builder.beginControlFlow("if (%T.verifyPermissions(*%N))", PERMISSION_UTILS, grantResultsParam)

        // Based on whether or not the method has parameters, delegate to the "pending request" object or invoke the method directly
        val hasParameters = needsMethod.parameters.isNotEmpty()
        if (hasParameters) {
            val pendingField = pendingRequestFieldName(needsMethod)
            builder.beginControlFlow("if (%N != null)", pendingField)
            builder.addStatement("%N.grant()", pendingField)
            builder.endControlFlow()
        } else {
            builder.addStatement("%N()", needsMethod.simpleString())
        }

        // Add the conditional for "permission denied" and/or "never ask again", if present
        val onNeverAsk: ExecutableElement? = rpe.findOnNeverAskForNeeds(needsMethod)
        val hasNeverAsk = onNeverAsk != null

        if (hasDenied || hasNeverAsk) {
            builder.nextControlFlow("else")
        }
        if (onNeverAsk != null) {
            // Split up the "else" case with another if condition checking for "never ask again" first
            addShouldShowRequestPermissionRationaleCondition(builder, permissionFieldName(needsMethod), false)
            builder.addStatement("%N()", onNeverAsk.simpleString())

            // If a "permission denied" is present as well, go into an else case, otherwise close this temporary branch
            if (hasDenied) {
                builder.nextControlFlow("else")
            } else {
                builder.endControlFlow()
            }
        }
        if (hasDenied) {
            // Add the "permissionDenied" statement
            builder.addStatement("%N()", onDenied!!.simpleString())
            // Close the additional control flow potentially opened by a "never ask again" method
            if (hasNeverAsk) {
                builder.endControlFlow()
            }
        }
        // Close the "switch" control flow
        builder.endControlFlow()

        // Remove the temporary pending request field, in case it was used for a method with parameters
        if (hasParameters) {
            builder.addStatement("%N = null", pendingRequestFieldName(needsMethod))
        }
    }

    private fun hasNormalPermission(rpe: RuntimePermissionsElement): Boolean {
        rpe.needsElements.forEach {
            val permissionValue: List<String> = it.getAnnotation(NeedsPermission::class.java).permissionValue()
            if (!permissionValue.contains(MANIFEST_SYSTEM_ALERT_WINDOW) && !permissionValue.contains(MANIFEST_WRITE_SETTING)) {
                return true
            }
        }
        return false
    }

    private fun hasSystemAlertWindowPermission(rpe: RuntimePermissionsElement): Boolean {
        return isDefinePermission(rpe, MANIFEST_SYSTEM_ALERT_WINDOW)
    }

    private fun hasWriteSettingPermission(rpe: RuntimePermissionsElement): Boolean {
        return isDefinePermission(rpe, MANIFEST_WRITE_SETTING)
    }

    private fun isDefinePermission(rpe: RuntimePermissionsElement, permissionName: String): Boolean {
        rpe.needsElements.forEach {
            val permissionValue: List<String> = it.getAnnotation(NeedsPermission::class.java).permissionValue()
            if (permissionValue.contains(permissionName)) {
                return true
            }
        }
        return false
    }

    private fun createPermissionRequestClasses(rpe: RuntimePermissionsElement): List<TypeSpec> {
        val classes: ArrayList<TypeSpec> = arrayListOf()
        rpe.needsElements.forEach {
            val onRationale = rpe.findOnRationaleForNeeds(it)
            if (onRationale != null || it.parameters.isNotEmpty()) {
                classes.add(createPermissionRequestClass(rpe, it))
            }
        }
        return classes
    }

    private fun createPermissionRequestClass(rpe: RuntimePermissionsElement, needsMethod: ExecutableElement): TypeSpec {
        // Select the superinterface of the generated class
        // based on whether or not the annotated method has parameters
        val hasParameters = needsMethod.parameters.isNotEmpty()
        val superInterfaceName = if (hasParameters) "GrantableRequest" else "PermissionRequest"

        val builder = TypeSpec.classBuilder(permissionRequestTypeName(rpe, needsMethod))
                .addTypeVariables(rpe.ktTypeVariables)
                .addSuperinterface(ClassName.bestGuess("permissions.dispatcher.$superInterfaceName"))
                .addModifiers(KModifier.PRIVATE)

        // Add required fields to the target
        val propName = "weakTarget"
        val parameterType = ParameterizedTypeName.get(ClassName("java.lang.ref", "WeakReference"), rpe.ktTypeName)
        val propertySpec = PropertySpec.builder(propName, parameterType, KModifier.PRIVATE)
        propertySpec.initializer("%N", "WeakReference(target)")
        builder.addProperty(propertySpec.build())

        needsMethod.parameters.forEach {
            builder.addProperty(it.simpleString(), it.asType().asTypeName(), KModifier.PRIVATE)
        }

        // Add constructor
        val targetParam = "target"
        val constructorSpec = FunSpec.constructorBuilder().addParameter(targetParam, rpe.ktTypeName)
        needsMethod.parameters.forEach {
            constructorSpec.addParameter(it.simpleString(), it.asType().asTypeName())
        }
        builder.primaryConstructor(constructorSpec.build())

        // Add proceed() override
        val proceedFun = FunSpec.builder("proceed")
                .addModifiers(KModifier.OVERRIDE)
                .addStatement("val target = %N.get() ?: return", propName)
        val requestCodeField = requestCodeFieldName(needsMethod)
        ADD_WITH_CHECK_BODY_MAP[needsMethod.getAnnotation(NeedsPermission::class.java).value[0]]?.addRequestPermissionsStatement(proceedFun, targetParam, requestCodeField)
                ?: addRequestPermissionsStatement(proceedFun, targetParam, permissionFieldName(needsMethod), requestCodeField)
        builder.addFun(proceedFun.build())

        // Add cancel() override method
        val cancelFun = FunSpec.builder("cancel")
                .addModifiers(KModifier.OVERRIDE)
        val onDenied = rpe.findOnDeniedForNeeds(needsMethod)
        if (onDenied != null) {
            cancelFun
                    .addStatement("val target = %N.get() ?: return", propName)
                    .addStatement("target.%N()", onDenied.simpleString())
        }
        builder.addFun(cancelFun.build())

        // For classes with additional parameters, add a "grant()" method
        if (hasParameters) {
            val grantFun = FunSpec.builder("grant")
                    .addModifiers(KModifier.OVERRIDE)
                    .addStatement("val target = %N.get() ?: return", propName)

            // Compose the call to the permission-protected method;
            // since the number of parameters is variable, utilize the low-level CodeBlock here
            // to compose the method call and its parameters
            grantFun.addCode(
                    CodeBlock.builder()
                            .add("target.%N(", needsMethod.simpleString())
                            .add(varargsKtParametersCodeBlock(needsMethod))
                            .addStatement(")")
                            .build()
            )
            builder.addFun(grantFun.build())
        }
        return builder.build()
    }
}