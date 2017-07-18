package permissions.dispatcher.samplekotlin

import android.support.v4.app.ActivityCompat
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.PermissionUtils
import java.lang.ref.WeakReference

private val PERMISSION_SHOWCAMERA = arrayOf("android.permission.CAMERA")

private val REQUEST_SHOWCAMERA = 0

private val REQUEST_SHOWCONTACTS = 1

private val PERMISSION_SHOWCONTACTS = arrayOf("android.permission.READ_CONTACTS", "android.permission.WRITE_CONTACTS")

private class ShowCameraPermissionRequest constructor(target: MainActivity) : PermissionRequest {
    private val weakTarget = WeakReference(target)

    override fun proceed() {
        val target = weakTarget.get() ?: return
        ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA)
    }

    override fun cancel() {
        val target = weakTarget.get() ?: return
        target.onCameraDenied()
    }
}

internal fun MainActivity.showCameraWithCheck() {
    if (PermissionUtils.hasSelfPermissions(this, PERMISSION_SHOWCAMERA)) {
        showCamera()
    } else {
        if (PermissionUtils.shouldShowRequestPermissionRationale(this, PERMISSION_SHOWCAMERA)) {
            showRationaleForCamera(ShowCameraPermissionRequest(this))
        } else {
            ActivityCompat.requestPermissions(this, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA)
        }
    }
}

fun MainActivity.showContactsWithCheck() {
    if (PermissionUtils.hasSelfPermissions(this, PERMISSION_SHOWCONTACTS)) {
        showContacts()
    } else {
        if (PermissionUtils.shouldShowRequestPermissionRationale(this, PERMISSION_SHOWCONTACTS)) {
            showRationaleForContact(ShowContactsPermissionRequest(this))
        } else {
            ActivityCompat.requestPermissions(this, PERMISSION_SHOWCONTACTS, REQUEST_SHOWCONTACTS)
        }
    }
}

private class ShowContactsPermissionRequest constructor(target: MainActivity) : PermissionRequest {
    private val weakTarget = WeakReference(target)

    override fun proceed() {
        val target = weakTarget.get() ?: return
        ActivityCompat.requestPermissions(target, PERMISSION_SHOWCONTACTS, REQUEST_SHOWCONTACTS)
    }

    override fun cancel() {}
}

fun MainActivity.onRequestPermissionsResult(requestCode: Int, grantResults: IntArray) {
    when (requestCode) {
        REQUEST_SHOWCAMERA -> if (PermissionUtils.verifyPermissions(*grantResults)) {
            showCamera()
        } else {
            if (!PermissionUtils.shouldShowRequestPermissionRationale(this, PERMISSION_SHOWCAMERA)) {
                onCameraNeverAskAgain()
            } else {
                onCameraDenied()
            }
        }
        REQUEST_SHOWCONTACTS -> if (PermissionUtils.verifyPermissions(*grantResults)) {
            showContacts()
        }
        else -> {
        }
    }
}
