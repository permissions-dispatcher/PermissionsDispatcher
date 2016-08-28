package permissions.dispatcher.processor

import java.util.concurrent.atomic.AtomicInteger

/**
 * Helper class providing app-level unique request codes
 * for a round trip of the annotation processor.
 */
class RequestCodeProvider {

    val currentCode = AtomicInteger(0)

    /**
     * Obtains the next unique request code.
     * This method atomically increments the value
     * returned upon the next invocation.
     */
    fun nextRequestCode(): Int = currentCode.andIncrement
}