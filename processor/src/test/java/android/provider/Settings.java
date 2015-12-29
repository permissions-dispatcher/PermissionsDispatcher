package android.provider;

import android.content.Context;

/**
 * DO NOT DO THIS IN PRODUCTION.
 * This class is mocking android.provider.Settings for testing purpose.
 */
public class Settings {
    public static String ACTION_MANAGE_WRITE_SETTINGS = "ACTION_MANAGE_WRITE_SETTINGS";
    public static String ACTION_MANAGE_OVERLAY_PERMISSION = "ACTION_MANAGE_OVERLAY_PERMISSION";

    public static boolean canDrawOverlays(Context context) {
        return false;
    }

    public static class System {
        public static boolean canWrite(Context context) {
            return false;
        }
    }
}
