package permissions.dispatcher.ktx

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import permissions.dispatcher.PermissionUtils
import permissions.dispatcher.PermissionUtils.verifyPermissions
import java.util.*

internal sealed class PermissionRequestFragment : Fragment() {
    protected val requestCode = Random().nextInt(1000)
    protected lateinit var viewModel: PermissionRequestViewModel

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        retainInstance = true
        viewModel = ViewModelProvider(requireActivity()).get(PermissionRequestViewModel::class.java)
    }

    protected fun dismiss() =
        fragmentManager?.beginTransaction()?.remove(this)?.commitAllowingStateLoss()

    internal class NormalRequestPermissionFragment : PermissionRequestFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            val permissions = arguments?.getStringArray(BUNDLE_PERMISSIONS_KEY) ?: return
            requestPermissions(permissions, requestCode)
        }

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (requestCode == this.requestCode) {
                // https://github.com/permissions-dispatcher/PermissionsDispatcher/issues/729
                val key = permissions.sortedArray().contentToString()
                if (verifyPermissions(*grantResults)) {
                    viewModel.postPermissionRequestResult(key, PermissionResult.GRANTED)
                } else {
                    if (!PermissionUtils.shouldShowRequestPermissionRationale(this, *permissions)) {
                        viewModel.postPermissionRequestResult(
                            key,
                            PermissionResult.DENIED_AND_DISABLED
                        )
                    } else {
                        viewModel.postPermissionRequestResult(key, PermissionResult.DENIED)
                    }
                }
            }
            dismiss()
        }

        companion object {
            const val BUNDLE_PERMISSIONS_KEY = "key:permissions"

            fun newInstance(permissions: Array<out String>) =
                NormalRequestPermissionFragment().apply {
                    arguments = Bundle().apply {
                        putStringArray(BUNDLE_PERMISSIONS_KEY, permissions)
                    }
                }
        }
    }

    internal class SpecialRequestPermissionFragment : PermissionRequestFragment() {
        private lateinit var action: String

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            action = arguments?.getString(BUNDLE_ACTION_KEY) ?: return
            val packageName = context?.packageName ?: return
            val uri = Uri.parse("package:$packageName")
            startActivityForResult(Intent(action, uri), requestCode)
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            if (requestCode == this.requestCode) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    && Settings.canDrawOverlays(activity)
                ) {
                    viewModel.postPermissionRequestResult(action, PermissionResult.GRANTED)
                } else {
                    viewModel.postPermissionRequestResult(action, PermissionResult.DENIED)
                }
            }
            dismiss()
        }

        companion object {
            const val BUNDLE_ACTION_KEY = "key:action"

            fun newInstance(action: String) = SpecialRequestPermissionFragment().apply {
                arguments = Bundle().apply {
                    putString(BUNDLE_ACTION_KEY, action)
                }
            }
        }
    }
}
