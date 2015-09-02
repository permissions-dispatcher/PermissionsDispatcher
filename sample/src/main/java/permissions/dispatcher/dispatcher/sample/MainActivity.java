package permissions.dispatcher.dispatcher.sample;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import permissions.dispatcher.dispatcher.sample.camera.CameraPreviewFragment;
import permissions.dispatcher.dispatcher.sample.contacts.ContactsFragment;

@RuntimePermissions
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button cameraButton = (Button) findViewById(R.id.button_camera);
        cameraButton.setOnClickListener(this);
        Button contactsButton = (Button) findViewById(R.id.button_contacts);
        contactsButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // NOTE: delegate the permission handling to generated method
        MainActivityPermissionsDispatcher.
                onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Needs(Manifest.permission.CAMERA)
    void showCamera() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.sample_content_fragment, CameraPreviewFragment.newInstance())
                .addToBackStack("camera")
                .commitAllowingStateLoss();
    }

    @Needs({Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS})
    void showContacts() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.sample_content_fragment, ContactsFragment.newInstance())
                .addToBackStack("contacts")
                .commitAllowingStateLoss();
    }

    @OnRationale(Manifest.permission.CAMERA)
    void showRationaleForCamera() {
        Toast.makeText(this, R.string.permission_camera_rationale, Toast.LENGTH_SHORT).show();
    }

    @OnRationale({Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS})
    void showRationaleForContact() {
        Toast.makeText(this, R.string.permission_contacts_rationale, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onBackClick(View view) {
        getSupportFragmentManager().popBackStack();
    }
}
