package permissions.dispatcher.processor.impl.kotlin

import com.squareup.kotlinpoet.FunSpec

interface SensitivePermissionInterface {
    fun addHasSelfPermissionsCondition(builder: FunSpec.Builder, activityParam: String, permissionField: String)

    fun addRequestPermissionsStatement(builder: FunSpec.Builder, targetParam: String = "this", activityParam: String, requestCodeField: String)
}
