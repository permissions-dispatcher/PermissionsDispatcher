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

sealed class PermissionRequestFragment : Fragment() {
    protected val requestCode = Random().nextInt(1000)
    protected val viewModel: PermissionRequestViewModel by lazy {
        ViewModelProvider(this).get(PermissionRequestViewModel::class.java)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        retainInstance = true
    }

    protected fun dismiss() =
        fragmentManager?.beginTransaction()?.remove(this)?.commitNowAllowingStateLoss()

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
                if (verifyPermissions(*grantResults)) {
                    viewModel.postPermissionRequestResult(PermissionResult.GRANTED)
                } else {
                    if (!PermissionUtils.shouldShowRequestPermissionRationale(this, *permissions)) {
                        viewModel.postPermissionRequestResult(PermissionResult.DENIED_AND_DISABLED)
                    } else {
                        viewModel.postPermissionRequestResult(PermissionResult.DENIED)
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
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            val action = arguments?.getString(BUNDLE_ACTION_KEY) ?: return
            val packageName = context?.packageName ?: return
            startActivityForResult(Intent(action, Uri.parse("package:$packageName")), requestCode)
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            if (requestCode == this.requestCode) {
                if (Build.VERSION.SDK_INT >= 23 && Settings.canDrawOverlays(activity)) {
                    viewModel.postPermissionRequestResult(PermissionResult.GRANTED)
                } else {
                    viewModel.postPermissionRequestResult(PermissionResult.DENIED)
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
