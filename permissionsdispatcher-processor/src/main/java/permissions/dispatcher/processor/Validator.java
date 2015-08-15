package permissions.dispatcher.processor;

import java.lang.invoke.WrongMethodTypeException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.ShowsRationale;
import permissions.dispatcher.processor.exceptions.DuplicatedValueException;
import permissions.dispatcher.processor.exceptions.NotDefinedException;
import permissions.dispatcher.processor.exceptions.WrongClassException;

import static permissions.dispatcher.processor.Utils.isEmpty;

final class Validator {

    static void checkNeedsPermissionSize(List<ExecutableElement> methods) {
        if (isEmpty(methods)) throw new NotDefinedException("@NeedsPermission is not defined");
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
                throw new DuplicatedValueException(value + " is duplicated in " +  NeedsPermission.class);
            }
        }
    }

    static void checkDuplicatedRationale(List<ExecutableElement> methods) {
        Set<String> values = new HashSet<>();
        for (ExecutableElement method : methods) {
            String value = method.getAnnotation(ShowsRationale.class).value();
            if (!values.add(value)) {
                throw new DuplicatedValueException(value + " is duplicated in " +  ShowsRationale.class);
            }
        }
    }

    static void checkPrivateMethod(List<ExecutableElement> elements) {
        for (ExecutableElement element : elements) {
            Set<Modifier> modifiers = element.getModifiers();
            if (modifiers.contains(Modifier.PRIVATE)) {
                throw new WrongMethodTypeException("Annotated method must be package private or above");
            }
        }
    }

}
