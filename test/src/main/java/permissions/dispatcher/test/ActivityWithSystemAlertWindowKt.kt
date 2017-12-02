package permissions.dispatcher.test

import android.Manifest
import android.content.Intent
import android.support.v7.app.AppCompatActivity

import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions
open class ActivityWithSystemAlertWindowKt : AppCompatActivity() {

    @NeedsPermission(Manifest.permission.SYSTEM_ALERT_WINDOW)
    internal fun systemAlertWindow() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        onActivityResult(requestCode)
    }
}
