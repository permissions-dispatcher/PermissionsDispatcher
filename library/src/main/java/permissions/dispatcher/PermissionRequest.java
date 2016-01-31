package permissions.dispatcher;

/**
 * Interface used by {@link OnShowRationale} methods to allow for continuation
 * or cancelation of a permission request.
 */
public interface PermissionRequest {

    String[] getPermissionNames();

    void proceed();

    void cancel();
}
