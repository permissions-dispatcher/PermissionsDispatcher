package permissions.dispatcher.sample.test;

import android.Manifest;
import android.app.Fragment;
import permissions.dispatcher.Needs;
import permissions.dispatcher.OnRationale;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by marcel on 03.09.15.
 */
@RuntimePermissions
public class NativeFragment extends Fragment {

    @Needs(Manifest.permission.CAMERA)
    void showCamera() {

    }

    @OnRationale(Manifest.permission.CAMERA)
    void cameraRationale() {

    }
}
