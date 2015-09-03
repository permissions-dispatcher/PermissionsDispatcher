package permissions.dispatcher.processor.util

import permissions.dispatcher.processor.ProcessorUnit
import permissions.dispatcher.processor.exception.DuplicatedValueException
import permissions.dispatcher.processor.exception.PrivateMethodException
import permissions.dispatcher.processor.exception.WrongClassException
import permissions.dispatcher.processor.exception.WrongReturnTypeException
import java.util.*
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.type.NoType
import javax.lang.model.type.TypeKind

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
            throw DuplicatedValueException(permissionValue, annotationClass)
        }
    }
}

/**
 * Checks the elements in the provided list annotated with an annotation
 * against private modifiers. Raises an exception if any element contains
 * the "private" modifier
 */
fun <A : Annotation> checkPrivateMethods(items: List<ExecutableElement>, annotationClass: Class<A>) {
    items.forEach {
        if (it.getModifiers().contains(Modifier.PRIVATE)) {
            throw PrivateMethodException(it, annotationClass)
        }
    }
}

/**
 * Checks the return type of the elements in the provided list. Raises an exception
 * if any element specifies a return type other than 'void'
 */
fun checkMethodReturnType(items : List<ExecutableElement>) {
    items.forEach {
        if (it.getReturnType().getKind() != TypeKind.VOID) {
            throw WrongReturnTypeException(it)
        }
    }
}
