package permissions.dispatcher.processor.exception

import permissions.dispatcher.processor.RuntimePermissionsElement

public class NoAnnotatedMethodsException(rpe: RuntimePermissionsElement, type: Class<*>): RuntimeException("Annotated class '${rpe.inputClassName}' doesn't have any method annotated with '@${type.simpleName}'") {
}