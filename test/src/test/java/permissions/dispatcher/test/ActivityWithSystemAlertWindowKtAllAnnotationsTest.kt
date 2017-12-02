package permissions.dispatcher.test

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Process
import android.provider.Settings
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
@PrepareForTest(ActivityCompat::class, PermissionChecker::class,
        AppOpsManagerCompat::class, Process::class, Settings::class, Build.VERSION::class, Uri::class)
class ActivityWithSystemAlertWindowKtAllAnnotationsTest {
    
    private lateinit var activity: ActivityWithSystemAlertWindowKtAllAnnotations

    companion object {
        private var requestCode = 0

        @BeforeClass
        @JvmStatic
        fun setUpForClass() {
            // TODO Reflection on Kotlin top-level properties?
            requestCode = 9
        }
    }

    @Before
    fun setUp() {
        activity = Mockito.mock(ActivityWithSystemAlertWindowKtAllAnnotations::class.java)
        PowerMockito.mockStatic(ActivityCompat::class.java)
        PowerMockito.mockStatic(PermissionChecker::class.java)
        PowerMockito.mockStatic(Process::class.java)
        PowerMockito.mockStatic(AppOpsManagerCompat::class.java)
        PowerMockito.mockStatic(Settings::class.java)
        PowerMockito.mockStatic(Uri::class.java)

        PowerMockito.mockStatic(Build.VERSION::class.java)
        PowerMockito.field(Build.VERSION::class.java, "SDK_INT").setInt(null, 25)
    }

    @Test
    fun `already granted call the method`() {
        mockCheckSelfPermission(true)

        activity.systemAlertWindowWithPermissionCheck()

        Mockito.verify(activity, Mockito.times(1)).systemAlertWindow()
    }

    @Test
    fun `checkSelfPermission returns false but canDrawOverlays returns true means granted`() {
        mockCheckSelfPermission(false)
        mockCanDrawOverlays(true)

        activity.systemAlertWindowWithPermissionCheck()

        Mockito.verify(activity, Mockito.times(1)).systemAlertWindow()
    }

    @Test
    fun `if permission not granted and no rationale activity, then call startActivityForResult`() {
        mockCheckSelfPermission(false)
        mockCanDrawOverlays(false)
        mockUriParse()
        mockShouldShowRequestPermissionRationaleActivity(false)

        activity.systemAlertWindowWithPermissionCheck()

        Mockito.verify(activity, Mockito.times(1)).startActivityForResult(Matchers.any(Intent::class.java), Matchers.eq(requestCode))
    }

    @Test
    fun `if permission not granted and requires rationale activity, then call method for show rationale`() {
        mockCheckSelfPermission(false)
        mockCanDrawOverlays(false)
        mockUriParse()
        mockShouldShowRequestPermissionRationaleActivity(true)

        activity.systemAlertWindowWithPermissionCheck()

        Mockito.verify(activity, Mockito.times(1)).showRationaleForSystemAlertWindow(Matchers.any(PermissionRequest::class.java))
    }

    @Test
    fun `do nothing if requestCode is wrong one`() {
        activity.onActivityResult(-1)

        Mockito.verify(activity, Mockito.times(0)).systemAlertWindow()
    }

    @Test
    fun `call the method if permission granted`() {
        mockCheckSelfPermission(true)

        activity.onActivityResult(requestCode)

        Mockito.verify(activity, Mockito.times(1)).systemAlertWindow()
    }

    @Test
    fun `call the method if canDrawOverlays returns true`() {
        mockCheckSelfPermission(false)
        mockCanDrawOverlays(true)

        activity.onActivityResult(requestCode)

        Mockito.verify(activity, Mockito.times(1)).systemAlertWindow()
    }

    @Test
    fun `No call the method if permission not granted`() {
        mockCheckSelfPermission(false)
        mockCanDrawOverlays(false)

        activity.onActivityResult(requestCode)

        Mockito.verify(activity, Mockito.times(0)).systemAlertWindow()
    }

    @Test
    fun `call showNeverAsk method if permission not granted and shouldShowRequestPermissionRationale false`() {
        mockCheckSelfPermission(false)
        mockCanDrawOverlays(false)
        mockShouldShowRequestPermissionRationaleActivity(false)

        activity.onActivityResult(requestCode)

        Mockito.verify(activity, Mockito.times(1)).showNeverAskForSystemAlertWindow()
    }

    @Test
    fun `call showDenied method if permission not granted and shouldShowRequestPermissionRationale true`() {
        mockCheckSelfPermission(false)
        mockCanDrawOverlays(false)
        mockShouldShowRequestPermissionRationaleActivity(true)

        activity.onActivityResult(requestCode)

        Mockito.verify(activity, Mockito.times(1)).showDeniedForSystemAlertWindow()
    }

}