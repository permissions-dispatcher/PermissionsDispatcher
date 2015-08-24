package permissions.dispatcher.processor;

import com.squareup.javapoet.ClassName;

interface ConstantsProvider {

    ClassName ACTIVITY_COMPAT = ClassName.get("android.support.v4.app", "ActivityCompat");
    ClassName PERMISSION_UTILS = ClassName.get("permissions.dispatcher", "PermissionUtils");
    String CLASS_SUFFIX = "PermissionsDispatcher";
    String METHOD_SUFFIX = "WithCheck";
    String REQUEST_CODE_PREFIX = "REQUEST_";
    String PERMISSION_PREFIX = "PERMISSION_";

}
