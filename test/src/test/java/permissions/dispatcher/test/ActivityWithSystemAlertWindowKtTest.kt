package permissions.dispatcher.test

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
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

@Suppress("IllegalIdentifier")
@RunWith(PowerMockRunner::class)
@PrepareForTest(ActivityCompat::class, PermissionChecker::class, Settings::class, Build.VERSION::class, Uri::class)
class ActivityWithSystemAlertWindowKtTest {
    private lateinit var activity: ActivityWithSystemAlertWindowKt

    companion object {
        private var requestCode = 0

        @BeforeClass
        @JvmStatic
        fun setUpForClass() {
            requestCode = ActivityWithSystemAlertWindowKt::systemAlertWindowWithPermissionCheck.packageLevelGetPropertyValueByName("REQUEST_SYSTEMALERTWINDOW") as Int
        }
    }

    @Before
    fun setUp() {
        activity = Mockito.mock(ActivityWithSystemAlertWindowKt::class.java)
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
    fun `if permission not granted, then start new activity for overlay`() {
        mockCheckSelfPermission(false)
        mockCanDrawOverlays(false)
        mockUriParse()

        activity.systemAlertWindowWithPermissionCheck()

        Mockito.verify(activity, Mockito.times(1)).startActivityForResult(Matchers.any(Intent::class.java), Matchers.eq(requestCode))
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
}