package permissions.dispatcher.processor

import com.squareup.javapoet.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.NeedsPermissions

import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import java.util.ArrayList

import permissions.dispatcher.processor.ConstantsProvider.*

object JavaFileBuilder {

    private fun createConstructor(): MethodSpec {
        return MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).build()
    }

    private fun createNeedsPermissionMethods(clazz: RuntimePermissionsAnnotatedElement): List<MethodSpec> {
        val methodSpecs = ArrayList<MethodSpec>()
        val elements = clazz.needsPermissionMethods
        for (element in elements) {
            val value = element.getAnnotation(javaClass<NeedsPermission>()).value
            val showsRationale = clazz.getShowsRationaleFromValue(value)
            val activity = clazz.classType.activity
            methodSpecs.add(createMethodWithCheck(activity, clazz.getClassName(), element, showsRationale))
        }
        return methodSpecs
    }

    private fun createNeedsPermissionsMethods(clazz: RuntimePermissionsAnnotatedElement): List<MethodSpec> {
        val methodSpecs = ArrayList<MethodSpec>()
        val elements = clazz.needsPermissionsMethods
        for (element in elements) {
            val value = element.getAnnotation(javaClass<NeedsPermissions>()).value
            val showsRationale = clazz.getShowsRationaleFromValue(value)
            val activity = clazz.classType.activity
            methodSpecs.add(createMethodWithCheck(activity, clazz.getClassName(), element, showsRationale))
        }
        return methodSpecs
    }

    private fun createMethodWithCheck(activity: String, target: ClassName, needsPermission: ExecutableElement, showsRationale: ExecutableElement?): MethodSpec {
        val methodName = needsPermission.getSimpleName().toString()
        val value = Utils.getPermissionFieldName(needsPermission.getSimpleName().toString())
        val methodBuilder = MethodSpec
                .methodBuilder(methodName + ConstantsProvider.METHOD_SUFFIX)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(Void.TYPE)
                .addParameter(target, "target")
                .beginControlFlow("if (\$T.hasSelfPermissions(\$N, \$N))", ConstantsProvider.PERMISSION_UTILS, activity, value)
                .addStatement("target.\$N()", methodName).nextControlFlow("else")
        if (showsRationale != null) {
            methodBuilder
                    .beginControlFlow("if (\$T.shouldShowRequestPermissionRationale(\$N, \$N))", ConstantsProvider.PERMISSION_UTILS, activity, value)
                    .addStatement("target.\$N()", showsRationale.getSimpleName()).endControlFlow()
        }
        return methodBuilder.addStatement("\$T.requestPermissions(\$N, \$N, \$N)", ConstantsProvider.ACTIVITY_COMPAT, activity, value, Utils.getRequestCodeFieldName(methodName)).endControlFlow().build()
    }

    private fun createOnRequestPermissionsResult(target: ClassName, methods: List<ExecutableElement>): MethodSpec {
        val methodBuilder = MethodSpec.methodBuilder("onRequestPermissionsResult").addModifiers(Modifier.PUBLIC, Modifier.STATIC).addParameter(target, "target").addParameter(Integer.TYPE, "requestCode").addParameter(javaClass<IntArray>(), "grantResults").returns(Void.TYPE).beginControlFlow("switch (requestCode)")
        for (method in methods) {
            val methodName = method.getSimpleName().toString()
            methodBuilder
                    .addCode("case \$N:\n", Utils.getRequestCodeFieldName(methodName))
                    .beginControlFlow("if (\$T.verifyPermissions(grantResults))", ConstantsProvider.PERMISSION_UTILS)
                    .addStatement("target.\$N()", methodName)
                    .endControlFlow()
                    .addStatement("break")
        }
        methodBuilder.addCode("default:\n").addStatement("break").endControlFlow()
        return methodBuilder.build()
    }

    private fun createFields(elements: List<ExecutableElement>): List<FieldSpec> {
        val fieldSpecs = ArrayList<FieldSpec>()
        var index = 0
        for (element in elements) {
            val requestCodeFieldName = Utils.getRequestCodeFieldName(element.getSimpleName().toString())
            fieldSpecs.add(createRequestCodeField(requestCodeFieldName, index++))
            val permissionFieldName = Utils.getPermissionFieldName(element.getSimpleName().toString())
            val needsPermission = element.getAnnotation(javaClass<NeedsPermission>())
            if (needsPermission != null) {
                val value = needsPermission.value
                fieldSpecs.add(createPermissionField(permissionFieldName, value))
            }
            val needsPermissions = element.getAnnotation(javaClass<NeedsPermissions>())
            if (needsPermissions != null) {
                val value = needsPermissions.value
                fieldSpecs.add(createPermissionField(permissionFieldName, *value))
            }
        }
        return fieldSpecs
    }

    private fun createRequestCodeField(name: String, index: Int): FieldSpec {
        return FieldSpec
                .builder(Integer.TYPE, name, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("\$L", index).build()
    }

    private fun createPermissionField(name: String, vararg value: String): FieldSpec {
        return FieldSpec
                .builder(javaClass<Array<String>>(), name, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("\$N", "new String[]" + Utils.toString(*value)).build()
    }

    fun createJavaFile(element: RuntimePermissionsAnnotatedElement): JavaFile {
        val permissionsMethods = element.getAllNeedsPermissionsMethods()
        val clazz = TypeSpec
                .classBuilder(element.getDispatcherClassName())
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addFields(createFields(permissionsMethods))
                .addMethod(createConstructor())
                .addMethods(createNeedsPermissionMethods(element))
                .addMethods(createNeedsPermissionsMethods(element))
                .addMethod(createOnRequestPermissionsResult(element.getClassName(), permissionsMethods))
                .build()
        val packageName = element.packageName
        return JavaFile.builder(packageName, clazz).build()
    }

}
