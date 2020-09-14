package permissions.dispatcher.ktx

import androidx.annotation.AnyThread

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 * ref: https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150
 */
@AnyThread
class Event<out T>(private val content: T) {
    @Volatile
    private var hasBeenHandled = false

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? = if (hasBeenHandled) {
        null
    } else {
        hasBeenHandled = true
        content
    }
}
