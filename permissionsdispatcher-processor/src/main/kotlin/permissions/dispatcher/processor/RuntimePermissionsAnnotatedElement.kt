package permissions.dispatcher.processor

import com.squareup.javapoet.ClassName
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.NeedsPermissions
import permissions.dispatcher.ShowsRationale
import permissions.dispatcher.ShowsRationales

import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import java.util.ArrayList

import permissions.dispatcher.processor.ConstantsProvider.CLASS_SUFFIX
import permissions.dispatcher.processor.Utils.*
import permissions.dispatcher.processor.Validator.*

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
        checkClassName(className)
        classType = ClassType.Companion.getClassType(className)
        needsPermissionMethods = findMethods(element, javaClass<NeedsPermission>())
        validateNeedsPermissionMethods()
        needsPermissionsMethods = findMethods(element, javaClass<NeedsPermissions>())
        validateNeedsPermissionsMethods()
        checkNeedsPermissionsSize(needsPermissionMethods, needsPermissionsMethods)
        showsRationaleMethods = findMethods(element, javaClass<ShowsRationale>())
        validateShowRationaleMethods()
        showsRationalesMethods = findMethods(element, javaClass<ShowsRationales>())
        validateShowRationalesMethods()
    }

    private fun validateNeedsPermissionMethods() {
        checkDuplicatedValue(needsPermissionMethods, javaClass<NeedsPermission>())
        checkPrivateMethods(needsPermissionMethods)
    }

    private fun validateNeedsPermissionsMethods() {
        checkDuplicatedValue(needsPermissionsMethods, javaClass<NeedsPermissions>())
        checkPrivateMethods(needsPermissionsMethods)
    }

    private fun validateShowRationaleMethods() {
        checkDuplicatedValue(showsRationaleMethods, javaClass<ShowsRationale>())
        checkPrivateMethods(showsRationaleMethods)
    }

    private fun validateShowRationalesMethods() {
        checkDuplicatedValue(showsRationalesMethods, javaClass<ShowsRationales>())
        checkPrivateMethods(showsRationalesMethods)
    }

    public fun getClassName(): ClassName {
        return ClassName.get(packageName, className)
    }

    public fun getDispatcherClassName(): String {
        return className + CLASS_SUFFIX
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
        return findShowsRationaleFromValue(value, showsRationaleMethods)
    }

    public fun getShowsRationaleFromValue(value: Array<String>): ExecutableElement {
        return findShowsRationalesFromValue(value, showsRationalesMethods)
    }

}
