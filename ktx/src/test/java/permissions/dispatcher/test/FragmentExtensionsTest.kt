package permissions.dispatcher.test

import android.Manifest
import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
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
import org.junit.runner.RunWith
import org.mockito.Mockito.never
import org.robolectric.Robolectric
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.PermissionUtils
import permissions.dispatcher.ktx.PermissionRequestType
import permissions.dispatcher.ktx.withPermissionsCheck
import permissions.dispatcher.ktx.withSystemAlertWindowPermissionCheck
import permissions.dispatcher.ktx.withWriteSettingsPermissionCheck

@RunWith(AndroidJUnit4::class)
class FragmentExtensionsTest {
    // workaround for mockito
    interface Func {
        fun invoke()
    }

    // workaround for mockito
    interface OnShowRationaleFunc {
        fun invoke(request: PermissionRequest)
    }

    private val fragment = Fragment()
    private lateinit var onShowRationale: OnShowRationaleFunc
    private lateinit var onPermissionDenied: Func
    private lateinit var onNeverAskAgain: Func
    private lateinit var requiresPermission: Func

    @Before
    fun setUp() {
        mockkObject(PermissionRequestType.Others)
        mockkObject(PermissionRequestType.SystemAlertWindow)
        mockkObject(PermissionRequestType.WriteSettings)

        onShowRationale = mock()
        onPermissionDenied =  mock()
        onNeverAskAgain =  mock()
        requiresPermission =  mock()

        Robolectric.setupActivity(FragmentActivity::class.java)
            .supportFragmentManager.beginTransaction()
            .add(fragment, Fragment::class.simpleName)
            .commitAllowingStateLoss()
    }

    @After
    fun tearDown() = unmockkAll()

    @Test
    fun `requiresPermission is called when Others#checkPermissions returns true`() {
        every { PermissionRequestType.Others.checkPermissions(any(), any()) } returns true

        fragment.withPermissionsCheck(
            Manifest.permission.READ_CALENDAR,
            onPermissionDenied = onPermissionDenied::invoke,
            onNeverAskAgain = onNeverAskAgain::invoke,
            onShowRationale = onShowRationale::invoke,
            requiresPermission = requiresPermission::invoke
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

        fragment.withPermissionsCheck(
            Manifest.permission.READ_CALENDAR,
            onPermissionDenied = onPermissionDenied::invoke,
            onNeverAskAgain = onNeverAskAgain::invoke,
            onShowRationale = onShowRationale::invoke,
            requiresPermission = requiresPermission::invoke
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

        fragment.withPermissionsCheck(
            Manifest.permission.READ_CALENDAR,
            onPermissionDenied = onPermissionDenied::invoke,
            onNeverAskAgain = onNeverAskAgain::invoke,
            onShowRationale = onShowRationale::invoke,
            requiresPermission = requiresPermission::invoke
        )

        verify(onPermissionDenied, never()).invoke()
        verify(onNeverAskAgain, never()).invoke()
        verify(onShowRationale, never()).invoke(any())
        verify(requiresPermission, never()).invoke()
        io.mockk.verify { PermissionRequestType.Others.requestPermissions(
            arrayOf(Manifest.permission.READ_CALENDAR), fragment.requireActivity(),
            requiresPermission::invoke, onNeverAskAgain::invoke, onPermissionDenied::invoke) }
    }

    @Test
    fun `requiresPermission is called when SystemAlertWindow#checkPermissions returns true`() {
        every { PermissionRequestType.SystemAlertWindow.checkPermissions(any(), any()) } returns true

        fragment.withSystemAlertWindowPermissionCheck (
            onPermissionDenied = onPermissionDenied::invoke,
            onShowRationale = onShowRationale::invoke,
            requiresPermission = requiresPermission::invoke
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

        fragment.withSystemAlertWindowPermissionCheck (
            onPermissionDenied = onPermissionDenied::invoke,
            onShowRationale = onShowRationale::invoke,
            requiresPermission = requiresPermission::invoke
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

        fragment.withSystemAlertWindowPermissionCheck (
            onPermissionDenied = onPermissionDenied::invoke,
            onShowRationale = onShowRationale::invoke,
            requiresPermission = requiresPermission::invoke
        )

        verify(onPermissionDenied, never()).invoke()
        verify(onShowRationale, never()).invoke(any())
        verify(requiresPermission, never()).invoke()
        io.mockk.verify { PermissionRequestType.SystemAlertWindow.requestPermissions(
            arrayOf(Manifest.permission.SYSTEM_ALERT_WINDOW), fragment.requireActivity(),
            requiresPermission::invoke, null, onPermissionDenied::invoke) }
    }

    @Test
    fun `requiresPermission is called when WriteSettings#checkPermissions returns true`() {
        every { PermissionRequestType.WriteSettings.checkPermissions(any(), any()) } returns true

        fragment.withWriteSettingsPermissionCheck(
            onPermissionDenied = onPermissionDenied::invoke,
            onShowRationale = onShowRationale::invoke,
            requiresPermission = requiresPermission::invoke
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

        fragment.withWriteSettingsPermissionCheck(
            onPermissionDenied = onPermissionDenied::invoke,
            onShowRationale = onShowRationale::invoke,
            requiresPermission = requiresPermission::invoke
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

        fragment.withWriteSettingsPermissionCheck(
            onPermissionDenied = onPermissionDenied::invoke,
            onShowRationale = onShowRationale::invoke,
            requiresPermission = requiresPermission::invoke
        )

        verify(onPermissionDenied, never()).invoke()
        verify(onShowRationale, never()).invoke(any())
        verify(requiresPermission, never()).invoke()
        io.mockk.verify { PermissionRequestType.WriteSettings.requestPermissions(
            arrayOf(Manifest.permission.WRITE_SETTINGS), fragment.requireActivity(),
            requiresPermission::invoke, null, onPermissionDenied::invoke) }
    }
}
