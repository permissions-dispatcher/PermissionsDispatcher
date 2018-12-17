package permissions.dispatcher.processor.util

import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.TypeName
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.processor.ELEMENT_UTILS
import permissions.dispatcher.processor.RuntimePermissionsElement
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.type.TypeMirror

/**
 * Class Reference to the kotlin.Metadata annotation class,
 * used by the processor to tell apart Kotlin from Java files during code generation.
 */
val kotlinMetadataClass: Class<Annotation>? by lazy {
    try {
        @Suppress("UNCHECKED_CAST")
        Class.forName("kotlin.Metadata") as Class<Annotation>
    } catch (e: Throwable) {
        // Java-only environment, or outdated Kotlin version
        null
    }
}

fun typeMirrorOf(className: String): TypeMirror = ELEMENT_UTILS.getTypeElement(className).asType()

fun typeNameOf(it: Element): TypeName = TypeName.get(it.asType())

fun requestCodeFieldName(e: ExecutableElement) = "$GEN_REQUEST_CODE_PREFIX${e.simpleString().trimDollarIfNeeded().toUpperCase()}"

fun permissionFieldName(e: ExecutableElement) = "$GEN_PERMISSION_PREFIX${e.simpleString().trimDollarIfNeeded().toUpperCase()}"

fun argumentFieldName(e: ExecutableElement, arg: Element) = "${e.simpleString()}${arg.simpleString().capitalize()}"

fun withPermissionCheckMethodName(e: ExecutableElement) = "${e.simpleString().trimDollarIfNeeded()}$GEN_WITH_PERMISSION_CHECK_SUFFIX"

fun proceedOnShowRationaleMethodName(e: ExecutableElement) = "proceed${e.simpleString().trimDollarIfNeeded().capitalize()}$GEN_PERMISSION_REQUEST_SUFFIX"

fun cancelOnShowRationaleMethodName(e: ExecutableElement) = "cancel${e.simpleString().trimDollarIfNeeded().capitalize()}$GEN_PERMISSION_REQUEST_SUFFIX"

fun <A : Annotation> findMatchingMethodForNeeds(needsElement: ExecutableElement, otherElements: List<ExecutableElement>, annotationType: Class<A>): ExecutableElement? {
    val value = needsElement.getAnnotation(NeedsPermission::class.java).permissionValue()
    return otherElements.firstOrNull {
        it.getAnnotation(annotationType).permissionValue() == value
    }
}

fun varargsParametersCodeBlock(needsElement: ExecutableElement, withCache: Boolean = false): CodeBlock {
    val varargsCall = CodeBlock.builder()
    needsElement.parameters.forEachIndexed { i, it ->
        val name = if (withCache) argumentFieldName(needsElement, it) else it.simpleString()
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
        val name = if (withCache) "${argumentFieldName(needsElement, it)}!!" else it.simpleString()
        varargsCall.add("%L", name)
        if (i < needsElement.parameters.size - 1) {
            varargsCall.add(", ")
        }
    }
    return varargsCall.build()
}
