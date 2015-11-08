package permissions.dispatcher.processor.util

import permissions.dispatcher.processor.ProcessorUnit
import permissions.dispatcher.processor.RuntimePermissionsElement
import permissions.dispatcher.processor.exception.*
import java.util.*
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror

/**
 * Obtains the ProcessorUnit implementation for the provided element.
 * Raises an exception if no suitable implementation exists
 */
fun findAndValidateProcessorUnit(units: List<ProcessorUnit>, e: Element): ProcessorUnit {
    val type = e.asType()
    try {
        return units.first { type.isSubtypeOf(it.getTargetType()) }
    } catch (ex: NoSuchElementException) {
        throw WrongClassException(type)
    }
}

/**
 * Checks the elements in the provided list annotated with an annotation
 * against duplicate values. Raises an exception if any annotation value
 * is found multiple times
 */
fun <A : Annotation> checkDuplicatedValue(items: List<ExecutableElement>, annotationClass: Class<A>) {
    val set: HashSet<String> = hashSetOf()
    items.forEach {
        val permissionValue: List<String> = it.getAnnotation(annotationClass).permissionValue()
        if (!set.addAll(permissionValue)) {
            throw DuplicatedValueException(permissionValue, it, annotationClass)
        }
    }
}

/**
 * Checks the elements in the provided list for elements. Raises an exception if
 * it doesn't contain any elements
 */
fun <A : Annotation> checkNotEmpty(items: List<ExecutableElement>, rpe: RuntimePermissionsElement, annotationClass: Class<A>) {
    if (items.isEmpty()) {
        throw NoAnnotatedMethodsException(rpe, annotationClass)
    }
}

/**
 * Checks the elements in the provided list annotated with an annotation
 * against private modifiers. Raises an exception if any element contains
 * the "private" modifier
 */
fun <A : Annotation> checkPrivateMethods(items: List<ExecutableElement>, annotationClass: Class<A>) {
    items.forEach {
        if (it.modifiers.contains(Modifier.PRIVATE)) {
            throw PrivateMethodException(it, annotationClass)
        }
    }
}

/**
 * Checks the return type of the elements in the provided list. Raises an exception
 * if any element specifies a return type other than 'void'
 */
fun checkMethodSignature(items : List<ExecutableElement>) {
    items.forEach {
        // Allow 'void' return type only
        if (it.returnType.kind != TypeKind.VOID) {
            throw WrongReturnTypeException(it)
        }
        // Allow methods without 'throws' declaration only
        if (it.thrownTypes.isNotEmpty()) {
            throw NoThrowsAllowedException(it)
        }
    }
}

fun checkMethodParameters(items : List<ExecutableElement>, numParams: Int, vararg requiredTypes: TypeMirror) {
    items.forEach {
        // Check each element's parameters against the requirements
        val params = it.parameters
        if (numParams == 0 && params.isNotEmpty()) {
            throw NoParametersAllowedException(it)
        }

        if (numParams != params.size) {
            throw WrongParametersException(it, requiredTypes)
        }

        params.forEachIndexed { i, param ->
            val requiredType = requiredTypes[i]
            if (!param.asType().isSubtypeOf(requiredType)) {
                throw WrongParametersException(it, requiredTypes)
            }
        }
    }
}
