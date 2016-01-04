package permissions.dispatcher.v13;

public final class PermissionUtilsV13 {

    private static final V13Access V13_ACCESS = new V13Access();

    private PermissionUtilsV13() {
    }

    public static V13Access getInstance() {
        return V13_ACCESS;
    }
}
