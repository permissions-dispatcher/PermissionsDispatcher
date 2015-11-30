package permissions.dispatcher.processor.impl

import com.squareup.javapoet.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.processor.ProcessorUnit
import permissions.dispatcher.processor.RequestCodeProvider
import permissions.dispatcher.processor.RuntimePermissionsElement
import permissions.dispatcher.processor.util.*
import java.util.*
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier

/**
 * Base class for ProcessorUnit implementations.
 * <p>
 * This generates the parts of code independent from specific permission method signatures for different target objects.
 */
public abstract class BaseProcessorUnit : ProcessorUnit {

    /**
     * Creates the JavaFile for the provided @RuntimePermissions element.
     * <p>
     * This will delegate to other methods that compose generated code.
     */
    override final fun createJavaFile(rpe: RuntimePermissionsElement, requestCodeProvider: RequestCodeProvider): JavaFile {
        // Check the prerequisites for creating a Java file for this element
        checkPrerequisites(rpe)

        return JavaFile.builder(rpe.packageName, createTypeSpec(rpe, requestCodeProvider))
                .addFileComment(FILE_COMMENT)
                .build()
    }

    /* Begin abstract */

    abstract fun checkPrerequisites(rpe: RuntimePermissionsElement)

    abstract fun addWithCheckBody(builder: MethodSpec.Builder, needsMethod: ExecutableElement, rpe: RuntimePermissionsElement, targetParam: String)

    abstract fun addResultCaseBody(builder: MethodSpec.Builder, needsMethod: ExecutableElement, rpe: RuntimePermissionsElement, targetParam: String, grantResultsParam: String)

    abstract fun addRequestPermissionsStatement(builder: MethodSpec.Builder, targetParam: String, permissionField: String, requestCodeField: String)

    /* Begin private */

    private fun createTypeSpec(rpe: RuntimePermissionsElement, requestCodeProvider: RequestCodeProvider): TypeSpec {
        return TypeSpec.classBuilder(rpe.generatedClassName)
                .addModifiers(Modifier.FINAL)
                .addFields(createFields(rpe.needsElements, requestCodeProvider))
                .addMethod(createConstructor())
                .addMethods(createWithCheckMethods(rpe))
                .addMethod(createPermissionResultMethod(rpe))
                .addTypes(createPermissionRequestClasses(rpe))
                .build()
    }

    private fun createFields(needsElements: List<ExecutableElement>, requestCodeProvider: RequestCodeProvider): List<FieldSpec> {
        val fields: ArrayList<FieldSpec> = arrayListOf()
        needsElements.forEach {
            // For each method annotated with @NeedsPermission, add REQUEST integer and PERMISSION String[] fields
            fields.add(createRequestCodeField(it, requestCodeProvider.nextRequestCode()))
            fields.add(createPermissionField(it))

            if (it.parameters.isNotEmpty()) {
                fields.add(createPendingRequestField(it))
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
        val permissionValue: List<String> = e.getAnnotation(NeedsPermission::class.java).permissionValue()
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
        return FieldSpec.builder(ClassName.get("permissions.dispatcher", "GrantableRequest"), pendingRequestFieldName(e))
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .build()
    }

    private fun createConstructor(): MethodSpec {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .build()
    }

    private fun createWithCheckMethods(rpe: RuntimePermissionsElement): List<MethodSpec> {
        val methods: ArrayList<MethodSpec> = arrayListOf()
        rpe.needsElements.forEach {
            // For each @NeedsPermission method, create the "WithCheck" equivalent
            methods.add(createWithCheckMethod(rpe, it))
        }
        return methods
    }

    private fun createWithCheckMethod(rpe: RuntimePermissionsElement, method: ExecutableElement): MethodSpec {
        val targetParam = "target"
        val builder = MethodSpec.methodBuilder(withCheckMethodName(method))
                .addModifiers(Modifier.STATIC)
                .returns(TypeName.VOID)
                .addParameter(rpe.typeName, targetParam)

        // If the method has parameters, add those as well
        method.parameters.forEach {
            builder.addParameter(typeNameOf(it), it.simpleString())
        }

        // Delegate method body generation to implementing classes
        addWithCheckBody(builder, method, rpe, targetParam)
        return builder.build()
    }

    private fun createPermissionResultMethod(rpe: RuntimePermissionsElement): MethodSpec {
        val targetParam = "target"
        val requestCodeParam = "requestCode"
        val grantResultsParam = "grantResults"
        val builder = MethodSpec.methodBuilder("onRequestPermissionsResult")
                .addModifiers(Modifier.STATIC)
                .returns(TypeName.VOID)
                .addParameter(rpe.typeName, targetParam)
                .addParameter(TypeName.INT, requestCodeParam)
                .addParameter(ArrayTypeName.of(TypeName.INT), grantResultsParam)

        // For each @NeedsPermission method, add a switch case
        builder.beginControlFlow("switch (\$N)", requestCodeParam)
        for (needsMethod in rpe.needsElements) {
            builder.addCode("case \$N:\n", requestCodeFieldName(needsMethod))

            // Delegate switch-case generation to implementing classes
            addResultCaseBody(builder, needsMethod, rpe, targetParam, grantResultsParam)
        }

        // Add the default case
        builder
                .addCode("default:\n")
                .addStatement("break")
                .endControlFlow();

        return builder.build()
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
        val hasParameters: Boolean = needsMethod.parameters.isNotEmpty()
        val superInterfaceName: String = if (hasParameters) "GrantableRequest" else "PermissionRequest"

        val targetType = rpe.typeName
        val builder = TypeSpec.classBuilder(permissionRequestTypeName(needsMethod))
                .addSuperinterface(ClassName.get("permissions.dispatcher", superInterfaceName))
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)

        // Add required fields to the target
        val weakFieldName: String = "weakTarget"
        val weakFieldType = ParameterizedTypeName.get(ClassName.get("java.lang.ref", "WeakReference"), targetType)
        builder.addField(weakFieldType, weakFieldName, Modifier.PRIVATE, Modifier.FINAL)
        needsMethod.parameters.forEach {
            builder.addField(typeNameOf(it), it.simpleString(), Modifier.PRIVATE, Modifier.FINAL)
        }

        // Add constructor
        val targetParam = "target"
        val constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addParameter(targetType, targetParam)
                .addStatement("this.\$L = new WeakReference<>(\$N)", weakFieldName, targetParam)
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
        addRequestPermissionsStatement(proceedMethod, targetParam, permissionFieldName(needsMethod), requestCodeFieldName(needsMethod))
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