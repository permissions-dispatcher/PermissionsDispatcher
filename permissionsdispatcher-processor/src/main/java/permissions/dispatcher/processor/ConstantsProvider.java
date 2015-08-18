package permissions.dispatcher.processor;

import com.squareup.javapoet.ClassName;

interface ConstantsProvider {

    ClassName ACTIVITY_COMPAT = ClassName.get("android.support.v4.app", "ActivityCompat");
    ClassName PERMISSION_UTIL = ClassName.get("permissions.dispatcher", "PermissionUtils");
    String CLASS_SUFFIX = "PermissionsDispatcher";
    String METHOD_SUFFIX = "WithCheck";
    String FIELD_PREFIX = "REQUEST_";

}
