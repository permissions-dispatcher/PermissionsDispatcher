package permissions.dispatcher.processor

import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeVariableName
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnShowRationale
import permissions.dispatcher.processor.util.*
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement

class RuntimePermissionsElement(e: TypeElement) {
    val typeName = TypeName.get(e.asType())
    val typeVariables = e.typeParameters.map { TypeVariableName.get(it) }
    val packageName = e.packageName()
    val inputClassName = e.simpleString()
    val generatedClassName = inputClassName + GEN_CLASS_SUFFIX
    val needsElements = e.childElementsAnnotatedWith(NeedsPermission::class.java)
    val onRationaleElements = e.childElementsAnnotatedWith(OnShowRationale::class.java)
    val onDeniedElements = e.childElementsAnnotatedWith(OnPermissionDenied::class.java)
    val onNeverAskElements = e.childElementsAnnotatedWith(OnNeverAskAgain::class.java)

    init {
        validateNeedsMethods()
        validateRationaleMethods()
        validateDeniedMethods()
        validateNeverAskMethods()
    }

    /* Begin private */

    private fun validateNeedsMethods() {
        checkNotEmpty(needsElements, this, NeedsPermission::class.java)
        checkPrivateMethods(needsElements, NeedsPermission::class.java)
        checkMethodSignature(needsElements)
        checkMixPermissionType(needsElements, NeedsPermission::class.java)
    }

    private fun validateRationaleMethods() {
        checkDuplicatedValue(onRationaleElements, OnShowRationale::class.java)
        checkPrivateMethods(onRationaleElements, OnShowRationale::class.java)
        checkMethodSignature(onRationaleElements)
        checkMethodParameters(onRationaleElements, 1, typeMirrorOf("permissions.dispatcher.PermissionRequest"))
    }

    private fun validateDeniedMethods() {
        checkDuplicatedValue(onDeniedElements, OnPermissionDenied::class.java)
        checkPrivateMethods(onDeniedElements, OnPermissionDenied::class.java)
        checkMethodSignature(onDeniedElements)
        checkMethodParameters(onDeniedElements, 0)
    }

    private fun validateNeverAskMethods() {
        checkDuplicatedValue(onNeverAskElements, OnNeverAskAgain::class.java)
        checkPrivateMethods(onNeverAskElements, OnNeverAskAgain::class.java)
        checkMethodSignature(onNeverAskElements)
        checkMethodParameters(onNeverAskElements, 0)
    }

    /* Begin public */

    fun findOnRationaleForNeeds(needsElement: ExecutableElement): ExecutableElement? {
        return findMatchingMethodForNeeds(needsElement, onRationaleElements, OnShowRationale::class.java)
    }

    fun findOnDeniedForNeeds(needsElement: ExecutableElement): ExecutableElement? {
        return findMatchingMethodForNeeds(needsElement, onDeniedElements, OnPermissionDenied::class.java)
    }

    fun findOnNeverAskForNeeds(needsElement: ExecutableElement): ExecutableElement? {
        return findMatchingMethodForNeeds(needsElement, onNeverAskElements, OnNeverAskAgain::class.java)
    }
}