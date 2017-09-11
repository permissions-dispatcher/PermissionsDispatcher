package permissions.dispatcher;

import org.intellij.lang.annotations.Language;

final class Utils {

    private Utils() {}

    static final String PACKAGE = "package permissions.dispatcher;\n";

    static final String SOURCE_PATH = "src/permissions/dispatcher/";

    static String getRuntimePermission() {
        @Language("JAVA") String runPermissions = ""
                + "package permissions.dispatcher;\n"
                + "import java.lang.annotation.ElementType;\n"
                + "import java.lang.annotation.Retention;\n"
                + "import java.lang.annotation.RetentionPolicy;\n"
                + "import java.lang.annotation.Target;\n"
                + "@Target(ElementType.TYPE)\n"
                + "@Retention(RetentionPolicy.CLASS)\n"
                + "public @interface RuntimePermissions {\n"
                + "}";
        return runPermissions;
    }

    static String getOnNeedsPermission() {
        @Language("JAVA") String onNeedsPermission = ""
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
                + "}";
        return onNeedsPermission;
    }

    static String getOnRationaleAnnotation() {
        @Language("JAVA") String onShow = ""
                + PACKAGE
                + "import java.lang.annotation.ElementType;\n"
                + "import java.lang.annotation.Retention;\n"
                + "import java.lang.annotation.RetentionPolicy;\n"
                + "import java.lang.annotation.Target;\n"
                + "@Target(ElementType.METHOD)\n"
                + "@Retention(RetentionPolicy.CLASS)\n"
                + "public @interface OnShowRationale {\n"
                + "String[] value();\n"
                + "}";
        return onShow;
    }
}
