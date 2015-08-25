package permissions.dispatcher.processor;

import com.squareup.javapoet.ClassName;

import permissions.dispatcher.annotations.NeedsPermission;
import permissions.dispatcher.annotations.NeedsPermissions;
import permissions.dispatcher.annotations.ShowsRationale;
import permissions.dispatcher.annotations.ShowsRationales;

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

    RuntimePermissionsAnnotatedElement(TypeElement element) {
        String qualifiedName = element.getQualifiedName().toString();
        packageName = Utils.getPackageName(qualifiedName);
        className = Utils.getClassName(qualifiedName);
        checkClassName(className);
        classType = ClassType.getClassType(className);
        needsPermissionMethods = findMethods(element, NeedsPermission.class);
        validateNeedsPermissionMethods();
        needsPermissionsMethods = findMethods(element, NeedsPermissions.class);
        validateNeedsPermissionsMethods();
        checkNeedsPermissionsSize(needsPermissionMethods, needsPermissionsMethods);
        showsRationaleMethods = findMethods(element, ShowsRationale.class);
        validateShowRationaleMethods();
        showsRationalesMethods = findMethods(element, ShowsRationales.class);
        validateShowRationalesMethods();
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

    public ExecutableElement getShowsRationaleFromValue(String value) {
        return findShowsRationaleFromValue(value, showsRationaleMethods);
    }

    public ExecutableElement getShowsRationaleFromValue(String[] value) {
        return findShowsRationalesFromValue(value, showsRationalesMethods);
    }

}
