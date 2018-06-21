package permissions.dispatcher.test_v13

import android.Manifest
import androidx.fragment.app.Fragment
import androidx.legacy.app.FragmentCompat
import permissions.dispatcher.*

@RuntimePermissions
open class FragmentWithAllAnnotationsKt : Fragment() {

    @NeedsPermission(Manifest.permission.CAMERA)
    fun showCamera() {
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun accessLocation(location: String?) {
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    fun showRationaleForCamera(request: PermissionRequest?) {
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    fun showDeniedForCamera() {
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    fun showNeverAskForCamera() {
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }
}
