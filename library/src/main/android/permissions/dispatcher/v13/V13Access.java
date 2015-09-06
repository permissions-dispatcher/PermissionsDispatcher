package permissions.dispatcher.v13;

import android.app.Fragment;
import android.support.v13.app.FragmentCompat;

public final class V13Access {

    V13Access() {
    }

    /**
     * Checks given permissions are needed to show rationale.
     *
     * @param fragment    fragment
     * @param permissions permission list
     * @return returns true if one of the permission is needed to show rationale.
     */
    public boolean shouldShowRequestPermissionRationale(Fragment fragment, String... permissions) {
        for (String permission : permissions) {
            if (FragmentCompat.shouldShowRequestPermissionRationale(fragment, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Requests the provided permissions for a Fragment instance.
     *
     * @param fragment    fragment
     * @param permissions permissions list
     * @param requestCode Request code connected to the permission request
     */
    public void requestPermissions(Fragment fragment, String[] permissions, int requestCode) {
        FragmentCompat.requestPermissions(fragment, permissions, requestCode);
    }
}
