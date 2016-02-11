package permissions.dispatcher.sample;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import permissions.dispatcher.*;

@RuntimePermissions
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private View mFab;
    private CoordinatorLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.second_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(this);
        mLayout = ((CoordinatorLayout) findViewById(R.id.coordinatorLayout));
//        setSupportActionBar(toolbar);
    }



    @NeedsPermission(Manifest.permission.CAMERA)
    void shoCamera(final int requestCode, final Uri createImageUri){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, createImageUri);
        takePictureIntent.putExtra("return-data", true);
        startActivityForResult(takePictureIntent,requestCode);

        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            this.startActivityForResult(takePictureIntent, requestCode);
        }
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    void showRationaleForCamera(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.permission_camera_rationale)
                .setCancelable(false)
                .setPositiveButton(R.string.button_allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        request.proceed();
                    }
                })
                .show();
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void showDeniedForCamera() {
        Snackbar.make(mLayout, "Sorry you didn't granted us the premissions", Snackbar.LENGTH_LONG).setAction("Go to the App Permissions",this).show();
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void showNeverAskForCamera() {
        Snackbar.make(mLayout, "We don't have premssions to the camera",Snackbar.LENGTH_LONG).setAction("Go to the App Permissions",this).show();
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()){
            case R.id.fab:  Uri uri = ImageHelper.getUriToTakeAPhoto();
                MainActivityPermissionsDispatcher.shoCameraWithCheck(MainActivity.this,12345,uri);
                break;
            default: showAppPermissionSettings();
        }

    }

    void showAppPermissionSettings(){
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode,
                                           @NonNull final String[] permissions,
                                           @NonNull final int[] grantResults) {
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
