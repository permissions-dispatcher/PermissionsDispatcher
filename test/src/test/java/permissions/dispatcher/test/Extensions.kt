package permissions.dispatcher.test

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Process
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.app.AppOpsManagerCompat
import android.support.v4.app.Fragment
import android.support.v4.content.PermissionChecker
import android.support.v7.app.AppCompatActivity
import org.mockito.Matchers.*
import org.powermock.api.mockito.PowerMockito
import java.lang.reflect.Field
import java.lang.reflect.Modifier

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

fun mockGetActivity(fragment: Fragment, result: AppCompatActivity) {
    PowerMockito.`when`(fragment.activity).thenReturn(result)
}

fun mockCanDrawOverlays(result: Boolean) {
    PowerMockito.`when`(Settings.canDrawOverlays(any(Context::class.java))).thenReturn(result)
}

fun mockCanWrite(result: Boolean) {
    PowerMockito.`when`(Settings.System.canWrite(any(Context::class.java))).thenReturn(result)
}

private fun getPrivateField(clazz: Class<*>, fieldName: String): Field {
    val field = clazz.getDeclaredField(fieldName)
    field.isAccessible = true
    return field
}

private fun getPrivateIntField(clazz: Class<*>, fieldName: String): Int {
    return getPrivateField(clazz, fieldName).getInt(null)
}

fun getRequestWritesetting(clazz: Class<*>)
        = getPrivateIntField(clazz, "REQUEST_WRITESETTING")

fun getRequestSystemAlertWindow(clazz: Class<*>)
        = getPrivateIntField(clazz, "REQUEST_SYSTEMALERTWINDOW")

fun getRequestCameraConstant(clazz: Class<*>)
        = getPrivateIntField(clazz, "REQUEST_SHOWCAMERA")

fun getPermissionRequestConstant(clazz: Class<*>)
        = getPrivateField(clazz, "PERMISSION_SHOWCAMERA").get(null) as Array<String>

fun overwriteCustomManufacture(manufactureText: String = "Xiaomi") {
    val modifiersField = Field::class.java.getDeclaredField("modifiers")
    modifiersField.isAccessible = true

    val manufacture = Build::class.java.getDeclaredField("MANUFACTURER")
    manufacture.isAccessible = true
    modifiersField.setInt(manufacture, manufacture.modifiers and Modifier.FINAL.inv())
    manufacture.set(null, manufactureText)
}

fun overwriteCustomSdkInt(sdkInt: Int = 23) {
    val modifiersField = Field::class.java.getDeclaredField("modifiers")
    modifiersField.isAccessible = true

    val field = Build.VERSION::class.java.getDeclaredField("SDK_INT")
    field.isAccessible = true
    modifiersField.setInt(field, field.modifiers and Modifier.FINAL.inv())
    field.set(null, sdkInt)
}

fun testForXiaomi() {
    overwriteCustomManufacture()
    overwriteCustomSdkInt()
}

fun mockPermissionToOp(result: String?) {
    PowerMockito.`when`(AppOpsManagerCompat.permissionToOp(anyString())).thenReturn(result)
}

fun mockMyUid() {
    PowerMockito.`when`(Process.myUid()).thenReturn(1)
}

fun mockNoteOp(result: Int) {
    mockMyUid()
    PowerMockito.`when`(AppOpsManagerCompat.noteOp(any(Context::class.java), anyString(), anyInt(), anyString())).thenReturn(result)
}

fun mockUriParse(result: Uri? = null) {
    PowerMockito.`when`(Uri.parse(anyString())).thenReturn(result)
}