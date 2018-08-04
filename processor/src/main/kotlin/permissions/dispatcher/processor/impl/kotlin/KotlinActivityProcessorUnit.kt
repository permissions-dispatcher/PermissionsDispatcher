package permissions.dispatcher.processor.impl.kotlin

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import permissions.dispatcher.processor.util.typeMirrorOf
import javax.annotation.processing.Messager
import javax.lang.model.type.TypeMirror

/**
 * [permissions.dispatcher.processor.KtProcessorUnit] implementation for Activity classes.
 */
class KotlinActivityProcessorUnit(messager: Messager) : KotlinBaseProcessorUnit(messager) {

    private val ACTIVITY_COMPAT = ClassName("androidx.core.app", "ActivityCompat")

    override fun isDeprecated(): Boolean = false

    override fun getTargetType(): TypeMirror = typeMirrorOf("android.app.Activity")

    override fun getActivityName(targetParam: String): String = targetParam

    override fun addShouldShowRequestPermissionRationaleCondition(builder: FunSpec.Builder, permissionField: String, isPositiveCondition: Boolean) {
        val condition = if (isPositiveCondition) "" else "!"
        builder.beginControlFlow("if (%N%T.shouldShowRequestPermissionRationale(%L, *%N))", condition, permissionUtilsClassName, "this", permissionField)
    }

    override fun addRequestPermissionsStatement(builder: FunSpec.Builder, targetParam: String, permissionField: String, requestCodeField: String) {
        builder.addStatement("%T.requestPermissions(%L, %N, %N)", ACTIVITY_COMPAT, targetParam, permissionField, requestCodeField)
    }
}
