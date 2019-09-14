package permissions.dispatcher.processor.impl.java

import androidx.annotation.NonNull
import com.squareup.javapoet.ArrayTypeName
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.processor.JavaProcessorUnit
import permissions.dispatcher.processor.RequestCodeProvider
import permissions.dispatcher.processor.RuntimePermissionsElement
import permissions.dispatcher.processor.util.*
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier

/**
 * Base class for [JavaProcessorUnit] implementations.
 * <p>
 * This generates the parts of code independent from specific permission method signatures for different target objects.
 */
abstract class JavaBaseProcessorUnit : JavaProcessorUnit {

    protected val permissionUtils: ClassName = ClassName.get("permissions.dispatcher", "PermissionUtils")
    private val grantableRequest = ClassName.get("permissions.dispatcher", "GrantableRequest")
    private val build = ClassName.get("android.os", "Build")
    private val writeSettings = "android.permission.WRITE_SETTINGS"
    private val systemAlertWindow = "android.permission.SYSTEM_ALERT_WINDOW"
    private val addWithCheckBodyMap = hashMapOf(
            systemAlertWindow to SystemAlertWindowHelper(),
            writeSettings to WriteSettingsHelper())

    final override fun createFile(rpe: RuntimePermissionsElement, requestCodeProvider: RequestCodeProvider): JavaFile {
        return JavaFile.builder(rpe.packageName, createTypeSpec(rpe, requestCodeProvider))
                .addFileComment(FILE_COMMENT)
                .build()
    }

    abstract fun addRequestPermissionsStatement(builder: MethodSpec.Builder, targetParam: String, permissionField: String, requestCodeField: String)

    abstract fun addShouldShowRequestPermissionRationaleCondition(builder: MethodSpec.Builder, targetParam: String, permissionField: String, isPositiveCondition: Boolean = true)

    abstract fun getActivityName(targetParam: String): String

    private fun createTypeSpec(rpe: RuntimePermissionsElement, requestCodeProvider: RequestCodeProvider): TypeSpec {
        return TypeSpec.classBuilder(rpe.generatedClassName)
                .addOriginatingElement(rpe.element) // for incremental annotation processing
                .addModifiers(Modifier.FINAL)
                .addFields(createFields(rpe, requestCodeProvider))
                .addMethod(createConstructor())
                .addMethods(createWithPermissionCheckMethods(rpe))
                .addMethods(createOnShowRationaleCallbackMethods(rpe))
                .addMethods(createPermissionHandlingMethods(rpe))
                .addTypes(createPermissionRequestClasses(rpe))
                .build()
    }

    private fun createFields(rpe: RuntimePermissionsElement, requestCodeProvider: RequestCodeProvider): List<FieldSpec> {
        val fields = arrayListOf<FieldSpec>()
        // The set of annotated elements needs to be ordered in order to achieve Deterministic, reproducible builds
        rpe.needsElements.sortedBy { it.simpleString() }.forEach {
            fields.add(createRequestCodeField(it, requestCodeProvider.nextRequestCode()))
            fields.add(createPermissionField(it))
            if (it.parameters.isNotEmpty()) {
                val hasOnRationaleParams = rpe.findOnRationaleForNeeds(it)?.parameters?.isNotEmpty()
                        ?: true
                if (hasOnRationaleParams) {
                    fields.add(createPendingRequestField(it))
                } else {
                    fields.addAll(createArgFields(it))
                }
            }
        }
        return fields
    }

    private fun createRequestCodeField(e: ExecutableElement, index: Int): FieldSpec {
        return FieldSpec.builder(Int::class.java, requestCodeFieldName(e))
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("\$L", index)
                .build()
    }

    private fun createPermissionField(e: ExecutableElement): FieldSpec {
        val permissionValue = e.getAnnotation(NeedsPermission::class.java).permissionValue()
        val formattedValue: String = permissionValue.joinToString(
                separator = ",",
                prefix = "{",
                postfix = "}",
                transform = { "\"$it\"" }
        )
        return FieldSpec.builder(ArrayTypeName.of(String::class.java), permissionFieldName(e))
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("\$N", "new String[] $formattedValue")
                .build()
    }

    private fun createPendingRequestField(e: ExecutableElement): FieldSpec {
        return FieldSpec.builder(grantableRequest, pendingRequestFieldName(e))
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .build()
    }

    private fun createArgFields(method: ExecutableElement): List<FieldSpec> {
        return method.parameters.map {
            FieldSpec.builder(typeNameOf(it), method.argumentFieldName(it))
                    .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                    .build()
        }
    }

