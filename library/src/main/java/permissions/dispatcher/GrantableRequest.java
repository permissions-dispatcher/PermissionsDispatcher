package permissions.dispatcher;

public interface GrantableRequest extends PermissionRequest {

    void grant();
}
