package permissions.dispatcher.processor.exception

import permissions.dispatcher.processor.util.simpleString
import javax.lang.model.element.ExecutableElement

public class PrivateMethodException<A: Annotation>(e: ExecutableElement, annotationType: Class<A>): RuntimeException("Method '${e.simpleString()}()' annotated with '@${annotationType.simpleName}' must not be private") {
}