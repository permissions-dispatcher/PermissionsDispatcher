package permissions.dispatcher.test;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class ActivityWithOnNeverAskAgain extends AppCompatActivity {
    @NeedsPermission(Manifest.permission.CAMERA)
    void showCamera() {
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void showNeverAskForCamera() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ActivityWithOnNeverAskAgainPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
