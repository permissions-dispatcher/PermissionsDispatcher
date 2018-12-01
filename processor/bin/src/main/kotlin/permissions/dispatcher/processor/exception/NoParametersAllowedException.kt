package permissions.dispatcher.processor.exception

import permissions.dispatcher.processor.util.simpleString
import javax.lang.model.element.ExecutableElement

class NoParametersAllowedException(e: ExecutableElement) : RuntimeException("Method '${e.simpleString()}()' must not have any parameters")