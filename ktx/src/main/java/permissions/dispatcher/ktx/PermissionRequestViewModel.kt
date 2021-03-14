package permissions.dispatcher.ktx

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel

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
        crossinline requiresPermission: Fun,
        noinline onPermissionDenied: Fun?,
        noinline onNeverAskAgain: Fun?
    ) {
        permissionRequestResult.observe(owner, Observer {
            when (it[key]?.getContentIfNotHandled()) {
                PermissionResult.GRANTED -> requiresPermission.invoke()
                PermissionResult.DENIED -> onPermissionDenied?.invoke()
                PermissionResult.DENIED_AND_DISABLED -> onNeverAskAgain?.invoke()
                else -> Unit
            }
        })
    }

    fun removeObservers(owner: LifecycleOwner) = permissionRequestResult.removeObservers(owner)

    private fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }
}
