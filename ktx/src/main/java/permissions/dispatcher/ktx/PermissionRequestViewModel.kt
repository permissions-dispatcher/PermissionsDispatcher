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

    fun observe(
        owner: LifecycleOwner,
        observer: Observer<MutableMap<String, Event<PermissionResult>>>,
    ) {
        permissionRequestResult.observe(owner, observer)
    }

    fun removeObserver(observer: Observer<MutableMap<String, Event<PermissionResult>>>) =
        permissionRequestResult.removeObserver(observer)

    private fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }
}
