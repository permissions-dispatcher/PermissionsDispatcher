package permissions.dispatcher.test

import android.annotation.SuppressLint
import android.app.Fragment
import android.content.Context
import android.content.pm.PackageManager
import android.support.v13.app.FragmentCompat
import android.support.v4.content.PermissionChecker
import org.mockito.Matchers.any
import org.mockito.Matchers.anyString
import org.powermock.api.mockito.PowerMockito

fun mockCheckSelfPermission(result: Boolean) {
    val value = if (result) PackageManager.PERMISSION_GRANTED else PackageManager.PERMISSION_DENIED
    PowerMockito.`when`(PermissionChecker.checkSelfPermission(any(Context::class.java), anyString())).thenReturn(value)
}

@SuppressLint("NewApi")
fun mockShouldShowRequestPermissionRationaleFragment(result: Boolean) {
    PowerMockito.`when`(FragmentCompat.shouldShowRequestPermissionRationale(any(Fragment::class.java), anyString())).thenReturn(result)
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