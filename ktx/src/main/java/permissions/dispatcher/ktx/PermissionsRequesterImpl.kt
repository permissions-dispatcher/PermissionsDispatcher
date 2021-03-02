package permissions.dispatcher.ktx

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import permissions.dispatcher.PermissionUtils

internal class PermissionsRequesterImpl(
    private val permissions: Array<out String>,
    private val view: ViewType,
    private val onShowRationale: ShowRationaleFun?,
    private val onPermissionDenied: Fun?,
    private val requiresPermission: Fun,
    private val onNeverAskAgain: Fun?,
    private val permissionRequestType: PermissionRequestType
) : PermissionsRequester {
    override fun launch() {
        if (permissionRequestType.checkPermissions(view.context, permissions)) {
            requiresPermission()
        } else {
            ViewModelProvider(view.viewModelStoreOwner).get(PermissionRequestViewModel::class.java)
                .observe(
                    view.lifeCycleOwner,
                    requiresPermission,
                    onPermissionDenied,
                    onNeverAskAgain
                )
            val requestFun = {
                view.fragmentManager
                    .beginTransaction()
                    .replace(android.R.id.content, permissionRequestType.fragment(permissions))
                    .commitNowAllowingStateLoss()
            }
            if (shouldShowRequestPermissionRationale(view, permissions)) {
                onShowRationale?.invoke(KtxPermissionRequest.create(onPermissionDenied, requestFun))
            } else {
                requestFun.invoke()
            }
        }
    }

    private fun shouldShowRequestPermissionRationale(
        view: ViewType,
        permissions: Array<out String>
    ): Boolean = when (view) {
        is ActivityType -> PermissionUtils.shouldShowRequestPermissionRationale(
            view.left,
            *permissions
        )
        is FragmentType -> PermissionUtils.shouldShowRequestPermissionRationale(
            view.right,
            *permissions
        )
    }
}

private val ViewType.context: Context
    get() = when (this) {
        is ActivityType -> left.applicationContext
        is FragmentType -> right.requireContext()
    }

private val ViewType.viewModelStoreOwner: ViewModelStoreOwner
    get() = when (this) {
        is ActivityType -> left
        is FragmentType -> right
    }

private val ViewType.lifeCycleOwner: LifecycleOwner
    get() = when (this) {
        is ActivityType -> left
        is FragmentType -> right
    }

private val ViewType.fragmentManager: FragmentManager
    get() = when (this) {
        is ActivityType -> left.supportFragmentManager
        is FragmentType -> right.childFragmentManager
    }
