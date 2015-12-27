package permissions.dispatcher.processor.impl

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import permissions.dispatcher.processor.RuntimePermissionsElement
import permissions.dispatcher.processor.util.*
import javax.lang.model.type.TypeMirror

/**
 * ProcessorUnit implementation for Activity classes.
 */
class ActivityProcessorUnit : BaseProcessorUnit() {

    private val ACTIVITY_COMPAT = ClassName.get("android.support.v4.app", "ActivityCompat")

    override fun getTargetType(): TypeMirror {
        return typeMirrorOf("android.app.Activity")
    }

    override fun checkPrerequisites(rpe: RuntimePermissionsElement) {
        // Nothing to check
    }

    override fun addHasSelfPermissionsCondition(builder: MethodSpec.Builder, targetParam: String, permissionField: String) {
        builder.beginControlFlow("if (\$T.hasSelfPermissions(\$N, \$N))", PERMISSION_UTILS, targetParam, permissionField)
    }

    override fun addShouldShowRequestPermissionRationaleCondition(builder: MethodSpec.Builder, targetParam: String, permissionField: String) {
        builder.beginControlFlow("if (\$T.shouldShowRequestPermissionRationale(\$N, \$N))", PERMISSION_UTILS, targetParam, permissionField)
    }

    override fun addRequestPermissionsStatement(builder: MethodSpec.Builder, targetParam: String, permissionField: String, requestCodeField: String) {
        builder.addStatement("\$T.requestPermissions(\$N, \$N, \$N)", ACTIVITY_COMPAT, targetParam, permissionField, requestCodeField)
    }
}