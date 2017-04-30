package permissions.dispatcher;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * This is just a dummy to resolve dependency in test build.
 */
public final class PermissionUtils {

    private PermissionUtils() {
    }

    public static boolean verifyPermissions(int... grantResults) {
        return true;
    }

    public static boolean hasSelfPermissions(Context context, String... permissions) {
        return true;
    }

    public static boolean shouldShowRequestPermissionRationale(Activity activity, String... permissions) {
        return true;
    }

    public static boolean shouldShowRequestPermissionRationale(Fragment fragment, String... permissions) {
        return true;
    }

    public static boolean shouldShowRequestPermissionRationale(android.app.Fragment fragment, String... permissions) {
        return true;
    }

    public static void requestPermissions(android.app.Fragment fragment, String[] permissions, int requestCode) {
    }

    public static int getTargetSdkVersion(Context context) {
        return 0;
    }
}
