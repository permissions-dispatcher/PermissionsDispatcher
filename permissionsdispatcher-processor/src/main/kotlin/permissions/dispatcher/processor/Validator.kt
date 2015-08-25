package permissions.dispatcher.processor

import permissions.dispatcher.processor.exceptions.DuplicatedValueException
import permissions.dispatcher.processor.exceptions.NotDefinedException
import permissions.dispatcher.processor.exceptions.WrongClassException

import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import java.lang.invoke.WrongMethodTypeException
import java.util.HashSet

object Validator {

    fun checkNeedsPermissionsSize(permission: List<ExecutableElement>, permissions: List<ExecutableElement>) {
        if (Utils.isEmpty(permission) && Utils.isEmpty(permissions)) {
            throw NotDefinedException("@NeedsPermission or @NeedsPermissions are not defined")
        }
    }

    fun checkClassName(name: String) {
        if (name.endsWith("Activity") || name.endsWith("Fragment")) {
            return
        }
        throw WrongClassException("Annotated class must be finished with 'Activity' or 'Fragment'")
    }

    fun checkDuplicatedValue(methods: List<ExecutableElement>, clazz: Class<out Annotation>) {
        val values = HashSet<String>()
        for (method in methods) {
            val value = Utils.getValueFromAnnotation(method, clazz)
            if (!values.addAll(value)) {
                throw DuplicatedValueException("${value} is duplicated in ${clazz}")
            }
        }
    }

    fun checkPrivateMethods(elements: List<ExecutableElement>) {
        for (element in elements) {
            val modifiers = element.getModifiers()
            if (modifiers.contains(Modifier.PRIVATE)) {
                throw WrongMethodTypeException("Annotated method must be package private or above")
            }
        }
    }

}
