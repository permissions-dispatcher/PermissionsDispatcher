package permissions.dispatcher;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;
import android.support.v13.app.FragmentCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.AppOpsManagerCompat;
import android.support.v4.util.SimpleArrayMap;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

public final class PermissionUtils {
    // Map of dangerous permissions introduced in later framework versions.
    // Used to conditionally bypass permission-hold checks on older devices.
    private static final SimpleArrayMap<String, Integer> MIN_SDK_PERMISSIONS;

    static {
        MIN_SDK_PERMISSIONS = new SimpleArrayMap<String, Integer>(8);
        MIN_SDK_PERMISSIONS.put("com.android.voicemail.permission.ADD_VOICEMAIL", 14);
        MIN_SDK_PERMISSIONS.put("android.permission.BODY_SENSORS", 20);
        MIN_SDK_PERMISSIONS.put("android.permission.READ_CALL_LOG", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.READ_EXTERNAL_STORAGE", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.USE_SIP", 9);
        MIN_SDK_PERMISSIONS.put("android.permission.WRITE_CALL_LOG", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.SYSTEM_ALERT_WINDOW", 23);
        MIN_SDK_PERMISSIONS.put("android.permission.WRITE_SETTINGS", 23);
    }

    private PermissionUtils() {
    }

    /**
     * Checks all given permissions have been granted.
     *
     * @param grantResults results
     * @return returns true if all permissions have been granted.
     */
    public static boolean verifyPermissions(int... grantResults) {
        if (grantResults.length == 0) {
            return false;
        }
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if the permission exists in this SDK version
     *
     * @param permission permission
     * @return returns true if the permission exists in this SDK version
     */
    private static boolean permissionExists(String permission) {
        // Check if the permission could potentially be missing on this device
        Integer minVersion = MIN_SDK_PERMISSIONS.get(permission);
        // If null was returned from the above call, there is no need for a device API level check for the permission;
        // otherwise, we check if its minimum API level requirement is met
        return minVersion == null || Build.VERSION.SDK_INT >= minVersion;
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
            if (permissionExists(permission) && !hasSelfPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determine context has access to the given permission.
     * <p>
     * This is a workaround for RuntimeException of Parcel#readException.
     * For more detail, check this issue https://github.com/hotchemi/PermissionsDispatcher/issues/107
     *
     * @param context    context
     * @param permission permission
     * @return returns true if context has access to the given permission, false otherwise.
     * @see #hasSelfPermissions(Context, String...)
     */
    private static boolean hasSelfPermission(Context context, String permission) {
        // Do not replace with Build.VERSION_CODES.M!
        // The Android version bundled with the annotation processor's unit tests
        // is ancient, doesn't know that constant, and a more recent dependency doesn't exist.
        if (Build.VERSION.SDK_INT >= 23 /* Build.VERSION_CODES.M */ && "Xiaomi".equalsIgnoreCase(Build.MANUFACTURER)) {
            return hasSelfPermissionForXiaomi(context, permission);
        }
        try {
            return checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
        } catch (RuntimeException t) {
            return false;
        }
    }

    private static boolean hasSelfPermissionForXiaomi(Context context, String permission) {
        String permissionToOp = AppOpsManagerCompat.permissionToOp(permission);
        if (permissionToOp == null) {
            // in case of normal permissions(e.g. INTERNET)
            return true;
        }
        int noteOp = AppOpsManagerCompat.noteOp(context, permissionToOp, Process.myUid(), context.getPackageName());
        return noteOp == AppOpsManagerCompat.MODE_ALLOWED && checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
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
     * Checks given permissions are needed to show rationale.
     *
     * @param fragment    fragment
     * @param permissions permission list
     * @return returns true if one of the permission is needed to show rationale.
     */
    public static boolean shouldShowRequestPermissionRationale(android.support.v4.app.Fragment fragment, String... permissions) {
        for (String permission : permissions) {
            if (fragment.shouldShowRequestPermissionRationale(permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks given permissions are needed to show rationale.
     *
     * @param fragment    fragment
     * @param permissions permission list
     * @return returns true if one of the permission is needed to show rationale.
     */
    public static boolean shouldShowRequestPermissionRationale(Fragment fragment, String... permissions) {
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
    public static void requestPermissions(Fragment fragment, String[] permissions, int requestCode) {
        FragmentCompat.requestPermissions(fragment, permissions, requestCode);
    }
}
