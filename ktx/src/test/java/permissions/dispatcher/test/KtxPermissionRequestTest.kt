package permissions.dispatcher.test

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.never
import permissions.dispatcher.ktx.*

class KtxPermissionRequestTest {
    private lateinit var onPermissionDenied: Func
    private lateinit var requiresPermission: Func

    @Before
    fun setUp() {
        onPermissionDenied = mock()
        requiresPermission = mock()
    }

    @Test
    fun `PermissionRequest#proceed invokes requiresPermission`() {
        val request = KtxPermissionRequest.create(onPermissionDenied, requiresPermission)
        request.proceed()

        verify(onPermissionDenied, never()).invoke()
        verify(requiresPermission).invoke()
    }

    @Test
    fun `PermissionRequest#cancel invokes onPermissionDenied`() {
        val request = KtxPermissionRequest.create(onPermissionDenied, requiresPermission)
        request.cancel()

        verify(onPermissionDenied).invoke()
        verify(requiresPermission, never()).invoke()
    }
}
