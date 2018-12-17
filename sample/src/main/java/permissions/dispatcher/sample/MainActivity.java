package permissions.dispatcher.sample;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.RuntimePermissions;
import permissions.dispatcher.sample.camera.CameraPreviewFragment;
import permissions.dispatcher.sample.contacts.ContactsFragment;

import static permissions.dispatcher.sample.MainActivityPermissionsDispatcher.cancelShowCameraPermissionRequest;
import static permissions.dispatcher.sample.MainActivityPermissionsDispatcher.cancelShowContactsPermissionRequest;
import static permissions.dispatcher.sample.MainActivityPermissionsDispatcher.proceedShowCameraPermissionRequest;
import static permissions.dispatcher.sample.MainActivityPermissionsDispatcher.proceedShowContactsPermissionRequest;
import static permissions.dispatcher.sample.MainActivityPermissionsDispatcher.showCameraWithPermissionCheck;
import static permissions.dispatcher.sample.MainActivityPermissionsDispatcher.showContactsWithPermissionCheck;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCameraWithPermissionCheck(MainActivity.this);
            }
        });
        findViewById(R.id.button_contacts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showContactsWithPermissionCheck(MainActivity.this);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    void showCamera() {
        // NOTE: Perform action that requires the permission. If this is run by PermissionsDispatcher, the permission will have been granted
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.sample_content_fragment, CameraPreviewFragment.newInstance())
                .addToBackStack("camera")
                .commitAllowingStateLoss();
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void onCameraDenied() {
        // NOTE: Deal with a denied permission, e.g. by showing specific UI or disabling certain functionality
        Toast.makeText(this, R.string.permission_camera_denied, Toast.LENGTH_SHORT).show();
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    void showRationaleForCamera() {
        // NOTE: Show a rationale to explain why the permission is needed, e.g. with a dialog.
        showRationaleDialog(R.string.permission_camera_rationale);
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void onCameraNeverAskAgain() {
        Toast.makeText(this, R.string.permission_camera_never_ask_again, Toast.LENGTH_SHORT).show();
    }

    @NeedsPermission({Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS})
    void showContacts() {
        // NOTE: Perform action that requires the permission.
        // If this is run by PermissionsDispatcher, the permission will have been granted
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.sample_content_fragment, ContactsFragment.newInstance())
                .addToBackStack("contacts")
                .commitAllowingStateLoss();
    }

    @OnPermissionDenied({Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS})
    void onContactsDenied() {
        // NOTE: Deal with a denied permission, e.g. by showing specific UI or disabling certain functionality
        Toast.makeText(this, R.string.permission_contacts_denied, Toast.LENGTH_SHORT).show();
    }

    @OnShowRationale({Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS})
    void showRationaleForContact() {
        // NOTE: Show a rationale to explain why the permission is needed, e.g. with a dialog.
        showRationaleDialog(R.string.permission_contacts_rationale);
    }

    @OnNeverAskAgain({Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS})
    void onContactsNeverAskAgain() {
        Toast.makeText(this, R.string.permission_contacts_never_ask_again, Toast.LENGTH_SHORT).show();
    }

    private void showRationaleDialog(@StringRes final int messageResId) {
        new AlertDialog.Builder(this)
                .setPositiveButton(R.string.button_allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        switch (messageResId) {
                            case R.string.permission_camera_rationale:
                                proceedShowCameraPermissionRequest(MainActivity.this);
                            case R.string.permission_contacts_rationale:
                                proceedShowContactsPermissionRequest(MainActivity.this);
                        }
                    }
                })
                .setNegativeButton(R.string.button_deny, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        switch (messageResId) {
                            case R.string.permission_camera_rationale:
                                cancelShowCameraPermissionRequest(MainActivity.this);
                            case R.string.permission_contacts_rationale:
                                cancelShowContactsPermissionRequest(MainActivity.this);
                        }
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }
}
