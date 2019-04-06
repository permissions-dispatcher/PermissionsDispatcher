package permissions.dispatcher.processor

import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeVariableName
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.asTypeVariableName
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.OnShowRationale
import permissions.dispatcher.processor.util.*
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement

class RuntimePermissionsElement(val element: TypeElement) {
    val typeName: TypeName = TypeName.get(element.asType())
    val ktTypeName = element.asType().asTypeName()
    val typeVariables = element.typeParameters.map { TypeVariableName.get(it) }
    val ktTypeVariables = element.typeParameters.map { it.asTypeVariableName() }
    val packageName = element.packageName()
    val inputClassName = element.simpleString()
    val generatedClassName = inputClassName + GEN_CLASS_SUFFIX
    val needsElements = element.childElementsAnnotatedWith(NeedsPermission::class.java)
    private val onRationaleElements = element.childElementsAnnotatedWith(OnShowRationale::class.java)
    private val onDeniedElements = element.childElementsAnnotatedWith(OnPermissionDenied::class.java)
    private val onNeverAskElements = element.childElementsAnnotatedWith(OnNeverAskAgain::class.java)

    init {
        validateNeedsMethods()
        validateRationaleMethods()
        validateDeniedMethods()
        validateNeverAskMethods()
    }

    private fun validateNeedsMethods() {
        checkNotEmpty(needsElements, this, NeedsPermission::class.java)
        checkPrivateMethods(needsElements, NeedsPermission::class.java)
        checkMethodSignature(needsElements)
        checkMixPermissionType(needsElements, NeedsPermission::class.java)
        checkDuplicatedMethodName(needsElements)
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
        checkSpecialPermissionsWithNeverAskAgain(onNeverAskElements)
    }

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