package permissions.dispatcher.test

import android.content.pm.PackageManager
import android.os.Process
import android.support.v4.app.ActivityCompat
import android.support.v4.app.AppOpsManagerCompat
import android.support.v4.content.PermissionChecker
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Matchers.any
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import permissions.dispatcher.PermissionRequest

@Suppress("IllegalIdentifier")
@RunWith(PowerMockRunner::class)
@PrepareForTest(ActivityCompat::class, PermissionChecker::class, AppOpsManagerCompat::class, Process::class)
class ActivityWithAllAnnotationsPermissionsDispatcherTest {

    companion object {
        private var requestCode = 0

        @BeforeClass
        @JvmStatic
        fun setUpForClass() {
            requestCode = getRequestCameraConstant(ActivityWithAllAnnotationsPermissionsDispatcher::class.java)
        }
    }

    @Before
    fun setUp() {
        PowerMockito.mockStatic(ActivityCompat::class.java)
        PowerMockito.mockStatic(PermissionChecker::class.java)
        PowerMockito.mockStatic(Process::class.java)
        PowerMockito.mockStatic(AppOpsManagerCompat::class.java)
    }

    @Test
    fun `already granted call the method`() {
        val activity = Mockito.mock(ActivityWithAllAnnotations::class.java)

        mockCheckSelfPermission(true)
        ActivityWithAllAnnotationsPermissionsDispatcher.showCameraWithPermissionCheck(activity)

        Mockito.verify(activity, Mockito.times(1)).showCamera()
    }

    @Test
    fun `not granted does not call the method`() {
        val activity = Mockito.mock(ActivityWithAllAnnotations::class.java)
        mockCheckSelfPermission(false)
        mockActivityCompatShouldShowRequestPermissionRationale(true)

        ActivityWithAllAnnotationsPermissionsDispatcher.showCameraWithPermissionCheck(activity)

        Mockito.verify(activity, Mockito.times(0)).showCamera()
    }

    @Test
    fun `not granted permission and show rationale is true then call the rationale method`() {
        val activity = Mockito.mock(ActivityWithAllAnnotations::class.java)
        mockCheckSelfPermission(false)
        mockActivityCompatShouldShowRequestPermissionRationale(true)

        ActivityWithAllAnnotationsPermissionsDispatcher.showCameraWithPermissionCheck(activity)

        Mockito.verify(activity, Mockito.times(1)).showRationaleForCamera(any(PermissionRequest::class.java))
    }

    @Test
    fun `not granted permission and show rationale is false then does not call the rationale method`() {
        val activity = Mockito.mock(ActivityWithAllAnnotations::class.java)
        mockCheckSelfPermission(false)
        mockActivityCompatShouldShowRequestPermissionRationale(false)

        ActivityWithAllAnnotationsPermissionsDispatcher.showCameraWithPermissionCheck(activity)

        Mockito.verify(activity, Mockito.times(0)).showRationaleForCamera(any(PermissionRequest::class.java))
    }

    @Test
    fun `the method is called if verifyPermission is true`() {
        val activity = Mockito.mock(ActivityWithAllAnnotations::class.java)
        ActivityWithAllAnnotationsPermissionsDispatcher.onRequestPermissionsResult(activity, requestCode, intArrayOf(PackageManager.PERMISSION_GRANTED))

        Mockito.verify(activity, Mockito.times(1)).showCamera()
    }

    @Test
    fun `the method is not called if verifyPermission is false`() {
        val activity = Mockito.mock(ActivityWithAllAnnotations::class.java)
        ActivityWithAllAnnotationsPermissionsDispatcher.onRequestPermissionsResult(activity, requestCode, intArrayOf(PackageManager.PERMISSION_DENIED))
        Mockito.verify(activity, Mockito.times(0)).showCamera()
    }

    @Test
    fun `show never ask method is call if verifyPermission is false and shouldShowRequestPermissionRationale is false`() {
        val activity = Mockito.mock(ActivityWithAllAnnotations::class.java)
        mockActivityCompatShouldShowRequestPermissionRationale(false)

        ActivityWithAllAnnotationsPermissionsDispatcher.onRequestPermissionsResult(activity, requestCode, intArrayOf(PackageManager.PERMISSION_DENIED))

        Mockito.verify(activity, Mockito.times(1)).showNeverAskForCamera()
    }

