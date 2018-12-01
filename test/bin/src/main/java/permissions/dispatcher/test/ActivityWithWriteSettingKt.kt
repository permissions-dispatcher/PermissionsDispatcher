package permissions.dispatcher.test

import android.Manifest
import android.content.Intent
import android.support.v7.app.AppCompatActivity

import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions
open class ActivityWithWriteSettingKt : AppCompatActivity() {

    @NeedsPermission(Manifest.permission.WRITE_SETTINGS)
    internal fun writeSetting() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        onActivityResult(requestCode)
    }
}