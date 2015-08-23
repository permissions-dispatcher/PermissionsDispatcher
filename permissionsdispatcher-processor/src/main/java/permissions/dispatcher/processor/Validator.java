package permissions.dispatcher.processor;

import java.lang.invoke.WrongMethodTypeException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.NeedsPermissions;
import permissions.dispatcher.ShowsRationale;
import permissions.dispatcher.ShowsRationales;
import permissions.dispatcher.processor.exceptions.DuplicatedValueException;
import permissions.dispatcher.processor.exceptions.NotDefinedException;
import permissions.dispatcher.processor.exceptions.WrongClassException;

import static java.util.Arrays.asList;
import static permissions.dispatcher.processor.Utils.isEmpty;

final class Validator {

    private Validator() {
    }

    static void checkNeedsPermissionsSize(List<ExecutableElement> methods) {
        if (isEmpty(methods)) {
            throw new NotDefinedException("@NeedsPermission or @NeedsPermissions are not defined");
        }
    }

    static void checkClassName(String name) {
        if (name.endsWith("Activity") || name.endsWith("Fragment")) {
            return;
        }
        throw new WrongClassException("Annotated class must be finished with 'Activity' or 'Fragment'");
    }

    static void checkDuplicatedPermission(List<ExecutableElement> methods) {
        Set<String> values = new HashSet<>();
        for (ExecutableElement method : methods) {
            String value = method.getAnnotation(NeedsPermission.class).value();
            if (!values.add(value)) {
                throw new DuplicatedValueException(value + " is duplicated in " + NeedsPermission.class);
            }
        }
    }

    static void checkDuplicatedPermissions(List<ExecutableElement> methods) {
        Set<String> values = new HashSet<>();
        for (ExecutableElement method : methods) {
            String[] value = method.getAnnotation(NeedsPermissions.class).value();
            if (!values.addAll(asList(value))) {
                throw new DuplicatedValueException(Arrays.toString(value) + " is duplicated in " + NeedsPermissions.class);
            }
        }
    }

    static void checkDuplicatedRationale(List<ExecutableElement> methods) {
        Set<String> values = new HashSet<>();
        for (ExecutableElement method : methods) {
            String value = method.getAnnotation(ShowsRationale.class).value();
            if (!values.add(value)) {
                throw new DuplicatedValueException(value + " is duplicated in " + ShowsRationale.class);
            }
        }
    }

    static void checkDuplicatedRationales(List<ExecutableElement> methods) {
        Set<String> values = new HashSet<>();
        for (ExecutableElement method : methods) {
            String[] value = method.getAnnotation(ShowsRationales.class).value();
            if (!values.addAll(asList(value))) {
                throw new DuplicatedValueException(Arrays.toString(value) + " is duplicated in " + ShowsRationales.class);
            }
        }
    }

    static void checkPrivateMethods(List<ExecutableElement> elements) {
        for (ExecutableElement element : elements) {
            Set<Modifier> modifiers = element.getModifiers();
            if (modifiers.contains(Modifier.PRIVATE)) {
                throw new WrongMethodTypeException("Annotated method must be package private or above");
            }
        }
    }

}
