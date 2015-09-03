package permissions.dispatcher.processor.exception

import permissions.dispatcher.processor.RuntimePermissionsElement

/**
 * Created by marcel on 03.09.15.
 */
public class SupportV13MissingException(e: RuntimePermissionsElement) : RuntimeException("Annotated class '${e.inputClassName}' can\'t be processed, because the support-v13 dependency is missing") {
}