package permissions.dispatcher.test

import android.Manifest
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test
import permissions.dispatcher.ktx.Func
import permissions.dispatcher.ktx.PermissionRequestType
import permissions.dispatcher.ktx.PermissionsRequestFragment
import permissions.dispatcher.ktx.ShowRationaleFunc

class PermissionRequestTypeTest {
    private lateinit var fragment: PermissionsRequestFragment
    private lateinit var onShowRationale: ShowRationaleFunc
    private lateinit var onPermissionDenied: Func
    private lateinit var onNeverAskAgain: Func
    private lateinit var requiresPermission: Func

    @Before
    fun setUp() {
        fragment = mock()
        onShowRationale = mock()
        onPermissionDenied = mock()
        onNeverAskAgain = mock()
        requiresPermission = mock()
    }

    @Test
    fun `Others#invokeRequest calls requestPermissions`() {
        val permissions = arrayOf(Manifest.permission.READ_CALENDAR)
        PermissionRequestType.Others.invokeRequest(
            fragment,
            permissions,
            requiresPermission,
            onNeverAskAgain,
            onPermissionDenied
        )

        verify(fragment).requestPermissions(
            permissions,
            requiresPermission,
            onNeverAskAgain,
            onPermissionDenied)
    }

    @Test
    fun `SystemAlertWindow#invokeRequest calls requestOverlayPermission`() {
        val permissions = arrayOf(Manifest.permission.READ_CALENDAR)
        PermissionRequestType.SystemAlertWindow.invokeRequest(
            fragment,
            permissions,
            requiresPermission,
            onNeverAskAgain,
            onPermissionDenied
        )

        verify(fragment).requestOverlayPermission(
            requiresPermission,
            onPermissionDenied)
    }

    @Test
    fun `WriteSettings#invokeRequest calls requestWriteSettingsPermission`() {
        val permissions = arrayOf(Manifest.permission.READ_CALENDAR)
        PermissionRequestType.WriteSettings.invokeRequest(
            fragment,
            permissions,
            requiresPermission,
            onNeverAskAgain,
            onPermissionDenied
        )

        verify(fragment).requestWriteSettingsPermission(
            requiresPermission,
            onPermissionDenied)
    }
}
