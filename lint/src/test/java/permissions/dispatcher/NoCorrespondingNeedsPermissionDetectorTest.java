package permissions.dispatcher;

import com.google.common.collect.ImmutableList;

import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;

import org.intellij.lang.annotations.Language;
import org.junit.Test;

import java.util.List;

public class NoCorrespondingNeedsPermissionDetectorTest extends BaseLintDetectorTest {

    @Override
    protected Detector getDetector() {
        return new NoCorrespondingNeedsPermissionDetector();
    }

    @Override
    protected List<Issue> getIssues() {
        return ImmutableList.of(NoCorrespondingNeedsPermissionDetector.ISSUE);
    }

    @Test
    public void testNoNeedsPermissionAnnotationNoErrors() throws Exception {
        @Language("JAVA") String onNeeds = getOnNeedsPermission();
        @Language("JAVA") String onShow = getOnRationaleAnnotation();
        @Language("JAVA") String foo = ""
                + PACKAGE
                + "public class Foo {\n"
                + "@NeedsPermission(\"Camera\")\n"
                + "public void showCamera() {\n"
                + "}\n"
                + "@OnShowRationale(\"Camera\")\n"
                + "public void someMethod() {\n"
                + "}\n"
                + "}";

        String result = lintProject(
                java(SOURCE_PATH + "NeedsPermission.java", onNeeds),
                java(SOURCE_PATH + "OnShowRationale.java", onShow),
                java(SOURCE_PATH + "Foo.java", foo));

        assertEquals(result, "No warnings.");
    }

    @Test
    public void testNoNeedsPermissionAnnotation() throws Exception {

        @Language("JAVA") String onShow = getOnRationaleAnnotation();

        @Language("JAVA") String foo = ""
                + PACKAGE
                + "public class Foo {\n"
                + "@OnShowRationale(\"Camera\")\n"
                + "public void someMethod() {\n"
                + "}\n"
                + "}";

        String result = lintProject(
                java(SOURCE_PATH + "OnShowRationale.java", onShow),
                java(SOURCE_PATH + "Foo.java", foo));

        String error = ""
                + SOURCE_PATH + "Foo.java:3: Error: Useless @OnShowRationale declaration "
                + "["
                + NoCorrespondingNeedsPermissionDetector.ISSUE.getId()
                + "]\n"
                + "@OnShowRationale(\"Camera\")\n"
                + "~~~~~~~~~~~~~~~~~~~~~~~~~~\n"
                + "1 errors, 0 warnings\n";
        assertEquals(result, error);
    }
}