    @Test
    fun `show deny method is call if verifyPermission is false and shouldShowRequestPermissionRationale is true`() {
        val activity = Mockito.mock(ActivityWithAllAnnotations::class.java)
        mockActivityCompatShouldShowRequestPermissionRationale(true)

        ActivityWithAllAnnotationsPermissionsDispatcher.onRequestPermissionsResult(activity, requestCode, intArrayOf(PackageManager.PERMISSION_DENIED))

        Mockito.verify(activity, Mockito.times(1)).showDeniedForCamera()
    }

    @Test
    fun `no the method call if request code is not related to the library`() {
        val activity = Mockito.mock(ActivityWithAllAnnotations::class.java)
        ActivityWithAllAnnotationsPermissionsDispatcher.onRequestPermissionsResult(activity, requestCode + 1000, null)

        Mockito.verify(activity, Mockito.times(0)).showCamera()
    }

    @Test
    fun `no denied method call if request code is not related to the library`() {
        val activity = Mockito.mock(ActivityWithAllAnnotations::class.java)
        ActivityWithAllAnnotationsPermissionsDispatcher.onRequestPermissionsResult(activity, requestCode + 1000, null)

        Mockito.verify(activity, Mockito.times(0)).showDeniedForCamera()
    }

    @Test
    fun `no never ask method call if request code is not related to the library`() {
        val activity = Mockito.mock(ActivityWithAllAnnotations::class.java)
        ActivityWithAllAnnotationsPermissionsDispatcher.onRequestPermissionsResult(activity, requestCode + 1000, null)

        Mockito.verify(activity, Mockito.times(0)).showNeverAskForCamera()
    }

    @Test
    fun `xiaomi device permissionToOp returns null grant permission`() {
        val activity = Mockito.mock(ActivityWithAllAnnotations::class.java)
        testForXiaomi()
        mockPermissionToOp(null)

        ActivityWithAllAnnotationsPermissionsDispatcher.showCameraWithPermissionCheck(activity)

        Mockito.verify(activity, Mockito.times(1)).showCamera()
    }

    @Test
    fun `xiaomi device grant permission`() {
        val activity = Mockito.mock(ActivityWithAllAnnotations::class.java)
        testForXiaomi()
        mockPermissionToOp("")
        mockNoteOp(AppOpsManagerCompat.MODE_ALLOWED)
        mockCheckSelfPermission(true)

        ActivityWithAllAnnotationsPermissionsDispatcher.showCameraWithPermissionCheck(activity)

        Mockito.verify(activity, Mockito.times(1)).showCamera()
    }

    @Test
    fun `xiaomi noteOp returns not allowed value should not call the method`() {
        val activity = Mockito.mock(ActivityWithAllAnnotations::class.java)
        testForXiaomi()
        mockPermissionToOp("")
        mockNoteOp(AppOpsManagerCompat.MODE_IGNORED)
        mockCheckSelfPermission(true)

        ActivityWithAllAnnotationsPermissionsDispatcher.showCameraWithPermissionCheck(activity)

        Mockito.verify(activity, Mockito.times(0)).showCamera()
    }

    @Test
    fun `xiaomi noteOp returns allowed but checkSelfPermission not allowed value should not call the method`() {
        val activity = Mockito.mock(ActivityWithAllAnnotations::class.java)
        testForXiaomi()
        mockPermissionToOp("")
        mockNoteOp(AppOpsManagerCompat.MODE_ALLOWED)
        mockCheckSelfPermission(false)

        ActivityWithAllAnnotationsPermissionsDispatcher.showCameraWithPermissionCheck(activity)

        Mockito.verify(activity, Mockito.times(0)).showCamera()
    }

    @Test
    fun `blow M follows checkSelfPermissions result false`() {
        val activity = Mockito.mock(ActivityWithAllAnnotations::class.java)
        overwriteCustomSdkInt(22)
        mockCheckSelfPermission(false)

        ActivityWithAllAnnotationsPermissionsDispatcher.showCameraWithPermissionCheck(activity)

        Mockito.verify(activity, Mockito.times(0)).showCamera()
    }

    @Test
    fun `blow M follows checkSelfPermissions result true`() {
        val activity = Mockito.mock(ActivityWithAllAnnotations::class.java)
        overwriteCustomSdkInt(22)
        mockCheckSelfPermission(true)

        ActivityWithAllAnnotationsPermissionsDispatcher.showCameraWithPermissionCheck(activity)

        Mockito.verify(activity, Mockito.times(1)).showCamera()
    }
}
