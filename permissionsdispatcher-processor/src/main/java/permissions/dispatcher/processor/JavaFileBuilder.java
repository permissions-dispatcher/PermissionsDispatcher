package permissions.dispatcher.processor;

import com.squareup.javapoet.*;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.NeedsPermissions;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

import static permissions.dispatcher.processor.ConstantsProvider.*;
import static permissions.dispatcher.processor.Utils.getPermissionFieldName;
import static permissions.dispatcher.processor.Utils.getRequestCodeFieldName;

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
            methodSpecs.add(createMethodWithCheck(activity, clazz.getClassName(), element, showsRationale));
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
            methodSpecs.add(createMethodWithCheck(activity, clazz.getClassName(), element, showsRationale));
        }
        return methodSpecs;
    }

    private static MethodSpec createMethodWithCheck(String activity, ClassName target, ExecutableElement needsPermission, ExecutableElement showsRationale) {
        String methodName = needsPermission.getSimpleName().toString();
        String value = getPermissionFieldName(needsPermission.getSimpleName().toString());
        MethodSpec.Builder
                methodBuilder = MethodSpec.methodBuilder(methodName + METHOD_SUFFIX)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(target, "target")
                .beginControlFlow("if ($T.hasSelfPermissions($N, $N))", PERMISSION_UTILS, activity, value)
                .addStatement("target.$N()", methodName)
                .nextControlFlow("else");
        if (showsRationale != null) {
            methodBuilder.beginControlFlow("if ($T.shouldShowRequestPermissionRationale($N, $N))",
                    PERMISSION_UTILS, activity, value)
                    .addStatement("target.$N()", showsRationale.getSimpleName())
                    .endControlFlow();
        }
        return methodBuilder.addStatement("$T.requestPermissions($N, $N, $N)",
                ACTIVITY_COMPAT, activity, value, getRequestCodeFieldName(methodName))
                .endControlFlow()
                .build();
    }

    private static MethodSpec createOnRequestPermissionsResult(ClassName target, List<ExecutableElement> methods) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("onRequestPermissionsResult")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(target, "target")
                .addParameter(int.class, "requestCode")
                .addParameter(int[].class, "grantResults")
                .returns(void.class)
                .beginControlFlow("switch (requestCode)");
        for (ExecutableElement method : methods) {
            String methodName = method.getSimpleName().toString();
            methodBuilder
                    .addCode("case $N:\n", getRequestCodeFieldName(methodName))
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
            String requestCodeFieldName = getRequestCodeFieldName(element.getSimpleName().toString());
            fieldSpecs.add(createRequestCodeField(requestCodeFieldName, index++));
            String permissionFieldName = getPermissionFieldName(element.getSimpleName().toString());
            NeedsPermission needsPermission = element.getAnnotation(NeedsPermission.class);
            if (needsPermission != null) {
                String value = needsPermission.value();
                fieldSpecs.add(createPermissionField(permissionFieldName, value));
            }
            NeedsPermissions needsPermissions = element.getAnnotation(NeedsPermissions.class);
            if (needsPermissions != null) {
                String[] value = needsPermissions.value();
                fieldSpecs.add(createPermissionField(permissionFieldName, value));
            }
        }
        return fieldSpecs;
    }

    private static FieldSpec createRequestCodeField(String name, int index) {
        return FieldSpec
                .builder(int.class, name, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("$L", index)
                .build();
    }

    private static FieldSpec createPermissionField(String name, String... value) {
        return FieldSpec
                .builder(String[].class, name, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("$N", "new String[]" + Utils.toString(value))
                .build();
    }

    static JavaFile createJavaFile(RuntimePermissionsAnnotatedElement element) {
        List<ExecutableElement> permissionsMethods = element.getAllNeedsPermissionsMethods();
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
