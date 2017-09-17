package permissions.dispatcher.processor.util

import com.squareup.kotlinpoet.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.OnShowRationale
import permissions.dispatcher.processor.TYPE_UTILS
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.PackageElement
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror

/**
 * Returns the package name of a TypeElement.
 */
fun TypeElement.packageName() = enclosingElement.packageName()

private fun Element?.packageName(): String {
    return when (this) {
        is TypeElement -> packageName()
        is PackageElement -> qualifiedName.toString()
        else -> this?.enclosingElement?.packageName() ?: ""
    }
}

// to address kotlin internal method try to remove `$module_name_build_variant` from element info.
// ex: showCamera$sample_kotlin_debug → showCamera
internal fun String.trimDollarIfNeeded(): String {
    val index = indexOf("$")
    return if (index == -1) this else substring(0, index)
}

/**
 * Returns the simple name of an Element as a string.
 */
fun Element.simpleString() = this.simpleName.toString().trimDollarIfNeeded()

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

fun FileSpec.Builder.addProperties(properties: List<PropertySpec>): FileSpec.Builder {
    properties.forEach { addProperty(it) }
    return this
}

fun FileSpec.Builder.addFunctions(functions: List<FunSpec>): FileSpec.Builder {
    functions.forEach { addFunction(it) }
    return this
}

fun FileSpec.Builder.addTypes(types: List<TypeSpec>): FileSpec.Builder {
    types.forEach { addType(it) }
    return this
}

/**
 * To avoid KotlinPoet bug that returns java.lang.String when type name is kotlin.String.
 * This method should be removed after addressing on KotlinPoet side.
 */
fun TypeName.checkStringType() =
        if (this.toString() == "java.lang.String") ClassName("kotlin", "String") else this
