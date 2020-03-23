package permissions.dispatcher.test

import android.Manifest
import android.app.Activity
import androidx.fragment.app.FragmentActivity
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.never
import permissions.dispatcher.PermissionUtils
import permissions.dispatcher.ktx.*

class ActivityExtensionsTest {
    private lateinit var activity: FragmentActivity
    private lateinit var onShowRationale: ShowRationaleFunc
    private lateinit var onPermissionDenied: Func
    private lateinit var onNeverAskAgain: Func
    private lateinit var requiresPermission: Func

    @Before
    fun setUp() {
        mockkObject(PermissionRequestType.Others)
        mockkObject(PermissionRequestType.SystemAlertWindow)
        mockkObject(PermissionRequestType.WriteSettings)

        activity = FragmentActivity()
        onShowRationale = mock()
        onPermissionDenied = mock()
        onNeverAskAgain = mock()
        requiresPermission = mock()
    }

    @After
    fun tearDown() = unmockkAll()

    @Test
    fun `requiresPermission is called when Others#checkPermissions returns true`() {
        every { PermissionRequestType.Others.checkPermissions(any(), any()) } returns true

        activity.withPermissionsCheck(
            Manifest.permission.READ_CALENDAR,
            onPermissionDenied = onPermissionDenied,
            onNeverAskAgain = onNeverAskAgain,
            onShowRationale = onShowRationale,
            requiresPermission = requiresPermission
        )

        verify(onPermissionDenied, never()).invoke()
        verify(onNeverAskAgain, never()).invoke()
        verify(onShowRationale, never()).invoke(any())
        verify(requiresPermission).invoke()
    }

    @Test
    fun `onShowRationale is called when Others#shouldShowRequestPermissionRationale returns true`
        () {
        every { PermissionRequestType.Others.checkPermissions(any(), any()) } returns false
        mockkStatic(PermissionUtils::class)
        every {
            PermissionUtils.shouldShowRequestPermissionRationale(any<Activity>(), any())
        } returns true

        activity.withPermissionsCheck(
            Manifest.permission.READ_CALENDAR,
            onPermissionDenied = onPermissionDenied,
            onNeverAskAgain = onNeverAskAgain,
            onShowRationale = onShowRationale,
            requiresPermission = requiresPermission
        )

        verify(onPermissionDenied, never()).invoke()
        verify(onNeverAskAgain, never()).invoke()
        verify(onShowRationale).invoke(any())
        verify(requiresPermission, never()).invoke()
    }

    @Test
    fun `no callback is called when Others#requestPermissions is called`() {
        every { PermissionRequestType.Others.checkPermissions(any(), any()) } returns false
        every {
            PermissionRequestType.Others.requestPermissions(any(), any(), any(), any(), any()
            )
        } returns Unit

        activity.withPermissionsCheck(
            Manifest.permission.READ_CALENDAR,
            onPermissionDenied = onPermissionDenied,
            onNeverAskAgain = onNeverAskAgain,
            onShowRationale = onShowRationale,
            requiresPermission = requiresPermission
        )

        verify(onPermissionDenied, never()).invoke()
        verify(onNeverAskAgain, never()).invoke()
        verify(onShowRationale, never()).invoke(any())
        verify(requiresPermission, never()).invoke()
        io.mockk.verify { PermissionRequestType.Others.requestPermissions(
            arrayOf(Manifest.permission.READ_CALENDAR), activity,
            requiresPermission, onNeverAskAgain, onPermissionDenied) }
    }

    @Test
    fun `requiresPermission is called when SystemAlertWindow#checkPermissions returns true`() {
        every { PermissionRequestType.SystemAlertWindow.checkPermissions(any(), any()) } returns true

        activity.withSystemAlertWindowPermissionCheck (
            onPermissionDenied = onPermissionDenied,
            onShowRationale = onShowRationale,
            requiresPermission = requiresPermission
        )

        verify(onPermissionDenied, never()).invoke()
        verify(onShowRationale, never()).invoke(any())
        verify(requiresPermission).invoke()
    }

