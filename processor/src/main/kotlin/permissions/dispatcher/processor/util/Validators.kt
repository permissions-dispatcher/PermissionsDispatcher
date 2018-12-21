package permissions.dispatcher.processor.util

import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.processor.ProcessorUnit
import permissions.dispatcher.processor.RuntimePermissionsElement
import permissions.dispatcher.processor.TYPE_UTILS
import permissions.dispatcher.processor.exception.*
import java.util.*
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror

private const val WRITE_SETTINGS = "android.permission.WRITE_SETTINGS"
private const val SYSTEM_ALERT_WINDOW = "android.permission.SYSTEM_ALERT_WINDOW"

/**
 * Obtains the [ProcessorUnit] implementation for the provided element.
 * Raises an exception if no suitable implementation exists
 */
fun <K> findAndValidateProcessorUnit(units: List<ProcessorUnit<K>>, element: Element): ProcessorUnit<K> {
    val type = element.asType()
    try {
        return units.first { type.isSubtypeOf(it.getTargetType()) }
    } catch (ex: NoSuchElementException) {
        throw WrongClassException(type)
    }
}

/**
 * Checks the elements in the provided list annotated with an annotation against duplicate values.
 * <p>
 * Raises an exception if any annotation value is found multiple times.
 */
fun <A : Annotation> checkDuplicatedValue(items: List<ExecutableElement>, annotationClass: Class<A>) {
    val allItems: HashSet<List<String>> = hashSetOf()
    items.forEach {
        val permissionValue = it.getAnnotation(annotationClass).permissionValue().sorted()
        if (allItems.contains(permissionValue)) {
            throw DuplicatedValueException(permissionValue, it, annotationClass)
        } else {
            allItems.add(permissionValue)
        }
    }
}

/**
 * Checks the elements in the provided list for elements.
 * <p>
 * Raises an exception if it doesn't contain any elements.
 */
fun <A : Annotation> checkNotEmpty(items: List<ExecutableElement>, rpe: RuntimePermissionsElement, annotationClass: Class<A>) {
    if (items.isEmpty()) {
        throw NoAnnotatedMethodsException(rpe, annotationClass)
    }
}

/**
 * Checks the elements in the provided list annotated with an annotation
 * against private modifiers.
 * <p>
 * Raises an exception if any element contains the "private" modifier.
 */
fun <A : Annotation> checkPrivateMethods(items: List<ExecutableElement>, annotationClass: Class<A>) {
    items.forEach {
        if (it.modifiers.contains(Modifier.PRIVATE)) {
            throw PrivateMethodException(it, annotationClass)
        }
    }
}

/**
 * Checks the return type of the elements in the provided list.
 * <p>
 * Raises an exception if any element specifies a return type other than 'void'.
 */
fun checkMethodSignature(items: List<ExecutableElement>) {
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

fun checkMethodParameters(items: List<ExecutableElement>, numParams: Int, requiredType: TypeMirror? = null) {
    items.forEach {
        val params = it.parameters
        if (numParams == 0 && params.isNotEmpty()) {
            throw NoParametersAllowedException(it)
        }
        if (requiredType == null) {
            return
        }
        if (numParams < params.size) {
            throw WrongParametersException(it, numParams, requiredType)
        }
        // maximum params size is 1
        params.forEach { param ->
            if (!TYPE_UTILS.isSameType(param.asType(), requiredType)) {
                throw WrongParametersException(it, numParams, requiredType)
            }
        }
    }
}

fun <A : Annotation> checkMixPermissionType(items: List<ExecutableElement>, annotationClass: Class<A>) {
    items.forEach {
        val permissionValue = it.getAnnotation(annotationClass).permissionValue()
        if (permissionValue.size > 1) {
            if (permissionValue.contains(WRITE_SETTINGS)) {
                throw MixPermissionTypeException(it, WRITE_SETTINGS)
            } else if (permissionValue.contains(SYSTEM_ALERT_WINDOW)) {
                throw MixPermissionTypeException(it, SYSTEM_ALERT_WINDOW)
            }
        }
    }
}

fun checkSpecialPermissionsWithNeverAskAgain(items: List<ExecutableElement>, annotationClass: Class<OnNeverAskAgain> = OnNeverAskAgain::class.java) {
    items.forEach {
        val permissionValue = it.getAnnotation(annotationClass).permissionValue()
        if (permissionValue.contains(WRITE_SETTINGS) || permissionValue.contains(SYSTEM_ALERT_WINDOW)) {
            throw SpecialPermissionsWithNeverAskAgainException()
        }
    }
}

fun checkDuplicatedMethodName(items: List<ExecutableElement>) {
    items.forEach { item ->
        items.firstOrNull { it != item && it.simpleName == item.simpleName }?.let {
            throw DuplicatedMethodNameException(item)
        }
    }
}
