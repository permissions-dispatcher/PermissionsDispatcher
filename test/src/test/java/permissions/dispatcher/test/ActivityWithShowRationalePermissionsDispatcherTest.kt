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
import org.mockito.Matchers
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import permissions.dispatcher.PermissionRequest

@Suppress("IllegalIdentifier")
@RunWith(PowerMockRunner::class)
@PrepareForTest(ActivityCompat::class, PermissionChecker::class, AppOpsManagerCompat::class, Process::class)
class ActivityWithShowRationalePermissionsDispatcherTest {

    private lateinit var activity: ActivityWithShowRationale

    companion object {
        private var requestCode = 0

        @BeforeClass
        @JvmStatic
        fun setUpForClass() {
            requestCode = getRequestCameraConstant(ActivityWithShowRationalePermissionsDispatcher::class.java)
        }
    }

    @Before
    fun setUp() {
        activity = Mockito.mock(ActivityWithShowRationale::class.java)
        PowerMockito.mockStatic(ActivityCompat::class.java)
        PowerMockito.mockStatic(PermissionChecker::class.java)
        PowerMockito.mockStatic(Process::class.java)
        PowerMockito.mockStatic(AppOpsManagerCompat::class.java)
    }

    @Test
    fun `already granted call the method`() {
        mockCheckSelfPermission(true)

        ActivityWithShowRationalePermissionsDispatcher.showCameraWithPermissionCheck(activity)

        Mockito.verify(activity, Mockito.times(1)).showCamera()
    }

    @Test
    fun `not granted does not call the method`() {
        mockCheckSelfPermission(false)
        mockShouldShowRequestPermissionRationaleActivity(true)

        ActivityWithShowRationalePermissionsDispatcher.showCameraWithPermissionCheck(activity)

        Mockito.verify(activity, Mockito.times(0)).showCamera()
    }

    @Test
    fun `not granted permission and show rationale is true then call the rationale method`() {
        mockCheckSelfPermission(false)
        mockShouldShowRequestPermissionRationaleActivity(true)

        ActivityWithShowRationalePermissionsDispatcher.showCameraWithPermissionCheck(activity)

        Mockito.verify(activity, Mockito.times(1)).showRationaleForCamera(Matchers.any(PermissionRequest::class.java))
    }

    @Test
    fun `not granted permission and show rationale is false then does not call the rationale method`() {
        mockCheckSelfPermission(false)
        mockShouldShowRequestPermissionRationaleActivity(false)

        ActivityWithShowRationalePermissionsDispatcher.showCameraWithPermissionCheck(activity)

        Mockito.verify(activity, Mockito.times(0)).showRationaleForCamera(Matchers.any(PermissionRequest::class.java))
    }

    @Test
    fun `the method is called if verifyPermission is true`() {
        ActivityWithShowRationalePermissionsDispatcher.onRequestPermissionsResult(activity, requestCode, intArrayOf(PackageManager.PERMISSION_GRANTED))

        Mockito.verify(activity, Mockito.times(1)).showCamera()
    }

    @Test
    fun `the method is not called if verifyPermission is false`() {
        ActivityWithShowRationalePermissionsDispatcher.onRequestPermissionsResult(activity, requestCode, intArrayOf(PackageManager.PERMISSION_DENIED))

        Mockito.verify(activity, Mockito.times(0)).showCamera()
    }

    @Test
    fun `no method call if request code is not related to the library`() {
        ActivityWithShowRationalePermissionsDispatcher.onRequestPermissionsResult(activity, requestCode + 1000, null)

        Mockito.verify(activity, Mockito.times(0)).showCamera()
    }

    @Test
    fun `xiaomi device permissionToOp returns null grant permission`() {
        testForXiaomi()
        mockPermissionToOp(null)

        ActivityWithShowRationalePermissionsDispatcher.showCameraWithPermissionCheck(activity)

        Mockito.verify(activity, Mockito.times(1)).showCamera()
    }

    @Test
    fun `xiaomi device grant permission`() {
        testForXiaomi()
        mockPermissionToOp("")
        mockNoteOp(AppOpsManagerCompat.MODE_ALLOWED)
        mockCheckSelfPermission(true)

        ActivityWithShowRationalePermissionsDispatcher.showCameraWithPermissionCheck(activity)

        Mockito.verify(activity, Mockito.times(1)).showCamera()
    }

    @Test
    fun `xiaomi noteOp returns not allowed value should not call the method`() {
        testForXiaomi()
        mockPermissionToOp("")
        mockNoteOp(AppOpsManagerCompat.MODE_IGNORED)
        mockCheckSelfPermission(true)

        ActivityWithShowRationalePermissionsDispatcher.showCameraWithPermissionCheck(activity)

        Mockito.verify(activity, Mockito.times(0)).showCamera()
    }

    @Test
    fun `xiaomi noteOp returns allowed but checkSelfPermission not allowed value should not call the method`() {
        testForXiaomi()
        mockPermissionToOp("")
        mockNoteOp(AppOpsManagerCompat.MODE_ALLOWED)
        mockCheckSelfPermission(false)

        ActivityWithShowRationalePermissionsDispatcher.showCameraWithPermissionCheck(activity)

        Mockito.verify(activity, Mockito.times(0)).showCamera()
    }

    @Test
    fun `blow M follows checkSelfPermissions result false`() {
        overwriteCustomSdkInt(22)
        mockCheckSelfPermission(false)

        ActivityWithShowRationalePermissionsDispatcher.showCameraWithPermissionCheck(activity)

        Mockito.verify(activity, Mockito.times(0)).showCamera()
    }

    @Test
    fun `blow M follows checkSelfPermissions result true`() {
        overwriteCustomSdkInt(22)
        mockCheckSelfPermission(true)

        ActivityWithShowRationalePermissionsDispatcher.showCameraWithPermissionCheck(activity)

        Mockito.verify(activity, Mockito.times(1)).showCamera()
    }
}