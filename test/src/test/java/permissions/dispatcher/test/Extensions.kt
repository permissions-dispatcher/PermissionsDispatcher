package permissions.dispatcher.test

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.PermissionChecker
import org.mockito.Matchers.any
import org.mockito.Matchers.anyString
import org.powermock.api.mockito.PowerMockito

fun mockShouldShowRequestPermissionRationaleActivity(result: Boolean) {
    PowerMockito.`when`(ActivityCompat.shouldShowRequestPermissionRationale(any(Activity::class.java), anyString())).thenReturn(result)
}

@SuppressLint("NewApi")
fun mockShouldShowRequestPermissionRationaleFragment(fragment: Fragment, result: Boolean) {
    PowerMockito.`when`(fragment.shouldShowRequestPermissionRationale(anyString())).thenReturn(result)
}

fun mockActivityCompatShouldShowRequestPermissionRationale(result: Boolean) {
    PowerMockito.`when`(ActivityCompat.shouldShowRequestPermissionRationale(any(Activity::class.java), anyString())).thenReturn(result)
}

fun mockCheckSelfPermission(result: Boolean) {
    val value = if (result) PackageManager.PERMISSION_GRANTED else PackageManager.PERMISSION_DENIED
    PowerMockito.`when`(PermissionChecker.checkSelfPermission(any(Context::class.java), anyString())).thenReturn(value)
}

fun getRequestCameraConstant(clazz: Class<*>): Int {
    val field = clazz.getDeclaredField("REQUEST_SHOWCAMERA")
    field.isAccessible = true
    return field.getInt(null)
}

fun getPermissionRequestConstant(clazz: Class<*>): Array<String> {
    val field = clazz.getDeclaredField("PERMISSION_SHOWCAMERA")
    field.isAccessible = true
    return field.get(null) as Array<String>
}