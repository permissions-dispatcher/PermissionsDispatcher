package permissions.dispatcher.ktx

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import permissions.dispatcher.PermissionUtils

internal class PermissionsRequesterImpl(
    private val permissions: Array<out String>,
    private val activity: FragmentActivity,
    private val onShowRationale: ShowRationaleFun?,
    private val onPermissionDenied: Fun?,
    private val requiresPermission: Fun,
    onNeverAskAgain: Fun?,
    private val permissionRequestType: PermissionRequestType
) : PermissionsRequester {
    init {
        val viewModel = ViewModelProvider(activity).get(PermissionRequestViewModel::class.java)
        if (viewModel.hasActiveObservers()) {
            viewModel.removeObservers(activity) // allow only 1 observer to receive the event
        }
        viewModel.observe(
            activity,
            requiresPermission,
            onPermissionDenied,
            onNeverAskAgain
        )
    }

    override fun launch() {
        if (permissionRequestType.checkPermissions(activity, permissions)) {
            requiresPermission()
        } else {
            val requestFun = {
                activity.supportFragmentManager
                    .beginTransaction()
                    .replace(android.R.id.content, permissionRequestType.fragment(permissions))
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
