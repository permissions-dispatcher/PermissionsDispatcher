package permissions.dispatcher;

import com.google.common.collect.ImmutableList;

import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;

import org.intellij.lang.annotations.Language;
import org.junit.Test;

import java.util.List;

public class CallOnRequestPermissionsResultDetectorTest extends BaseLintDetectorTest {

    @Override
    protected Detector getDetector() {
        return new CallOnRequestPermissionsResultDetector();
    }

    @Override
    protected List<Issue> getIssues() {
        return ImmutableList.of(CallOnRequestPermissionsResultDetector.ISSUE);
    }

    @Test
    public void testCallOnRequestPermissionsResultDetectorNoError() throws Exception {
        @Language("JAVA") String runtimePerms = getRuntimePermission();

        @Language("JAVA") String onNeeds = getOnNeedsPermission();

        String onShow = getOnRationaleAnnotation();

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
                        runtimePerms.toString()),
                java("src/permissions/dispatcher/NeedsPermission.java", onNeeds),
                java("src/permissions/dispatcher/OnShowRationale.java", onShow),
                java("src/permissions/dispatcher/FooPermissionsDispatcher.java",
                        generatedClass),
                java("src/permissions/dispatcher/Foo.java", foo));

        assertEquals(result, "No warnings.");
    }

    @Test
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
                java(SOURCE_PATH + "RuntimePermissions.java", runtimePerms),
                java(SOURCE_PATH + "NeedsPermission.java", onNeeds),
                java(SOURCE_PATH + "OnShowRationale.java", onShow),
                java(SOURCE_PATH + "Foo.java", foo));

        String error = ""
                + SOURCE_PATH + "Foo.java:4: Error: {\n"
                + "Generated onRequestPermissionsResult method not called [NeedOnRequestPermissionsResult]\n"
                + "public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {\n"
                + "^\n"
                + "1 errors, 0 warnings\n";
        assertEquals(result, error);
    }

    private String getRuntimePermission() {
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
}
