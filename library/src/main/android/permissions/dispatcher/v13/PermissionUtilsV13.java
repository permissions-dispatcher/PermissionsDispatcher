package permissions.dispatcher.v13;

public final class PermissionUtilsV13 {

    public static final boolean V13_SUPPORTED = checkSupportV13OnClasspath();

    private static V13Access V13_ACCESS;

    private PermissionUtilsV13() {
    }

    public static V13Access getInstance() {
        if (V13_ACCESS == null) {
            V13_ACCESS = new V13Access();
        }
        return V13_ACCESS;
    }

    /* Begin private */

    private static boolean checkSupportV13OnClasspath() {
        try {
            Class.forName("android.support.v13.app.FragmentCompat");
            return true;
        } catch (ClassNotFoundException ignored) {
        }
        return false;
    }
}
