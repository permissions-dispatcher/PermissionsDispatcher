package permissions.dispatcher.test

import android.Manifest
import androidx.fragment.app.Fragment
import permissions.dispatcher.*

@RuntimePermissions
open class SupportFragmentWithAllAnnotationsKt : Fragment() {

    @NeedsPermission(Manifest.permission.CAMERA)
    fun showCamera() {
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
