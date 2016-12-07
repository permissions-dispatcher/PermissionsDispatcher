package permissions.dispatcher;

import com.android.tools.lint.checks.infrastructure.LintDetectorTest;

import org.intellij.lang.annotations.Language;

public abstract class BaseLintDetectorTest extends LintDetectorTest {

    protected static final String PACKAGE = "package permissions.dispatcher;\n";

    protected static final String SOURCE_PATH = "src/permissions/dispatcher/";

    protected String getOnNeedsPermission() {
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

    protected String getOnRationaleAnnotation() {
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
