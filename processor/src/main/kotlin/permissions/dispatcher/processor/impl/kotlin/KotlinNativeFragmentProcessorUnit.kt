package permissions.dispatcher.processor.impl.kotlin

import com.squareup.kotlinpoet.FunSpec
import permissions.dispatcher.processor.util.*
import javax.annotation.processing.Messager
import javax.lang.model.type.TypeMirror

class KotlinNativeFragmentProcessorUnit(messager: Messager) : KotlinBaseProcessorUnit(messager) {

    override fun getTargetType(): TypeMirror = typeMirrorOf("android.app.Fragment")

    override fun getActivityName(): String = "activity"

    override fun addShouldShowRequestPermissionRationaleCondition(builder: FunSpec.Builder, permissionField: String, isPositiveCondition: Boolean) {
        val condition = if (isPositiveCondition) "" else "!"
        builder.beginControlFlow("if (%N%T.shouldShowRequestPermissionRationale(%N, *%N))", condition, PERMISSION_UTILS, "this", permissionField)
    }

    override fun addRequestPermissionsStatement(builder: FunSpec.Builder, targetParam: String, permissionField: String, requestCodeField: String) {
        builder.addStatement("%T.requestPermissions(%N, %N, %N)", PERMISSION_UTILS, targetParam, permissionField, requestCodeField)
    }
}
