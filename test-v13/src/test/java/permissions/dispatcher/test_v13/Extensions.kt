package permissions.dispatcher.test

import android.annotation.SuppressLint
import android.app.Fragment
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Process
import android.support.v13.app.FragmentCompat
import android.support.v4.app.AppOpsManagerCompat
import android.support.v4.content.PermissionChecker
import android.support.v7.app.AppCompatActivity
import org.mockito.Matchers
import org.mockito.Matchers.any
import org.mockito.Matchers.anyString
import org.powermock.api.mockito.PowerMockito
import java.lang.reflect.Field
import java.lang.reflect.Modifier

fun mockCheckSelfPermission(result: Boolean) {
    val value = if (result) PackageManager.PERMISSION_GRANTED else PackageManager.PERMISSION_DENIED
    PowerMockito.`when`(PermissionChecker.checkSelfPermission(any(Context::class.java), anyString())).thenReturn(value)
}

@SuppressLint("NewApi")
fun mockShouldShowRequestPermissionRationaleFragment(result: Boolean) {
    PowerMockito.`when`(FragmentCompat.shouldShowRequestPermissionRationale(any(Fragment::class.java), anyString())).thenReturn(result)
}

fun mockGetActivity(fragment: Fragment, result: AppCompatActivity) {
    PowerMockito.`when`(fragment.activity).thenReturn(result)
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
    PowerMockito.`when`(AppOpsManagerCompat.noteOp(any(Context::class.java), anyString(), Matchers.anyInt(), anyString())).thenReturn(result)
}