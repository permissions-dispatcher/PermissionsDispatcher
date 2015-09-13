package permissions.dispatcher.processor;

import com.squareup.javapoet.ClassName;
import permissions.dispatcher.*;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.List;

import static permissions.dispatcher.processor.ConstantsProvider.CLASS_SUFFIX;
import static permissions.dispatcher.processor.Utils.*;
import static permissions.dispatcher.processor.Validator.*;

class RuntimePermissionsAnnotatedElement {

    private final String packageName;

    private final String className;

    private final ClassType classType;

    private final List<ExecutableElement> needsPermissionMethods;

    private final List<ExecutableElement> needsPermissionsMethods;

    private final List<ExecutableElement> showsRationaleMethods;

    private final List<ExecutableElement> showsRationalesMethods;

    private final List<ExecutableElement> deniedPermissionMethods;

    private final List<ExecutableElement> deniedPermissionsMethods;

    RuntimePermissionsAnnotatedElement(TypeElement element, TypeResolver resolver) {
        String qualifiedName = element.getQualifiedName().toString();
        packageName = Utils.getPackageName(qualifiedName);
        className = Utils.getClassName(qualifiedName);
        classType = ClassType.getClassType(className, resolver);
        checkClassType(classType);
        needsPermissionMethods = findMethods(element, NeedsPermission.class);
        validateNeedsPermissionMethods();
        needsPermissionsMethods = findMethods(element, NeedsPermissions.class);
        validateNeedsPermissionsMethods();
        checkNeedsPermissionsSize(needsPermissionMethods, needsPermissionsMethods);
        showsRationaleMethods = findMethods(element, ShowsRationale.class);
        validateShowRationaleMethods();
        showsRationalesMethods = findMethods(element, ShowsRationales.class);
        validateShowRationalesMethods();
        deniedPermissionMethods = findMethods(element, DeniedPermission.class);
        validateDeniedPermissionMethods();
        deniedPermissionsMethods = findMethods(element, DeniedPermissions.class);
        validateDeniedPermissionsMethods();
    }

    private void validateNeedsPermissionMethods() {
        checkDuplicatedValue(needsPermissionMethods, NeedsPermission.class);
        checkPrivateMethods(needsPermissionMethods);
    }

    private void validateNeedsPermissionsMethods() {
        checkDuplicatedValue(needsPermissionsMethods, NeedsPermissions.class);
        checkPrivateMethods(needsPermissionsMethods);
    }

    private void validateShowRationaleMethods() {
        checkDuplicatedValue(showsRationaleMethods, ShowsRationale.class);
        checkPrivateMethods(showsRationaleMethods);
    }

    private void validateShowRationalesMethods() {
        checkDuplicatedValue(showsRationalesMethods, ShowsRationales.class);
        checkPrivateMethods(showsRationalesMethods);
    }

    private void validateDeniedPermissionMethods() {
        checkDuplicatedValue(deniedPermissionMethods, DeniedPermission.class);
        checkPrivateMethods(deniedPermissionMethods);
        checkMatchingValues(deniedPermissionMethods, DeniedPermission.class, needsPermissionMethods, NeedsPermission.class);
    }

    private void validateDeniedPermissionsMethods() {
        checkDuplicatedValue(deniedPermissionsMethods, DeniedPermissions.class);
        checkPrivateMethods(deniedPermissionsMethods);
        checkMatchingValues(deniedPermissionsMethods, DeniedPermissions.class, needsPermissionsMethods, NeedsPermissions.class);
    }

    public String getPackageName() {
        return packageName;
    }

    public ClassName getClassName() {
        return ClassName.get(packageName, className);
    }

    public ClassType getClassType() {
        return classType;
    }

    public String getDispatcherClassName() {
        return className + CLASS_SUFFIX;
    }

    public List<ExecutableElement> getNeedsPermissionMethods() {
        return needsPermissionMethods;
    }

    public List<ExecutableElement> getNeedsPermissionsMethods() {
        return needsPermissionsMethods;
    }

    public List<ExecutableElement> getAllNeedsPermissionsMethods() {
        return new ArrayList<ExecutableElement>() {
            {
                addAll(needsPermissionMethods);
                addAll(needsPermissionsMethods);
            }
        };
    }

    public ExecutableElement getDeniedPermissionFromValue(String value) {
        return findDeniedPermissionFromValue(value, deniedPermissionMethods);
    }

    public ExecutableElement getDeniedPermissionFromValue(String[] value) {
        return findDeniedPermissionFromValue(value, deniedPermissionsMethods);
    }

    public ExecutableElement getShowsRationaleFromValue(String value) {
        return findShowsRationaleFromValue(value, showsRationaleMethods);
    }

    public ExecutableElement getShowsRationaleFromValue(String[] value) {
        return findShowsRationalesFromValue(value, showsRationalesMethods);
    }

    public ExecutableElement getDeniedPermissionFromElement(ExecutableElement permissionElement) {
        return findDeniedPermissionFromElement(this, permissionElement);
    }

}
