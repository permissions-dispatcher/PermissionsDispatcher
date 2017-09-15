package permissions.dispatcher.test_v13

import android.content.pm.PackageManager
import android.os.Process
import android.support.v13.app.FragmentCompat
import android.support.v4.app.AppOpsManagerCompat
import android.support.v4.content.PermissionChecker
import android.support.v7.app.AppCompatActivity
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Matchers.any
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.rule.PowerMockRule
import org.robolectric.RobolectricTestRunner
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.test.*

@Suppress("IllegalIdentifier")
@RunWith(RobolectricTestRunner::class)
@PowerMockIgnore("org.mockito.*", "org.robolectric.*", "android.*")
@PrepareForTest(PermissionChecker::class, FragmentCompat::class, AppOpsManagerCompat::class, Process::class)
class FragmentWithAllAnnotationsPermissionsDispatcherTest {

    private lateinit var fragment: FragmentWithAllAnnotations

    @get:Rule val rule = PowerMockRule()

    companion object {
        private var requestCode = 0
        lateinit private var requestPermissions: Array<String>

        @BeforeClass
        @JvmStatic
        fun setUpForClass() {
            requestCode = getRequestCameraConstant(FragmentWithAllAnnotationsPermissionsDispatcher::class.java)
            requestPermissions = getPermissionRequestConstant(FragmentWithAllAnnotationsPermissionsDispatcher::class.java)
        }
    }

    @Before
    fun setUp() {
        fragment = Mockito.spy(FragmentWithAllAnnotations())

        PowerMockito.mockStatic(PermissionChecker::class.java)
        PowerMockito.mockStatic(FragmentCompat::class.java)
        PowerMockito.mockStatic(Process::class.java)
        PowerMockito.mockStatic(AppOpsManagerCompat::class.java)
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
        mockShouldShowRequestPermissionRationaleFragment(true)

        FragmentWithAllAnnotationsPermissionsDispatcher.showCameraWithPermissionCheck(fragment)

        Mockito.verify(fragment, Mockito.times(0)).showCamera()
    }

    @Test
    fun `not granted permission and show rationale is true then call the rationale method`() {
        mockCheckSelfPermission(false)
        mockShouldShowRequestPermissionRationaleFragment(true)

        FragmentWithAllAnnotationsPermissionsDispatcher.showCameraWithPermissionCheck(fragment)

        Mockito.verify(fragment, Mockito.times(1)).showRationaleForCamera(any(PermissionRequest::class.java))
    }

    @Test
    fun `not granted permission and show rationale is false then does not call the rationale method`() {
        mockCheckSelfPermission(false)
        mockShouldShowRequestPermissionRationaleFragment(false)

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
        mockShouldShowRequestPermissionRationaleFragment(false)

        FragmentWithAllAnnotationsPermissionsDispatcher.onRequestPermissionsResult(fragment, requestCode, intArrayOf(PackageManager.PERMISSION_DENIED))

        Mockito.verify(fragment, Mockito.times(1)).showNeverAskForCamera()
    }

    @Test
    fun `show deny method is call if verifyPermission is false and shouldShowRequestPermissionRationale is true`() {
        mockShouldShowRequestPermissionRationaleFragment(true)

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
    fun `xiaomi device permissionToOp returns null grant permission`() {
        testForXiaomi()
        mockPermissionToOp(null)

        FragmentWithAllAnnotationsPermissionsDispatcher.showCameraWithPermissionCheck(fragment)

        Mockito.verify(fragment, Mockito.times(1)).showCamera()
    }

    @Test
    fun `xiaomi device grant permission`() {
        testForXiaomi()
        mockPermissionToOp("")
        mockNoteOp(AppOpsManagerCompat.MODE_ALLOWED)
        mockCheckSelfPermission(true)
        mockGetActivity(fragment, Mockito.mock(AppCompatActivity::class.java))

        FragmentWithAllAnnotationsPermissionsDispatcher.showCameraWithPermissionCheck(fragment)

        Mockito.verify(fragment, Mockito.times(1)).showCamera()
    }

    @Test
    fun `xiaomi noteOp returns not allowed value should not call the method`() {
        testForXiaomi()
        mockPermissionToOp("")
        mockNoteOp(AppOpsManagerCompat.MODE_IGNORED)
        mockCheckSelfPermission(true)
        mockGetActivity(fragment, Mockito.mock(AppCompatActivity::class.java))

        FragmentWithAllAnnotationsPermissionsDispatcher.showCameraWithPermissionCheck(fragment)

        Mockito.verify(fragment, Mockito.times(0)).showCamera()
    }

    @Test
    fun `xiaomi noteOp returns allowed but checkSelfPermission not allowed value should not call the method`() {
        testForXiaomi()
        mockPermissionToOp("")
        mockNoteOp(AppOpsManagerCompat.MODE_ALLOWED)
        mockCheckSelfPermission(false)
        mockGetActivity(fragment, Mockito.mock(AppCompatActivity::class.java))

        FragmentWithAllAnnotationsPermissionsDispatcher.showCameraWithPermissionCheck(fragment)

        Mockito.verify(fragment, Mockito.times(0)).showCamera()
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
