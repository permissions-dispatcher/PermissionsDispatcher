/*
* Copyright 2015 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package permissions.dispatcher.sample.camera;

import android.annotation.SuppressLint;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import permissions.dispatcher.sample.R;

/**
 * Displays a {@link CameraPreview} of the first {@link Camera}.
 * An error message is displayed if the Camera is not available.
 * <p>
 * This Fragment is only used to illustrate that access to the Camera API has been granted (or
 * denied) as part of the runtime permissions model. It is not relevant for the use of the
 * permissions API.
 * <p>
 * Implementation is based directly on the documentation at
 * http://developer.android.com/guide/topics/media/camera.html
 */
public class CameraPreviewFragment extends Fragment {

    private static final String TAG = "CameraPreview";

    /**
     * Id of the camera to access. 0 is the first camera.
     */
    private static final int CAMERA_ID = 0;

    private CameraPreview preview;
    private Camera camera;

    public static CameraPreviewFragment newInstance() {
        return new CameraPreviewFragment();
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button backButton = view.findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackClick();
            }
        });
        initCamera();
    }

    private void initCamera() {
        camera = getCameraInstance(CAMERA_ID);
        Camera.CameraInfo cameraInfo = null;

        if (camera != null) {
            // Get camera info only if the camera is available
            cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(CAMERA_ID, cameraInfo);
        }

        // Get the rotation of the screen to adjust the preview image accordingly.
        final int displayRotation = getActivity().getWindowManager().getDefaultDisplay()
                .getRotation();

        if (getView() == null) {
            return;
        }

        FrameLayout preview = getView().findViewById(R.id.camera_preview);
        preview.removeAllViews();

        if (this.preview == null) {
            // Create the Preview view and set it as the content of this Activity.
            this.preview = new CameraPreview(getActivity(), camera, cameraInfo, displayRotation);
        } else {
            this.preview.setCamera(camera, cameraInfo, displayRotation);
        }

        preview.addView(this.preview);
    }

    private void onBackClick() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (camera == null) {
            initCamera();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop camera access
        releaseCamera();
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(int cameraId) {
        Camera c = null;
        try {
            c = Camera.open(cameraId); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            Log.d(TAG, "Camera " + cameraId + " is not available: " + e.getMessage());
        }
        return c; // returns null if camera is unavailable
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.release();        // release the camera for other applications
            camera = null;
        }
    }
}
