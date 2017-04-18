package tests;

import android.Manifest;
import android.app.Fragment;

import permissions.dispatcher.RuntimePermissions;
import permissions.dispatcher.NeedsPermission;

@RuntimePermissions
public class MyFragment extends Fragment {
    @NeedsPermission(Manifest.permission.CAMERA)
    void showCamera() {
    }
}