package permissions.dispatcher.test;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class ActivityWithWriteSettingAllAnnotations extends AppCompatActivity {

    @NeedsPermission(Manifest.permission.WRITE_SETTINGS)
    void writeSetting() {
    }

    @OnShowRationale(Manifest.permission.WRITE_SETTINGS)
    void showRationaleForWriteSettings(final PermissionRequest request) {
    }

    @OnPermissionDenied(Manifest.permission.WRITE_SETTINGS)
    void showDeniedForWriteSettings() {
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_SETTINGS)
    void showNeverAskForWriteSettings() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ActivityWithWriteSettingAllAnnotationsPermissionsDispatcher.onActivityResult(this, requestCode);
    }
}
