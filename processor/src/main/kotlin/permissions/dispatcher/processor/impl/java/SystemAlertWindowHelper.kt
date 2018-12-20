package permissions.dispatcher.processor.impl.java

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec

class SystemAlertWindowHelper : SensitivePermissionInterface {

    private val PERMISSION_UTILS = ClassName.get("permissions.dispatcher", "PermissionUtils")
    private val SETTINGS = ClassName.get("android.provider", "Settings")
    private val INTENT = ClassName.get("android.content", "Intent")
    private val URI = ClassName.get("android.net", "Uri")

    override fun addHasSelfPermissionsCondition(builder: MethodSpec.Builder, activityVar: String, permissionField: String) {
        builder.beginControlFlow("if (\$T.hasSelfPermissions(\$N, \$N) || \$T.canDrawOverlays(\$N))", PERMISSION_UTILS, activityVar, permissionField, SETTINGS, activityVar)
    }

    override fun addRequestPermissionsStatement(builder: MethodSpec.Builder, targetParam: String, activityVar: String, requestCodeField: String) {
        builder.addStatement("\$T intent = new \$T(\$T.ACTION_MANAGE_OVERLAY_PERMISSION, \$T.parse(\"package:\" + \$N.getPackageName()))", INTENT, INTENT, SETTINGS, URI, activityVar)
        builder.addStatement("\$N.startActivityForResult(intent, \$N)", targetParam, requestCodeField)
    }
}