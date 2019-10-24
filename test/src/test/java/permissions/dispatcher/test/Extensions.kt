package permissions.dispatcher.test

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import org.mockito.Matchers.any
import org.mockito.Matchers.anyString
import org.powermock.api.mockito.PowerMockito
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import kotlin.jvm.internal.CallableReference
import kotlin.reflect.KFunction

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

private fun getPrivateIntField(clazz: Class<*>, fieldName: String): Int = getPrivateField(clazz, fieldName).getInt(null)

fun getRequestWriteSetting(clazz: Class<*>) = getPrivateIntField(clazz, "REQUEST_WRITESETTING")

fun getRequestSystemAlertWindow(clazz: Class<*>) = getPrivateIntField(clazz, "REQUEST_SYSTEMALERTWINDOW")

fun getRequestCameraConstant(clazz: Class<*>) = getPrivateIntField(clazz, "REQUEST_SHOWCAMERA")

fun getPermissionRequestConstant(clazz: Class<*>) = getPrivateField(clazz, "PERMISSION_SHOWCAMERA").get(null) as Array<String>

fun overwriteCustomSdkInt(sdkInt: Int = 23) {
    val modifiersField = Field::class.java.getDeclaredField("modifiers")
    modifiersField.isAccessible = true

    val field = Build.VERSION::class.java.getDeclaredField("SDK_INT")
    field.isAccessible = true
    modifiersField.setInt(field, field.modifiers and Modifier.FINAL.inv())
    field.set(null, sdkInt)
}

fun clearCustomSdkInt() {
    overwriteCustomSdkInt()
}

fun mockUriParse(result: Uri? = null) {
    PowerMockito.`when`(Uri.parse(anyString())).thenReturn(result)
}

/**
 * get other package level property value by other package level function name which is in the same kotlin file
 */
fun <R> KFunction<R>.packageLevelGetPropertyValueByName(otherPropertyName: String): Any? {
    return getTopPropertyValueByName(this as CallableReference, otherPropertyName)
}

fun getTopPropertyValueByName(otherCallableReference: CallableReference, propertyName: String): Any? {
    val owner = otherCallableReference.owner ?: return null
    val containerClass: Class<*>
    try {
        containerClass = owner::class.members.firstOrNull { it.name == "jClass" }?.call(owner) as Class<*>
    } catch (e: Exception) {
        throw IllegalArgumentException("No such property 'jClass'")
    }

    var tobeSearchMethodClass: Class<*>? = containerClass
    while (tobeSearchMethodClass != null) {
        tobeSearchMethodClass.declaredFields.forEach {
            if (it.name == propertyName) {
                it.isAccessible = true
                // top property(package property) should be static in java level
                if (Modifier.isStatic(it.modifiers)) {
                    return it.get(null)
                } else {
                    throw IllegalStateException("It is not a top property : $propertyName")
                }
            }
        }
        tobeSearchMethodClass = tobeSearchMethodClass.superclass
    }

    throw IllegalArgumentException("Can't find the property named :$propertyName in the same file with ${otherCallableReference.name}")
}
