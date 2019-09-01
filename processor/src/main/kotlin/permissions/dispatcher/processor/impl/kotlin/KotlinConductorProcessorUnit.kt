package permissions.dispatcher.processor.impl.kotlin

import com.squareup.kotlinpoet.FunSpec
import permissions.dispatcher.processor.util.typeMirrorOf
import javax.lang.model.type.TypeMirror

class KotlinConductorProcessorUnit : KotlinBaseProcessorUnit() {

    override fun getTargetType(): TypeMirror = typeMirrorOf("com.bluelinelabs.conductor.Controller")

    override fun getActivityName(targetParam: String): String = "$targetParam.getActivity()"

    override fun addShouldShowRequestPermissionRationaleCondition(builder: FunSpec.Builder, permissionField: String, isPositiveCondition: Boolean) {
        val condition = if (isPositiveCondition) "" else "!"
        val activity = getActivityName("this")
        builder.beginControlFlow("if (%L%T.shouldShowRequestPermissionRationale(%L, *%N))", condition, PERMISSION_UTILS, activity, permissionField)
    }

    override fun addRequestPermissionsStatement(builder: FunSpec.Builder, targetParam: String, permissionField: String, requestCodeField: String) {
        builder.addStatement("%L.requestPermissions(%L, %N)", targetParam, permissionField, requestCodeField)
    }
}
