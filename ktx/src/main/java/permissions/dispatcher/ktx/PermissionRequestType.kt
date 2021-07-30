package permissions.dispatcher.ktx

import android.content.Context
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import permissions.dispatcher.PermissionUtils.hasSelfPermissions

internal sealed class PermissionRequestType {
    object Normal : PermissionRequestType() {
        override fun checkPermissions(context: Context, permissions: Array<out String>): Boolean =
            hasSelfPermissions(context, *permissions)

        override fun fragment(permissions: Array<out String>): PermissionRequestFragment =
            PermissionRequestFragment.NormalRequestPermissionFragment.newInstance(
                permissions
            )
    }

    object SystemAlertWindow : PermissionRequestType() {
        override fun checkPermissions(context: Context, permissions: Array<out String>): Boolean =
            Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(context)

        @RequiresApi(Build.VERSION_CODES.M)
        override fun fragment(permissions: Array<out String>): PermissionRequestFragment =
            PermissionRequestFragment.SpecialRequestPermissionFragment.newInstance(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION
            )
    }

    object WriteSettings : PermissionRequestType() {
        override fun checkPermissions(context: Context, permissions: Array<out String>): Boolean =
            Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.System.canWrite(context)

        @RequiresApi(Build.VERSION_CODES.M)
        override fun fragment(permissions: Array<out String>): PermissionRequestFragment =
            PermissionRequestFragment.SpecialRequestPermissionFragment.newInstance(
                Settings.ACTION_MANAGE_WRITE_SETTINGS
            )
    }

    abstract fun checkPermissions(context: Context, permissions: Array<out String>): Boolean

    abstract fun fragment(permissions: Array<out String>): PermissionRequestFragment
}
