package permissions.dispatcher.ktx

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import permissions.dispatcher.PermissionUtils

/**
 * An intermediate class that is able to open runtime permissions request process if necessary.
 * [request] method kicks off the actual process.
 */
interface PermissionsRequester {
    fun request()
}

internal class PermissionsRequesterImpl(
    private val permissions: Array<out String>,
    private val activity: FragmentActivity,
    private val onShowRationale: ShowRationaleFunc?,
    private val onPermissionDenied: Func?,
    private val requiresPermission: Func,
    onNeverAskAgain: Func?,
    private val permissionRequestType: PermissionRequestType
): PermissionsRequester {
    init {
        val viewModel = ViewModelProvider(activity).get(PermissionRequestViewModel::class.java)
        viewModel.permissionRequestResult.observe(
            activity,
            requiresPermission,
            onPermissionDenied,
            onNeverAskAgain
        )
    }

    override fun request() {
        if (permissionRequestType.checkPermissions(activity, permissions)) {
            requiresPermission()
        } else {
            val fragment = permissionRequestType.fragment(permissions)
            val requestFun = {
                activity.supportFragmentManager
                    .beginTransaction()
                    .replace(android.R.id.content, fragment)
                    .commitNowAllowingStateLoss()
            }
            if (PermissionUtils.shouldShowRequestPermissionRationale(activity, *permissions)) {
                onShowRationale?.invoke(KtxPermissionRequest.create(onPermissionDenied, requestFun))
            } else {
                requestFun.invoke()
            }
        }
    }
}
