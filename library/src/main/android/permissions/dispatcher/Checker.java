package permissions.dispatcher;

import android.app.Activity;
import android.content.Context;

public interface Checker {

    /**
     * Checks all given permissions have been granted.
     *
     * @param grantResults results
     * @return returns true if all permissions have been granted.
     */
    boolean verifyPermissions(int... grantResults);

    /**
     * Returns true if the Activity or Fragment has access to all given permissions.
     *
     * @param context     context
     * @param permissions permission list
     * @return returns true if the Activity or Fragment has access to all given permissions.
     */
    boolean hasSelfPermissions(Context context, String... permissions);

    /**
     * Checks given permissions are needed to show rationale.
     *
     * @param activity    activity
     * @param permissions permission list
     * @return returns true if one of the permission is needed to show rationale.
     */
    boolean shouldShowRequestPermissionRationale(Activity activity, String... permissions);

    /**
     * Checks given permissions are denied when the device is under marshmallow.
     *
     * @param context     context
     * @param permissions permission list
     * @return returns true if one of the permission is needed to show rationale.
     */
    boolean isDeniedUnderMarshmallow(Context context, String... permissions);
}
