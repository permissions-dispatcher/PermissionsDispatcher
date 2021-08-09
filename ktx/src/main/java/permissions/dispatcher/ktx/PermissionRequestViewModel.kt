package permissions.dispatcher.ktx

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.ref.WeakReference

@MainThread
internal class PermissionRequestViewModel : ViewModel() {
    private val permissionRequestResult =
        MutableLiveData<MutableMap<String, Event<PermissionResult>>>()
        get() {
            field.value ?: run { field.value = mutableMapOf() }
            return field
        }

    fun postPermissionRequestResult(key: String, value: PermissionResult) {
        permissionRequestResult.value?.set(key, Event(value))
        permissionRequestResult.notifyObserver()
    }

    inline fun observe(
        owner: LifecycleOwner,
        key: String,
        requiresPermission: WeakReference<Fun>,
        onPermissionDenied: WeakReference<Fun>?,
        onNeverAskAgain: WeakReference<Fun>?
    ) {
        permissionRequestResult.observe(owner, {
            when (it[key]?.getContentIfNotHandled()) {
                PermissionResult.GRANTED -> requiresPermission.get()?.invoke()
                PermissionResult.DENIED -> onPermissionDenied?.get()?.invoke()
                PermissionResult.DENIED_AND_DISABLED -> onNeverAskAgain?.get()?.invoke()
                else -> Unit
            }
        })
    }

    fun removeObservers(owner: LifecycleOwner) = permissionRequestResult.removeObservers(owner)

    private fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }
}
