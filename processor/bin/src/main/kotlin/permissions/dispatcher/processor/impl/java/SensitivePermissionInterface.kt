package permissions.dispatcher.processor.impl.java

import com.squareup.javapoet.MethodSpec

interface SensitivePermissionInterface {
    fun addHasSelfPermissionsCondition(builder: MethodSpec.Builder, activityVar: String, permissionField: String)

    fun addRequestPermissionsStatement(builder: MethodSpec.Builder, targetParam: String, activityVar: String, requestCodeField: String)
}