package permissions.dispatcher.processor.exception

import permissions.dispatcher.processor.RuntimePermissionsElement

public class NoAnnotatedMethodsException<A : Annotation>(rpe: RuntimePermissionsElement, type: Class<A>): RuntimeException("Annotated class '${rpe.inputClassName}' doesn't have any method annotated with '@${type.getSimpleName()}'") {
}