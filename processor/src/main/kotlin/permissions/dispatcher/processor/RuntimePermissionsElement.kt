package permissions.dispatcher.processor

import com.squareup.javapoet.TypeName
import permissions.dispatcher.OnDenied
import permissions.dispatcher.Needs
import permissions.dispatcher.OnRationale
import permissions.dispatcher.processor.util.*
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror

class RuntimePermissionsElement(e: TypeElement) {
    val typeName = TypeName.get(e.asType())
    val packageName = e.packageName()
    val inputClassName = e.simpleString()
    val generatedClassName = inputClassName + GEN_CLASS_SUFFIX
    val needsElements = e.childElementsAnnotatedWith(javaClass<Needs>())
    val onRationaleElements = e.childElementsAnnotatedWith(javaClass<OnRationale>())
    val onDeniedElements = e.childElementsAnnotatedWith(javaClass<OnDenied>())
    init {
        // Validate annotated elements
        validateNeedsMethods()
        validateRationaleMethods()
        validateDeniedMethods()
    }

    /* Begin private */

    private fun validateNeedsMethods() {
        checkPrivateMethods(needsElements, javaClass<Needs>())
        checkMethodReturnType(needsElements)
    }

    private fun validateRationaleMethods() {
        checkDuplicatedValue(onRationaleElements, javaClass<OnRationale>())
        checkPrivateMethods(onRationaleElements, javaClass<OnRationale>())
        checkMethodReturnType(onRationaleElements)
    }

    private fun validateDeniedMethods() {
        checkDuplicatedValue(onDeniedElements, javaClass<OnDenied>())
        checkPrivateMethods(onDeniedElements, javaClass<OnDenied>())
        checkMethodReturnType(onDeniedElements)
    }

    /* Begin public */

    fun findOnRationaleForNeeds(needsElement: ExecutableElement): ExecutableElement? {
        return findMatchingMethodForNeeds(needsElement, onRationaleElements, javaClass<OnRationale>())
    }

    fun findOnDeniedForNeeds(needsElement: ExecutableElement): ExecutableElement? {
        return findMatchingMethodForNeeds(needsElement, onDeniedElements, javaClass<OnDenied>())
    }
}