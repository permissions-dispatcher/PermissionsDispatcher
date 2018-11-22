package permissions.dispatcher.processor.impl.kotlin

import com.squareup.kotlinpoet.FunSpec
import permissions.dispatcher.processor.util.typeMirrorOf
import javax.annotation.processing.Messager
import javax.lang.model.type.TypeMirror

/**
 * [permissions.dispatcher.processor.KtProcessorUnit] implementation for Controllers.
 */
class KotlinConductorControllerProcessorUnit(messager: Messager) : KotlinBaseProcessorUnit(messager) {

    override fun isDeprecated(): Boolean = false

    override fun getTargetType(): TypeMirror = typeMirrorOf("com.bluelinelabs.conductor.Controller")

    override fun getActivityName(targetParam: String): String = "$targetParam.getActivity()"

    override fun addShouldShowRequestPermissionRationaleCondition(builder: FunSpec.Builder, permissionField: String, isPositiveCondition: Boolean) {
        val condition = if (isPositiveCondition) "" else "!"
        builder.beginControlFlow("if (%N%T.shouldShowRequestPermissionRationale(%L, *%N))", condition, PERMISSION_UTILS, "this" /* Controller */, permissionField)
    }

    override fun addRequestPermissionsStatement(builder: FunSpec.Builder, targetParam: String, permissionField: String, requestCodeField: String) {
        builder.addStatement("%L.requestPermissions(%L, %N)", targetParam, permissionField, requestCodeField)
    }
}
