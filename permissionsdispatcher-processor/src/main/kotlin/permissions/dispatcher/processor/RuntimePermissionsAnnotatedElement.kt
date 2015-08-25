package permissions.dispatcher.processor

import com.squareup.javapoet.ClassName
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.NeedsPermissions
import permissions.dispatcher.ShowsRationale
import permissions.dispatcher.ShowsRationales

import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import java.util.ArrayList

class RuntimePermissionsAnnotatedElement(element: TypeElement) {

    public val packageName: String

    private val className: String

    public val classType: ClassType

    public val needsPermissionMethods: List<ExecutableElement>

    public val needsPermissionsMethods: List<ExecutableElement>

    private val showsRationaleMethods: List<ExecutableElement>

    private val showsRationalesMethods: List<ExecutableElement>

    init {
        val qualifiedName = element.getQualifiedName().toString()
        packageName = Utils.getPackageName(qualifiedName)
        className = Utils.getClassName(qualifiedName)
        Validator.checkClassName(className)
        classType = ClassType.Companion.getClassType(className) as ClassType
        needsPermissionMethods = Utils.findMethods(element, javaClass<NeedsPermission>())
        validateNeedsPermissionMethods()
        needsPermissionsMethods = Utils.findMethods(element, javaClass<NeedsPermissions>())
        validateNeedsPermissionsMethods()
        Validator.checkNeedsPermissionsSize(needsPermissionMethods, needsPermissionsMethods)
        showsRationaleMethods = Utils.findMethods(element, javaClass<ShowsRationale>())
        validateShowRationaleMethods()
        showsRationalesMethods = Utils.findMethods(element, javaClass<ShowsRationales>())
        validateShowRationalesMethods()
    }

    private fun validateNeedsPermissionMethods() {
        Validator.checkDuplicatedValue(needsPermissionMethods, javaClass<NeedsPermission>())
        Validator.checkPrivateMethods(needsPermissionMethods)
    }

    private fun validateNeedsPermissionsMethods() {
        Validator.checkDuplicatedValue(needsPermissionsMethods, javaClass<NeedsPermissions>())
        Validator.checkPrivateMethods(needsPermissionsMethods)
    }

    private fun validateShowRationaleMethods() {
        Validator.checkDuplicatedValue(showsRationaleMethods, javaClass<ShowsRationale>())
        Validator.checkPrivateMethods(showsRationaleMethods)
    }

    private fun validateShowRationalesMethods() {
        Validator.checkDuplicatedValue(showsRationalesMethods, javaClass<ShowsRationales>())
        Validator.checkPrivateMethods(showsRationalesMethods)
    }

    public fun getClassName(): ClassName {
        return ClassName.get(packageName, className)
    }

    public fun getDispatcherClassName(): String {
        return className + ConstantsProvider.CLASS_SUFFIX
    }

    public fun getAllNeedsPermissionsMethods(): List<ExecutableElement> {
        return object : ArrayList<ExecutableElement>() {
            init {
                addAll(needsPermissionMethods)
                addAll(needsPermissionsMethods)
            }
        }
    }

    public fun getShowsRationaleFromValue(value: String): ExecutableElement {
        return Utils.findShowsRationaleFromValue(value, showsRationaleMethods) as ExecutableElement
    }

    public fun getShowsRationaleFromValue(value: Array<String>): ExecutableElement {
        return Utils.findShowsRationalesFromValue(value, showsRationalesMethods) as ExecutableElement
    }

}
