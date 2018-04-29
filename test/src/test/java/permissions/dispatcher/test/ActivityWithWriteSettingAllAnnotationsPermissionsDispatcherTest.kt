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
import permissions.dispatcher.test.ActivityWithWriteSettingAllAnnotationsPermissionsDispatcher.*

@Suppress("IllegalIdentifier")
@RunWith(PowerMockRunner::class)
@PrepareForTest(ActivityCompat::class, PermissionChecker::class,
        AppOpsManagerCompat::class, Process::class, Settings::class, Build.VERSION::class, Uri::class)
class ActivityWithWriteSettingAllAnnotationsPermissionsDispatcherTest {
    private lateinit var activity: ActivityWithWriteSettingAllAnnotations

    companion object {
        private var requestCode = 0

        @BeforeClass
        @JvmStatic
        fun setUpForClass() {
            requestCode = getRequestWritesetting(ActivityWithWriteSettingAllAnnotationsPermissionsDispatcher::class.java)
        }
    }

    @Before
    fun setUp() {
        activity = Mockito.mock(ActivityWithWriteSettingAllAnnotations::class.java)
        PowerMockito.mockStatic(ActivityCompat::class.java)
        PowerMockito.mockStatic(PermissionChecker::class.java)
        PowerMockito.mockStatic(Process::class.java)
        PowerMockito.mockStatic(AppOpsManagerCompat::class.java)
        PowerMockito.mockStatic(Settings.System::class.java)
        PowerMockito.mockStatic(Uri::class.java)

        PowerMockito.mockStatic(Build.VERSION::class.java)
        PowerMockito.field(Build.VERSION::class.java, "SDK_INT").setInt(null, 25)
    }

    @Test
    fun `already granted call the method`() {
        mockCheckSelfPermission(true)

        writeSettingWithPermissionCheck(activity)

        Mockito.verify(activity, Mockito.times(1)).writeSetting()
    }

    @Test
    fun `checkSelfPermission returns false but canWrite returns true means granted`() {
        mockCheckSelfPermission(false)
        mockCanWrite(true)

        writeSettingWithPermissionCheck(activity)

        Mockito.verify(activity, Mockito.times(1)).writeSetting()
    }

    @Test
    fun `if permission not granted, then start new activity for overlay`() {
        mockCheckSelfPermission(false)
        mockCanWrite(false)
        mockUriParse()

        writeSettingWithPermissionCheck(activity)

        Mockito.verify(activity, Mockito.times(1)).startActivityForResult(Matchers.any(Intent::class.java), Matchers.eq(requestCode))
    }

    @Test
    fun `do nothing if requestCode is wrong one`() {
        onActivityResult(activity,-1)

        Mockito.verify(activity, Mockito.times(0)).writeSetting()
    }

    @Test
    fun `call the method if permission granted`() {
        mockCheckSelfPermission(true)

        onActivityResult(activity, requestCode)

        Mockito.verify(activity, Mockito.times(1)).writeSetting()
    }

    @Test
    fun `call the method if canWrite returns true`() {
        mockCheckSelfPermission(false)
        mockCanWrite(true)

        onActivityResult(activity, requestCode)

        Mockito.verify(activity, Mockito.times(1)).writeSetting()
    }

    @Test
    fun `No call the method if permission not granted`() {
        mockCheckSelfPermission(false)
        mockCanWrite(false)

        onActivityResult(activity, requestCode)

        Mockito.verify(activity, Mockito.times(0)).writeSetting()
    }

    @Test
    fun `call showNeverAsk method if permission not granted and shouldShowRequestPermissionRationale false`() {
        mockCheckSelfPermission(false)
        mockCanWrite(false)
        mockShouldShowRequestPermissionRationaleActivity(false)

        onActivityResult(activity, requestCode)

        Mockito.verify(activity, Mockito.times(1)).showNeverAskForWriteSettings()
    }

    @Test
    fun `call showDenied method if permission not granted and shouldShowRequestPermissionRationale true`() {
        mockCheckSelfPermission(false)
        mockCanWrite(false)
        mockShouldShowRequestPermissionRationaleActivity(true)

        onActivityResult(activity, requestCode)

        Mockito.verify(activity, Mockito.times(1)).showDeniedForWriteSettings()
    }
}