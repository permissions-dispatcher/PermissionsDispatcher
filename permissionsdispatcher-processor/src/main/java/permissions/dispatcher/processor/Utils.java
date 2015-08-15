package permissions.dispatcher.processor;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

import permissions.dispatcher.RuntimePermissions;
import permissions.dispatcher.ShowsRationale;

import static permissions.dispatcher.processor.ConstantsProvider.FIELD_PREFIX;

final class Utils {

    private Utils() {
    }

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

    static ExecutableElement findShowsRationalFromValue(String value, List<ExecutableElement> elements) {
        for (ExecutableElement element : elements) {
            ShowsRationale annotation = element.getAnnotation(ShowsRationale.class);
            if (value.equals(annotation.value())) {
                return element;
            }
        }
        return null;
    }

    static String getPackageName(String name) {
        return name.substring(0, name.lastIndexOf("."));
    }

    static String getClassName(String name) {
        return name.substring(name.lastIndexOf(".") + 1);
    }

    static String getFieldName(String name) {
        return FIELD_PREFIX + name.toUpperCase();
    }

    static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

}