    @Test
    fun `onShowRationale is called when SystemAlertWindow#shouldShowRequestPermissionRationale returns true`() {
        every { PermissionRequestType.SystemAlertWindow.checkPermissions(any(), any()) } returns false
        mockkStatic(PermissionUtils::class)
        every {
            PermissionUtils.shouldShowRequestPermissionRationale(any<Activity>(), any())
        } returns true

        activity.withSystemAlertWindowPermissionCheck(
            onPermissionDenied = onPermissionDenied,
            onShowRationale = onShowRationale,
            requiresPermission = requiresPermission
        )

        verify(onPermissionDenied, never()).invoke()
        verify(onShowRationale).invoke(any())
        verify(requiresPermission, never()).invoke()
    }

    @Test
    fun `no callback is called when SystemAlertWindow#requestPermissions is called`() {
        every { PermissionRequestType.SystemAlertWindow.checkPermissions(any(), any()) } returns false
        every {
            PermissionRequestType.SystemAlertWindow.requestPermissions(any(), any(), any(), any(), any()
            )
        } returns Unit

        activity.withSystemAlertWindowPermissionCheck(
            onPermissionDenied = onPermissionDenied,
            onShowRationale = onShowRationale,
            requiresPermission = requiresPermission
        )

        verify(onPermissionDenied, never()).invoke()
        verify(onShowRationale, never()).invoke(any())
        verify(requiresPermission, never()).invoke()
        io.mockk.verify { PermissionRequestType.SystemAlertWindow.requestPermissions(
            arrayOf(Manifest.permission.SYSTEM_ALERT_WINDOW), activity,
            requiresPermission, null, onPermissionDenied) }
    }

    @Test
    fun `requiresPermission is called when WriteSettings#checkPermissions returns true`() {
        every { PermissionRequestType.WriteSettings.checkPermissions(any(), any()) } returns true

        activity.withWriteSettingsPermissionCheck(
            onPermissionDenied = onPermissionDenied,
            onShowRationale = onShowRationale,
            requiresPermission = requiresPermission
        )

        verify(onPermissionDenied, never()).invoke()
        verify(onShowRationale, never()).invoke(any())
        verify(requiresPermission).invoke()
    }

    @Test
    fun `onShowRationale is called when WriteSettings#shouldShowRequestPermissionRationale returns true`() {
        every { PermissionRequestType.WriteSettings.checkPermissions(any(), any()) } returns false
        mockkStatic(PermissionUtils::class)
        every {
            PermissionUtils.shouldShowRequestPermissionRationale(any<Activity>(), any())
        } returns true

        activity.withWriteSettingsPermissionCheck(
            onPermissionDenied = onPermissionDenied,
            onShowRationale = onShowRationale,
            requiresPermission = requiresPermission
        )

        verify(onPermissionDenied, never()).invoke()
        verify(onShowRationale).invoke(any())
        verify(requiresPermission, never()).invoke()
    }

    @Test
    fun `no callback is called when WriteSettings#requestPermissions is called`() {
        every { PermissionRequestType.WriteSettings.checkPermissions(any(), any()) } returns false
        every {
            PermissionRequestType.WriteSettings.requestPermissions(any(), any(), any(), any(), any()
            )
        } returns Unit

        activity.withWriteSettingsPermissionCheck(
            onPermissionDenied = onPermissionDenied,
            onShowRationale = onShowRationale,
            requiresPermission = requiresPermission
        )

        verify(onPermissionDenied, never()).invoke()
        verify(onShowRationale, never()).invoke(any())
        verify(requiresPermission, never()).invoke()
        io.mockk.verify { PermissionRequestType.WriteSettings.requestPermissions(
            arrayOf(Manifest.permission.WRITE_SETTINGS), activity,
            requiresPermission, null, onPermissionDenied) }
    }
}
