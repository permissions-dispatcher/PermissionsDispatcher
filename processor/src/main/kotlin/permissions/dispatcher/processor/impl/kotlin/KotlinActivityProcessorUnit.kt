package permissions.dispatcher.processor.impl.kotlin

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import permissions.dispatcher.processor.util.*
import javax.annotation.processing.Messager
import javax.lang.model.type.TypeMirror

/**
 * [permissions.dispatcher.processor.KtProcessorUnit] implementation for Activity classes.
 */
class KotlinActivityProcessorUnit(messager: Messager) : KotlinBaseProcessorUnit(messager) {

    private val ACTIVITY_COMPAT = ClassName("android.support.v4.app", "ActivityCompat")

    override fun getTargetType(): TypeMirror = typeMirrorOf("android.app.Activity")

    override fun getActivityName(): String = "this"

    override fun addShouldShowRequestPermissionRationaleCondition(builder: FunSpec.Builder, permissionField: String, isPositiveCondition: Boolean) {
        val condition = if (isPositiveCondition) "" else "!"
        val activity = getActivityName()
        builder.beginControlFlow("if (%N%T.shouldShowRequestPermissionRationale(%N, *%N))", condition, PERMISSION_UTILS, activity, permissionField)
    }

    override fun addRequestPermissionsStatement(builder: FunSpec.Builder, targetParam: String, permissionField: String, requestCodeField: String) {
        builder.addStatement("%T.requestPermissions(%N, %N, %N)", ACTIVITY_COMPAT, targetParam, permissionField, requestCodeField)
    }
}
