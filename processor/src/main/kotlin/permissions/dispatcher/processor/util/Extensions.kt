package permissions.dispatcher.processor.util

import permissions.dispatcher.Needs
import permissions.dispatcher.OnDenied
import permissions.dispatcher.OnRationale
import permissions.dispatcher.processor.TYPE_UTILS
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror

/**
 * Returns the package name of a TypeElement
 */
fun TypeElement.packageName(): String {
    val qn = this.getQualifiedName().toString()
    return qn.substring(0, qn.lastIndexOf('.'))
}

/**
 * Returns the simple name of an Element as a string
 */
fun Element.simpleString(): String {
    return this.getSimpleName().toString()
}

/**
 * Returns whether or not an Element is annotated with the provided Annotation class
 */
fun <A : Annotation> Element.hasAnnotation(annotationType: Class<A>): Boolean {
    return this.getAnnotation(annotationType) != null
}

/**
 * Returns the inherent value() of a permission Annotation. If
 * this is invoked on an Annotation that's not defined by
 * PermissionsDispatcher, this returns an empty list
 */
fun Annotation.permissionValue(): List<String> {
    if (annotationType().equals(javaClass<Needs>())) {
        return (this as Needs).value.asList()
    } else if (annotationType().equals(javaClass<OnRationale>())) {
        return (this as OnRationale).value.asList()
    } else if (annotationType().equals(javaClass<OnDenied>())) {
        return (this as OnDenied).value.asList()
    }
    return emptyList()
}

/**
 * Returns a list of enclosed elements annotated with the provided Annotations
 */
fun <A : Annotation> Element.childElementsAnnotatedWith(annotationClass: Class<A>) : List<ExecutableElement> {
    return this.getEnclosedElements()
            .filter { it.hasAnnotation(annotationClass) }
            .map { it as ExecutableElement }
}

/**
 * Returns whether or not a TypeMirror is a subtype of the provided other TypeMirror
 */
fun TypeMirror.isSubtypeOf(ofType: TypeMirror): Boolean {
    return TYPE_UTILS.isSubtype(this, ofType)
}
