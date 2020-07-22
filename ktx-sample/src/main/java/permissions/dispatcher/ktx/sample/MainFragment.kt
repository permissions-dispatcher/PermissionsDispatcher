package permissions.dispatcher.ktx.sample

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.ktx.PermissionsRequester
import permissions.dispatcher.ktx.sample.camera.CameraPreviewFragment
import permissions.dispatcher.ktx.withPermissionsCheck

class MainFragment : Fragment() {
    private lateinit var permissionsRequester: PermissionsRequester

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        permissionsRequester = withPermissionsCheck(Manifest.permission.CAMERA,
            onShowRationale = ::onCameraShowRationale,
            onPermissionDenied = ::onCameraDenied,
            onNeverAskAgain = ::onCameraNeverAskAgain) {
            fragmentManager?.beginTransaction()
                ?.replace(R.id.sample_content_fragment, CameraPreviewFragment.newInstance())
                ?.addToBackStack("camera")
                ?.commitAllowingStateLoss()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buttonCamera: Button = view.findViewById(R.id.button_camera)
        buttonCamera.setOnClickListener {
            permissionsRequester.request()
        }
    }

    private fun onCameraDenied() {
        Toast.makeText(requireContext(), R.string.permission_camera_denied, Toast.LENGTH_SHORT).show()
    }

    private fun onCameraShowRationale(request: PermissionRequest) {
        request.proceed()
        Toast.makeText(requireContext(), R.string.permission_camera_rationale, Toast.LENGTH_SHORT).show()
    }

    private fun onCameraNeverAskAgain() {
        Toast.makeText(requireContext(), R.string.permission_camera_never_ask_again, Toast.LENGTH_SHORT).show()
    }
}
