package permissions.dispatcher.processor;

import com.squareup.javapoet.*;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.NeedsPermissions;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

import static permissions.dispatcher.processor.ConstantsProvider.*;
import static permissions.dispatcher.processor.Utils.getFieldName;

final class JavaFileBuilder {

    private static MethodSpec createConstructor() {
        return MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).build();
    }

    private static List<MethodSpec> createNeedsPermissionMethods(RuntimePermissionsAnnotatedElement clazz) {
        List<MethodSpec> methodSpecs = new ArrayList<>();
        List<ExecutableElement> elements = clazz.getNeedsPermissionMethods();
        for (ExecutableElement element : elements) {
            String value = element.getAnnotation(NeedsPermission.class).value();
            ExecutableElement showsRationale = clazz.getShowsRationaleFromValue(value);
            String activity = clazz.getClassType().getActivity();
            methodSpecs.add(createNeedsPermissionMethod(activity, clazz.getClassName(), element, showsRationale));
        }
        return methodSpecs;
    }

    private static List<MethodSpec> createNeedsPermissionsMethods(RuntimePermissionsAnnotatedElement clazz) {
        List<MethodSpec> methodSpecs = new ArrayList<>();
        List<ExecutableElement> elements = clazz.getNeedsPermissionsMethods();
        for (ExecutableElement element : elements) {
            String[] value = element.getAnnotation(NeedsPermissions.class).value();
            ExecutableElement showsRationale = clazz.getShowsRationaleFromValue(value);
            String activity = clazz.getClassType().getActivity();
            methodSpecs.add(createNeedsPermissionsMethod(activity, clazz.getClassName(), element, showsRationale));
        }
        return methodSpecs;
    }

    private static MethodSpec createNeedsPermissionMethod(String activity, ClassName target, ExecutableElement needsPermission, ExecutableElement showsRationale) {
        String methodName = needsPermission.getSimpleName().toString();
        String value = needsPermission.getAnnotation(NeedsPermission.class).value();
        MethodSpec.Builder
                methodBuilder = MethodSpec.methodBuilder(methodName + METHOD_SUFFIX)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(target, "target")
                .beginControlFlow("if ($T.hasSelfPermissions($N, $S))", PERMISSION_UTILS, activity, value)
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

    private static MethodSpec createNeedsPermissionsMethod(String activity, ClassName target, ExecutableElement needsPermission, ExecutableElement showsRationale) {
        String methodName = needsPermission.getSimpleName().toString();
        String[] value = needsPermission.getAnnotation(NeedsPermissions.class).value();
        String array = "new String[]" + Utils.toString(value);
        MethodSpec.Builder
                methodBuilder = MethodSpec.methodBuilder(methodName + METHOD_SUFFIX)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(target, "target")
                .beginControlFlow("if ($T.hasSelfPermissions($N, $L))", PERMISSION_UTILS, activity, array)
                .addStatement("target.$N()", methodName)
                .nextControlFlow("else");
        if (showsRationale != null) {
            methodBuilder.beginControlFlow("if ($T.shouldShowRequestPermissionRationale($N, $L))",
                    PERMISSION_UTILS, activity, array)
                    .addStatement("target.$N()", showsRationale.getSimpleName())
                    .endControlFlow();
        }
        return methodBuilder.addStatement("$T.requestPermissions($N, $L, $N)",
                ACTIVITY_COMPAT, activity, array, getFieldName(methodName))
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
                    .beginControlFlow("if ($T.verifyPermissions(grantResults))", PERMISSION_UTILS)
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

    static JavaFile createJavaFile(RuntimePermissionsAnnotatedElement element) {
        List<ExecutableElement> permissionsMethods = new ArrayList<ExecutableElement>() {
            {
                addAll(element.getNeedsPermissionMethods());
            }

            {
                addAll(element.getNeedsPermissionsMethods());
            }
        };
        TypeSpec clazz = TypeSpec.classBuilder(element.getDispatcherClassName())
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addFields(createFields(permissionsMethods))
                .addMethod(createConstructor())
                .addMethods(createNeedsPermissionMethods(element))
                .addMethods(createNeedsPermissionsMethods(element))
                .addMethod(createOnRequestPermissionsResult(element.getClassName(), permissionsMethods))
                .build();
        String packageName = element.getPackageName();
        return JavaFile.builder(packageName, clazz).build();
    }

}
