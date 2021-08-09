package permissions.dispatcher.test

import android.Manifest
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import permissions.dispatcher.ktx.Fun
import permissions.dispatcher.ktx.PermissionRequestViewModel
import permissions.dispatcher.ktx.PermissionResult
import java.lang.ref.WeakReference

class PermissionRequestViewModelTest {
    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val permission = arrayOf(Manifest.permission.CAMERA).contentToString()
    private lateinit var viewModel: PermissionRequestViewModel
    private lateinit var lifecycleOwner: LifecycleOwner
    private lateinit var requiresPermission: Fun
    private lateinit var onPermissionDenied: Fun
    private lateinit var onNeverAskAgain: Fun

    @Before
    fun setup() {
        viewModel = PermissionRequestViewModel()

        lifecycleOwner = mock()
        val lifecycle = LifecycleRegistry(mock())
        lifecycle.markState(Lifecycle.State.RESUMED)
        whenever(lifecycleOwner.lifecycle).thenReturn(lifecycle)

        onPermissionDenied = mock()
        requiresPermission = mock()
        onNeverAskAgain = mock()
    }

    @After
    fun cleanup() {
        viewModel.removeObservers(lifecycleOwner)
    }

    @Test
    fun `GRANTED emits requiresPermission`() {
        viewModel.observe(
            lifecycleOwner,
            permission,
            WeakReference(requiresPermission),
            WeakReference(onPermissionDenied),
            WeakReference(onNeverAskAgain)
        )
        viewModel.postPermissionRequestResult(permission, PermissionResult.GRANTED)

        verify(requiresPermission).invoke()
        verify(onPermissionDenied, never()).invoke()
        verify(onNeverAskAgain, never()).invoke()
    }

    @Test
    fun `DENIED emits onPermissionDenied`() {
        viewModel.observe(
            lifecycleOwner,
            permission,
            WeakReference(requiresPermission),
            WeakReference(onPermissionDenied),
            WeakReference(onNeverAskAgain)
        )
        viewModel.postPermissionRequestResult(permission, PermissionResult.DENIED)

        verify(requiresPermission, never()).invoke()
        verify(onPermissionDenied).invoke()
        verify(onNeverAskAgain, never()).invoke()
    }

    @Test
    fun `DENIED_AND_DISABLED emits onNeverAskAgain`() {
        viewModel.observe(
            lifecycleOwner,
            permission,
            WeakReference(requiresPermission),
            WeakReference(onPermissionDenied),
            WeakReference(onNeverAskAgain)
        )
        viewModel.postPermissionRequestResult(permission, PermissionResult.DENIED_AND_DISABLED)

        verify(requiresPermission, never()).invoke()
        verify(onPermissionDenied, never()).invoke()
        verify(onNeverAskAgain).invoke()
    }
}
