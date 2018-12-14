package permissions.dispatcher.processor.exception

import permissions.dispatcher.processor.util.simpleString
import javax.lang.model.element.ExecutableElement

class WrongParameterSizeException(e: ExecutableElement, size: Int) : RuntimeException("Method '${e.simpleString()}()' must have $size parameters")