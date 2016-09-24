package permissions.dispatcher.processor.exception

import permissions.dispatcher.processor.RuntimePermissionsElement

class NoAnnotatedMethodsException(rpe: RuntimePermissionsElement, type: Class<*>) : RuntimeException("Annotated class '${rpe.inputClassName}' doesn't have any method annotated with '@${type.simpleName}'")