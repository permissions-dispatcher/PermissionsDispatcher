package permissions.dispatcher.processor.impl

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import permissions.dispatcher.processor.RuntimePermissionsElement
import permissions.dispatcher.processor.exception.SupportV13MissingException
import permissions.dispatcher.processor.util.permissionFieldName
import permissions.dispatcher.processor.util.requestCodeFieldName
import permissions.dispatcher.processor.util.simpleString
import permissions.dispatcher.processor.util.typeMirrorOf
import javax.lang.model.element.ExecutableElement
import javax.lang.model.type.TypeMirror

class NativeFragmentProcessorUnit: BaseProcessorUnit() {

    private val PERMISSION_UTILS: ClassName = ClassName.get("permissions.dispatcher", "PermissionUtils")
    private val PERMISSION_UTILS_V13: ClassName = ClassName.get("permissions.dispatcher.v13", "PermissionUtilsV13")

    override fun getTargetType(): TypeMirror {
        return typeMirrorOf("android.app.Fragment")
    }

    override fun checkPrerequisites(rpe: RuntimePermissionsElement) {
        try {
            // Check if FragmentCompat can be accessed; if not, throw an exception
            Class.forName("android.support.v13.app.FragmentCompat")

        } catch (ex: ClassNotFoundException) {
            // Thrown if support-v13 is missing on the classpath
            throw SupportV13MissingException(rpe)

        } catch (ex: NoClassDefFoundError) {
            // Expected in success cases, because the Android environment is still missing
            // when this is called from within the Annotation processor. 'FragmentCompat' itself
            // can be resolved, but accessing it requires an Android environment, which doesn't exist
            // since this is an annotation processor
        }
    }

    override fun addWithCheckBody(builder: MethodSpec.Builder, needsMethod: ExecutableElement, rpe: RuntimePermissionsElement, targetParam: String) {
        // Create field names for the constants to use
        val requestCodeField = requestCodeFieldName(needsMethod)
        val permissionField = permissionFieldName(needsMethod)

        // Add the conditional for when permission has already been granted
        builder.beginControlFlow("if (\$T.hasSelfPermissions(\$N.getActivity(), \$N))", PERMISSION_UTILS, targetParam, permissionField)
        builder.addStatement("\$N.\$N()", targetParam, needsMethod.simpleString())

        // Add the conditional for "OnRationale", if present
        val onRationale: ExecutableElement? = rpe.findOnRationaleForNeeds(needsMethod)
        if (onRationale != null) {
            builder.nextControlFlow("else if (\$T.getInstance().shouldShowRequestPermissionRationale(\$N, \$N))", PERMISSION_UTILS_V13, targetParam, permissionField)
            builder.addStatement("\$N.\$N()", targetParam, onRationale.simpleString())
        }

        // Add the branch for "request permission"
        builder.nextControlFlow("else")
        builder.addStatement("\$T.getInstance().requestPermissions(\$N, \$N, \$N)", PERMISSION_UTILS_V13, targetParam, permissionField, requestCodeField)
        builder.endControlFlow()
    }

    override fun addResultCaseBody(builder: MethodSpec.Builder, needsMethod: ExecutableElement, rpe: RuntimePermissionsElement, targetParam: String, grantResultsParam: String) {
        // Add the conditional for "permission verified"
        builder.beginControlFlow("if (\$T.verifyPermissions(\$N))", PERMISSION_UTILS, grantResultsParam)
        builder.addStatement("target.\$N()", needsMethod.simpleString())

        // Add the conditional for "permission denied", if present
        val onDenied: ExecutableElement? = rpe.findOnDeniedForNeeds(needsMethod)
        if (onDenied != null) {
            builder.nextControlFlow("else")
            builder.addStatement("\$N.\$N()", targetParam, onDenied.simpleString())
        }
        // Close the control flow
        builder.endControlFlow()
        builder.addStatement("break");
    }
}