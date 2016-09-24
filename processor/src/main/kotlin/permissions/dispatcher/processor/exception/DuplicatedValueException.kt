package permissions.dispatcher.processor.exception

import permissions.dispatcher.processor.util.simpleString
import javax.lang.model.element.ExecutableElement

class DuplicatedValueException(value: List<String>, e: ExecutableElement, annotation: Class<*>) : RuntimeException("$value is duplicated in '${e.simpleString()}()' annotated with '@${annotation.simpleName}'")