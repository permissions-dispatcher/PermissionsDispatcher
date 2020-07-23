package permissions.dispatcher.ktx

internal enum class PermissionResult {
    /** Permission result: The permission is granted.  */
    GRANTED,

    /** Permission result: The permission is denied.  */
    DENIED,

    /** Permission result: The permission is denied with "Don't ask again" option. */
    DENIED_AND_DISABLED
}
