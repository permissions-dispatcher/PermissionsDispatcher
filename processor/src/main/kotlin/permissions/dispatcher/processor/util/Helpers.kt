package permissions.dispatcher.processor.util

import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.TypeName
import kotlinx.metadata.ClassName
import kotlinx.metadata.Flag
import kotlinx.metadata.Flags
import kotlinx.metadata.KmClassVisitor
import kotlinx.metadata.jvm.KotlinClassHeader
import kotlinx.metadata.jvm.KotlinClassMetadata
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.processor.ELEMENT_UTILS
import permissions.dispatcher.processor.RuntimePermissionsElement
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.type.TypeMirror

fun typeMirrorOf(className: String): TypeMirror = ELEMENT_UTILS.getTypeElement(className).asType()

fun typeNameOf(it: Element): TypeName = TypeName.get(it.asType())

fun requestCodeFieldName(e: ExecutableElement) = "$GEN_REQUEST_CODE_PREFIX${e.simpleString().trimDollarIfNeeded().toUpperCase()}"

fun permissionFieldName(e: ExecutableElement) = "$GEN_PERMISSION_PREFIX${e.simpleString().trimDollarIfNeeded().toUpperCase()}"

fun pendingRequestFieldName(e: ExecutableElement) = "$GEN_PENDING_PREFIX${e.simpleString().trimDollarIfNeeded().toUpperCase()}"

fun withPermissionCheckMethodName(e: ExecutableElement) = "${e.simpleString().trimDollarIfNeeded()}$GEN_WITH_PERMISSION_CHECK_SUFFIX"

fun Element.kotlinMetadata(): KotlinClassMetadata? =
        getAnnotation(Metadata::class.java)?.run {
            KotlinClassMetadata.read(KotlinClassHeader(kind, metadataVersion, bytecodeVersion, data1, data2, extraString, packageName, extraInt))
        }

val Element.isInternal: Boolean
    get() {
        val classMetadata = kotlinMetadata()
                as? KotlinClassMetadata.Class
                ?: return false

        var returnValue = false
        classMetadata.accept(object : KmClassVisitor() {
            override fun visit(flags: Flags, name: ClassName) {
                if (Flag.IS_INTERNAL(flags)) {
                    returnValue = true
                }
            }
        })
        return returnValue
    }

fun ExecutableElement.argumentFieldName(arg: Element) = "${simpleString()}${arg.simpleString().capitalize()}"

fun ExecutableElement.proceedOnShowRationaleMethodName() = "proceed${simpleString().trimDollarIfNeeded().capitalize()}$GEN_PERMISSION_REQUEST_SUFFIX"

fun ExecutableElement.cancelOnShowRationaleMethodName() = "cancel${simpleString().trimDollarIfNeeded().capitalize()}$GEN_PERMISSION_REQUEST_SUFFIX"

fun permissionRequestTypeName(rpe: RuntimePermissionsElement, e: ExecutableElement) =
        "${rpe.inputClassName}${e.simpleString().trimDollarIfNeeded().capitalize()}$GEN_PERMISSION_REQUEST_SUFFIX"

fun <A : Annotation> findMatchingMethodForNeeds(needsElement: ExecutableElement, otherElements: List<ExecutableElement>, annotationType: Class<A>): ExecutableElement? {
    val value: List<String> = needsElement.getAnnotation(NeedsPermission::class.java).permissionValue()
    return otherElements.firstOrNull {
        it.getAnnotation(annotationType).permissionValue() == value
    }
}

fun varargsParametersCodeBlock(needsElement: ExecutableElement, withCache: Boolean = false): CodeBlock {
    val varargsCall = CodeBlock.builder()
    needsElement.parameters.forEachIndexed { i, it ->
        val name = if (withCache) needsElement.argumentFieldName(it) else it.simpleString()
        varargsCall.add("\$L", name)
        if (i < needsElement.parameters.size - 1) {
            varargsCall.add(", ")
        }
    }
    return varargsCall.build()
}

fun varargsKtParametersCodeBlock(needsElement: ExecutableElement, withCache: Boolean = false): com.squareup.kotlinpoet.CodeBlock {
    val varargsCall = com.squareup.kotlinpoet.CodeBlock.builder()
    needsElement.parameters.forEachIndexed { i, it ->
        val name = if (withCache) "${needsElement.argumentFieldName(it)}!!" else it.simpleString()
        varargsCall.add("%L", name)
        if (i < needsElement.parameters.size - 1) {
            varargsCall.add(", ")
        }
    }
    return varargsCall.build()
}
