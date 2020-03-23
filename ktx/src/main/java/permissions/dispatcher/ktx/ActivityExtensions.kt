package permissions.dispatcher.ktx

import android.Manifest
import androidx.fragment.app.FragmentActivity

/**
 * Wraps [requiresPermission] in runtime permissions check with the given arguments.
 *
 * @param permissions the permissions [requiresPermission] requires.
 * @param onShowRationale the method explains why the permissions are required.
 * @param onPermissionDenied the method invoked if the user doesn't grant the permissions.
 * @param onNeverAskAgain the method invoked if the user does not deny the permissions with
 * "never ask again" option.
 * @param requiresPermission the action requires [permissions].
 */
fun FragmentActivity.withPermissionsCheck(
    vararg permissions: String,
    onShowRationale: ShowRationaleFunc? = null,
    onPermissionDenied: Func? = null,
    onNeverAskAgain: Func? = null,
    requiresPermission: Func) {
    PermissionRequestType.Others.invoke(
        permissions = permissions,
        activity = this,
        onShowRationale = onShowRationale,
        onPermissionDenied = onPermissionDenied,
        onNeverAskAgain = onNeverAskAgain,
        requiresPermission = requiresPermission)
}

/**
 * Wraps [requiresPermission] in the dedicated runtime permission check for
 * [Manifest.permission.WRITE_SETTINGS] with the given arguments.
 *
 * @param onShowRationale the method explains why the permissions are required.
 * @param onPermissionDenied the method invoked if the user doesn't grant the permissions.
 * @param requiresPermission the action requires [permissions].
 */
fun FragmentActivity.withWriteSettingsPermissionCheck(
    onShowRationale: ShowRationaleFunc? = null,
    onPermissionDenied: Func? = null,
    requiresPermission: Func) {
    PermissionRequestType.WriteSettings.invoke(
        permissions = arrayOf(Manifest.permission.WRITE_SETTINGS),
        activity = this,
        onShowRationale = onShowRationale,
        onPermissionDenied = onPermissionDenied,
        onNeverAskAgain = null,
        requiresPermission = requiresPermission)
}

/**
 * Wraps [requiresPermission] in the dedicated runtime permission check for
 * [Manifest.permission.SYSTEM_ALERT_WINDOW] with the given arguments.
 *
 * @param onShowRationale the method explains why the permissions are required.
 * @param onPermissionDenied the method invoked if the user doesn't grant the permissions.
 * @param requiresPermission the action requires [permissions].
 */
fun FragmentActivity.withSystemAlertWindowPermissionCheck(
    onShowRationale: ShowRationaleFunc? = null,
    onPermissionDenied: Func? = null,
    requiresPermission: Func) {
    PermissionRequestType.SystemAlertWindow.invoke(
        permissions = arrayOf(Manifest.permission.SYSTEM_ALERT_WINDOW),
        activity = this,
        onShowRationale = onShowRationale,
        onPermissionDenied = onPermissionDenied,
        onNeverAskAgain = null,
        requiresPermission = requiresPermission)
}
