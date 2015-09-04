package permissions.dispatcher.processor.exception

import permissions.dispatcher.processor.RuntimePermissionsElement

/**
 * Created by marcel on 04.09.15.
 */
public class NoAnnotatedMethodsException<A : Annotation>(rpe: RuntimePermissionsElement, type: Class<A>): RuntimeException("Annotated class '${rpe.inputClassName}' doesn't have any method annotated with '@${type.getSimpleName()}'") {
}