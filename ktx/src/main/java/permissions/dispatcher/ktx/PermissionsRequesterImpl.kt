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
    private val viewModel = ViewModelProvider(activity).get(PermissionRequestViewModel::class.java)

    init {
        viewModel.observe(
            activity,
            permissions.contentToString(),
            requiresPermission,
            onPermissionDenied,
            onNeverAskAgain
        )
    }

    override fun launch() {
        if (permissionRequestType.checkPermissions(activity, permissions)) {
            viewModel.removeObservers(activity)
            requiresPermission()
        } else {
            val requestFun: Fun = {
                activity.supportFragmentManager
                    .beginTransaction()
                    .replace(android.R.id.content, permissionRequestType.fragment(permissions))
                    .commitAllowingStateLoss()
            }
            if (PermissionUtils.shouldShowRequestPermissionRationale(activity, *permissions)) {
                onShowRationale?.invoke(KtxPermissionRequest.create(onPermissionDenied, requestFun))
            } else {
                requestFun.invoke()
            }
        }
    }
}
