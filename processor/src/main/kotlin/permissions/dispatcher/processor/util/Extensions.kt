package permissions.dispatcher.processor.util

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.OnShowRationale
import permissions.dispatcher.processor.TYPE_UTILS
import javax.lang.model.element.*
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
 * Returns whether a variable is nullable by inspecting its annotations.
 */
fun VariableElement.isNullable(): Boolean =
        this.annotationMirrors
                .asSequence()
                .map { it.annotationType.simpleString() }
                .toList()
                .contains("Nullable")

/**
 * Maps a variable to its TypeName, applying necessary transformations
 * for Java primitive types & mirroring the variable's nullability settings.
 */
fun VariableElement.asPreparedType(): TypeName =
        this.asType()
                .asTypeName()
                .correctJavaTypeToKotlinType()
                .mapToNullableTypeIf(this.isNullable())

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
                .asSequence()
                .filter { it.hasAnnotation(annotationClass) }
                .map { it as ExecutableElement }
                .toList()

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
 * To avoid KotlinPoet bugs return java.lang.class when type name is in kotlin package.
 * TODO: Remove this method after being addressed on KotlinPoet side.
 */
fun TypeName.correctJavaTypeToKotlinType(): TypeName {
    return if (this is ParameterizedTypeName) {
        val typeArguments = this.typeArguments.map { it.correctJavaTypeToKotlinType() }.toTypedArray()
        val rawType = ClassName.bestGuess(this.rawType.correctJavaTypeToKotlinType().toString())
        return rawType.parameterizedBy(*typeArguments)
    } else when (toString()) {
        "java.lang.Byte" -> ClassName("kotlin", "Byte")
        "java.lang.Double" -> ClassName("kotlin", "Double")
        "java.lang.Object" -> ClassName("kotlin", "Any")
        "java.lang.String" -> ClassName("kotlin", "String")
        "java.util.Set" -> ClassName("kotlin.collections", "MutableSet")
        "java.util.List" -> ClassName("kotlin.collections", "MutableList")
        // https://github.com/permissions-dispatcher/PermissionsDispatcher/issues/599
        // https://github.com/permissions-dispatcher/PermissionsDispatcher/issues/619
        "kotlin.ByteArray", "kotlin.collections.List", "kotlin.collections.Set",
        "kotlin.collections.MutableList", "kotlin.collections.MutableSet" -> this
        else -> this
    }
}

/**
 * Returns this TypeName as nullable or non-nullable based on the given condition.
 */
fun TypeName.mapToNullableTypeIf(nullable: Boolean) = copy(nullable = nullable)
