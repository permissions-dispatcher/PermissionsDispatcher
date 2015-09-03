package permissions.dispatcher.processor.impl

import com.squareup.javapoet.*
import permissions.dispatcher.Needs
import permissions.dispatcher.processor.ProcessorUnit
import permissions.dispatcher.processor.RuntimePermissionsElement
import permissions.dispatcher.processor.util.*
import java.util.*
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.type.TypeKind

/**
 * Base class for ProcessorUnit implementations. This generates the parts of code independent
 * from specific permission method signatures for different target objects
 */
public abstract class BaseProcessorUnit : ProcessorUnit {

    /**
     * Creates the JavaFile for the provided @RuntimePermissions element.
     * This will delegate to other methods that compose generated code
     */
    override final fun createJavaFile(rpe: RuntimePermissionsElement): JavaFile {
        // Check the prerequisites for creating a Java file for this element
        checkPrerequisites(rpe)

        return JavaFile.builder(rpe.packageName, createTypeSpec(rpe))
                .addFileComment(FILE_COMMENT)
                .build()
    }

    /* Begin abstract */

    abstract fun checkPrerequisites(rpe: RuntimePermissionsElement)

    abstract fun addWithCheckBody(builder: MethodSpec.Builder, needsMethod: ExecutableElement, rpe: RuntimePermissionsElement, targetParam: String)

    abstract fun addResultCaseBody(builder: MethodSpec.Builder, needsMethod: ExecutableElement, rpe: RuntimePermissionsElement, targetParam: String, grantResultsParam: String)

    /* Begin private */

    private fun createTypeSpec(rpe: RuntimePermissionsElement): TypeSpec {
        val builder: TypeSpec.Builder = TypeSpec.classBuilder(rpe.generatedClassName)
                .addModifiers(Modifier.FINAL)
                .addFields(createFields(rpe.needsElements))
                .addMethod(createConstructor())
                .addMethods(createWithCheckMethods(rpe))
                .addMethod(createPermissionResultMethod(rpe))
        return builder.build()
    }

    private fun createFields(needsElements: List<ExecutableElement>): List<FieldSpec> {
        val fields: ArrayList<FieldSpec> = arrayListOf()
        var index: Int = 0
        needsElements.forEach {
            // For each method annotated with @Needs, add REQUEST integer and PERMISSION String[] fields
            fields.add(createRequestCodeField(it, index++))
            fields.add(createPermissionField(it))
        }
        return fields
    }

    private fun createRequestCodeField(e: ExecutableElement, index: Int): FieldSpec {
        return FieldSpec.builder(javaClass<Int>(), requestCodeFieldName(e))
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("\$L", index)
                .build()
    }

    private fun createPermissionField(e: ExecutableElement): FieldSpec {
        val permissionValue: List<String> = e.getAnnotation(javaClass<Needs>()).permissionValue()
        val formattedValue: String = permissionValue.joinToString(
                separator = ",",
                prefix = "{",
                postfix = "}",
                transform = { "\"$it\"" }
        )
        return FieldSpec.builder(ArrayTypeName.of(javaClass<String>()), permissionFieldName(e))
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("\$N", "new String[] $formattedValue")
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
            // For each @Needs method, create the "WithCheck" equivalent
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

        // For each @Needs method, add a switch case
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
}