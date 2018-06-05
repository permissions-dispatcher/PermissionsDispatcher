package permissions.dispatcher.processor.impl.java

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec

class WriteSettingsHelper : SensitivePermissionInterface {

    private val PERMISSION_UTILS = ClassName.get("permissions.dispatcher", "PermissionUtils")
    private val SETTINGS = ClassName.get("android.provider", "Settings")
    private val INTENT = ClassName.get("android.content", "Intent")
    private val URI = ClassName.get("android.net", "Uri")

    override fun addHasSelfPermissionsCondition(builder: MethodSpec.Builder, activityParam: String, permissionField: String) {
        builder.beginControlFlow("if (\$T.hasSelfPermissions(\$N, \$N) || \$T.System.canWrite(\$N))", PERMISSION_UTILS, activityParam, permissionField, SETTINGS, activityParam)
    }

    override fun addRequestPermissionsStatement(builder: MethodSpec.Builder, targetParam: String, activityParam: String, requestCodeField: String) {
        builder.addStatement("\$T intent = new \$T(\$T.ACTION_MANAGE_WRITE_SETTINGS, \$T.parse(\"package:\" + \$N.getPackageName()))", INTENT, INTENT, SETTINGS, URI, activityParam)
        builder.addStatement("\$N.startActivityForResult(intent, \$N)", targetParam, requestCodeField)
    }
}