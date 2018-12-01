package permissions.dispatcher.processor.impl.kotlin

import com.squareup.kotlinpoet.FunSpec

interface SensitivePermissionInterface {
    fun addHasSelfPermissionsCondition(builder: FunSpec.Builder, activity: String, permissionField: String)

    fun addRequestPermissionsStatement(builder: FunSpec.Builder, targetParam: String = "this", activityVar: String, requestCodeField: String)
}
