package permissions.dispatcher.processor.exception

import permissions.dispatcher.processor.util.simpleString
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement

public class WrongReturnTypeException(e: ExecutableElement): RuntimeException("Method '${e.simpleString()}()' must specify return type 'void', not '${e.getReturnType()}'") {
}