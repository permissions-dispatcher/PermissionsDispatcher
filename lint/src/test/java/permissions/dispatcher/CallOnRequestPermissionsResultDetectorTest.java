package permissions.dispatcher;

import org.intellij.lang.annotations.Language;
import org.junit.Test;

import static com.android.tools.lint.checks.infrastructure.TestFiles.java;
import static com.android.tools.lint.checks.infrastructure.TestLintTask.lint;
import static permissions.dispatcher.Utils.SOURCE_PATH;
import static permissions.dispatcher.Utils.getOnNeedsPermission;
import static permissions.dispatcher.Utils.getOnRationaleAnnotation;
import static permissions.dispatcher.Utils.getRuntimePermission;

public final class CallOnRequestPermissionsResultDetectorTest {

    @Test
    public void callOnRequestPermissionsResultDetectorNoError() throws Exception {
        @Language("JAVA") String runtimePerms = getRuntimePermission();

        @Language("JAVA") String onNeeds = getOnNeedsPermission();

        @Language("JAVA") String onShow = getOnRationaleAnnotation();

        @Language("JAVA") String foo = ""
                + "package permissions.dispatcher;\n"
                + "@RuntimePermissions\n"
                + "public class Foo extends android.app.Activity {\n"
                + "public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {\n"
                + "super.onRequestPermissionsResult(requestCode, permissions, grantResults);\n"
                + "FooPermissionsDispatcher.onRequestPermissionsResult(requestCode, grantResults);\n"
                + "}\n"
                + "@NeedsPermission(\"Camera\")"
                + "public void showCamera() {"
                + "}\n"
                + "@OnShowRationale(\"Camera\")"
                + "public void someMethod() {"
                + "}\n"
                + "}";

        @Language("JAVA") String generatedClass = ""
                + "package permissions.dispatcher;\n"
                + "public class FooPermissionsDispatcher {\n"
                + "public static void onRequestPermissionsResult(int requestCode, int[] grantResults) {\n"
                + "}\n"
                + "}";

        lint()
                .files(
                        java("src/permissions/dispatcher/RuntimePermissions.java", runtimePerms),
                        java("src/permissions/dispatcher/NeedsPermission.java", onNeeds),
                        java("src/permissions/dispatcher/OnShowRationale.java", onShow),
                        java("src/permissions/dispatcher/FooPermissionsDispatcher.java", generatedClass),
                        java("src/permissions/dispatcher/Foo.java", foo))
                .issues(CallOnRequestPermissionsResultDetector.ISSUE)
                .run()
                .expectClean();
    }

    @Test
    public void callOnRequestPermissionsResultDetector() throws Exception {
        @Language("JAVA") String runtimePerms = getRuntimePermission();

        @Language("JAVA") String onNeeds = getOnNeedsPermission();

        @Language("JAVA") String onShow = getOnRationaleAnnotation();

        @Language("JAVA") String foo = ""
                + "package permissions.dispatcher;\n"
                + "@RuntimePermissions\n"
                + "public class Foo extends android.app.Activity {\n"
                + "public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {\n"
                + "super.onRequestPermissionsResult(requestCode, permissions, grantResults);\n"
                + "}\n"
                + "@NeedsPermission(\"Camera\")"
                + "public void showCamera() {"
                + "}\n"
                + "@OnShowRationale(\"Camera\")"
                + "public void someMethod() {"
                + "}\n"
                + "}";

        String expectedText = ""
                + SOURCE_PATH + "Foo.java:4: Error: Generated onRequestPermissionsResult method not called [NeedOnRequestPermissionsResult]\n"
                + "public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {\n"
                + "^\n"
                + "1 errors, 0 warnings\n";

        lint()
                .files(
                        java(SOURCE_PATH + "RuntimePermissions.java", runtimePerms),
                        java(SOURCE_PATH + "NeedsPermission.java", onNeeds),
                        java(SOURCE_PATH + "OnShowRationale.java", onShow),
                        java(SOURCE_PATH + "Foo.java", foo))
                .issues(CallOnRequestPermissionsResultDetector.ISSUE)
                .run()
                .expect(expectedText)
                .expectErrorCount(1)
                .expectWarningCount(0);
    }
}
