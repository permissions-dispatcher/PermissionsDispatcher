package permissions.dispatcher;

import org.intellij.lang.annotations.Language;

public final class Utils {

    private Utils() {
    }

    public static final String PACKAGE = "package permissions.dispatcher;\n";

    public static final String SOURCE_PATH = "src/permissions/dispatcher/";

    public static String getRuntimePermission() {
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

    public static String getOnNeedsPermission() {
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

    public static String getOnRationaleAnnotation() {
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
