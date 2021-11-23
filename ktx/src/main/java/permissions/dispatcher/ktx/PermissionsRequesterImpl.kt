package permissions.dispatcher.ktx

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
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
    private val observer: Observer<MutableMap<String, Event<PermissionResult>>>

    init {
        // https://github.com/permissions-dispatcher/PermissionsDispatcher/issues/729
        val key = permissions.sortedArray().contentToString()
        observer = Observer<MutableMap<String, Event<PermissionResult>>> {
            when (it[key]?.getContentIfNotHandled()) {
                PermissionResult.GRANTED -> WeakReference(requiresPermission).get()?.invoke()
                PermissionResult.DENIED -> WeakReference(onPermissionDenied).get()?.invoke()
                PermissionResult.DENIED_AND_DISABLED -> WeakReference(onNeverAskAgain).get()?.invoke()
                else -> Unit
            }
        }
        viewModel.observe(activity, observer)
    }

    override fun launch() {
        if (permissionRequestType.checkPermissions(activity, permissions)) {
            viewModel.removeObserver(observer)
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
