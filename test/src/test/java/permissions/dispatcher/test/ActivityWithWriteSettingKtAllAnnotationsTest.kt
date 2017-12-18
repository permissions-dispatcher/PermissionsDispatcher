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
        AppOpsManagerCompat::class, Process::class, Settings.System::class, Build.VERSION::class, Uri::class)
class ActivityWithWriteSettingKtAllAnnotationsTest {
    
    private lateinit var activity: ActivityWithWriteSettingKtAllAnnotations

    companion object {
        private var requestCode = 0

        @BeforeClass
        @JvmStatic
        fun setUpForClass() {
            // TODO Reflection on Kotlin top-level properties?
            requestCode = 13
        }
    }

    @Before
    fun setUp() {
        activity = Mockito.mock(ActivityWithWriteSettingKtAllAnnotations::class.java)
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

        activity.writeSettingWithPermissionCheck()

        Mockito.verify(activity, Mockito.times(1)).writeSetting()
    }

    @Test
    fun `checkSelfPermission returns false but canWrite returns true means granted`() {
        mockCheckSelfPermission(false)
        mockCanWrite(true)

        activity.writeSettingWithPermissionCheck()

        Mockito.verify(activity, Mockito.times(1)).writeSetting()
    }

    @Test
    fun `if permission not granted and no rationale activity, then call startActivityForResult`() {
        mockCheckSelfPermission(false)
        mockCanWrite(false)
        mockUriParse()
        mockShouldShowRequestPermissionRationaleActivity(false)

        activity.writeSettingWithPermissionCheck()

        Mockito.verify(activity, Mockito.times(1)).startActivityForResult(Matchers.any(Intent::class.java), Matchers.eq(requestCode))
    }

    @Test
    fun `if permission not granted and requires rationale activity, then call method for show rationale`() {
        mockCheckSelfPermission(false)
        mockCanWrite(false)
        mockUriParse()
        mockShouldShowRequestPermissionRationaleActivity(true)

        activity.writeSettingWithPermissionCheck()

        Mockito.verify(activity, Mockito.times(1)).showRationaleForWriteSettings(Matchers.any(PermissionRequest::class.java))
    }

    @Test
    fun `do nothing if requestCode is wrong one`() {
        activity.onActivityResult(-1)

        Mockito.verify(activity, Mockito.times(0)).writeSetting()
    }

    @Test
    fun `call the method if permission granted`() {
        mockCheckSelfPermission(true)

        activity.onActivityResult(requestCode)

        Mockito.verify(activity, Mockito.times(1)).writeSetting()
    }

    @Test
    fun `call the method if writeSetting returns true`() {
        mockCheckSelfPermission(false)
        mockCanWrite(true)

        activity.onActivityResult(requestCode)

        Mockito.verify(activity, Mockito.times(1)).writeSetting()
    }

    @Test
    fun `No call the method if permission not granted`() {
        mockCheckSelfPermission(false)
        mockCanWrite(false)

        activity.onActivityResult(requestCode)

        Mockito.verify(activity, Mockito.times(0)).writeSetting()
    }

    @Test
    fun `call showNeverAsk method if permission not granted and shouldShowRequestPermissionRationale false`() {
        mockCheckSelfPermission(false)
        mockCanWrite(false)
        mockShouldShowRequestPermissionRationaleActivity(false)

        activity.onActivityResult(requestCode)

        Mockito.verify(activity, Mockito.times(1)).showNeverAskForWriteSettings()
    }

    @Test
    fun `call showDenied method if permission not granted and shouldShowRequestPermissionRationale true`() {
        mockCheckSelfPermission(false)
        mockCanWrite(false)
        mockShouldShowRequestPermissionRationaleActivity(true)

        activity.onActivityResult(requestCode)

        Mockito.verify(activity, Mockito.times(1)).showDeniedForWriteSettings()
    }

}