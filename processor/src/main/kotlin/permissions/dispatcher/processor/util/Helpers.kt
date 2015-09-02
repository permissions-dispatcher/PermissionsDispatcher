package permissions.dispatcher.processor.util

import com.squareup.javapoet.TypeName
import permissions.dispatcher.Needs
import permissions.dispatcher.OnDenied
import permissions.dispatcher.processor.ELEMENT_UTILS
import permissions.dispatcher.processor.TYPE_UTILS
import javax.lang.model.element.ExecutableElement
import javax.lang.model.type.TypeMirror

public fun typeMirrorOf(className: String): TypeMirror {
    return ELEMENT_UTILS.getTypeElement(className).asType()
}

public fun requestCodeFieldName(e: ExecutableElement): String {
    return "$GEN_REQUESTCODE_PREFIX${e.simpleString().toUpperCase()}"
}

public fun permissionFieldName(e: ExecutableElement): String {
    return "$GEN_PERMISSION_PREFIX${e.simpleString().toUpperCase()}"
}

public fun withCheckMethodName(e: ExecutableElement): String {
    return "${e.simpleString()}$GEN_WITHCHECK_SUFFIX"
}

fun <A : Annotation> findMatchingMethodForNeeds(needsElement: ExecutableElement, otherElements: List<ExecutableElement>, annotationType: Class<A>): ExecutableElement? {
    val value: List<String> = needsElement.getAnnotation(javaClass<Needs>()).permissionValue()
    return otherElements.firstOrNull {
        it.getAnnotation(annotationType).permissionValue().equals(value)
    }
}
