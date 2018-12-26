package permissions.dispatcher.test

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.app.AppOpsManagerCompat
import androidx.core.content.PermissionChecker
import org.junit.After
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
import permissions.dispatcher.test.ActivityWithSystemAlertWindowAllAnnotationsPermissionsDispatcher.onActivityResult
import permissions.dispatcher.test.ActivityWithSystemAlertWindowAllAnnotationsPermissionsDispatcher.systemAlertWindowWithPermissionCheck

@Suppress("IllegalIdentifier")
@RunWith(PowerMockRunner::class)
@PrepareForTest(ActivityCompat::class, PermissionChecker::class,
        AppOpsManagerCompat::class, Settings::class, Build.VERSION::class, Uri::class)
class ActivityWithSystemAlertWindowAllAnnotationsPermissionsDispatcherTest {
    private lateinit var activity: ActivityWithSystemAlertWindowAllAnnotations

    companion object {
        private var requestCode = 0

        @BeforeClass
        @JvmStatic
        fun setUpForClass() {
            requestCode = getRequestSystemAlertWindow(ActivityWithSystemAlertWindowAllAnnotationsPermissionsDispatcher::class.java)
        }
    }

    @Before
    fun setUp() {
        activity = Mockito.mock(ActivityWithSystemAlertWindowAllAnnotations::class.java)
        PowerMockito.mockStatic(ActivityCompat::class.java)
        PowerMockito.mockStatic(PermissionChecker::class.java)
        PowerMockito.mockStatic(Settings::class.java)
        PowerMockito.mockStatic(Uri::class.java)

        PowerMockito.mockStatic(Build.VERSION::class.java)
        PowerMockito.field(Build.VERSION::class.java, "SDK_INT").setInt(null, 25)
    }

    @After
    fun tearDown() {
        clearCustomSdkInt()
    }

    @Test
    fun `already granted call the method`() {
        mockCheckSelfPermission(true)

        systemAlertWindowWithPermissionCheck(activity)

        Mockito.verify(activity, Mockito.times(1)).systemAlertWindow()
    }

    @Test
    fun `checkSelfPermission returns false but canDrawOverlays returns true means granted`() {
        mockCheckSelfPermission(false)
        mockCanDrawOverlays(true)

        systemAlertWindowWithPermissionCheck(activity)

        Mockito.verify(activity, Mockito.times(1)).systemAlertWindow()
    }

    @Test
    fun `if permission not granted and no rationale activity, then call startActivityForResult`() {
        mockCheckSelfPermission(false)
        mockCanDrawOverlays(false)
        mockUriParse()
        mockShouldShowRequestPermissionRationaleActivity(false)

        systemAlertWindowWithPermissionCheck(activity)

        Mockito.verify(activity, Mockito.times(1)).startActivityForResult(Matchers.any(Intent::class.java), Matchers.eq(requestCode))
    }

    @Test
    fun `if permission not granted and requires rationale activity, then call method for show rationale`() {
        mockCheckSelfPermission(false)
        mockCanDrawOverlays(false)
        mockUriParse()
        mockShouldShowRequestPermissionRationaleActivity(true)

        systemAlertWindowWithPermissionCheck(activity)

        Mockito.verify(activity, Mockito.times(1)).showRationaleForSystemAlertWindow(Matchers.any(PermissionRequest::class.java))
    }

    @Test
    fun `do nothing if requestCode is wrong one`() {
        onActivityResult(activity, -1)

        Mockito.verify(activity, Mockito.times(0)).systemAlertWindow()
    }

    @Test
    fun `call the method if permission granted`() {
        mockCheckSelfPermission(true)

        onActivityResult(activity, requestCode)

        Mockito.verify(activity, Mockito.times(1)).systemAlertWindow()
    }

    @Test
    fun `call the method if canDrawOverlays returns true`() {
        mockCheckSelfPermission(false)
        mockCanDrawOverlays(true)

        onActivityResult(activity, requestCode)

        Mockito.verify(activity, Mockito.times(1)).systemAlertWindow()
    }

    @Test
    fun `No call the method if permission not granted`() {
        mockCheckSelfPermission(false)
        mockCanDrawOverlays(false)

        onActivityResult(activity, requestCode)

        Mockito.verify(activity, Mockito.times(0)).systemAlertWindow()
    }

    @Test
    fun `call showDenied method if permission not granted and shouldShowRequestPermissionRationale true`() {
        mockCheckSelfPermission(false)
        mockCanDrawOverlays(false)
        mockShouldShowRequestPermissionRationaleActivity(true)

        onActivityResult(activity, requestCode)

        Mockito.verify(activity, Mockito.times(1)).showDeniedForSystemAlertWindow()
    }
}