package permissions.dispatcher;

/**
 * Interface used by {@link OnShowRationale} methods to allow for continuation
 * or cancellation of a permission request.
 */
public interface PermissionRequest {

    void proceed();

    void cancel();
}
