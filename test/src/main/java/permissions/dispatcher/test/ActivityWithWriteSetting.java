package permissions.dispatcher.test;

import android.Manifest;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class ActivityWithWriteSetting extends AppCompatActivity {

    @NeedsPermission(Manifest.permission.WRITE_SETTINGS)
    void writeSetting() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ActivityWithWriteSettingPermissionsDispatcher.onActivityResult(this, requestCode);
    }
}
