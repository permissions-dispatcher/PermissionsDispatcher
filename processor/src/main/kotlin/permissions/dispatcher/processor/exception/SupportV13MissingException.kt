package permissions.dispatcher.processor.exception

import permissions.dispatcher.processor.RuntimePermissionsElement

class SupportV13MissingException(e: RuntimePermissionsElement) : RuntimeException("PermissionsDispatcher for annotated class '${e.inputClassName}' can\'t be generated, because the support-v13 dependency is missing on your project")