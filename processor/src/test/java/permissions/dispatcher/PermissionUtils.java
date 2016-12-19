package permissions.dispatcher;

import android.app.Activity;
import android.content.Context;

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

    public static int getTargetSdkVersion(Context context) {
        return 0;
    }
}
