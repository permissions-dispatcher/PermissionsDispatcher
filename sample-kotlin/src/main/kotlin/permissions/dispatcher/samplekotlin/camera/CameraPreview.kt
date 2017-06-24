package permissions.dispatcher.samplekotlin.camera

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Camera
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView

import java.io.IOException
import kotlin.properties.Delegates


/**
 * Camera preview that displays a [Camera].

 * Handles basic lifecycle methods to display and stop the preview.
 *
 *
 * Implementation is based directly on the documentation at
 * http://developer.android.com/guide/topics/media/camera.html
 */
@SuppressLint("ViewConstructor")
class CameraPreview(context: Context,
                    camera: Camera,
                    cameraInfo: Camera.CameraInfo,
                    displayOrientation: Int) : SurfaceView(context), SurfaceHolder.Callback {
    private val mHolder: SurfaceHolder
    private var mCamera: Camera by Delegates.notNull()
    private var mCameraInfo: Camera.CameraInfo by Delegates.notNull()
    private var mDisplayOrientation: Int = 0

    init {
        setCamera(camera, cameraInfo, displayOrientation)

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = holder
        mHolder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder)
            mCamera.startPreview()
            Log.d(TAG, "Camera preview started.")
        } catch (e: IOException) {
            Log.d(TAG, "Error setting camera preview: " + e.message)
        }

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, w: Int, h: Int) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        mHolder.takeIf { it.surface != null }?.let{
            // stop preview before making changes
            try {
                mCamera.stopPreview()
                Log.d(TAG, "Preview stopped.")
            } catch (e: Exception) {
                // ignore: tried to stop a non-existent preview
                Log.d(TAG, "Error starting camera preview: " + e.message)
            }

            val orientation = calculatePreviewOrientation(mCameraInfo, mDisplayOrientation)
            mCamera.setDisplayOrientation(orientation)

            try {
                mCamera.setPreviewDisplay(it)
                mCamera.startPreview()
                Log.d(TAG, "Camera preview started.")
            } catch (e: Exception) {
                Log.d(TAG, "Error starting camera preview: " + e.message)
            }
        } ?: let {
            // preview surface does not exist
            Log.d(TAG, "Preview surface does not exist")
        }
    }

    fun setCamera(camera: Camera,
                  cameraInfo: Camera.CameraInfo,
                  displayOrientation: Int) {
        mCamera = camera
        mCameraInfo = cameraInfo
        mDisplayOrientation = displayOrientation
    }

    companion object {
        private val TAG = "CameraPreview"

        /**
         * Calculate the correct orientation for a [Camera] preview that is displayed on screen.

         * Implementation is based on the sample code provided in
         * [Camera.setDisplayOrientation].
         */
        fun calculatePreviewOrientation(info: Camera.CameraInfo, rotation: Int) = when {
                info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT ->
                    // compensate the mirror
                    (360 - (info.orientation + degrees(rotation)) % 360) % 360
                // back-facing
                else -> (info.orientation - degrees(rotation) + 360) % 360
            }

        private fun degrees(rotation: Int) = when (rotation) {
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> 0
        }
    }
}
