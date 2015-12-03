package permissions.dispatcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import static android.os.Build.VERSION_CODES.FROYO;
import static android.os.Build.VERSION_CODES.GINGERBREAD;
import static android.support.v4.content.PermissionChecker.checkSelfPermission;

public final class PermissionUtils {

    private PermissionUtils() {
    }

    /**
     * Checks all given permissions have been granted.
     *
     * @param grantResults results
     * @return returns true if all permissions have been granted.
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
     * @param context     context
     * @param permissions permission list
     * @return returns true if the Activity or Fragment has access to all given permissions.
     */
    public static boolean hasSelfPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks given permissions are needed to show rationale.
     *
     * @param activity    activity
     * @param permissions permission list
     * @return returns true if one of the permission is needed to show rationale.
     */
    public static boolean shouldShowRequestPermissionRationale(Activity activity, String... permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates an {@link Intent} that redirects the user to the app's settings page.
     *
     * @param context Context used to access the app's package name
     * @return An intent that, when used to {@link Activity#startActivity(Intent)}, will open the running app's Settings page.
     * If no matching Activity could be found for the device's settings, this method returns null.
     */
    public static @Nullable Intent createAppSettingsIntent(Context context) {
        Intent intent;
        String packageName = context.getPackageName();
        if (Build.VERSION.SDK_INT >= GINGERBREAD) {
            // Utilize the dedicated Settings Action on API 9+
            intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", packageName, null);
            intent.setData(uri);

        } else {
            // Compatibility implementation using a View intent and the native Settings app
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            String extraKey = Build.VERSION.SDK_INT == FROYO ? "pkg" : "com.android.settings.ApplicationPkgName";
            intent.putExtra(extraKey, packageName);
        }

        // If the intent can be used to resolve an Activity, return that intent. Otherwise, return null
        return context.getPackageManager().resolveActivity(intent, 0) != null ? intent : null;
    }
}
