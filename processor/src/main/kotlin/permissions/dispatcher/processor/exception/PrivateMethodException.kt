package permissions.dispatcher.processor.exception

import permissions.dispatcher.processor.util.simpleString
import javax.lang.model.element.ExecutableElement

class PrivateMethodException(e: ExecutableElement, annotationType: Class<*>) : RuntimeException("Method '${e.simpleString()}()' annotated with '@${annotationType.simpleName}' must not be private")