package permissions.dispatcher.processor.impl.kotlin

import com.squareup.kotlinpoet.FunSpec
import permissions.dispatcher.processor.util.*
import javax.annotation.processing.Messager
import javax.lang.model.type.TypeMirror

/**
 * [permissions.dispatcher.processor.KtProcessorUnit] implementation for Fragments defined in the support-v4 library.
 */
class KotlinSupportFragmentProcessorUnit(messager: Messager) : KotlinBaseProcessorUnit(messager) {

    override fun getTargetType(): TypeMirror = typeMirrorOf("android.support.v4.app.Fragment")

    override fun getActivityName(): String = "activity"

    override fun addShouldShowRequestPermissionRationaleCondition(builder: FunSpec.Builder, permissionField: String, isPositiveCondition: Boolean) {
        val condition = if (isPositiveCondition) "" else "!"
        builder.beginControlFlow("if (%N%T.shouldShowRequestPermissionRationale(%N, *%N))", condition, PERMISSION_UTILS, "this" /* Fragment */, permissionField)
    }

    override fun addRequestPermissionsStatement(builder: FunSpec.Builder, targetParam: String, permissionField: String, requestCodeField: String) {
        builder.addStatement("%N.requestPermissions(%N, %N)", targetParam, permissionField, requestCodeField)
    }
}
