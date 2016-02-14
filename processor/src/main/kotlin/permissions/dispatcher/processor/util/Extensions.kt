package permissions.dispatcher.processor.util

import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.OnShowRationale
import permissions.dispatcher.processor.TYPE_UTILS
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror

/**
 * Returns the package name of a TypeElement.
 */
fun TypeElement.packageName(): String {
    val qn = this.qualifiedName.toString()
    return qn.substring(0, qn.lastIndexOf('.'))
}

/**
 * Returns the simple name of an Element as a string.
 */
fun Element.simpleString(): String = this.simpleName.toString()

/**
 * Returns the simple name of a TypeMirror as a string.
 */
fun TypeMirror.simpleString(): String {
    val toString: String = this.toString()
    val indexOfDot: Int = toString.lastIndexOf('.')
    return if (indexOfDot == -1) toString else toString.substring(indexOfDot + 1)
}

/**
 * Returns whether or not an Element is annotated with the provided Annotation class.
 */
fun <A : Annotation> Element.hasAnnotation(annotationType: Class<A>): Boolean =
        this.getAnnotation(annotationType) != null

/**
 * Returns the inherent value() of a permission Annotation.
 * <p>
 * If this is invoked on an Annotation that's not defined by PermissionsDispatcher, this returns an empty list.
 */
fun Annotation.permissionValue(): List<String> {
    when (this) {
        is NeedsPermission -> return this.value.asList()
        is OnShowRationale -> return this.value.asList()
        is OnPermissionDenied -> return this.value.asList()
        is OnNeverAskAgain -> return this.value.asList()
    }
    return emptyList()
}

/**
 * Returns a list of enclosed elements annotated with the provided Annotations.
 */
fun <A : Annotation> Element.childElementsAnnotatedWith(annotationClass: Class<A>): List<ExecutableElement> =
        this.enclosedElements
                .filter { it.hasAnnotation(annotationClass) }
                .map { it as ExecutableElement }

/**
 * Returns whether or not a TypeMirror is a subtype of the provided other TypeMirror.
 */
fun TypeMirror.isSubtypeOf(ofType: TypeMirror): Boolean = TYPE_UTILS.isSubtype(this, ofType)
