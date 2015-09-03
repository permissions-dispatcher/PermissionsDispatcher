package permissions.dispatcher.v13;

public final class PermissionUtilsV13 {

    private static V13Access V13_ACCESS;

    private PermissionUtilsV13() {
    }

    public static V13Access getInstance() {
        if (V13_ACCESS == null) {
            V13_ACCESS = new V13Access();
        }
        return V13_ACCESS;
    }
}
