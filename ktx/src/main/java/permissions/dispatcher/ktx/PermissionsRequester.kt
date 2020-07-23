package permissions.dispatcher.ktx

/**
 * An intermediate class that is able to launch permissions request process as appropriate.
 * [launch] method kicks off the actual process.
 */
interface PermissionsRequester {
    fun launch()
}