    private fun createConstructor(): MethodSpec {
        return MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).build()
    }

    private fun createWithPermissionCheckMethods(rpe: RuntimePermissionsElement): List<MethodSpec> {
        return rpe.needsElements.map { createWithPermissionCheckMethod(rpe, it) }
    }

    private fun createWithPermissionCheckMethod(rpe: RuntimePermissionsElement, method: ExecutableElement): MethodSpec {
        val targetParam = "target"
        val builder = MethodSpec.methodBuilder(withPermissionCheckMethodName(method))
                .addTypeVariables(rpe.typeVariables)
                .addModifiers(Modifier.STATIC)
                .returns(TypeName.VOID)
                .addParameter(ParameterSpec.builder(rpe.typeName, targetParam).addAnnotation(NonNull::class.java).build())

        method.parameters.forEach {
            builder.addParameter(typeNameOf(it), it.simpleString())
        }
        addWithPermissionCheckBody(builder, method, rpe, targetParam)
        return builder.build()
    }

    private fun addWithPermissionCheckBody(builder: MethodSpec.Builder, needsMethod: ExecutableElement, rpe: RuntimePermissionsElement, targetParam: String) {
        // Create field names for the constants to use
        val requestCodeField = requestCodeFieldName(needsMethod)
        val permissionField = permissionFieldName(needsMethod)

        // if maxSdkVersion is lower than os level does nothing
        val needsPermissionMaxSdkVersion = needsMethod.getAnnotation(NeedsPermission::class.java).maxSdkVersion
        if (needsPermissionMaxSdkVersion > 0) {
            builder.beginControlFlow("if (\$T.VERSION.SDK_INT > \$L)", build, needsPermissionMaxSdkVersion)
                    .addCode(CodeBlock.builder()
                            .add("\$N.\$N(", targetParam, needsMethod.simpleString())
                            .add(varargsParametersCodeBlock(needsMethod))
                            .addStatement(")")
                            .addStatement("return")
                            .build())
                    .endControlFlow()
        }

        // Add the conditional for when permission has already been granted
        val needsPermissionParameter = needsMethod.getAnnotation(NeedsPermission::class.java).value[0]
        val activityVar = getActivityName(targetParam)
        addWithCheckBodyMap[needsPermissionParameter]?.addHasSelfPermissionsCondition(builder, activityVar, permissionField)
                ?: builder.beginControlFlow("if (\$T.hasSelfPermissions(\$N, \$N))", permissionUtils, activityVar, permissionField)
        builder.addCode(CodeBlock.builder()
                .add("\$N.\$N(", targetParam, needsMethod.simpleString())
                .add(varargsParametersCodeBlock(needsMethod))
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
                        .add("\$N = new \$N(\$N, ",
                                pendingRequestFieldName(needsMethod),
                                permissionRequestTypeName(rpe, needsMethod),
                                targetParam
                        )
                        .add(varargsParametersCodeBlock(needsMethod))
                        .addStatement(")")
                builder.addCode(varargsCall.build())
            } else {
                needsMethod.parameters.forEach {
                    val varargsCall = CodeBlock.builder().addStatement(
                            "\$N = \$N",
                            needsMethod.argumentFieldName(it),
                            it.simpleName)
                    builder.addCode(varargsCall.build())
                }
            }
        }
        if (onRationale != null) {
            addShouldShowRequestPermissionRationaleCondition(builder, targetParam, permissionField)
            if (hasParameters) {
                if (hasOnRationaleParams) {
                    // For methods with parameters, use the PermissionRequest instantiated above
                    builder.addStatement("\$N.\$N(\$N)", targetParam, onRationale.simpleString(), pendingRequestFieldName(needsMethod))
                } else {
                    builder.addStatement("\$N.\$N()", targetParam, onRationale.simpleString())
                }
            } else {
                if (hasOnRationaleParams) {
                    // Otherwise, create a new PermissionRequest on-the-fly
                    builder.addStatement("\$N.\$N(new \$N(\$N))", targetParam, onRationale.simpleString(), permissionRequestTypeName(rpe, needsMethod), targetParam)
                } else {
                    builder.addStatement("\$N.\$N()", targetParam, onRationale.simpleString())
                }
            }
            builder.nextControlFlow("else")
        }

        // Add the branch for "request permission"
        addWithCheckBodyMap[needsPermissionParameter]?.addRequestPermissionsStatement(builder, targetParam, activityVar, requestCodeField)
                ?: addRequestPermissionsStatement(builder, targetParam, permissionField, requestCodeField)
        if (onRationale != null) {
            builder.endControlFlow()
        }
        builder.endControlFlow()
    }

    private fun createPermissionHandlingMethods(rpe: RuntimePermissionsElement): List<MethodSpec> {
        val methods = arrayListOf<MethodSpec>()
        if (hasNormalPermission(rpe)) {
            methods.add(createPermissionResultMethod(rpe))
        }
        if (isDefinePermission(rpe, systemAlertWindow) || isDefinePermission(rpe, writeSettings)) {
            methods.add(createOnActivityResultMethod(rpe))
        }
        return methods
    }

    private fun createOnActivityResultMethod(rpe: RuntimePermissionsElement): MethodSpec {
        val targetParam = "target"
        val requestCodeParam = "requestCode"
        val grantResultsParam = "grantResults"
        val builder = MethodSpec.methodBuilder("onActivityResult")
                .addTypeVariables(rpe.typeVariables)
                .addModifiers(Modifier.STATIC)
                .returns(TypeName.VOID)
                .addParameter(ParameterSpec.builder(rpe.typeName, targetParam).addAnnotation(NonNull::class.java).build())
                .addParameter(TypeName.INT, requestCodeParam)

        builder.beginControlFlow("switch (\$N)", requestCodeParam)
        for (needsMethod in rpe.needsElements) {
            val needsPermissionParameter = needsMethod.getAnnotation(NeedsPermission::class.java).value[0]
            if (!addWithCheckBodyMap.containsKey(needsPermissionParameter)) {
                continue
            }

            builder.addCode("case \$N:\n", requestCodeFieldName(needsMethod))

            addResultCaseBody(builder, needsMethod, rpe, targetParam, grantResultsParam)
        }

        builder
                .addCode("default:\n")
                .addStatement("break")
                .endControlFlow()

        return builder.build()
    }

    private fun createPermissionResultMethod(rpe: RuntimePermissionsElement): MethodSpec {
        val targetParam = "target"
        val requestCodeParam = "requestCode"
        val grantResultsParam = "grantResults"
        val builder = MethodSpec.methodBuilder("onRequestPermissionsResult")
                .addTypeVariables(rpe.typeVariables)
                .addModifiers(Modifier.STATIC)
                .returns(TypeName.VOID)
                .addParameter(ParameterSpec.builder(rpe.typeName, targetParam).addAnnotation(NonNull::class.java).build())
                .addParameter(TypeName.INT, requestCodeParam)
                .addParameter(ArrayTypeName.of(TypeName.INT), grantResultsParam)

        // For each @NeedsPermission method, add a switch case
        builder.beginControlFlow("switch (\$N)", requestCodeParam)
        for (needsMethod in rpe.needsElements) {
            val needsPermissionParameter = needsMethod.getAnnotation(NeedsPermission::class.java).value[0]
            if (addWithCheckBodyMap.containsKey(needsPermissionParameter)) {
                continue
            }

            builder.addCode("case \$N:\n", requestCodeFieldName(needsMethod))

            // Delegate switch-case generation to implementing classes
            addResultCaseBody(builder, needsMethod, rpe, targetParam, grantResultsParam)
        }

        // Add the default case
        builder
                .addCode("default:\n")
                .addStatement("break")
                .endControlFlow()

        return builder.build()
    }

    private fun addResultCaseBody(builder: MethodSpec.Builder, needsMethod: ExecutableElement, rpe: RuntimePermissionsElement, targetParam: String, grantResultsParam: String) {
        val onDenied = rpe.findOnDeniedForNeeds(needsMethod)
        val hasDenied = onDenied != null
        val needsPermissionParameter = needsMethod.getAnnotation(NeedsPermission::class.java).value[0]
        val permissionField = permissionFieldName(needsMethod)

        // Add the conditional for "permission verified"
        addWithCheckBodyMap[needsPermissionParameter]?.addHasSelfPermissionsCondition(builder, getActivityName(targetParam), permissionField)
                ?: builder.beginControlFlow("if (\$T.verifyPermissions(\$N))", permissionUtils, grantResultsParam)

        // Based on whether or not the method has parameters, delegate to the "pending request" object or invoke the method directly
        val onRationale = rpe.findOnRationaleForNeeds(needsMethod)
        val hasOnRationaleParams = onRationale?.parameters?.isNotEmpty() ?: true
        val hasParameters = needsMethod.parameters.isNotEmpty()
        if (hasParameters) {
            if (hasOnRationaleParams) {
                val pendingField = pendingRequestFieldName(needsMethod)
                builder.beginControlFlow("if (\$N != null)", pendingField)
                builder.addStatement("\$N.grant()", pendingField)
                builder.endControlFlow()
            } else {
                builder.addCode(CodeBlock.builder()
                        .add("\$N.\$N(", targetParam, needsMethod.simpleString())
                        .add(varargsParametersCodeBlock(needsMethod, withCache = true))
                        .addStatement(")")
                        .build())
            }
        } else {
            builder.addStatement("target.\$N()", needsMethod.simpleString())
        }

        // Add the conditional for "permission denied" and/or "never ask again", if present
        val onNeverAsk = rpe.findOnNeverAskForNeeds(needsMethod)
        val hasNeverAsk = onNeverAsk != null

        if (hasDenied || hasNeverAsk) {
            builder.nextControlFlow("else")
        }
        if (hasNeverAsk) {
            // Split up the "else" case with another if condition checking for "never ask again" first
            addShouldShowRequestPermissionRationaleCondition(builder, targetParam, permissionFieldName(needsMethod), false)
            builder.addStatement("target.\$N()", onNeverAsk?.simpleString())
            // If a "permission denied" is present as well, go into an else case, otherwise close this temporary branch
            if (hasDenied) {
                builder.nextControlFlow("else")
            } else {
                builder.endControlFlow()
            }
        }
        if (hasDenied) {
            // Add the "permissionDenied" statement
            builder.addStatement("\$N.\$N()", targetParam, onDenied!!.simpleString())
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
                builder.addStatement("\$N = null", pendingRequestFieldName(needsMethod))
            } else {
                needsMethod.parameters.forEach {
                    if (!typeNameOf(it).isPrimitive) {
                        builder.addStatement("\$N = null", needsMethod.argumentFieldName(it))
                    }
                }
            }
        }
        builder.addStatement("break")
    }

    private fun hasNormalPermission(rpe: RuntimePermissionsElement): Boolean {
        rpe.needsElements.forEach {
            val permissionValue = it.getAnnotation(NeedsPermission::class.java).permissionValue()
            if (!permissionValue.contains(systemAlertWindow) && !permissionValue.contains(writeSettings)) {
                return true
            }
        }
        return false
    }

    private fun isDefinePermission(rpe: RuntimePermissionsElement, permissionName: String): Boolean {
        rpe.needsElements.forEach {
            val permissionValue = it.getAnnotation(NeedsPermission::class.java).permissionValue()
            if (permissionValue.contains(permissionName)) {
                return true
            }
        }
        return false
    }

    private fun createOnShowRationaleCallbackMethods(rpe: RuntimePermissionsElement): List<MethodSpec> {
        val methods = arrayListOf<MethodSpec>()
        rpe.needsElements.forEach {
            val rationale = rpe.findOnRationaleForNeeds(it)
            val isRationaleParamsEmpty = rationale?.parameters?.isEmpty() ?: false
            if (isRationaleParamsEmpty) {
                methods.add(createProceedPermissionRequestMethod(rpe, it))
                val onDenied = rpe.findOnDeniedForNeeds(it)
                if (onDenied != null) {
                    methods.add(createCancelPermissionRequestMethod(rpe, onDenied, it))
                }
            }
        }
        return methods
    }

    private fun createProceedPermissionRequestMethod(rpe: RuntimePermissionsElement, needsMethod: ExecutableElement): MethodSpec {
        val targetParam = "target"
        val builder = MethodSpec.methodBuilder(needsMethod.proceedOnShowRationaleMethodName())
                .addTypeVariables(rpe.typeVariables)
                .addModifiers(Modifier.STATIC)
                .returns(TypeName.VOID)
                .addParameter(ParameterSpec.builder(rpe.typeName, targetParam).addAnnotation(NonNull::class.java).build())
        val needsPermissionParameter = needsMethod.getAnnotation(NeedsPermission::class.java).value[0]
        val activityVar = getActivityName(targetParam)
        val requestCodeField = requestCodeFieldName(needsMethod)
        addWithCheckBodyMap[needsPermissionParameter]?.addRequestPermissionsStatement(builder, targetParam, activityVar, requestCodeField)
                ?: addRequestPermissionsStatement(builder, targetParam, permissionFieldName(needsMethod), requestCodeField)
        return builder.build()
    }

    private fun createCancelPermissionRequestMethod(rpe: RuntimePermissionsElement, onDenied: ExecutableElement, needsMethod: ExecutableElement): MethodSpec {
        return MethodSpec.methodBuilder(needsMethod.cancelOnShowRationaleMethodName())
                .addTypeVariables(rpe.typeVariables)
                .addModifiers(Modifier.STATIC)
                .returns(TypeName.VOID)
                .addParameter(ParameterSpec.builder(rpe.typeName, "target").addAnnotation(NonNull::class.java).build())
                .addStatement("target.\$N()", onDenied.simpleString())
                .build()
    }

    private fun createPermissionRequestClasses(rpe: RuntimePermissionsElement): List<TypeSpec> {
        val classes = arrayListOf<TypeSpec>()
        rpe.needsElements.forEach {
            val onRationale = rpe.findOnRationaleForNeeds(it)
            val hasOnRationaleParams = onRationale?.parameters?.isNotEmpty() ?: true
            if (hasOnRationaleParams && (onRationale != null || it.parameters.isNotEmpty())) {
                classes.add(createPermissionRequestClass(rpe, it))
            }
        }
        return classes
    }

    private fun createPermissionRequestClass(rpe: RuntimePermissionsElement, needsMethod: ExecutableElement): TypeSpec {
        // Select the superinterface of the generated class
        // based on whether or not the annotated method has parameters
        val hasParameters: Boolean = needsMethod.parameters.isNotEmpty()
        val superInterfaceName: String = if (hasParameters) "GrantableRequest" else "PermissionRequest"

        val targetType = rpe.typeName
        val builder = TypeSpec.classBuilder(permissionRequestTypeName(rpe, needsMethod))
                .addTypeVariables(rpe.typeVariables)
                .addSuperinterface(ClassName.get("permissions.dispatcher", superInterfaceName))
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)

        // Add required fields to the target
        val weakFieldName = "weakTarget"
        val weakFieldType = ParameterizedTypeName.get(ClassName.get("java.lang.ref", "WeakReference"), targetType)
        builder.addField(weakFieldType, weakFieldName, Modifier.PRIVATE, Modifier.FINAL)
        needsMethod.parameters.forEach {
            builder.addField(typeNameOf(it), it.simpleString(), Modifier.PRIVATE, Modifier.FINAL)
        }

        // Add constructor
        val targetParam = "target"
        val constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addParameter(ParameterSpec.builder(targetType, targetParam).addAnnotation(NonNull::class.java).build())
                .addStatement("this.\$L = new WeakReference<\$T>(\$N)", weakFieldName, targetType, targetParam)
        needsMethod.parameters.forEach {
            val fieldName = it.simpleString()
            constructorBuilder
                    .addParameter(typeNameOf(it), fieldName)
                    .addStatement("this.\$L = \$N", fieldName, fieldName)
        }
        builder.addMethod(constructorBuilder.build())

        // Add proceed() override
        val proceedMethod: MethodSpec.Builder = MethodSpec.methodBuilder("proceed")
                .addAnnotation(Override::class.java)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID)
                .addStatement("\$T target = \$N.get()", targetType, weakFieldName)
                .addStatement("if (target == null) return")
        val requestCodeField = requestCodeFieldName(needsMethod)
        addWithCheckBodyMap[needsMethod.getAnnotation(NeedsPermission::class.java).value[0]]?.addRequestPermissionsStatement(proceedMethod, targetParam, getActivityName(targetParam), requestCodeField)
                ?: addRequestPermissionsStatement(proceedMethod, targetParam, permissionFieldName(needsMethod), requestCodeField)
        builder.addMethod(proceedMethod.build())

        // Add cancel() override method
        val cancelMethod: MethodSpec.Builder = MethodSpec.methodBuilder("cancel")
                .addAnnotation(Override::class.java)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID)
        val onDenied = rpe.findOnDeniedForNeeds(needsMethod)
        if (onDenied != null) {
            cancelMethod
                    .addStatement("\$T target = \$N.get()", targetType, weakFieldName)
                    .addStatement("if (target == null) return")
                    .addStatement("target.\$N()", onDenied.simpleString())
        }
        builder.addMethod(cancelMethod.build())

        // For classes with additional parameters, add a "grant()" method
        if (hasParameters) {
            val grantMethod: MethodSpec.Builder = MethodSpec.methodBuilder("grant")
                    .addAnnotation(Override::class.java)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.VOID)
            grantMethod
                    .addStatement("\$T target = \$N.get()", targetType, weakFieldName)
                    .addStatement("if (target == null) return")

            // Compose the call to the permission-protected method;
            // since the number of parameters is variable, utilize the low-level CodeBlock here
            // to compose the method call and its parameters
            grantMethod.addCode(
                    CodeBlock.builder()
                            .add("target.\$N(", needsMethod.simpleString())
                            .add(varargsParametersCodeBlock(needsMethod))
                            .addStatement(")")
                            .build()
            )
            builder.addMethod(grantMethod.build())
        }
        return builder.build()
    }
}
