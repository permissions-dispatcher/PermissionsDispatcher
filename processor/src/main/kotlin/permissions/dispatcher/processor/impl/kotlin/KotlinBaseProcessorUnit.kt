package permissions.dispatcher.processor.impl.kotlin

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.plusParameter
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.processor.KtProcessorUnit
import permissions.dispatcher.processor.RequestCodeProvider
import permissions.dispatcher.processor.RuntimePermissionsElement
import permissions.dispatcher.processor.util.*
import javax.lang.model.element.ExecutableElement

/**
 * Base class for [KtProcessorUnit] implementations.
 * <p>
 * This generates the parts of code independent from specific permission method signatures for different target objects.
 */
abstract class KotlinBaseProcessorUnit : KtProcessorUnit {

    protected val permissionUtils = ClassName("permissions.dispatcher", "PermissionUtils")
    private val build = ClassName("android.os", "Build")
    private val intArray = ClassName("kotlin", "IntArray")
    private val weakReference = ClassName("java.lang.ref", "WeakReference")
    private val manifestWriteSettings = "android.permission.WRITE_SETTINGS"
    private val manifestSystemAlertWindow = "android.permission.SYSTEM_ALERT_WINDOW"
    private val addWithCheckBodyMap = hashMapOf(
            manifestSystemAlertWindow to SystemAlertWindowHelper(),
            manifestWriteSettings to WriteSettingsHelper())

    abstract fun addRequestPermissionsStatement(builder: FunSpec.Builder, targetParam: String = "this", permissionField: String, requestCodeField: String)

    abstract fun addShouldShowRequestPermissionRationaleCondition(builder: FunSpec.Builder, permissionField: String, isPositiveCondition: Boolean = true)

    abstract fun getActivityName(targetParam: String = "this"): String

    override fun createFile(rpe: RuntimePermissionsElement, requestCodeProvider: RequestCodeProvider): FileSpec {
        return FileSpec.builder(rpe.packageName, rpe.generatedClassName)
                .addComment(FILE_COMMENT)
                .addAnnotation(createJvmNameAnnotation(rpe.generatedClassName))
                .addProperties(createProperties(rpe, requestCodeProvider))
                .addFunctions(createWithPermissionCheckFuns(rpe))
                .addFunctions(createOnShowRationaleCallbackFuns(rpe))
                .addFunctions(createPermissionHandlingFuns(rpe))
                .addTypes(createPermissionRequestClasses(rpe))
                .build()
    }

    private fun createJvmNameAnnotation(generatedClassName: String): AnnotationSpec {
        return AnnotationSpec.builder(ClassName("", "JvmName"))
                .addMember("%S", generatedClassName)
                .build()
    }

    private fun createProperties(rpe: RuntimePermissionsElement, requestCodeProvider: RequestCodeProvider): List<PropertySpec> {
        val properties = arrayListOf<PropertySpec>()
        // The set of annotated elements needs to be ordered in order to achieve deterministic, reproducible builds
        rpe.needsElements.sortedBy { it.simpleString() }.forEach {
            properties.add(createRequestCodeProp(it, requestCodeProvider.nextRequestCode()))
            properties.add(createPermissionProperty(it))
            if (it.parameters.isNotEmpty()) {
                val hasOnRationaleParams = rpe.findOnRationaleForNeeds(it)?.parameters?.isNotEmpty()
                        ?: true
                if (hasOnRationaleParams) {
                    properties.add(createPendingRequestProperty(it))
                } else {
                    properties.addAll(createArgProps(it))
                }
            }
        }
        return properties
    }

    private fun createRequestCodeProp(e: ExecutableElement, index: Int): PropertySpec {
        return PropertySpec.builder(requestCodeFieldName(e), Int::class.java, KModifier.CONST, KModifier.PRIVATE)
                .initializer("%L", index)
                .build()
    }

    private fun createArgProps(method: ExecutableElement): List<PropertySpec> {
        return method.parameters.map {
            PropertySpec.builder(method.argumentFieldName(it), it.asPreparedType().copy(nullable = true), KModifier.PRIVATE)
                    .mutable()
                    .initializer(CodeBlock.of("null"))
                    .build()
        }
    }

    private fun createPermissionProperty(e: ExecutableElement): PropertySpec {
        val permissionValue = e.getAnnotation(NeedsPermission::class.java).permissionValue()
        val formattedValue = permissionValue.joinToString(
                separator = ", ",
                transform = { "\"$it\"" }
        )
        val parameterType = ARRAY.plusParameter(ClassName("kotlin", "String"))
        return PropertySpec.builder(permissionFieldName(e), parameterType, KModifier.PRIVATE)
                .initializer("arrayOf(%L)", formattedValue)
                .build()
    }

