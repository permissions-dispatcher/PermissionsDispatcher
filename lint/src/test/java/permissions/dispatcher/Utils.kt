package permissions.dispatcher

import org.intellij.lang.annotations.Language
import permissions.dispatcher.Utils.PACKAGE

internal object Utils {

    val PACKAGE = "package permissions.dispatcher;\n"

    val SOURCE_PATH = "src/permissions/dispatcher/"

    val runtimePermission: String
        get() = (""
                + "package permissions.dispatcher;\n"
                + "import java.lang.annotation.ElementType;\n"
                + "import java.lang.annotation.Retention;\n"
                + "import java.lang.annotation.RetentionPolicy;\n"
                + "import java.lang.annotation.Target;\n"
                + "@Target(ElementType.TYPE)\n"
                + "@Retention(RetentionPolicy.CLASS)\n"
                + "public @interface RuntimePermissions {\n"
                + "}")

    val onNeedsPermission: String
        get() = (""
                + PACKAGE
                + "import java.lang.annotation.ElementType;\n"
                + "import java.lang.annotation.Retention;\n"
                + "import java.lang.annotation.RetentionPolicy;\n"
                + "import java.lang.annotation.Target;\n"
                + "@Target(ElementType.METHOD)\n"
                + "@Retention(RetentionPolicy.CLASS)\n"
                + "public @interface NeedsPermission {\n"
                + "String[] value();\n"
                + "int maxSdkVersion() default 0;\n"
                + "}")

    val onRationaleAnnotation: String
        get() = (""
                + PACKAGE
                + "import java.lang.annotation.ElementType;\n"
                + "import java.lang.annotation.Retention;\n"
                + "import java.lang.annotation.RetentionPolicy;\n"
                + "import java.lang.annotation.Target;\n"
                + "@Target(ElementType.METHOD)\n"
                + "@Retention(RetentionPolicy.CLASS)\n"
                + "public @interface OnShowRationale {\n"
                + "String[] value();\n"
                + "}")
}
