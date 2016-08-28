package permissions.dispatcher.processor.util

import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.TypeName
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.processor.ELEMENT_UTILS
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.type.TypeMirror

fun typeMirrorOf(className: String): TypeMirror = ELEMENT_UTILS.getTypeElement(className).asType()

fun typeNameOf(it: Element): TypeName = TypeName.get(it.asType())

fun requestCodeFieldName(e: ExecutableElement): String = "$GEN_REQUESTCODE_PREFIX${e.simpleString().toUpperCase()}"

fun permissionFieldName(e: ExecutableElement): String = "$GEN_PERMISSION_PREFIX${e.simpleString().toUpperCase()}"

fun pendingRequestFieldName(e: ExecutableElement): String = "$GEN_PENDING_PREFIX${e.simpleString().toUpperCase()}"

fun withCheckMethodName(e: ExecutableElement): String = "${e.simpleString()}$GEN_WITHCHECK_SUFFIX"

fun permissionRequestTypeName(e: ExecutableElement): String = "${e.simpleString().capitalize()}$GEN_PERMISSIONREQUEST_SUFFIX"

fun <A : Annotation> findMatchingMethodForNeeds(needsElement: ExecutableElement, otherElements: List<ExecutableElement>, annotationType: Class<A>): ExecutableElement? {
    val value: List<String> = needsElement.getAnnotation(NeedsPermission::class.java).permissionValue()
    return otherElements.firstOrNull {
        it.getAnnotation(annotationType).permissionValue().equals(value)
    }
}

fun varargsParametersCodeBlock(needsElement: ExecutableElement): CodeBlock {
    val varargsCall = CodeBlock.builder()
    needsElement.parameters.forEachIndexed { i, it ->
        varargsCall.add("\$L", it.simpleString())
        if (i < needsElement.parameters.size - 1) {
            varargsCall.add(", ")
        }
    }
    return varargsCall.build()
}
