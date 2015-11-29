package permissions.dispatcher.processor.impl

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.MethodSpec
import permissions.dispatcher.processor.RuntimePermissionsElement
import permissions.dispatcher.processor.util.*
import javax.lang.model.element.ExecutableElement
import javax.lang.model.type.TypeMirror

/**
 * ProcessorUnit implementation for Activity classes
 */
class ActivityProcessorUnit : BaseProcessorUnit() {

    private val PERMISSION_UTILS: ClassName = ClassName.get("permissions.dispatcher", "PermissionUtils")
    private val ACTIVITY_COMPAT: ClassName = ClassName.get("android.support.v4.app", "ActivityCompat")

    override fun getTargetType(): TypeMirror {
        return typeMirrorOf("android.app.Activity")
    }

    override fun checkPrerequisites(rpe: RuntimePermissionsElement) {
        // Nothing to check
    }

    override fun addWithCheckBody(builder: MethodSpec.Builder, needsMethod: ExecutableElement, rpe: RuntimePermissionsElement, targetParam: String) {
        // Create field names for the constants to use
        val requestCodeField = requestCodeFieldName(needsMethod)
        val permissionField = permissionFieldName(needsMethod)

        // Add the conditional for when permission has already been granted
        builder.beginControlFlow("if (\$T.hasSelfPermissions(\$N, \$N))", PERMISSION_UTILS, targetParam, permissionField)
        builder.addCode(CodeBlock.builder()
                .add("\$N.\$N(", targetParam, needsMethod.simpleString())
                .add(varargsParametersCodeBlock(needsMethod))
                .addStatement(")")
                .build()
        )
        builder.nextControlFlow("else")

        // Add the conditional for "OnShowRationale", if present
        val onRationale: ExecutableElement? = rpe.findOnRationaleForNeeds(needsMethod)
        val hasParameters: Boolean = needsMethod.parameters.isNotEmpty()
        if (hasParameters) {
            // If the method has parameters, precede the potential OnRationale call with
            // an instantiation of the temporary Request object
            val varargsCall = CodeBlock.builder()
                    .add("\$N = new \$N(\$N, ",
                            pendingRequestFieldName(needsMethod),
                            permissionRequestTypeName(needsMethod),
                            targetParam
                    )
                    .add(varargsParametersCodeBlock(needsMethod))
                    .addStatement(")")
            builder.addCode(varargsCall.build())
        }
        if (onRationale != null) {
            builder.beginControlFlow("if (\$T.shouldShowRequestPermissionRationale(\$N, \$N))", PERMISSION_UTILS, targetParam, permissionField)
            if (hasParameters) {
                // For methods with parameters, use the PermissionRequest instantiated above
                builder.addStatement("\$N.\$N(\$N)", targetParam, onRationale.simpleString(), pendingRequestFieldName(needsMethod))
            } else {
                // Otherwise, create a new PermissionRequest on-the-fly
                builder.addStatement("\$N.\$N(new \$N(\$N))", targetParam, onRationale.simpleString(), permissionRequestTypeName(needsMethod), targetParam)
            }
            builder.nextControlFlow("else")
        }

        // Add the branch for "request permission"
        addRequestPermissionsStatement(builder, targetParam, permissionField, requestCodeField)
        if (onRationale != null) {
            builder.endControlFlow()
        }
        builder.endControlFlow()
    }

    override fun addRequestPermissionsStatement(builder: MethodSpec.Builder, targetParam: String, permissionField: String, requestCodeField: String) {
        builder.addStatement("\$T.requestPermissions(\$N, \$N, \$N)", ACTIVITY_COMPAT, targetParam, permissionField, requestCodeField)
    }

    override fun addResultCaseBody(builder: MethodSpec.Builder, needsMethod: ExecutableElement, rpe: RuntimePermissionsElement, targetParam: String, grantResultsParam: String) {
        // Add the conditional for "permission verified"
        builder.beginControlFlow("if (\$T.verifyPermissions(\$N))", PERMISSION_UTILS, grantResultsParam)

        // Based on whether or not the method has parameters, delegate to the "pending request" object or invoke the method directly
        val hasParameters = needsMethod.parameters.isNotEmpty()
        if (hasParameters) {
            val pendingField = pendingRequestFieldName(needsMethod)
            builder.beginControlFlow("if (\$N != null)", pendingField)
            builder.addStatement("\$N.grant()", pendingField)
            builder.endControlFlow()
        } else {
            builder.addStatement("target.\$N()", needsMethod.simpleString())
        }

        // Add the conditional for "permission denied", if present
        val onDenied: ExecutableElement? = rpe.findOnDeniedForNeeds(needsMethod)
        if (onDenied != null) {
            builder.nextControlFlow("else")
            builder.addStatement("\$N.\$N()", targetParam, onDenied.simpleString())
        }
        // Close the control flow
        builder.endControlFlow()

        // Remove the temporary pending request field, in case it was used for a method with parameters
        if (hasParameters) {
            builder.addStatement("\$N = null", pendingRequestFieldName(needsMethod))
        }
        builder.addStatement("break");
    }
}