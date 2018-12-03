package permissions.dispatcher.test;

import android.Manifest;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class ActivityWithSystemAlertWindowAllAnnotations extends AppCompatActivity {

    @NeedsPermission(Manifest.permission.SYSTEM_ALERT_WINDOW)
    void systemAlertWindow() {
    }

    @OnShowRationale(Manifest.permission.SYSTEM_ALERT_WINDOW)
    void showRationaleForSystemAlertWindow(final PermissionRequest request) {
    }

    @OnPermissionDenied(Manifest.permission.SYSTEM_ALERT_WINDOW)
    void showDeniedForSystemAlertWindow() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ActivityWithSystemAlertWindowAllAnnotationsPermissionsDispatcher.onActivityResult(this, requestCode);
    }
}
