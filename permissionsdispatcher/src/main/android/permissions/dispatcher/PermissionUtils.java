package permissions.dispatcher;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

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
        if (isNotMNC()) {
            return true;
        }
        for (String permission : permissions) {
            if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private static boolean isNotMNC() {
        return !isMNC();
    }

    private static boolean isMNC() {
         return Build.VERSION.SDK_INT == Build.VERSION_CODES.M;
    }

}
