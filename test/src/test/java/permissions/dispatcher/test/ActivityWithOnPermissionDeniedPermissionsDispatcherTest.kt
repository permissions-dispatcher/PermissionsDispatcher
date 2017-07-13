package permissions.dispatcher.test

import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.PermissionChecker
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@Suppress("IllegalIdentifier")
@RunWith(PowerMockRunner::class)
@PrepareForTest(ActivityCompat::class, PermissionChecker::class)
class ActivityWithOnPermissionDeniedPermissionsDispatcherTest {
    private lateinit var activity: ActivityWithOnPermissionDenied

    companion object {
        private var requestCode = 0

        @BeforeClass
        @JvmStatic
        fun setUpForClass() {
            requestCode = getRequestCameraConstant(ActivityWithOnPermissionDeniedPermissionsDispatcher::class.java)
        }
    }

    @Before
    fun setUp() {
        activity = Mockito.mock(ActivityWithOnPermissionDenied::class.java)
        PowerMockito.mockStatic(ActivityCompat::class.java)
        PowerMockito.mockStatic(PermissionChecker::class.java)
    }

    @Test
    fun `already granted call the method`() {
        mockCheckSelfPermission(true)

        ActivityWithOnPermissionDeniedPermissionsDispatcher.showCameraWithCheck(activity)

        Mockito.verify(activity, Mockito.times(1)).showCamera()
    }

    @Test
    fun `not granted does not call the method`() {
        mockCheckSelfPermission(false)
        mockShouldShowRequestPermissionRationaleActivity(true)

        ActivityWithOnPermissionDeniedPermissionsDispatcher.showCameraWithCheck(activity)

        Mockito.verify(activity, Mockito.times(0)).showCamera()
    }

    @Test
    fun `the method is called if verifyPermission is true`() {
        ActivityWithOnPermissionDeniedPermissionsDispatcher.onRequestPermissionsResult(activity, requestCode, intArrayOf(PackageManager.PERMISSION_GRANTED))

        Mockito.verify(activity, Mockito.times(1)).showCamera()
    }

    @Test
    fun `the method is not called if verifyPermission is false`() {
        ActivityWithOnPermissionDeniedPermissionsDispatcher.onRequestPermissionsResult(activity, requestCode, intArrayOf(PackageManager.PERMISSION_DENIED))

        Mockito.verify(activity, Mockito.times(0)).showCamera()
    }

    @Test
    fun `show deny method is call if verifyPermission is false and shouldShowRequestPermissionRationale is true`() {
        mockShouldShowRequestPermissionRationaleActivity(true)

        ActivityWithOnPermissionDeniedPermissionsDispatcher.onRequestPermissionsResult(activity, requestCode, intArrayOf(PackageManager.PERMISSION_DENIED))

        Mockito.verify(activity, Mockito.times(1)).showDeniedForCamera()
    }

    @Test
    fun `no the method call if request code is not related to the library`() {
        ActivityWithOnPermissionDeniedPermissionsDispatcher.onRequestPermissionsResult(activity, requestCode + 1000, null)

        Mockito.verify(activity, Mockito.times(0)).showCamera()
    }

    @Test
    fun `no denied method call if request code is not related to the library`() {
        ActivityWithOnPermissionDeniedPermissionsDispatcher.onRequestPermissionsResult(activity, requestCode + 1000, null)

        Mockito.verify(activity, Mockito.times(0)).showDeniedForCamera()
    }

}