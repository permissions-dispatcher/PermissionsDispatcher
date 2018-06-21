package permissions.dispatcher.test;

import android.Manifest;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class ActivityWithSystemAlertWindow extends AppCompatActivity {

    @NeedsPermission(Manifest.permission.SYSTEM_ALERT_WINDOW)
    void systemAlertWindow() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ActivityWithSystemAlertWindowPermissionsDispatcher.onActivityResult(this, requestCode);
    }
}