    private fun createPendingRequestProperty(e: ExecutableElement): PropertySpec {
        val type = ClassName("permissions.dispatcher", "GrantableRequest").copy(nullable = true)
        return PropertySpec
                .builder(pendingRequestFieldName(e), type, KModifier.PRIVATE)
                .mutable()
                .initializer(CodeBlock.of("null"))
                .build()
    }

    private fun createWithPermissionCheckFuns(rpe: RuntimePermissionsElement): List<FunSpec> {
        return rpe.needsElements.map { createWithPermissionCheckFun(rpe, it) }
    }

    private fun createWithPermissionCheckFun(rpe: RuntimePermissionsElement, method: ExecutableElement): FunSpec {
        val builder = FunSpec.builder(withPermissionCheckMethodName(method))
                .addOriginatingElement(rpe.element)
                .addTypeVariables(rpe.ktTypeVariables)
                .receiver(rpe.ktTypeName)
        if (method.enclosingElement.isInternal) {
            builder.addModifiers(KModifier.INTERNAL)
        }
        method.parameters.forEach {
            builder.addParameter(it.simpleString(), it.asPreparedType())
        }
        addWithPermissionCheckBody(builder, method, rpe)
        return builder.build()
    }

    private fun addWithPermissionCheckBody(builder: FunSpec.Builder, needsMethod: ExecutableElement, rpe: RuntimePermissionsElement) {
        // Create field names for the constants to use
        val requestCodeField = requestCodeFieldName(needsMethod)
        val permissionField = permissionFieldName(needsMethod)

        // if maxSdkVersion is lower than os level does nothing
        val maxSdkVersion = needsMethod.getAnnotation(NeedsPermission::class.java).maxSdkVersion
        if (maxSdkVersion > 0) {
            builder.beginControlFlow("if (%T.VERSION.SDK_INT > %L)", build, maxSdkVersion)
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
        addWithCheckBodyMap[needsPermissionParameter]?.addHasSelfPermissionsCondition(builder, activity, permissionField)
                ?: builder.beginControlFlow("if (%T.hasSelfPermissions(%L, *%N))", permissionUtils, activity, permissionField)
        builder.addCode(CodeBlock.builder()
                .add("%N(", needsMethod.simpleString())
                .add(varargsKtParametersCodeBlock(needsMethod))
                .addStatement(")")
                .build()
        )
        builder.nextControlFlow("else")

        // Add the conditional for "OnShowRationale", if present
        val onRationale = rpe.findOnRationaleForNeeds(needsMethod)
        val hasOnRationaleParams = onRationale?.parameters?.isNotEmpty() ?: true
        val hasParameters = needsMethod.parameters.isNotEmpty()
        if (hasParameters) {
            if (hasOnRationaleParams) {
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
            } else {
                needsMethod.parameters.forEach {
                    val code = CodeBlock.builder().addStatement("%N = %N", needsMethod.argumentFieldName(it), it.simpleString())
                    builder.addCode(code.build())
                }
            }
        }
        if (onRationale != null) {
            addShouldShowRequestPermissionRationaleCondition(builder, permissionField)
            if (hasParameters) {
                if (hasOnRationaleParams) {
                    // For methods with parameters, use the PermissionRequest instantiated above
                    builder.addStatement("%N?.let { %N(it) }", pendingRequestFieldName(needsMethod), onRationale.simpleString())
                } else {
                    builder.addStatement("%N()", onRationale.simpleString())
                }
            } else {
                if (hasOnRationaleParams) {
                    // Otherwise, create a new PermissionRequest on-the-fly
                    builder.addStatement("%N(%N(this))", onRationale.simpleString(), permissionRequestTypeName(rpe, needsMethod))
                } else {
                    builder.addStatement("%N()", onRationale.simpleString())
                }
            }
            builder.nextControlFlow("else")
        }

        addWithCheckBodyMap[needsPermissionParameter]?.addRequestPermissionsStatement(builder = builder, activityVar = getActivityName(), requestCodeField = requestCodeField)
                ?: addRequestPermissionsStatement(builder = builder, permissionField = permissionField, requestCodeField = requestCodeField)
        if (onRationale != null) {
            builder.endControlFlow()
        }
        builder.endControlFlow()
    }

    private fun createOnShowRationaleCallbackFuns(rpe: RuntimePermissionsElement): List<FunSpec> {
        val methods = arrayListOf<FunSpec>()
        rpe.needsElements.forEach {
            if (rpe.findOnRationaleForNeeds(it) != null) {
                val rationale = rpe.findOnRationaleForNeeds(it)
                val isRationaleParamsEmpty = rationale?.parameters?.isEmpty() ?: false
                if (isRationaleParamsEmpty) {
                    methods.add(createProceedPermissionRequestFun(rpe, it))
                    val onDenied = rpe.findOnDeniedForNeeds(it)
                    if (onDenied != null) {
                        methods.add(createCancelPermissionRequestFun(rpe, onDenied, it))
                    }
                }
            }
        }
        return methods
    }

    private fun createProceedPermissionRequestFun(rpe: RuntimePermissionsElement, needsMethod: ExecutableElement): FunSpec {
        val builder = FunSpec.builder(needsMethod.proceedOnShowRationaleMethodName())
                .addOriginatingElement(rpe.element)
                .addTypeVariables(rpe.ktTypeVariables)
                .receiver(rpe.ktTypeName)
        val targetParam = "this"
        val requestCodeField = requestCodeFieldName(needsMethod)
        addWithCheckBodyMap[needsMethod.getAnnotation(NeedsPermission::class.java).value[0]]?.addRequestPermissionsStatement(builder, targetParam, getActivityName(targetParam), requestCodeField)
                ?: addRequestPermissionsStatement(builder, targetParam, permissionFieldName(needsMethod), requestCodeField)
        return builder.build()
    }

    private fun createCancelPermissionRequestFun(rpe: RuntimePermissionsElement, onDenied: ExecutableElement, needsMethod: ExecutableElement): FunSpec {
        return FunSpec.builder(needsMethod.cancelOnShowRationaleMethodName())
                .addOriginatingElement(rpe.element)
                .addTypeVariables(rpe.ktTypeVariables)
                .receiver(rpe.ktTypeName)
                .addStatement("this.%N()", onDenied.simpleString())
                .build()
    }

    private fun createPermissionHandlingFuns(rpe: RuntimePermissionsElement): List<FunSpec> {
        val methods = arrayListOf<FunSpec>()
        if (hasNormalPermission(rpe)) {
            methods.add(createPermissionResultFun(rpe))
        }
        if (isDefinePermission(rpe, manifestSystemAlertWindow) || isDefinePermission(rpe, manifestWriteSettings)) {
            methods.add(createOnActivityResultFun(rpe))
        }
        return methods
    }

    private fun createOnActivityResultFun(rpe: RuntimePermissionsElement): FunSpec {
        val requestCodeParam = "requestCode"
        val grantResultsParam = "grantResults"
        val builder = FunSpec.builder("onActivityResult")
                .addOriginatingElement(rpe.element)
                .addTypeVariables(rpe.ktTypeVariables)
                .receiver(rpe.ktTypeName)
                .addParameter(requestCodeParam, INT)

        if (rpe.element.isInternal) {
            builder.addModifiers(KModifier.INTERNAL)
        }

        builder.beginControlFlow("when (%N)", requestCodeParam)
        for (needsMethod in rpe.needsElements) {
            val needsPermissionParameter = needsMethod.getAnnotation(NeedsPermission::class.java).value[0]
            if (!addWithCheckBodyMap.containsKey(needsPermissionParameter)) {
                continue
            }
            builder.beginControlFlow("%N ->", requestCodeFieldName(needsMethod))
            addResultCaseBody(builder, needsMethod, rpe, grantResultsParam)
            builder.endControlFlow()
        }

        builder.endControlFlow()
        return builder.build()
    }

    private fun createPermissionResultFun(rpe: RuntimePermissionsElement): FunSpec {
        val requestCodeParam = "requestCode"
        val grantResultsParam = "grantResults"
        val builder = FunSpec.builder("onRequestPermissionsResult")
                .addOriginatingElement(rpe.element)
                .addTypeVariables(rpe.ktTypeVariables)
                .receiver(rpe.ktTypeName)
                .addParameter(requestCodeParam, INT)
                .addParameter(grantResultsParam, intArray)

        if (rpe.element.isInternal) {
            builder.addModifiers(KModifier.INTERNAL)
        }

        builder.beginControlFlow("when (%N)", requestCodeParam)
        for (needsMethod in rpe.needsElements) {
            val needsPermissionParameter = needsMethod.getAnnotation(NeedsPermission::class.java).value[0]
            if (addWithCheckBodyMap.containsKey(needsPermissionParameter)) {
                continue
            }
            builder.beginControlFlow("%N ->\n", requestCodeFieldName(needsMethod))
            addResultCaseBody(builder, needsMethod, rpe, grantResultsParam)
            builder.endControlFlow()
        }
        builder.endControlFlow()
        return builder.build()
    }


    private fun addResultCaseBody(builder: FunSpec.Builder, needsMethod: ExecutableElement, rpe: RuntimePermissionsElement, grantResultsParam: String) {
        val onDenied = rpe.findOnDeniedForNeeds(needsMethod)
        val hasDenied = onDenied != null
        val needsPermissionParameter = needsMethod.getAnnotation(NeedsPermission::class.java).value[0]
        val permissionField = permissionFieldName(needsMethod)

        // Add the conditional for "permission verified"
        val activity = getActivityName()
        addWithCheckBodyMap[needsPermissionParameter]?.addHasSelfPermissionsCondition(builder, activity, permissionField)
                ?: builder.beginControlFlow("if (%T.verifyPermissions(*%N))", permissionUtils, grantResultsParam)

        val onRationale = rpe.findOnRationaleForNeeds(needsMethod)
        val hasOnRationaleParams = onRationale?.parameters?.isNotEmpty() ?: true
        val hasParameters = needsMethod.parameters.isNotEmpty()
        if (hasParameters) {
            if (hasOnRationaleParams) {
                val pendingField = pendingRequestFieldName(needsMethod)
                builder.addStatement("%N?.grant()", pendingField)
            } else {
                addWithCheckBodyMap[needsPermissionParameter]?.addHasSelfPermissionsCondition(builder, activity, permissionField)
                        ?: builder.beginControlFlow("if (%T.hasSelfPermissions(%L, *%N))", permissionUtils, activity, permissionField)
                builder.addCode(CodeBlock.builder()
                        .add("%N(", needsMethod.simpleString())
                        .add(varargsKtParametersCodeBlock(needsMethod, withCache = true))
                        .addStatement(")")
                        .build())
                builder.endControlFlow()
            }
        } else {
            builder.addStatement("%N()", needsMethod.simpleString())
        }

        // Add the conditional for "permission denied" and/or "never ask again", if present
        val onNeverAsk = rpe.findOnNeverAskForNeeds(needsMethod)
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
            if (hasOnRationaleParams) {
                builder.addStatement("%N = null", pendingRequestFieldName(needsMethod))
            } else {
                needsMethod.parameters.forEach {
                    builder.addStatement("%N = null", needsMethod.argumentFieldName(it))
                }
            }
        }
    }

