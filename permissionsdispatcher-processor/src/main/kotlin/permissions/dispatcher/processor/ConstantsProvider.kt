package permissions.dispatcher.processor

import com.squareup.javapoet.ClassName

interface ConstantsProvider {
    companion object {

        public val ACTIVITY_COMPAT: ClassName = ClassName.get("android.support.v4.app", "ActivityCompat")
        public val PERMISSION_UTILS: ClassName = ClassName.get("permissions.dispatcher", "PermissionUtils")
        public val CLASS_SUFFIX: String = "PermissionsDispatcher"
        public val METHOD_SUFFIX: String = "WithCheck"
        public val REQUEST_CODE_PREFIX: String = "REQUEST_"
        public val PERMISSION_PREFIX: String = "PERMISSION_"
    }

}
