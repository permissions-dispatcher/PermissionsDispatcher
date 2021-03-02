package permissions.dispatcher.ktx

import android.annotation.SuppressLint
import android.provider.Settings
import androidx.fragment.app.FragmentActivity

/**
 * Constructs a request for ordinary permissions that require a grant from the user.
 * Be sure to invoke the method when an activity is created to capture the valid callbacks.
 *
 * @param permissions the permissions [requiresPermission] requires.
 * @param onShowRationale the method explains why the permissions are required.
 * @param onPermissionDenied the method invoked if the user doesn't grant the permissions.
 * @param onNeverAskAgain the method invoked if the user does not deny the permissions with
 * "never ask again" option.
 * @param requiresPermission the action requires [permissions].
 * @see PermissionsRequester
 */
fun FragmentActivity.constructPermissionsRequest(
    vararg permissions: String,
    onShowRationale: ShowRationaleFun? = null,
    onPermissionDenied: Fun? = null,
    onNeverAskAgain: Fun? = null,
    requiresPermission: Fun
): PermissionsRequester = PermissionsRequesterImpl(
    permissions = permissions,
    view = ActivityType(this),
    onShowRationale = onShowRationale,
    onPermissionDenied = onPermissionDenied,
    onNeverAskAgain = onNeverAskAgain,
    requiresPermission = requiresPermission,
    permissionRequestType = PermissionRequestType.Normal
)

/**
 * Constructs a request for location permissions that require a grant from the user.
 * Be sure to invoke the method when an activity is created to capture the valid callbacks.
 *
 * @param onShowRationale the method explains why the permissions are required.
 * @param onPermissionDenied the method invoked if the user doesn't grant the permissions.
 * @param requiresPermission the action requires [permissions].
 * @see PermissionsRequester
 */
fun FragmentActivity.constructLocationPermissionRequest(
    vararg permissions: LocationPermission,
    onShowRationale: ShowRationaleFun? = null,
    onPermissionDenied: Fun? = null,
    onNeverAskAgain: Fun? = null,
    requiresPermission: Fun
): PermissionsRequester = PermissionsRequesterImpl(
    permissions = permissions.filterByApiLevel(),
    view = ActivityType(this),
    onShowRationale = onShowRationale,
    onPermissionDenied = onPermissionDenied,
    onNeverAskAgain = onNeverAskAgain,
    requiresPermission = requiresPermission,
    permissionRequestType = PermissionRequestType.Normal
)

/**
 * Constructs a request for [android.Manifest.permission.WRITE_SETTINGS].
 * Be sure to invoke the method when an activity is created to capture the valid callbacks.
 *
 * @param onShowRationale the method explains why the permissions are required.
 * @param onPermissionDenied the method invoked if the user doesn't grant the permissions.
 * @param requiresPermission the action requires [permissions].
 * @see PermissionsRequester
 */
@SuppressLint("InlinedApi")
fun FragmentActivity.constructWriteSettingsPermissionRequest(
    onShowRationale: ShowRationaleFun? = null,
    onPermissionDenied: Fun? = null,
    requiresPermission: Fun
): PermissionsRequester = PermissionsRequesterImpl(
    permissions = arrayOf(Settings.ACTION_MANAGE_WRITE_SETTINGS),
    view = ActivityType(this),
    onShowRationale = onShowRationale,
    onPermissionDenied = onPermissionDenied,
    onNeverAskAgain = null,
    requiresPermission = requiresPermission,
    permissionRequestType = PermissionRequestType.WriteSettings
)

/**
 * Constructs a request for [android.Manifest.permission.SYSTEM_ALERT_WINDOW].
 * Be sure to invoke the method when an activity is created to capture the valid callbacks.
 *
 * @param onShowRationale the method explains why the permissions are required.
 * @param onPermissionDenied the method invoked if the user doesn't grant the permissions.
 * @param requiresPermission the action requires [permissions].
 * @see PermissionsRequester
 */
@SuppressLint("InlinedApi")
fun FragmentActivity.constructSystemAlertWindowPermissionRequest(
    onShowRationale: ShowRationaleFun? = null,
    onPermissionDenied: Fun? = null,
    requiresPermission: Fun
): PermissionsRequester = PermissionsRequesterImpl(
    permissions = arrayOf(Settings.ACTION_MANAGE_OVERLAY_PERMISSION),
    view = ActivityType(this),
    onShowRationale = onShowRationale,
    onPermissionDenied = onPermissionDenied,
    onNeverAskAgain = null,
    requiresPermission = requiresPermission,
    permissionRequestType = PermissionRequestType.SystemAlertWindow
)
