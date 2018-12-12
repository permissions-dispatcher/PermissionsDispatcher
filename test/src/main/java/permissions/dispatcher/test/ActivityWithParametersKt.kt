package permissions.dispatcher.test

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import permissions.dispatcher.*

@RuntimePermissions
open class ActivityWithParametersKt : AppCompatActivity() {

    @NeedsPermission(Manifest.permission.CAMERA)
    fun showCamera(arg: Array<out String>) {
    }

    @NeedsPermission(Manifest.permission.READ_CALENDAR)
    fun showCalendar(arg: Any) {
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }
}
