package permissions.dispatcher.processor.impl.java

import com.squareup.javapoet.MethodSpec
import permissions.dispatcher.processor.util.typeMirrorOf
import javax.lang.model.type.TypeMirror

class JavaConductorProcessorUnit : JavaBaseProcessorUnit() {

    override fun getTargetType(): TypeMirror = typeMirrorOf("com.bluelinelabs.conductor.Controller")

    override fun getActivityName(targetParam: String): String = "$targetParam.getActivity()"

    override fun addShouldShowRequestPermissionRationaleCondition(builder: MethodSpec.Builder, targetParam: String, permissionField: String, isPositiveCondition: Boolean) {
        val condition = if (isPositiveCondition) "" else "!"
        val activity = getActivityName(targetParam)
        builder.beginControlFlow("if (\$N\$T.shouldShowRequestPermissionRationale(\$N, \$N))", condition, permissionUtils, activity, permissionField)
    }

    override fun addRequestPermissionsStatement(builder: MethodSpec.Builder, targetParam: String, permissionField: String, requestCodeField: String) {
        builder.addStatement("\$N.requestPermissions(\$N, \$N)", targetParam, permissionField, requestCodeField)
    }
}