    private fun hasNormalPermission(rpe: RuntimePermissionsElement): Boolean {
        rpe.needsElements.forEach {
            val permissionValue = it.getAnnotation(NeedsPermission::class.java).permissionValue()
            if (!permissionValue.contains(manifestSystemAlertWindow) && !permissionValue.contains(manifestWriteSettings)) {
                return true
            }
        }
        return false
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
        val classes = arrayListOf<TypeSpec>()
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
                .addOriginatingElement(rpe.element)
                .addTypeVariables(rpe.ktTypeVariables)
                .addSuperinterface(ClassName("permissions.dispatcher", superInterfaceName))
                .addModifiers(KModifier.PRIVATE)

        // Add required fields to the target
        val propName = "weakTarget"
        val parameterType = weakReference.plusParameter(rpe.ktTypeName)
        val propertySpec = PropertySpec.builder(propName, parameterType, KModifier.PRIVATE)
        propertySpec.initializer("%T(target)", weakReference)
        builder.addProperty(propertySpec.build())

        needsMethod.parameters.forEach {
            builder.addProperty(
                    PropertySpec.builder(it.simpleString(), it.asPreparedType(), KModifier.PRIVATE)
                            .initializer(CodeBlock.of(it.simpleString()))
                            .build()
            )
        }

        // Add constructor
        val targetParam = "target"
        val constructorSpec = FunSpec.constructorBuilder().addParameter(targetParam, rpe.ktTypeName)
        needsMethod.parameters.forEach {
            constructorSpec.addParameter(it.simpleString(), it.asPreparedType())
        }
        builder.primaryConstructor(constructorSpec.build())

        // Add proceed() override
        val proceedFun = FunSpec.builder("proceed")
                .addOriginatingElement(rpe.element)
                .addModifiers(KModifier.OVERRIDE)
                .addStatement("val target = %N.get() ?: return", propName)
        val requestCodeField = requestCodeFieldName(needsMethod)
        addWithCheckBodyMap[needsMethod.getAnnotation(NeedsPermission::class.java).value[0]]?.addRequestPermissionsStatement(proceedFun, targetParam, getActivityName(targetParam), requestCodeField)
                ?: addRequestPermissionsStatement(proceedFun, targetParam, permissionFieldName(needsMethod), requestCodeField)
        builder.addFunction(proceedFun.build())

        // Add cancel() override method
        val cancelFun = FunSpec.builder("cancel")
                .addOriginatingElement(rpe.element)
                .addModifiers(KModifier.OVERRIDE)
        val onDenied = rpe.findOnDeniedForNeeds(needsMethod)
        if (onDenied != null) {
            cancelFun
                    .addStatement("val target = %N.get() ?: return", propName)
                    .addStatement("target.%N()", onDenied.simpleString())
        }
        builder.addFunction(cancelFun.build())

        // For classes with additional parameters, add a "grant()" method
        if (hasParameters) {
            val grantFun = FunSpec.builder("grant")
                    .addOriginatingElement(rpe.element)
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
            builder.addFunction(grantFun.build())
        }
        return builder.build()
    }
}
