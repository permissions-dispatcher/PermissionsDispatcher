package permissions.dispatcher.test

import android.content.pm.PackageManager
import android.os.Process
import android.support.v4.app.AppOpsManagerCompat
import android.support.v4.content.PermissionChecker
import android.support.v7.app.AppCompatActivity
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
@PrepareForTest(PermissionChecker::class, SupportFragmentWithAllAnnotationsKt::class, AppOpsManagerCompat::class, Process::class)
class SupportFragmentWithAllAnnotationsKtPermissionsDispatcherTest {

    private lateinit var fragment: SupportFragmentWithAllAnnotationsKt

    companion object {
        private var requestCode = 0
        lateinit private var requestPermissions: Array<String>

        @BeforeClass
        @JvmStatic
        fun setUpForClass() {
            // TODO Reflection on Kotlin top-level properties?
            requestCode = 15
            requestPermissions = arrayOf("android.permission.CAMERA")
        }
    }

    @Before
    fun setUp() {
        fragment = PowerMockito.mock(SupportFragmentWithAllAnnotationsKt::class.java)

        PowerMockito.mockStatic(PermissionChecker::class.java)
        PowerMockito.mockStatic(Process::class.java)
        PowerMockito.mockStatic(AppOpsManagerCompat::class.java)
    }

    @Test
    fun `already granted call the method`() {
        mockCheckSelfPermission(true)

        fragment.showCameraWithPermissionCheck()

        Mockito.verify(fragment, Mockito.times(1)).showCamera()
    }

    @Test
    fun `not granted does not call the method`() {
        mockCheckSelfPermission(false)
        mockShouldShowRequestPermissionRationaleFragment(fragment, true)

        fragment.showCameraWithPermissionCheck()

        Mockito.verify(fragment, Mockito.times(0)).showCamera()
    }

    @Test
    fun `not granted permission and show rationale is true then call the rationale method`() {
        mockCheckSelfPermission(false)
        mockShouldShowRequestPermissionRationaleFragment(fragment, true)

        fragment.showCameraWithPermissionCheck()

        Mockito.verify(fragment, Mockito.times(1)).showRationaleForCamera(any(PermissionRequest::class.java))
    }

    @Test
    fun `not granted permission and show rationale is false then does not call the rationale method`() {
        mockCheckSelfPermission(false)
        mockShouldShowRequestPermissionRationaleFragment(fragment, false)

        fragment.showCameraWithPermissionCheck()

        Mockito.verify(fragment, Mockito.times(0)).showRationaleForCamera(any(PermissionRequest::class.java))
    }

    @Test
    fun `the method is called if verifyPermission is true`() {
        fragment.onRequestPermissionsResult(requestCode, intArrayOf(PackageManager.PERMISSION_GRANTED))

        Mockito.verify(fragment, Mockito.times(1)).showCamera()
    }

    @Test
    fun `the method is not called if verifyPermission is false`() {
        fragment.onRequestPermissionsResult(requestCode, intArrayOf(PackageManager.PERMISSION_DENIED))

        Mockito.verify(fragment, Mockito.times(0)).showCamera()
    }

    @Test
    fun `show never ask method is call if verifyPermission is false and shouldShowRequestPermissionRationale is false`() {
        mockShouldShowRequestPermissionRationaleFragment(fragment, false)

        fragment.onRequestPermissionsResult(requestCode, intArrayOf(PackageManager.PERMISSION_DENIED))

        Mockito.verify(fragment, Mockito.times(1)).showNeverAskForCamera()
    }

    @Test
    fun `show deny method is call if verifyPermission is false and shouldShowRequestPermissionRationale is true`() {
        mockShouldShowRequestPermissionRationaleFragment(fragment, true)

        fragment.onRequestPermissionsResult(requestCode, intArrayOf(PackageManager.PERMISSION_DENIED))

        Mockito.verify(fragment, Mockito.times(1)).showDeniedForCamera()
    }

    @Test
    fun `no the method call if request code is not related to the library`() {
        fragment.onRequestPermissionsResult(requestCode + 1000, intArrayOf())

        Mockito.verify(fragment, Mockito.times(0)).showCamera()
    }

    @Test
    fun `no denied method call if request code is not related to the library`() {
        fragment.onRequestPermissionsResult(requestCode + 1000, intArrayOf())

        Mockito.verify(fragment, Mockito.times(0)).showDeniedForCamera()
    }

    @Test
    fun `no never ask method call if request code is not related to the library`() {
        fragment.onRequestPermissionsResult(requestCode + 1000, intArrayOf())

        Mockito.verify(fragment, Mockito.times(0)).showNeverAskForCamera()
    }

    @Test
    fun `xiaomi device permissionToOp returns null grant permission`() {
        testForXiaomi()
        mockPermissionToOp(null)

        fragment.showCameraWithPermissionCheck()

        Mockito.verify(fragment, Mockito.times(1)).showCamera()
    }

    @Test
    fun `xiaomi device grant permission`() {
        testForXiaomi()
        mockPermissionToOp("")
        mockNoteOp(AppOpsManagerCompat.MODE_ALLOWED)
        mockCheckSelfPermission(true)
        mockGetActivity(fragment, Mockito.mock(AppCompatActivity::class.java))

        fragment.showCameraWithPermissionCheck()

        Mockito.verify(fragment, Mockito.times(1)).showCamera()
    }

    @Test
    fun `xiaomi noteOp returns not allowed value should not call the method`() {
        testForXiaomi()
        mockPermissionToOp("")
        mockNoteOp(AppOpsManagerCompat.MODE_IGNORED)
        mockCheckSelfPermission(true)
        mockGetActivity(fragment, Mockito.mock(AppCompatActivity::class.java))

        fragment.showCameraWithPermissionCheck()

        Mockito.verify(fragment, Mockito.times(0)).showCamera()
    }

    @Test
    fun `xiaomi noteOp returns allowed but checkSelfPermission not allowed value should not call the method`() {
        testForXiaomi()
        mockPermissionToOp("")
        mockNoteOp(AppOpsManagerCompat.MODE_ALLOWED)
        mockCheckSelfPermission(false)
        mockGetActivity(fragment, Mockito.mock(AppCompatActivity::class.java))

        fragment.showCameraWithPermissionCheck()

        Mockito.verify(fragment, Mockito.times(0)).showCamera()
    }

    @Test
    fun `blow M follows checkSelfPermissions result false`() {
        overwriteCustomSdkInt(22)
        mockCheckSelfPermission(false)

        fragment.showCameraWithPermissionCheck()

        Mockito.verify(fragment, Mockito.times(0)).showCamera()
    }

    @Test
    fun `blow M follows checkSelfPermissions result true`() {
        overwriteCustomSdkInt(22)
        mockCheckSelfPermission(true)

        fragment.showCameraWithPermissionCheck()

        Mockito.verify(fragment, Mockito.times(1)).showCamera()
    }
}
