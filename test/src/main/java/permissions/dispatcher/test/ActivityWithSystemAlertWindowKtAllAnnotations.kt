package permissions.dispatcher.test

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import permissions.dispatcher.*

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        onActivityResult(requestCode)
    }
}