package permissions.dispatcher.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;

import permissions.dispatcher.NeedsPermission;

import static permissions.dispatcher.processor.ConstantsProvider.ACTIVITY_COMPAT;
import static permissions.dispatcher.processor.ConstantsProvider.METHOD_SUFFIX;
import static permissions.dispatcher.processor.ConstantsProvider.PERMISSION_UTIL;
import static permissions.dispatcher.processor.Utils.getFieldName;

final class JavaFileBuilder {

    private JavaFileBuilder() {
    }

    private static MethodSpec createConstructor() {
        return MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).build();
    }

    private static List<MethodSpec> createMethodsWithCheck(RuntimePermissionsAnnotatedElement clazz) {
        List<MethodSpec> methodSpecs = new ArrayList<>();
        List<ExecutableElement> elements = clazz.getNeedsPermissionMethods();
        for (ExecutableElement element : elements) {
            String value = element.getAnnotation(NeedsPermission.class).value();
            ExecutableElement showsRationale = clazz.getShowsRationaleMethodFromValue(value);
            methodSpecs.add(createMethodWithCheck(clazz.getClassType(), clazz.getClassName(), element, showsRationale));
        }
        return methodSpecs;
    }

    private static MethodSpec createMethodWithCheck(ClassType classType, ClassName target, ExecutableElement needsPermission, ExecutableElement showsRationale) {
        String methodName = needsPermission.getSimpleName().toString();
        String value = needsPermission.getAnnotation(NeedsPermission.class).value();
        String activity = classType.getActivity();
        MethodSpec.Builder
                methodBuilder = MethodSpec.methodBuilder(methodName + METHOD_SUFFIX)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(target, "target")
                .beginControlFlow("if ($T.hasSelfPermissions($N, $S))", PERMISSION_UTIL, activity, value)
                .addStatement("target.$N()", methodName)
                .nextControlFlow("else");

        if (showsRationale != null) {
            methodBuilder.beginControlFlow("if ($T.shouldShowRequestPermissionRationale($N, $S))",
                    ACTIVITY_COMPAT, activity, value)
                    .addStatement("target.$N()", showsRationale.getSimpleName())
                    .endControlFlow();
        }
        return methodBuilder.addStatement("$T.requestPermissions($N, new String[]{$S}, $N)",
                ACTIVITY_COMPAT, activity, value, getFieldName(methodName))
                .endControlFlow()
                .build();
    }

    private static MethodSpec createOnRequestPermissionsResult(ClassName target, List<ExecutableElement> methods) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("onRequestPermissionsResult")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(target, "target")
                .addParameter(int.class, "requestCode")
                .addParameter(String[].class, "permissions")
                .addParameter(int[].class, "grantResults")
                .returns(void.class)
                .beginControlFlow("switch (requestCode)");

        for (ExecutableElement method : methods) {
            String methodName = method.getSimpleName().toString();
            methodBuilder
                    .addCode("case $N:\n", getFieldName(methodName))
                    .beginControlFlow("if ($T.verifyPermissions(grantResults))", PERMISSION_UTIL)
                    .addStatement("target.$N()", methodName)
                    .endControlFlow()
                    .addStatement("break");
        }
        methodBuilder
                .addCode("default:\n")
                .addStatement("break")
                .endControlFlow();
        return methodBuilder.build();
    }

    private static List<FieldSpec> createFields(List<ExecutableElement> elements) {
        List<FieldSpec> fieldSpecs = new ArrayList<>();
        int index = 0;
        for (ExecutableElement element : elements) {
            String fieldName = getFieldName(element.getSimpleName().toString());
            fieldSpecs.add(createField(fieldName, index++));
        }
        return fieldSpecs;
    }

    private static FieldSpec createField(String name, int index) {
        return FieldSpec.builder(int.class, name, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("$L", index)
                .build();
    }

    public static JavaFile createJavaFile(RuntimePermissionsAnnotatedElement element) {
        List<ExecutableElement> needsPermissionMethods = element.getNeedsPermissionMethods();
        TypeSpec clazz = TypeSpec.classBuilder(element.getDispatchClassName())
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addFields(createFields(needsPermissionMethods))
                .addMethod(createConstructor())
                .addMethods(createMethodsWithCheck(element))
                .addMethod(createOnRequestPermissionsResult(element.getClassName(), needsPermissionMethods))
                        .build();
        String packageName = element.getPackageName();
        return JavaFile.builder(packageName, clazz).build();
    }

}
