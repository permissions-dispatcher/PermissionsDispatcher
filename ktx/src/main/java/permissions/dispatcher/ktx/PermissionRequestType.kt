package permissions.dispatcher.ktx

import android.content.Context
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import permissions.dispatcher.PermissionUtils.hasSelfPermissions
import permissions.dispatcher.PermissionUtils.shouldShowRequestPermissionRationale

internal sealed class PermissionRequestType {
    object Others : PermissionRequestType() {
        override fun checkPermissions(context: Context, permissions: Array<out String>): Boolean =
            hasSelfPermissions(context, *permissions)

        override fun invokeRequest(fragment: PermissionsRequestFragment,
                                   permissions: Array<out String>,
                                   requiresPermission: Func,
                                   onNeverAskAgain: Func?,
                                   onPermissionDenied: Func?) =
            fragment.requestPermissions(
                permissions = permissions,
                requiresPermission = requiresPermission,
                onNeverAskAgain = onNeverAskAgain,
                onPermissionDenied = onPermissionDenied
            )
    }

    object SystemAlertWindow : PermissionRequestType() {
        override fun checkPermissions(context: Context, permissions: Array<out String>): Boolean =
            Build.VERSION.SDK_INT < 23 || Settings.canDrawOverlays(context)

        @RequiresApi(Build.VERSION_CODES.M)
        override fun invokeRequest(fragment: PermissionsRequestFragment,
                                   permissions: Array<out String>,
                                   requiresPermission: Func,
                                   onNeverAskAgain: Func?,
                                   onPermissionDenied: Func?) =
            fragment.requestOverlayPermission(
                requiresPermission = requiresPermission,
                onPermissionDenied = onPermissionDenied
            )
    }

    object WriteSettings : PermissionRequestType() {
        override fun checkPermissions(context: Context, permissions: Array<out String>): Boolean =
            Build.VERSION.SDK_INT < 23 || Settings.System.canWrite(context)

        @RequiresApi(Build.VERSION_CODES.M)
        override fun invokeRequest(fragment: PermissionsRequestFragment,
                                   permissions: Array<out String>,
                                   requiresPermission: Func,
                                   onNeverAskAgain: Func?,
                                   onPermissionDenied: Func?) =
            fragment.requestWriteSettingsPermission(
                requiresPermission = requiresPermission,
                onPermissionDenied = onPermissionDenied
            )
    }

    abstract fun checkPermissions(context: Context, permissions: Array<out String>): Boolean

    abstract fun invokeRequest(fragment: PermissionsRequestFragment,
                               permissions: Array<out String>,
                               requiresPermission: Func,
                               onNeverAskAgain: Func?,
                               onPermissionDenied: Func?)

    internal fun requestPermissions(permissions: Array<out String>,
                                   target: FragmentActivity,
                                   requiresPermission: Func,
                                   onNeverAskAgain: Func?,
                                   onPermissionDenied: Func?) {
        var fragment = target.supportFragmentManager
            .findFragmentByTag(PermissionsRequestFragment.tag) as? PermissionsRequestFragment
        if (fragment == null) {
            fragment = PermissionsRequestFragment.newInstance()
            target.supportFragmentManager.beginTransaction()
                .add(fragment, PermissionsRequestFragment.tag)
                .commitNowAllowingStateLoss()
        }
        invokeRequest(
            fragment = fragment,
            permissions = permissions,
            requiresPermission = requiresPermission,
            onNeverAskAgain = onNeverAskAgain,
            onPermissionDenied = onPermissionDenied
        )
    }

    fun invoke(permissions: Array<out String>,
               activity: FragmentActivity,
               onShowRationale: ShowRationaleFunc?,
               onPermissionDenied: Func?,
               onNeverAskAgain: Func?,
               requiresPermission: Func) {
        if (checkPermissions(activity, permissions)) {
            requiresPermission()
        } else {
            if (shouldShowRequestPermissionRationale(activity, *permissions)) {
                onShowRationale?.invoke(KtxPermissionRequest.create(onPermissionDenied) {
                    requestPermissions(
                        permissions = permissions,
                        target = activity,
                        requiresPermission = requiresPermission,
                        onNeverAskAgain = onNeverAskAgain,
                        onPermissionDenied = onPermissionDenied
                    )
                })
            } else {
                requestPermissions(
                    permissions = permissions,
                    target = activity,
                    requiresPermission = requiresPermission,
                    onNeverAskAgain = onNeverAskAgain,
                    onPermissionDenied = onPermissionDenied
                )
            }
        }
    }
}
