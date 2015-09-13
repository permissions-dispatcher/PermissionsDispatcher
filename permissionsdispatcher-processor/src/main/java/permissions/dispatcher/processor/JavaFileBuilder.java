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
            methodSpecs.add(createMethodWithCheck(clazz.getClassType(), activity,
                    clazz.getClassName(), element, showsRationale));
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
            methodSpecs.add(createMethodWithCheck(clazz.getClassType(), activity,
                    clazz.getClassName(), element, showsRationale));
        }
        return methodSpecs;
    }

    private static MethodSpec createMethodWithCheck(ClassType classType, String activity, ClassName target,
                                                    ExecutableElement needsPermission, ExecutableElement showsRationale) {
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
            String rationaleTarget = null;
            if (classType == ClassType.ACTIVITY) {
                rationaleTarget = activity;
            } else if (classType == ClassType.V4FRAGMENT) {
                rationaleTarget = "target";
            }
            methodBuilder
                    .beginControlFlow("if ($T.shouldShowRequestPermissionRationale($N, $N))",
                            PERMISSION_UTILS, rationaleTarget, value)
                    .addStatement("target.$N()", showsRationale.getSimpleName())
                    .endControlFlow();
        }
        if (classType == ClassType.ACTIVITY) {
            methodBuilder.addStatement("$T.requestPermissions($N, $N, $N)",
                    ACTIVITY_COMPAT, activity, value, getRequestCodeFieldName(methodName));
        } else if (classType == ClassType.V4FRAGMENT) {
            methodBuilder.addStatement("target.requestPermissions($N, $N)",
                    value, getRequestCodeFieldName(methodName));
        }
        return methodBuilder.endControlFlow().build();
    }

    private static MethodSpec createOnRequestPermissionsResult(RuntimePermissionsAnnotatedElement element) {
        ClassName target = element.getClassName();
        List<ExecutableElement> permissionMethods = element.getAllNeedsPermissionsMethods();
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("onRequestPermissionsResult")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(target, "target")
                .addParameter(int.class, "requestCode")
                .addParameter(int[].class, "grantResults")
                .returns(void.class)
                .beginControlFlow("switch (requestCode)");
        for (ExecutableElement method : permissionMethods) {
            String methodName = method.getSimpleName().toString();
            methodBuilder
                    .addCode("case $N:\n", getRequestCodeFieldName(methodName))
                    .beginControlFlow("if ($T.verifyPermissions(grantResults))", PERMISSION_UTILS)
                    .addStatement("target.$N()", methodName);
            ExecutableElement deniedPermission = element.getDeniedPermissionFromElement(method);
            if (deniedPermission != null) {
                methodBuilder
                        .nextControlFlow("else")
                        .addStatement("target.$N()", deniedPermission.getSimpleName().toString());
            }
            methodBuilder
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
                .addMethod(createOnRequestPermissionsResult(element))
                .build();
        String packageName = element.getPackageName();
        return JavaFile.builder(packageName, clazz).build();
    }

}
