package permissions.dispatcher.processor;

import permissions.dispatcher.*;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Arrays.deepEquals;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static permissions.dispatcher.processor.ConstantsProvider.PERMISSION_PREFIX;
import static permissions.dispatcher.processor.ConstantsProvider.REQUEST_CODE_PREFIX;

final class Utils {

    static List<RuntimePermissionsAnnotatedElement> getAnnotatedClasses(RoundEnvironment env) {
        List<RuntimePermissionsAnnotatedElement> models = new ArrayList<>();
        Set<? extends Element> elements = env.getElementsAnnotatedWith(RuntimePermissions.class);
        for (Element element : elements) {
            models.add(new RuntimePermissionsAnnotatedElement((TypeElement) element));
        }
        return models;
    }

    static List<ExecutableElement> findMethods(Element element, Class<? extends Annotation> clazz) {
        List<ExecutableElement> methods = new ArrayList<>();
        for (Element enclosedElement : element.getEnclosedElements()) {
            Annotation annotation = enclosedElement.getAnnotation(clazz);
            if (annotation != null) {
                methods.add((ExecutableElement) enclosedElement);
            }
        }
        return methods;
    }

    static ExecutableElement findShowsRationaleFromValue(String value, List<ExecutableElement> elements) {
        for (ExecutableElement element : elements) {
            ShowsRationale annotation = element.getAnnotation(ShowsRationale.class);
            if (value.equals(annotation.value())) {
                return element;
            }
        }
        return null;
    }

    static ExecutableElement findShowsRationalesFromValue(String[] value, List<ExecutableElement> elements) {
        for (ExecutableElement element : elements) {
            ShowsRationales annotation = element.getAnnotation(ShowsRationales.class);
            if (deepEquals(value, annotation.value())) {
                return element;
            }
        }
        return null;
    }

    static ExecutableElement findDeniedPermissionFromValue(String value, List<ExecutableElement> elements) {
        for (ExecutableElement element : elements) {
            DeniedPermission annotation = element.getAnnotation(DeniedPermission.class);
            if (value.equals(annotation.value())) {
                return element;
            }
        }
        return null;
    }

    static ExecutableElement findDeniedPermissionFromValue(String[] value, List<ExecutableElement> elements) {
        for (ExecutableElement element : elements) {
            DeniedPermissions annotation = element.getAnnotation(DeniedPermissions.class);
            if (deepEquals(value, annotation.value())) {
                return element;
            }
        }
        return null;
    }

    static <A extends Annotation> List<String> getValueFromAnnotation(ExecutableElement element, Class<A> clazz) {
        if (Objects.equals(clazz, NeedsPermission.class)) {
            return singletonList(element.getAnnotation(NeedsPermission.class).value());
        } else if (Objects.equals(clazz, NeedsPermissions.class)) {
            return asList(element.getAnnotation(NeedsPermissions.class).value());
        } else if (Objects.equals(clazz, ShowsRationale.class)) {
            return singletonList(element.getAnnotation(ShowsRationale.class).value());
        } else if (Objects.equals(clazz, ShowsRationales.class)) {
            return asList(element.getAnnotation(ShowsRationales.class).value());
        } else if (Objects.equals(clazz, DeniedPermission.class)) {
            return singletonList(element.getAnnotation(DeniedPermission.class).value());
        } else if (Objects.equals(clazz, DeniedPermissions.class)) {
            return asList(element.getAnnotation(DeniedPermissions.class).value());
        } else {
            return emptyList();
        }
    }

    static String getPackageName(String name) {
        return name.substring(0, name.lastIndexOf("."));
    }

    static String getClassName(String name) {
        return name.substring(name.lastIndexOf(".") + 1);
    }

    static String getRequestCodeFieldName(String name) {
        return REQUEST_CODE_PREFIX + name.toUpperCase();
    }

    static String getPermissionFieldName(String name) {
        return PERMISSION_PREFIX + name.toUpperCase();
    }

    static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    static String toString(String... array) {
        if (array == null)
            return null;
        int max = array.length - 1;
        StringBuilder b = new StringBuilder();
        b.append('{');
        for (int i = 0; ; i++) {
            b.append("\"").append(array[i]).append("\"");
            if (i == max)
                return b.append('}').toString();
            b.append(", ");
        }
    }

}
