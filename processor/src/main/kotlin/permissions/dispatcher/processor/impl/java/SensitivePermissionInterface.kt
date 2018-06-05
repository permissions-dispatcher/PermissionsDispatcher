package permissions.dispatcher.processor.impl.java

import com.squareup.javapoet.MethodSpec

interface SensitivePermissionInterface {
    fun addHasSelfPermissionsCondition(builder: MethodSpec.Builder, activityParam: String, permissionField: String)

    fun addRequestPermissionsStatement(builder: MethodSpec.Builder, targetParam: String, activityParam: String, requestCodeField: String)
}