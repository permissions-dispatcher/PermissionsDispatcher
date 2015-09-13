package permissions.dispatcher.processor;

import java.lang.annotation.Annotation;
import java.lang.invoke.WrongMethodTypeException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;

import permissions.dispatcher.processor.exceptions.DuplicatedValueException;
import permissions.dispatcher.processor.exceptions.MatchedValueMissingException;
import permissions.dispatcher.processor.exceptions.NotDefinedException;
import permissions.dispatcher.processor.exceptions.WrongClassException;

import static permissions.dispatcher.processor.Utils.getValueFromAnnotation;
import static permissions.dispatcher.processor.Utils.isEmpty;

final class Validator {

    private Validator() {
    }

    static void checkNeedsPermissionsSize(List<ExecutableElement> permission, List<ExecutableElement> permissions) {
        if (isEmpty(permission) && isEmpty(permissions)) {
            throw new NotDefinedException("@NeedsPermission or @NeedsPermissions are not defined");
        }
    }

    static void checkClassType(ClassType classType) {
        if (classType != null) {
            return;
        }
        throw new WrongClassException("Annotated class must be a subclass of 'android.app.Activity' or 'android.support.v4.app.Fragment'");
    }

    static void checkDuplicatedValue(List<ExecutableElement> methods, Class<? extends Annotation> clazz) {
        Set<String> values = new HashSet<>();
        for (ExecutableElement method : methods) {
            List<String> value = getValueFromAnnotation(method, clazz);
            if (!values.addAll(value)) {
                throw new DuplicatedValueException(value + " is duplicated in " + clazz);
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

    static void checkMatchingValues(List<ExecutableElement> elements1, Class<? extends Annotation> clazz1, List<ExecutableElement> elements2, Class<? extends Annotation> clazz2) {
        for (ExecutableElement element1 : elements1) {
            boolean matchFound = false;
            List<String> value1 = getValueFromAnnotation(element1, clazz1);
            for (ExecutableElement element2 : elements2) {
                List<String> value2 = getValueFromAnnotation(element2, clazz2);
                if (value1.equals(value2)) {
                    matchFound = true;
                    break;
                }
            }
            if (!matchFound) {
                throw new MatchedValueMissingException("@DeniedPermissions for " + value1 + " doesn't have a matching @NeedsPermissions method");
            }
        }
    }

}
