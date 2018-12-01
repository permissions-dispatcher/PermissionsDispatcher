package permissions.dispatcher.processor.exception

import permissions.dispatcher.processor.util.simpleString
import javax.lang.model.element.ExecutableElement

class NoThrowsAllowedException(e: ExecutableElement) : RuntimeException("Method '${e.simpleString()}()' must not have any 'throws' declaration in its signature")