package permissions.dispatcher.test

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import permissions.dispatcher.test.ActivityWithWriteSettingAllAnnotationsPermissionsDispatcher.onActivityResult
import permissions.dispatcher.test.ActivityWithWriteSettingAllAnnotationsPermissionsDispatcher.writeSettingWithPermissionCheck

@Suppress("IllegalIdentifier")
@RunWith(PowerMockRunner::class)
@PrepareForTest(ActivityCompat::class, ContextCompat::class, Settings::class, Build.VERSION::class, Uri::class)
class ActivityWithWriteSettingAllAnnotationsPermissionsDispatcherTest {
    private lateinit var activity: ActivityWithWriteSettingAllAnnotations

    companion object {
        private var requestCode = 0

        @BeforeClass
        @JvmStatic
        fun setUpForClass() {
            requestCode = getRequestWriteSetting(ActivityWithWriteSettingAllAnnotationsPermissionsDispatcher::class.java)
        }
    }

    @Before
    fun setUp() {
        activity = Mockito.mock(ActivityWithWriteSettingAllAnnotations::class.java)
        PowerMockito.mockStatic(ActivityCompat::class.java)
        PowerMockito.mockStatic(ContextCompat::class.java)
        PowerMockito.mockStatic(Settings.System::class.java)
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
    fun `call showDenied method if permission not granted and shouldShowRequestPermissionRationale true`() {
        mockCheckSelfPermission(false)
        mockCanWrite(false)
        mockShouldShowRequestPermissionRationaleActivity(true)

        onActivityResult(activity, requestCode)

        Mockito.verify(activity, Mockito.times(1)).showDeniedForWriteSettings()
    }
}