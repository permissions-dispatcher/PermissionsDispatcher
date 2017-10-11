package permissions.dispatcher.detectors;

import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.google.common.collect.ImmutableList;

import org.intellij.lang.annotations.Language;

import java.util.List;

import static permissions.dispatcher.detectors.Utils.PACKAGE;
import static permissions.dispatcher.detectors.Utils.SOURCE_PATH;
import static permissions.dispatcher.detectors.Utils.getOnNeedsPermission;
import static permissions.dispatcher.detectors.Utils.getOnRationaleAnnotation;

public class PsiNoCorrespondingNeedsPermissionDetectorTest extends LintDetectorTest {

    @Override
    protected Detector getDetector() {
        return new PsiNoCorrespondingNeedsPermissionDetector();
    }

    @Override
    protected List<Issue> getIssues() {
        return ImmutableList.of(PsiNoCorrespondingNeedsPermissionDetector.ISSUE);
    }

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
                + PsiNoCorrespondingNeedsPermissionDetector.ISSUE.getId()
                + "]\n"
                + "@OnShowRationale(\"Camera\")\n"
                + "~~~~~~~~~~~~~~~~~~~~~~~~~~\n"
                + "1 errors, 0 warnings\n";
        assertEquals(result, error);
    }
}
