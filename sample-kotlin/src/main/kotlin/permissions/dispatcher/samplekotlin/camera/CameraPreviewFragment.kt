package permissions.dispatcher.samplekotlin.camera

import android.annotation.SuppressLint
import android.hardware.Camera
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout

import permissions.dispatcher.samplekotlin.R

/**
 * Displays a [CameraPreview] of the first [Camera].
 * An error message is displayed if the Camera is not available.
 *
 *
 * This Fragment is only used to illustrate that access to the Camera API has been granted (or
 * denied) as part of the runtime permissions model. It is not relevant for the use of the
 * permissions API.
 *
 *
 * Implementation is based directly on the documentation at
 * http://developer.android.com/guide/topics/media/camera.html
 */
class CameraPreviewFragment : Fragment() {

    private var preview: CameraPreview? = null
    private var camera: Camera? = null

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_camera, null)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button: Button? = view.findViewById(R.id.back)
        button?.setOnClickListener {
            fragmentManager.popBackStack()
        }
        initCamera()
    }

    private fun initCamera() {
        camera = getCameraInstance(CAMERA_ID)?.also { camera ->
            val cameraInfo = Camera.CameraInfo().also { Camera.getCameraInfo(CAMERA_ID, it) }
            // Get the rotation of the screen to adjust the preview image accordingly.
            val displayRotation = activity.windowManager.defaultDisplay.rotation
            val previewFrameLayout: FrameLayout? = view?.findViewById(R.id.camera_preview)
            previewFrameLayout?.removeAllViews()
            preview?.setCamera(camera, cameraInfo, displayRotation) ?: run {
                // Create the Preview view and set it as the content of this Activity.
                preview = CameraPreview(activity, camera, cameraInfo, displayRotation)
            }
            previewFrameLayout?.addView(preview)
        }
    }

    override fun onResume() {
        super.onResume()
        camera ?: initCamera()
    }

    override fun onPause() {
        super.onPause()
        // Stop camera access
        releaseCamera()
    }

    private fun releaseCamera() {
        camera?.release()?.run { camera = null }
        // release destroyed preview
        preview = null
    }

    companion object {
        private val TAG = "CameraPreview"

        /**
         * Id of the camera to access. 0 is the first camera.
         */
        private val CAMERA_ID = 0

        fun newInstance(): CameraPreviewFragment = CameraPreviewFragment()

        /** A safe way to get an instance of the Camera object.  */
        fun getCameraInstance(cameraId: Int): Camera? = try {
                // attempt to get a Camera instance
                Camera.open(cameraId)
            } catch (e: Exception) {
                // Camera is not available (in use or does not exist)
                Log.d(TAG, "Camera " + cameraId + " is not available: " + e.message)
                null
            }
    }
}
