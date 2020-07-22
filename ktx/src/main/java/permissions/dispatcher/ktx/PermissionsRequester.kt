package permissions.dispatcher.ktx

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider

class PermissionsRequester(
    private val permissions: Array<out String>,
    private val activity: FragmentActivity,
    private val onShowRationale: ShowRationaleFunc?,
    private val onPermissionDenied: Func?,
    private val requiresPermission: Func,
    onNeverAskAgain: Func?,
    private val permissionRequestType: PermissionRequestType
) {
    init {
        val viewModel = ViewModelProvider(activity).get(PermissionRequestViewModel::class.java)
        viewModel.permissionRequestResult.observe(
            activity,
            requiresPermission,
            onPermissionDenied,
            onNeverAskAgain
        )
    }

    fun request() = permissionRequestType.invoke(
        permissions = permissions,
        activity = activity,
        onShowRationale = onShowRationale,
        onPermissionDenied = onPermissionDenied,
        requiresPermission = requiresPermission
    )
}
