package permissions.dispatcher.processor.impl

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import permissions.dispatcher.processor.RuntimePermissionsElement
import permissions.dispatcher.processor.util.*
import javax.lang.model.type.TypeMirror

/**
 * ProcessorUnit implementation for Fragments defined in the support-v4 library.
 */
class SupportFragmentProcessorUnit: BaseProcessorUnit() {

    private val ACTIVITY_LOCAL_VAR = "activity"

    private val ACTIVITY: ClassName = ClassName.get("android.app", "Activity")

    override fun getTargetType(): TypeMirror {
        return typeMirrorOf("android.support.v4.app.Fragment")
    }

    override fun checkPrerequisites(rpe: RuntimePermissionsElement) {
        // Nothing to check
    }

    override fun addHasSelfPermissionsCondition(builder: MethodSpec.Builder, targetParam: String, permissionField: String) {
        builder.addStatement("\$T \$N = \$N.getActivity()", ACTIVITY, ACTIVITY_LOCAL_VAR, targetParam)
        // Add the conditional for when permission has already been granted
        builder.beginControlFlow("if (\$T.hasSelfPermissions(\$N, \$N))", PERMISSION_UTILS, ACTIVITY_LOCAL_VAR, permissionField)
    }

    override fun addShouldShowRequestPermissionRationaleCondition(builder: MethodSpec.Builder, targetParam: String, permissionField: String) {
        builder.beginControlFlow("if (\$T.shouldShowRequestPermissionRationale(\$N, \$N))", PERMISSION_UTILS, ACTIVITY_LOCAL_VAR, permissionField)
    }

    override fun addRequestPermissionsStatement(builder: MethodSpec.Builder, targetParam: String, permissionField: String, requestCodeField: String) {
        builder.addStatement("\$N.requestPermissions(\$N, \$N)", targetParam, permissionField, requestCodeField)
    }

}