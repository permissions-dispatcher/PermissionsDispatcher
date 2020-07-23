package permissions.dispatcher.ktx

/**
 * An intermediate class that is able to open runtime permissions request process if necessary.
 * [request] method kicks off the actual process.
 */
interface PermissionsRequester {
    fun request()
}
