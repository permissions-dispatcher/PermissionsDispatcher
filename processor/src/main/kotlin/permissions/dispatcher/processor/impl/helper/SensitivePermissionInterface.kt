package permissions.dispatcher.processor.impl.helper

import com.squareup.javapoet.MethodSpec

interface SensitivePermissionInterface {
    fun addHasSelfPermissionsCondition(builder: MethodSpec.Builder, activityVar: String, permissionField: String)

    fun addRequestPermissionsStatement(builder: MethodSpec.Builder, activityVar: String, requestCodeField: String)
}