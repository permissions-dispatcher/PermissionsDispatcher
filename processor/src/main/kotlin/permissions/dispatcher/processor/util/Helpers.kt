package permissions.dispatcher.processor.util

import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.processor.ELEMENT_UTILS
import javax.lang.model.element.ExecutableElement
import javax.lang.model.type.TypeMirror

public fun typeMirrorOf(className: String): TypeMirror = ELEMENT_UTILS.getTypeElement(className).asType()

public fun requestCodeFieldName(e: ExecutableElement): String = "$GEN_REQUESTCODE_PREFIX${e.simpleString().toUpperCase()}"

public fun permissionFieldName(e: ExecutableElement): String = "$GEN_PERMISSION_PREFIX${e.simpleString().toUpperCase()}"

public fun withCheckMethodName(e: ExecutableElement): String = "${e.simpleString()}$GEN_WITHCHECK_SUFFIX"

public fun permissionRequestMethodName(e: ExecutableElement): String = "${e.simpleString().capitalize()}$GEN_PERMISSIONREQUEST_SUFFIX"

fun <A : Annotation> findMatchingMethodForNeeds(needsElement: ExecutableElement, otherElements: List<ExecutableElement>, annotationType: Class<A>): ExecutableElement? {
    val value: List<String> = needsElement.getAnnotation(NeedsPermission::class.java).permissionValue()
    return otherElements.firstOrNull {
        it.getAnnotation(annotationType).permissionValue().equals(value)
    }
}
