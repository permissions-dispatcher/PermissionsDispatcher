package permissions.dispatcher.test;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class SupportFragmentWithAllAnnotations extends Fragment {

    @NeedsPermission(Manifest.permission.CAMERA)
    void showCamera() {
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    void showRationaleForCamera(final PermissionRequest request) {
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void showDeniedForCamera() {
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void showNeverAskForCamera() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SupportFragmentWithAllAnnotationsPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

}
