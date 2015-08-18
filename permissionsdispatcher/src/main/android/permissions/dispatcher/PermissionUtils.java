package permissions.dispatcher;

import android.content.Context;
import android.content.pm.PackageManager;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

/**
 * Utility class which provides basic helper methods about runtime permissions.
 */
public class PermissionUtils {

    private PermissionUtils() {
    }

    /**
     * Checks all given permissions have been granted.
     *
     * @param grantResults results
     * @return return true if all permissions have been granted.
     */
    public static boolean verifyPermissions(int... grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if the Activity or Fragment has access to all given permissions.
     *
     * @param context context
     * @param permissions permission list
     * @return returns if the Activity or Fragment has access to all given permissions.
     */
    public static boolean hasSelfPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

}
