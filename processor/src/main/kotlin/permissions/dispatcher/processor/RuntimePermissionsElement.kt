package permissions.dispatcher.processor

import com.squareup.javapoet.TypeName
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnShowRationale
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
    val needsElements = e.childElementsAnnotatedWith(javaClass<NeedsPermission>())
    val onRationaleElements = e.childElementsAnnotatedWith(javaClass<OnShowRationale>())
    val onDeniedElements = e.childElementsAnnotatedWith(javaClass<OnPermissionDenied>())
    init {
        // Validate annotated elements
        validateNeedsMethods()
        validateRationaleMethods()
        validateDeniedMethods()
    }

    /* Begin private */

    private fun validateNeedsMethods() {
        checkNotEmpty(needsElements, this, javaClass<NeedsPermission>())
        checkDuplicatedValue(needsElements, javaClass<NeedsPermission>())
        checkPrivateMethods(needsElements, javaClass<NeedsPermission>())
        checkMethodSignature(needsElements)
        checkMethodParameters(needsElements, 0)
    }

    private fun validateRationaleMethods() {
        checkDuplicatedValue(onRationaleElements, javaClass<OnShowRationale>())
        checkPrivateMethods(onRationaleElements, javaClass<OnShowRationale>())
        checkMethodSignature(onRationaleElements)
        checkMethodParameters(onRationaleElements, 1, typeMirrorOf("permissions.dispatcher.PermissionRequest"))
    }

    private fun validateDeniedMethods() {
        checkDuplicatedValue(onDeniedElements, javaClass<OnPermissionDenied>())
        checkPrivateMethods(onDeniedElements, javaClass<OnPermissionDenied>())
        checkMethodSignature(onDeniedElements)
        checkMethodParameters(onDeniedElements, 0)
    }

    /* Begin public */

    fun findOnRationaleForNeeds(needsElement: ExecutableElement): ExecutableElement? {
        return findMatchingMethodForNeeds(needsElement, onRationaleElements, javaClass<OnShowRationale>())
    }

    fun findOnDeniedForNeeds(needsElement: ExecutableElement): ExecutableElement? {
        return findMatchingMethodForNeeds(needsElement, onDeniedElements, javaClass<OnPermissionDenied>())
    }
}