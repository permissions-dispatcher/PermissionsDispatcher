package permissions.dispatcher.test

import android.Manifest
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import permissions.dispatcher.*

@RuntimePermissions
open class ConductorWithAllAnnotationsKt : Controller() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        throw IllegalArgumentException()
    }

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