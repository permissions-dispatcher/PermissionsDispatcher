package permissions.dispatcher.test;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class ActivityWithOnPermissionDenied extends AppCompatActivity {
    @NeedsPermission(Manifest.permission.CAMERA)
    void showCamera() {
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void showDeniedForCamera() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ActivityWithOnPermissionDeniedPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
