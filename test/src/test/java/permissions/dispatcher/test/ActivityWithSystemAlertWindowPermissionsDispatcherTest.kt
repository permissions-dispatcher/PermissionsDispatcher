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

@Suppress("IllegalIdentifier")
@RunWith(PowerMockRunner::class)
@PrepareForTest(ActivityCompat::class, PermissionChecker::class,
        AppOpsManagerCompat::class, Process::class, Settings::class, Build.VERSION::class, Uri::class)
class ActivityWithSystemAlertWindowPermissionsDispatcherTest {
    private lateinit var activity: ActivityWithSystemAlertWindow

    companion object {
        private var requestCode = 0

        @BeforeClass
        @JvmStatic
        fun setUpForClass() {
            requestCode = getRequestSystemAlertWindow(ActivityWithSystemAlertWindowPermissionsDispatcher::class.java)
        }
    }

    @Before
    fun setUp() {
        activity = Mockito.mock(ActivityWithSystemAlertWindow::class.java)
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

        ActivityWithSystemAlertWindowPermissionsDispatcher.systemAlertWindowWithPermissionCheck(activity)

        Mockito.verify(activity, Mockito.times(1)).systemAlertWindow()
    }

    @Test
    fun `checkSelfPermission returns false but canDrawOverlays returns true means granted`() {
        mockCheckSelfPermission(false)
        mockCanDrawOverlays(true)

        ActivityWithSystemAlertWindowPermissionsDispatcher.systemAlertWindowWithPermissionCheck(activity)

        Mockito.verify(activity, Mockito.times(1)).systemAlertWindow()
    }

    @Test
    fun `if permission not granted, then start new activity for overlay`() {
        mockCheckSelfPermission(false)
        mockCanDrawOverlays(false)
        mockUriParse()

        ActivityWithSystemAlertWindowPermissionsDispatcher.systemAlertWindowWithPermissionCheck(activity)

        Mockito.verify(activity, Mockito.times(1)).startActivityForResult(Matchers.any(Intent::class.java), Matchers.eq(requestCode))
    }

    @Test
    fun `do nothing if requestCode is wrong one`() {
        ActivityWithSystemAlertWindowPermissionsDispatcher.onActivityResult(activity, -1)

        Mockito.verify(activity, Mockito.times(0)).systemAlertWindow()
    }

    @Test
    fun `call the method if permission granted`() {
        mockCheckSelfPermission(true)

        ActivityWithSystemAlertWindowPermissionsDispatcher.onActivityResult(activity, requestCode)

        Mockito.verify(activity, Mockito.times(1)).systemAlertWindow()
    }

    @Test
    fun `call the method if canDrawOverlays returns true`() {
        mockCheckSelfPermission(false)
        mockCanDrawOverlays(true)

        ActivityWithSystemAlertWindowPermissionsDispatcher.onActivityResult(activity, requestCode)

        Mockito.verify(activity, Mockito.times(1)).systemAlertWindow()
    }

    @Test
    fun `No call the method if permission not granted`() {
        mockCheckSelfPermission(false)
        mockCanDrawOverlays(false)

        ActivityWithSystemAlertWindowPermissionsDispatcher.onActivityResult(activity, requestCode)

        Mockito.verify(activity, Mockito.times(0)).systemAlertWindow()
    }

}