package permissions.dispatcher.test

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import permissions.dispatcher.*

@RuntimePermissions
open class ActivityWithAllAnnotationsKt : AppCompatActivity() {

    @NeedsPermission(Manifest.permission.CAMERA)
    fun showCamera() {
    }

    /**
     * PermissionRequest is nullable for testing, but it shouldn't
     */
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
