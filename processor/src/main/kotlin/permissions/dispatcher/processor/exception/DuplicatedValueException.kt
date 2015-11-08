package permissions.dispatcher.processor.exception

import permissions.dispatcher.processor.util.simpleString
import javax.lang.model.element.ExecutableElement

public class DuplicatedValueException<A : Annotation>(value: List<String>, e: ExecutableElement, annotation: Class<A>): RuntimeException("$value is duplicated in '${e.simpleString()}()' annotated with '@${annotation.simpleName}'") {
}