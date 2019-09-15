package permissions.dispatcher.processor.impl.kotlin

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import permissions.dispatcher.processor.util.typeMirrorOf
import javax.lang.model.type.TypeMirror

/**
 * [permissions.dispatcher.processor.KtProcessorUnit] implementation for Activity classes.
 */
class KotlinActivityProcessorUnit : KotlinBaseProcessorUnit() {

    override fun getTargetType(): TypeMirror = typeMirrorOf("android.app.Activity")

    override fun getActivityName(targetParam: String): String = targetParam

    override fun addShouldShowRequestPermissionRationaleCondition(builder: FunSpec.Builder, permissionField: String, isPositiveCondition: Boolean) {
        val condition = if (isPositiveCondition) "" else "!"
        builder.beginControlFlow("if (%L%T.shouldShowRequestPermissionRationale(%L, *%N))", condition, permissionUtils, "this", permissionField)
    }

    override fun addRequestPermissionsStatement(builder: FunSpec.Builder, targetParam: String, permissionField: String, requestCodeField: String) {
        builder.addStatement("%T.requestPermissions(%L, %N, %N)", ClassName("androidx.core.app", "ActivityCompat"), targetParam, permissionField, requestCodeField)
    }
}
