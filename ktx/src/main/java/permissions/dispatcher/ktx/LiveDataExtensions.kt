package permissions.dispatcher.ktx

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

internal inline fun LiveData<PermissionResult>.observe(
    owner: LifecycleOwner,
    crossinline requiresPermission: Func,
    noinline onPermissionDenied: Func?,
    noinline onNeverAskAgain: Func?
) {
    observe(owner, Observer {
        when (it) {
            PermissionResult.GRANTED -> requiresPermission.invoke()
            PermissionResult.DENIED -> onPermissionDenied?.invoke()
            PermissionResult.DENIED_AND_DISABLED -> onNeverAskAgain?.invoke()
            else -> Unit
        }
    })
}
