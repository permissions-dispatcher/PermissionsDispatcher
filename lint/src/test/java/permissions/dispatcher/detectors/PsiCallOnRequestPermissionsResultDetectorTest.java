package permissions.dispatcher.detectors;

import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.google.common.collect.ImmutableList;

import org.intellij.lang.annotations.Language;

import java.util.List;

import static permissions.dispatcher.Utils.SOURCE_PATH;
import static permissions.dispatcher.Utils.getOnNeedsPermission;
import static permissions.dispatcher.Utils.getOnRationaleAnnotation;
import static permissions.dispatcher.Utils.getRuntimePermission;

/**
 * @author Henry Addo
 */
public class PsiCallOnRequestPermissionsResultDetectorTest extends LintDetectorTest {

    @Override
    protected Detector getDetector() {
        return new PsiCallOnRequestPermissionsResultDetector();
    }

    @Override
    protected List<Issue> getIssues() {
        return ImmutableList.of(PsiCallOnRequestPermissionsResultDetector.ISSUE);

    }

    public void testCallOnRequestPermissionsResultDetectorNoError() throws Exception {
        @Language("JAVA") String runtimePerms = getRuntimePermission();

        @Language("JAVA") String onNeeds = getOnNeedsPermission();

        @Language("JAVA") String onShow = getOnRationaleAnnotation();

        @Language("JAVA") String foo = ""
                + "package permissions.dispatcher;\n"
                + "@RuntimePermissions\n"
                + "public class Foo {\n"
                + "public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {\n"
                + "FooPermissionsDispatcher.onRequestPermissionsResult();\n"
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
                + "public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {\n"
                + "}\n"
                + "public static void onRequestPermissionsResult() {"
                + "}\n"
                + "}";

        String result = lintProject(
                java("src/permissions/dispatcher/RuntimePermissions.java",
                        runtimePerms),
                java("src/permissions/dispatcher/NeedsPermission.java", onNeeds),
                java("src/permissions/dispatcher/OnShowRationale.java", onShow),
                java("src/permissions/dispatcher/FooPermissionsDispatcher.java",
                        generatedClass),
                java("src/permissions/dispatcher/Foo.java", foo));

        assertEquals(result, "No warnings.");
    }

    public void testCallOnRequestPermissionsResultDetector() throws Exception {

        @Language("JAVA") String runtimePerms = getRuntimePermission();

        @Language("JAVA") String onNeeds = getOnNeedsPermission();

        @Language("JAVA") String onShow = getOnRationaleAnnotation();

        @Language("JAVA") String foo = ""
                + "package permissions.dispatcher;\n"
                + "@RuntimePermissions\n"
                + "public class Foo {\n"
                + "public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {\n"
                + "}\n"
                + "@NeedsPermission(\"Camera\")"
                + "public void showCamera() {"
                + "}\n"
                + "@OnShowRationale(\"Camera\")"
                + "public void someMethod() {"
                + "}\n"
                + "}";

        String result = lintProject(
                java(SOURCE_PATH + "RuntimePermissions.java",
                        runtimePerms),
                java(SOURCE_PATH + "NeedsPermission.java", onNeeds),
                java(SOURCE_PATH + "OnShowRationale.java", onShow),
                java(SOURCE_PATH + "Foo.java", foo));

        String error = ""
                + SOURCE_PATH + "Foo.java:4: Error: {\n"
                + "}Generated onRequestPermissionsResult method not called [NeedOnRequestPermissionsResult]\n"
                + "public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {\n"
                + "^\n"
                + "1 errors, 0 warnings\n";

        assertEquals(result, error);
    }
}
