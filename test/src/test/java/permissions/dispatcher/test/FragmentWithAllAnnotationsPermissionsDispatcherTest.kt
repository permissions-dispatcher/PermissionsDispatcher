package permissions.dispatcher.test

import android.content.pm.PackageManager
import androidx.core.content.PermissionChecker
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Matchers.any
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import permissions.dispatcher.PermissionRequest

@Suppress("IllegalIdentifier")
@RunWith(PowerMockRunner::class)
@PrepareForTest(PermissionChecker::class, FragmentWithAllAnnotationsKt::class)
class FragmentWithAllAnnotationsPermissionsDispatcherTest {

    private lateinit var fragment: FragmentWithAllAnnotations

    companion object {
        private var requestCode = 0
        private lateinit var requestPermissions: Array<String>

        @BeforeClass
        @JvmStatic
        fun setUpForClass() {
            requestCode = getRequestCameraConstant(FragmentWithAllAnnotationsPermissionsDispatcher::class.java)
            requestPermissions = getPermissionRequestConstant(FragmentWithAllAnnotationsPermissionsDispatcher::class.java)
        }
    }

    @Before
    fun setUp() {
        fragment = PowerMockito.mock(FragmentWithAllAnnotations::class.java)

        PowerMockito.mockStatic(PermissionChecker::class.java)
    }

    @After
    fun tearDown() {
        clearCustomSdkInt()
    }

    @Test
    fun `already granted call the method`() {
        mockCheckSelfPermission(true)

        FragmentWithAllAnnotationsPermissionsDispatcher.showCameraWithPermissionCheck(fragment)

        Mockito.verify(fragment, Mockito.times(1)).showCamera()
    }

    @Test
    fun `not granted does not call the method`() {
        mockCheckSelfPermission(false)
        mockShouldShowRequestPermissionRationaleFragment(fragment, true)

        FragmentWithAllAnnotationsPermissionsDispatcher.showCameraWithPermissionCheck(fragment)

        Mockito.verify(fragment, Mockito.times(0)).showCamera()
    }

    @Test
    fun `not granted permission and show rationale is true then call the rationale method`() {
        mockCheckSelfPermission(false)
        mockShouldShowRequestPermissionRationaleFragment(fragment, true)

        FragmentWithAllAnnotationsPermissionsDispatcher.showCameraWithPermissionCheck(fragment)

        Mockito.verify(fragment, Mockito.times(1)).showRationaleForCamera(any(PermissionRequest::class.java))
    }

    @Test
    fun `not granted permission and show rationale is false then does not call the rationale method`() {
        mockCheckSelfPermission(false)
        mockShouldShowRequestPermissionRationaleFragment(fragment, false)

        FragmentWithAllAnnotationsPermissionsDispatcher.showCameraWithPermissionCheck(fragment)

        Mockito.verify(fragment, Mockito.times(0)).showRationaleForCamera(any(PermissionRequest::class.java))
    }

    @Test
    fun `the method is called if verifyPermission is true`() {
        FragmentWithAllAnnotationsPermissionsDispatcher.onRequestPermissionsResult(fragment, requestCode, intArrayOf(PackageManager.PERMISSION_GRANTED))

        Mockito.verify(fragment, Mockito.times(1)).showCamera()
    }

    @Test
    fun `the method is not called if verifyPermission is false`() {
        FragmentWithAllAnnotationsPermissionsDispatcher.onRequestPermissionsResult(fragment, requestCode, intArrayOf(PackageManager.PERMISSION_DENIED))

        Mockito.verify(fragment, Mockito.times(0)).showCamera()
    }

    @Test
    fun `show never ask method is call if verifyPermission is false and shouldShowRequestPermissionRationale is false`() {
        mockShouldShowRequestPermissionRationaleFragment(fragment, false)

        FragmentWithAllAnnotationsPermissionsDispatcher.onRequestPermissionsResult(fragment, requestCode, intArrayOf(PackageManager.PERMISSION_DENIED))

        Mockito.verify(fragment, Mockito.times(1)).showNeverAskForCamera()
    }

    @Test
    fun `show deny method is call if verifyPermission is false and shouldShowRequestPermissionRationale is true`() {
        mockShouldShowRequestPermissionRationaleFragment(fragment, true)

        FragmentWithAllAnnotationsPermissionsDispatcher.onRequestPermissionsResult(fragment, requestCode, intArrayOf(PackageManager.PERMISSION_DENIED))

        Mockito.verify(fragment, Mockito.times(1)).showDeniedForCamera()
    }

    @Test
    fun `no the method call if request code is not related to the library`() {
        FragmentWithAllAnnotationsPermissionsDispatcher.onRequestPermissionsResult(fragment, requestCode + 1000, null)

        Mockito.verify(fragment, Mockito.times(0)).showCamera()
    }

    @Test
    fun `no denied method call if request code is not related to the library`() {
        FragmentWithAllAnnotationsPermissionsDispatcher.onRequestPermissionsResult(fragment, requestCode + 1000, null)

        Mockito.verify(fragment, Mockito.times(0)).showDeniedForCamera()
    }

    @Test
    fun `no never ask method call if request code is not related to the library`() {
        FragmentWithAllAnnotationsPermissionsDispatcher.onRequestPermissionsResult(fragment, requestCode + 1000, null)

        Mockito.verify(fragment, Mockito.times(0)).showNeverAskForCamera()
    }

    @Test
    fun `blow M follows checkSelfPermissions result false`() {
        overwriteCustomSdkInt(22)
        mockCheckSelfPermission(false)

        FragmentWithAllAnnotationsPermissionsDispatcher.showCameraWithPermissionCheck(fragment)

        Mockito.verify(fragment, Mockito.times(0)).showCamera()
    }

    @Test
    fun `blow M follows checkSelfPermissions result true`() {
        overwriteCustomSdkInt(22)
        mockCheckSelfPermission(true)

        FragmentWithAllAnnotationsPermissionsDispatcher.showCameraWithPermissionCheck(fragment)

        Mockito.verify(fragment, Mockito.times(1)).showCamera()
    }
}
