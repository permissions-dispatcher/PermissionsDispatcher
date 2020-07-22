package permissions.dispatcher.ktx

import android.content.Context
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import permissions.dispatcher.PermissionUtils.hasSelfPermissions
import permissions.dispatcher.PermissionUtils.shouldShowRequestPermissionRationale

sealed class PermissionRequestType {
    object Normal : PermissionRequestType() {
        override fun checkPermissions(context: Context, permissions: Array<out String>): Boolean =
            hasSelfPermissions(context, *permissions)

        override fun createFragment(vararg permissions: String): PermissionRequestFragment =
            PermissionRequestFragment.NormalRequestPermissionFragment.newInstance(
                permissions
            )
    }

    object SystemAlertWindow : PermissionRequestType() {
        override fun checkPermissions(context: Context, permissions: Array<out String>): Boolean =
            Build.VERSION.SDK_INT < 23 || Settings.canDrawOverlays(context)

        @RequiresApi(Build.VERSION_CODES.M)
        override fun createFragment(vararg permissions: String): PermissionRequestFragment =
            PermissionRequestFragment.SpecialRequestPermissionFragment.newInstance(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION
            )
    }

    object WriteSettings : PermissionRequestType() {
        override fun checkPermissions(context: Context, permissions: Array<out String>): Boolean =
            Build.VERSION.SDK_INT < 23 || Settings.System.canWrite(context)

        @RequiresApi(Build.VERSION_CODES.M)
        override fun createFragment(vararg permissions: String): PermissionRequestFragment =
            PermissionRequestFragment.SpecialRequestPermissionFragment.newInstance(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION
            )
    }

    abstract fun checkPermissions(context: Context, permissions: Array<out String>): Boolean

    abstract fun createFragment(vararg permissions: String): PermissionRequestFragment

    fun invoke(
        permissions: Array<out String>,
        activity: FragmentActivity,
        onShowRationale: ShowRationaleFunc?,
        onPermissionDenied: Func?,
        requiresPermission: Func
    ) {
        if (checkPermissions(activity, permissions)) {
            requiresPermission()
        } else {
            val requestFun = { activity.commitFragment(createFragment(*permissions)) }
            if (shouldShowRequestPermissionRationale(activity, *permissions)) {
                onShowRationale?.invoke(KtxPermissionRequest.create(onPermissionDenied, requestFun))
            } else {
                requestFun.invoke()
            }
        }
    }

    private fun FragmentActivity.commitFragment(fragment: PermissionRequestFragment) =
        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, fragment)
            .commitNowAllowingStateLoss()
}
