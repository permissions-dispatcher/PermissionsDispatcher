package permissions.dispatcher.processor.impl.kotlin

import com.squareup.kotlinpoet.FunSpec
import permissions.dispatcher.processor.util.typeMirrorOf
import javax.lang.model.type.TypeMirror

/**
 * [permissions.dispatcher.processor.KtProcessorUnit] implementation for Fragments.
 */
class KotlinFragmentProcessorUnit : KotlinBaseProcessorUnit() {

    override fun getTargetType(): TypeMirror = typeMirrorOf("androidx.fragment.app.Fragment")

    override fun getActivityName(targetParam: String): String = "$targetParam.requireActivity()"

    override fun addShouldShowRequestPermissionRationaleCondition(builder: FunSpec.Builder, permissionField: String, isPositiveCondition: Boolean) {
        val condition = if (isPositiveCondition) "" else "!"
        builder.beginControlFlow("if (%L%T.shouldShowRequestPermissionRationale(%L, *%N))", condition, permissionUtils, "this" /* Fragment */, permissionField)
    }

    override fun addRequestPermissionsStatement(builder: FunSpec.Builder, targetParam: String, permissionField: String, requestCodeField: String) {
        builder.addStatement("%L.requestPermissions(%L, %N)", targetParam, permissionField, requestCodeField)
    }
}
