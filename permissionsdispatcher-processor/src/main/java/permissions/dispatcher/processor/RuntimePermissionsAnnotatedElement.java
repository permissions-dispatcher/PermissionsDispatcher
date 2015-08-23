package permissions.dispatcher.processor;

import com.squareup.javapoet.ClassName;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.NeedsPermissions;
import permissions.dispatcher.ShowsRationale;
import permissions.dispatcher.ShowsRationales;

import static permissions.dispatcher.processor.ConstantsProvider.CLASS_SUFFIX;
import static permissions.dispatcher.processor.Utils.findMethods;
import static permissions.dispatcher.processor.Utils.findShowsRationaleFromValue;
import static permissions.dispatcher.processor.Utils.findShowsRationalesFromValue;
import static permissions.dispatcher.processor.Validator.checkClassName;
import static permissions.dispatcher.processor.Validator.checkDuplicatedPermission;
import static permissions.dispatcher.processor.Validator.checkDuplicatedPermissions;
import static permissions.dispatcher.processor.Validator.checkDuplicatedRationale;
import static permissions.dispatcher.processor.Validator.checkDuplicatedRationales;
import static permissions.dispatcher.processor.Validator.checkNeedsPermissionsSize;
import static permissions.dispatcher.processor.Validator.checkPrivateMethods;
import static permissions.dispatcher.processor.Validator.checkShowsRationalesSize;

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
        checkDuplicatedPermission(needsPermissionMethods);
        checkPrivateMethods(needsPermissionMethods);
    }

    private void validateNeedsPermissionsMethods() {
        checkDuplicatedPermissions(needsPermissionsMethods);
        checkPrivateMethods(needsPermissionsMethods);
    }

    private void validateShowRationaleMethods() {
        checkDuplicatedRationale(showsRationaleMethods);
        checkShowsRationalesSize(showsRationaleMethods.size(), needsPermissionMethods.size());
        checkPrivateMethods(showsRationaleMethods);
    }

    private void validateShowRationalesMethods() {
        checkDuplicatedRationales(showsRationalesMethods);
        checkShowsRationalesSize(showsRationalesMethods.size(), needsPermissionsMethods.size());
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

    public ExecutableElement getShowsRationaleFromValue(String value) {
        return findShowsRationaleFromValue(value, showsRationaleMethods);
    }

    public ExecutableElement getShowsRationalesFromValue(String[] value) {
        return findShowsRationalesFromValue(value, showsRationaleMethods);
    }

}
