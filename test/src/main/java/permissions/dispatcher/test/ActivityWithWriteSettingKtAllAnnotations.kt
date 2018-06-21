package permissions.dispatcher.test

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import permissions.dispatcher.*

@RuntimePermissions
open class ActivityWithWriteSettingKtAllAnnotations : AppCompatActivity() {

    @NeedsPermission(Manifest.permission.WRITE_SETTINGS)
    internal fun writeSetting() {
    }

    @OnShowRationale(Manifest.permission.WRITE_SETTINGS)
    internal fun showRationaleForWriteSettings(request: PermissionRequest?) {
    }

    @OnPermissionDenied(Manifest.permission.WRITE_SETTINGS)
    internal fun showDeniedForWriteSettings() {
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_SETTINGS)
    internal fun showNeverAskForWriteSettings() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        onActivityResult(requestCode)
    }
}