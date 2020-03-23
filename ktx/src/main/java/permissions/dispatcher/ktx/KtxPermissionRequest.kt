package permissions.dispatcher.ktx

import permissions.dispatcher.PermissionRequest
import java.lang.ref.WeakReference

internal class KtxPermissionRequest(
    private val requestPermission: WeakReference<Func>,
    private val permissionDenied: WeakReference<Func>?
) : PermissionRequest {
    override fun proceed() {
        requestPermission.get()?.invoke()
    }

    override fun cancel() {
        permissionDenied?.get()?.invoke()
    }

    companion object {
        fun create(onPermissionDenied: Func?, requestPermission: Func) = KtxPermissionRequest(
            requestPermission = WeakReference(requestPermission),
            permissionDenied = onPermissionDenied?.let { WeakReference(it) }
        )
    }
}
