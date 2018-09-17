package permissions.dispatcher.processor.impl.kotlin

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec

class WriteSettingsHelper : SensitivePermissionInterface {

    private val PERMISSION_UTILS = ClassName("permissions.dispatcher", "PermissionUtils")
    private val SETTINGS = ClassName("android.provider", "Settings")
    private val INTENT = ClassName("android.content", "Intent")
    private val URI = ClassName("android.net", "Uri")

    override fun addHasSelfPermissionsCondition(builder: FunSpec.Builder, activity: String, permissionField: String) {
        builder.beginControlFlow("if (%T.hasSelfPermissions(%L, *%N) || %T.System.canWrite(%L))", PERMISSION_UTILS, activity, permissionField, SETTINGS, activity)
    }

    override fun addRequestPermissionsStatement(builder: FunSpec.Builder, targetParam: String, activityVar: String, requestCodeField: String) {
        builder.addStatement("val intent = %T(%T.ACTION_MANAGE_WRITE_SETTINGS, %T.parse(\"package:\" + %L.getPackageName()))", INTENT, SETTINGS, URI, activityVar)
        builder.addStatement("%L.startActivityForResult(intent, %N)", targetParam, requestCodeField)
    }
}