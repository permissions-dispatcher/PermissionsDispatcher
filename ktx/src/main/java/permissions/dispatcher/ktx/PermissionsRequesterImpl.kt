package permissions.dispatcher.ktx

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import permissions.dispatcher.PermissionUtils.shouldShowRequestPermissionRationale
import java.lang.ref.WeakReference

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
    private val requestFun: Fun = {
        activity.supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, permissionRequestType.fragment(permissions))
            .commitAllowingStateLoss()
    }

    init {
        viewModel.observe(
            activity,
            permissions.contentToString(),
            WeakReference(requiresPermission),
            WeakReference(onPermissionDenied),
            WeakReference(onNeverAskAgain)
        )
    }

    override fun launch() {
        if (permissionRequestType.checkPermissions(activity, permissions)) {
            viewModel.removeObservers(activity)
            requiresPermission()
        } else {
            if (shouldShowRequestPermissionRationale(activity, *permissions) && onShowRationale != null) {
                onShowRationale.invoke(KtxPermissionRequest.create(onPermissionDenied, requestFun))
            } else {
                requestFun.invoke()
            }
        }
    }
}
