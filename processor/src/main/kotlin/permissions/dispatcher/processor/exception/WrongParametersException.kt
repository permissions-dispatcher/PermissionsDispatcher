package permissions.dispatcher.processor.exception

import permissions.dispatcher.processor.util.simpleString
import javax.lang.model.element.ExecutableElement
import javax.lang.model.type.TypeMirror

class WrongParametersException(e: ExecutableElement, requiredType: TypeMirror?) : RuntimeException("Method '${e.simpleString()}()' must declare parameters of type '${requiredType?.simpleString()}'")