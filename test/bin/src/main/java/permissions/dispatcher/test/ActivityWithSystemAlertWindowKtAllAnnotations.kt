package permissions.dispatcher.test

import android.Manifest
import android.content.Intent
import android.support.v7.app.AppCompatActivity

import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.OnShowRationale
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions
open class ActivityWithSystemAlertWindowKtAllAnnotations : AppCompatActivity() {

    @NeedsPermission(Manifest.permission.SYSTEM_ALERT_WINDOW)
    internal fun systemAlertWindow() {
    }

    @OnShowRationale(Manifest.permission.SYSTEM_ALERT_WINDOW)
    internal fun showRationaleForSystemAlertWindow(request: PermissionRequest?) {
    }

    @OnPermissionDenied(Manifest.permission.SYSTEM_ALERT_WINDOW)
    internal fun showDeniedForSystemAlertWindow() {
    }

    @OnNeverAskAgain(Manifest.permission.SYSTEM_ALERT_WINDOW)
    internal fun showNeverAskForSystemAlertWindow() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        onActivityResult(requestCode)
    }
}