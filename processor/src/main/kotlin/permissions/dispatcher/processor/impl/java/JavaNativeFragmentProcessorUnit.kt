package permissions.dispatcher.processor.impl.java

import com.squareup.javapoet.MethodSpec
import permissions.dispatcher.processor.util.*
import javax.annotation.processing.Messager
import javax.lang.model.type.TypeMirror

class JavaNativeFragmentProcessorUnit(messager: Messager) : JavaBaseProcessorUnit(messager) {

    override fun getTargetType(): TypeMirror {
        return typeMirrorOf("android.app.Fragment")
    }

    override fun getActivityName(targetParam: String): String {
        return targetParam + ".getActivity()"
    }

    override fun addShouldShowRequestPermissionRationaleCondition(builder: MethodSpec.Builder, targetParam: String, permissionField: String, isPositiveCondition: Boolean) {
        builder.beginControlFlow("if (\$N\$T.shouldShowRequestPermissionRationale(\$N, \$N))", if (isPositiveCondition) "" else "!", PERMISSION_UTILS, targetParam, permissionField)
    }

    override fun addRequestPermissionsStatement(builder: MethodSpec.Builder, targetParam: String, permissionField: String, requestCodeField: String) {
        builder.addStatement("\$T.requestPermissions(\$N, \$N, \$N)", PERMISSION_UTILS, targetParam, permissionField, requestCodeField)
    }
}
