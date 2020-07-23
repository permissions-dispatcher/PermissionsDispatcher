package permissions.dispatcher.ktx

import android.annotation.SuppressLint
import android.provider.Settings
import androidx.fragment.app.Fragment

/**
 * Constructs a request for ordinary permissions that require a grant from the user.
 * Be sure invoking the method when an activity is created to capture the callbacks appropriately.
 *
 * @param permissions the permissions [requiresPermission] requires.
 * @param onShowRationale the method explains why the permissions are required.
 * @param onPermissionDenied the method invoked if the user doesn't grant the permissions.
 * @param onNeverAskAgain the method invoked if the user does not deny the permissions with
 * "never ask again" option.
 * @param requiresPermission the action requires [permissions].
 * @see PermissionsRequester
 */
fun Fragment.constructPermissionsRequest(
    vararg permissions: String,
    onShowRationale: ShowRationaleFunc? = null,
    onPermissionDenied: Func? = null,
    onNeverAskAgain: Func? = null,
    requiresPermission: Func
): PermissionsRequester = PermissionsRequesterImpl(
    permissions = permissions,
    activity = requireActivity(),
    onShowRationale = onShowRationale,
    onPermissionDenied = onPermissionDenied,
    onNeverAskAgain = onNeverAskAgain,
    requiresPermission = requiresPermission,
    permissionRequestType = PermissionRequestType.Normal
)

/**
 * Constructs a request for [android.Manifest.permission.WRITE_SETTINGS].
 * Be sure invoking the method when an activity is created to capture the callbacks appropriately.
 *
 * @param onShowRationale the method explains why the permissions are required.
 * @param onPermissionDenied the method invoked if the user doesn't grant the permissions.
 * @param requiresPermission the action requires [permissions].
 * @see PermissionsRequester
 */
@SuppressLint("InlinedApi")
fun Fragment.constructWriteSettingsPermissionRequest(
    onShowRationale: ShowRationaleFunc? = null,
    onPermissionDenied: Func? = null,
    requiresPermission: Func
): PermissionsRequester = PermissionsRequesterImpl(
    permissions = arrayOf(Settings.ACTION_MANAGE_WRITE_SETTINGS),
    activity = requireActivity(),
    onShowRationale = onShowRationale,
    onPermissionDenied = onPermissionDenied,
    onNeverAskAgain = null,
    requiresPermission = requiresPermission,
    permissionRequestType = PermissionRequestType.WriteSettings
)

/**
 * Constructs a request for [android.Manifest.permission.SYSTEM_ALERT_WINDOW].
 * Be sure invoking the method when an activity is created to capture the callbacks appropriately.
 *
 * @param onShowRationale the method explains why the permissions are required.
 * @param onPermissionDenied the method invoked if the user doesn't grant the permissions.
 * @param requiresPermission the action requires [permissions].
 * @see PermissionsRequester
 */
@SuppressLint("InlinedApi")
fun Fragment.constructSystemAlertWindowPermissionRequest(
    onShowRationale: ShowRationaleFunc? = null,
    onPermissionDenied: Func? = null,
    requiresPermission: Func
): PermissionsRequester = PermissionsRequesterImpl(
    permissions = arrayOf(Settings.ACTION_MANAGE_OVERLAY_PERMISSION),
    activity = requireActivity(),
    onShowRationale = onShowRationale,
    onPermissionDenied = onPermissionDenied,
    onNeverAskAgain = null,
    requiresPermission = requiresPermission,
    permissionRequestType = PermissionRequestType.SystemAlertWindow
)
