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
}
