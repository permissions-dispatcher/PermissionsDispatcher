package permissions.dispatcher.processor.impl.java

import com.squareup.javapoet.MethodSpec
import permissions.dispatcher.processor.util.typeMirrorOf
import javax.annotation.processing.Messager
import javax.lang.model.type.TypeMirror

/**
 * ProcessorUnit implementation for Fragments defined in the support-v4 library.
 */
class JavaSupportFragmentProcessorUnit(messager: Messager) : JavaBaseProcessorUnit(messager) {

    override fun isDeprecated(): Boolean = false

    override fun getTargetType(): TypeMirror {
        return typeMirrorOf("androidx.fragment.app.Fragment")
    }

    override fun getActivityName(targetParam: String): String {
        return "$targetParam.getActivity()"
    }

    override fun addShouldShowRequestPermissionRationaleCondition(builder: MethodSpec.Builder, targetParam: String, permissionField: String, isPositiveCondition: Boolean) {
        builder.beginControlFlow("if (\$N\$T.shouldShowRequestPermissionRationale(\$N, \$N))", if (isPositiveCondition) "" else "!", PERMISSION_UTILS, targetParam, permissionField)
    }

    override fun addRequestPermissionsStatement(builder: MethodSpec.Builder, targetParam: String, permissionField: String, requestCodeField: String) {
        builder.addStatement("\$N.requestPermissions(\$N, \$N)", targetParam, permissionField, requestCodeField)
    }
}
