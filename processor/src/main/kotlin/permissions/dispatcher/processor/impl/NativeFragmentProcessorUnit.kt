package permissions.dispatcher.processor.impl

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.MethodSpec
import permissions.dispatcher.processor.RuntimePermissionsElement
import permissions.dispatcher.processor.exception.SupportV13MissingException
import permissions.dispatcher.processor.util.*
import javax.lang.model.element.ExecutableElement
import javax.lang.model.type.TypeMirror

class NativeFragmentProcessorUnit: BaseProcessorUnit() {

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
            builder.beginControlFlow("if (\$T.getInstance().shouldShowRequestPermissionRationale(\$N, \$N))", PERMISSION_UTILS_V13, targetParam, permissionField)
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
        builder.addStatement("\$T.getInstance().requestPermissions(\$N, \$N, \$N)", PERMISSION_UTILS_V13, targetParam, permissionField, requestCodeField)
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

        // Add the conditional for "permission denied" and/or "never ask again", if present
        val onDenied: ExecutableElement? = rpe.findOnDeniedForNeeds(needsMethod)
        val hasDenied = onDenied != null
        val onNeverAsk: ExecutableElement? = rpe.findOnNeverAskForNeeds(needsMethod)
        val hasNeverAsk = onNeverAsk != null

        if (hasDenied || hasNeverAsk) {
            builder.nextControlFlow("else")
        }
        if (hasNeverAsk) {
            // Split up the "else" case with another if condition checking for "never ask again" first
            builder.beginControlFlow("if (!\$T.getInstance().shouldShowRequestPermissionRationale(target, \$N))", PERMISSION_UTILS_V13, permissionFieldName(needsMethod))
            builder.addStatement("target.\$N()", onNeverAsk!!.simpleString())

            // If a "permission denied" is present as well, go into an else case, otherwise close this temporary branch
            if (hasDenied) {
                builder.nextControlFlow("else")
            } else {
                builder.endControlFlow()
            }
        }
        if (hasDenied) {
            // Add the "permissionDenied" statement
            builder.addStatement("\$N.\$N()", targetParam, onDenied!!.simpleString())

            // Close the additional control flow potentially opened by a "never ask again" method
            if (hasNeverAsk) {
                builder.endControlFlow()
            }
        }
        // Close the "switch" control flow
        builder.endControlFlow()

        // Remove the temporary pending request field, in case it was used for a method with parameters
        if (hasParameters) {
            builder.addStatement("\$N = null", pendingRequestFieldName(needsMethod))
        }
        builder.addStatement("break");
    }
}