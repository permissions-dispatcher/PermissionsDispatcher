package permissions.dispatcher.processor.impl

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import permissions.dispatcher.processor.RuntimePermissionsElement
import permissions.dispatcher.processor.exception.SupportV13MissingException
import permissions.dispatcher.processor.util.*
import javax.lang.model.type.TypeMirror

class NativeFragmentProcessorUnit: BaseProcessorUnit() {

    private val PERMISSION_UTILS_V13: ClassName = ClassName.get("permissions.dispatcher.v13", "PermissionUtilsV13")

    override fun getTargetType(): TypeMirror {
        return typeMirrorOf("android.app.Fragment")
    }

    override fun checkPrerequisites(rpe: RuntimePermissionsElement) {
        try {
            // Check if FragmentCompat can be accessed; if not, throw an exception
            Class.forName("android.support.v13.app.FragmentCompat")

        } catch (ex: ClassNotFoundException) {
            // Thrown if support-v13 is missing on the classpath
            throw SupportV13MissingException(rpe)

        } catch (ex: NoClassDefFoundError) {
            // Expected in success cases, because the Android environment is still missing
            // when this is called from within the Annotation processor. 'FragmentCompat' itself
            // can be resolved, but accessing it requires an Android environment, which doesn't exist
            // since this is an annotation processor
        }
    }

    override fun getActivityName(targetParam: String): String {
        return targetParam + ".getActivity()"
    }

    override fun addShouldShowRequestPermissionRationaleCondition(builder: MethodSpec.Builder, targetParam: String, permissionField: String, isPositiveCondition: Boolean) {
        builder.beginControlFlow("if (\$N\$T.getInstance().shouldShowRequestPermissionRationale(\$N, \$N))", if (isPositiveCondition) "" else "!", PERMISSION_UTILS_V13, targetParam, permissionField)
    }

    override fun addRequestPermissionsStatement(builder: MethodSpec.Builder, targetParam: String, permissionField: String, requestCodeField: String) {
        builder.addStatement("\$T.getInstance().requestPermissions(\$N, \$N, \$N)", PERMISSION_UTILS_V13, targetParam, permissionField, requestCodeField)
    }
}