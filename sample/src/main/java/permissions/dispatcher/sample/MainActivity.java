package permissions.dispatcher.sample;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import permissions.dispatcher.*;
import permissions.dispatcher.sample.camera.CameraPreviewFragment;
import permissions.dispatcher.sample.contacts.ContactsFragment;

@RuntimePermissions
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button_camera).setOnClickListener(this);
        findViewById(R.id.button_contacts).setOnClickListener(this);
    }

    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.button_camera:
                // NOTE: delegate the permission handling to generated method
                MainActivityPermissionsDispatcher.showCameraWithCheck(this);
                break;
            case R.id.button_contacts:
                // NOTE: delegate the permission handling to generated method
                MainActivityPermissionsDispatcher.showContactsWithCheck(this);
                break;
        }
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

    @NeedsPermission({Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS})
    void showContacts() {
        // NOTE: Perform action that requires the permission.
        // If this is run by PermissionsDispatcher, the permission will have been granted
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.sample_content_fragment, ContactsFragment.newInstance())
                .addToBackStack("contacts")
                .commitAllowingStateLoss();
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    void showRationaleForCamera(PermissionRequest request) {
        // NOTE: Show a rationale to explain why the permission is needed, e.g. with a dialog.
        // Call proceed() or cancel() on the provided PermissionRequest to continue or abort
        showRationaleDialog(R.string.permission_camera_rationale, request);
    }

    @OnShowRationale({Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS})
    void showRationaleForContact(PermissionRequest request) {
        // NOTE: Show a rationale to explain why the permission is needed, e.g. with a dialog.
        // Call proceed() or cancel() on the provided PermissionRequest to continue or abort
        showRationaleDialog(R.string.permission_contacts_rationale, request);
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void onCameraDenied() {
        // NOTE: Deal with a denied permission, e.g. by showing specific UI
        // or disabling certain functionality
        Toast.makeText(this, R.string.permission_camera_denied, Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void onCameraNeverAskAgain() {
        Toast.makeText(this, R.string.permission_camera_never_askagain, Toast.LENGTH_SHORT).show();
    }

    public void onBackClick(View view) {
        getSupportFragmentManager().popBackStack();
    }

    private void showRationaleDialog(@StringRes int messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton(R.string.button_allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton(R.string.button_deny, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }
}
