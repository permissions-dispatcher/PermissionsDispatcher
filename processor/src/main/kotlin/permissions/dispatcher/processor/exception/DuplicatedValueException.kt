package permissions.dispatcher.processor.exception

public class DuplicatedValueException<A : Annotation>(value: List<String>, annotation: Class<A>): RuntimeException("$value is duplicated in $annotation") {
}