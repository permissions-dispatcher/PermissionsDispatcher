package permissions.dispatcher.ktx

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

internal class PermissionRequestViewModel : ViewModel() {
    private val _permissionRequestResult: MutableLiveData<PermissionResult> = MutableLiveData()

    val permissionRequestResult: LiveData<PermissionResult> = _permissionRequestResult

    fun postPermissionRequestResult(permissionResult: PermissionResult) =
        _permissionRequestResult.postValue(permissionResult)
}
