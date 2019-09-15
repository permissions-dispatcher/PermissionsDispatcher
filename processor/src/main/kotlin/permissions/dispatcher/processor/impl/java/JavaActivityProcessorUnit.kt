package permissions.dispatcher.processor.impl.java

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import permissions.dispatcher.processor.util.typeMirrorOf
import javax.lang.model.type.TypeMirror

/**
 * ProcessorUnit implementation for Activity.
 */
class JavaActivityProcessorUnit : JavaBaseProcessorUnit() {

    override fun getTargetType(): TypeMirror = typeMirrorOf("android.app.Activity")

    override fun getActivityName(targetParam: String): String = targetParam

    override fun addShouldShowRequestPermissionRationaleCondition(builder: MethodSpec.Builder, targetParam: String, permissionField: String, isPositiveCondition: Boolean) {
        builder.beginControlFlow("if (\$N\$T.shouldShowRequestPermissionRationale(\$N, \$N))", if (isPositiveCondition) "" else "!", permissionUtils, targetParam, permissionField)
    }

    override fun addRequestPermissionsStatement(builder: MethodSpec.Builder, targetParam: String, permissionField: String, requestCodeField: String) {
        builder.addStatement("\$T.requestPermissions(\$N, \$N, \$N)", ClassName.get("androidx.core.app", "ActivityCompat"), targetParam, permissionField, requestCodeField)
    }
}
