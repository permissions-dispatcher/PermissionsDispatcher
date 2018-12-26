package permissions.dispatcher.test

import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@Suppress("IllegalIdentifier")
@RunWith(PowerMockRunner::class)
@PrepareForTest(ActivityCompat::class, PermissionChecker::class)
class ActivityWithStringParameterKtPermissionsDispatcherTest {

    private lateinit var activity: ActivityWithParametersKt

    @Before
    fun setUp() {
        activity = Mockito.mock(ActivityWithParametersKt::class.java)
        PowerMockito.mockStatic(ActivityCompat::class.java)
        PowerMockito.mockStatic(PermissionChecker::class.java)
    }

    @After
    fun tearDown() {
        clearCustomSdkInt()
    }

    @Test
    fun `call showCameraWithPermissionCheck`() {
        mockCheckSelfPermission(true)
        activity.showCameraWithPermissionCheck(arrayOf("option"))
        Mockito.verify(activity, Mockito.times(1)).showCamera(arrayOf("option"))
    }

    @Test
    fun `call showCalendarWithPermissionCheckd`() {
        mockCheckSelfPermission(true)
        activity.showCalendarWithPermissionCheck(1)
        Mockito.verify(activity, Mockito.times(1)).showCalendarWithPermissionCheck(1)
    }
}
