package permissions.dispatcher.processor.impl.helper

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec

class WriteSettingsHelper : SensitivePermissionInterface {

    private val PERMISSION_UTILS = ClassName.get("permissions.dispatcher", "PermissionUtils")
    private val SETTINGS = ClassName.get("android.provider", "Settings")
    private val INTENT = ClassName.get("android.content", "Intent")
    private val URI = ClassName.get("android.net", "Uri")

    override fun addHasSelfPermissionsCondition(builder: MethodSpec.Builder, activityVar: String, permissionField: String) {
        builder.beginControlFlow("if (\$T.hasSelfPermissions(\$N, \$N) || \$T.System.canWrite(\$N))", PERMISSION_UTILS, activityVar, permissionField, SETTINGS, activityVar)
    }

    override fun addRequestPermissionsStatement(builder: MethodSpec.Builder, activityVar: String, requestCodeField: String) {
        builder.addStatement("\$T intent = new \$T(\$T.ACTION_MANAGE_WRITE_SETTINGS, \$T.parse(\"package:\" + \$N.getPackageName()))", INTENT, INTENT, SETTINGS, URI, activityVar)
        builder.addStatement("\$N.startActivityForResult(intent, \$N)", activityVar, requestCodeField)
    }
}