package permissions.dispatcher.detectors;

import com.android.tools.lint.detector.api.Issue;

import org.intellij.lang.annotations.Language;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static com.android.tools.lint.checks.infrastructure.TestFiles.java;
import static com.android.tools.lint.checks.infrastructure.TestLintTask.lint;
import static permissions.dispatcher.detectors.Utils.PACKAGE;
import static permissions.dispatcher.detectors.Utils.SOURCE_PATH;
import static permissions.dispatcher.detectors.Utils.getOnNeedsPermission;
import static permissions.dispatcher.detectors.Utils.getOnRationaleAnnotation;

@RunWith(Parameterized.class)
public final class NoCorrespondingNeedsPermissionDetectorTest {

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> issues() {
        return Arrays.asList(new Object[][]{
                {"Uast", UastNoCorrespondingNeedsPermissionDetector.ISSUE},
                {"Psi", PsiNoCorrespondingNeedsPermissionDetector.ISSUE}
        });
    }

    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private final String implName;
    private final Issue issue;

    public NoCorrespondingNeedsPermissionDetectorTest(String implName, Issue issue) {
        this.implName = implName;
        this.issue = issue;
    }

    @Test
    public void noNeedsPermissionAnnotationNoErrors() throws Exception {
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

        lint()
                .files(
                        java(SOURCE_PATH + "NeedsPermission.java", onNeeds),
                        java(SOURCE_PATH + "OnShowRationale.java", onShow),
                        java(SOURCE_PATH + "Foo.java", foo))
                .issues(issue)
                .run()
                .expectClean();
    }

    @Test
    public void noNeedsPermissionAnnotation() throws Exception {

        @Language("JAVA") String onShow = getOnRationaleAnnotation();

        @Language("JAVA") String foo = ""
                + PACKAGE
                + "public class Foo {\n"
                + "@OnShowRationale(\"Camera\")\n"
                + "public void someMethod() {\n"
                + "}\n"
                + "}";

        String expectedText = ""
                + SOURCE_PATH + "Foo.java:3: Error: Useless @OnShowRationale declaration "
                + "["
                + UastNoCorrespondingNeedsPermissionDetector.ISSUE.getId()
                + "]\n"
                + "@OnShowRationale(\"Camera\")\n"
                + "~~~~~~~~~~~~~~~~~~~~~~~~~~\n"
                + "1 errors, 0 warnings\n";

        lint()
                .files(
                        java(SOURCE_PATH + "OnShowRationale.java", onShow),
                        java(SOURCE_PATH + "Foo.java", foo))
                .issues(issue)
                .run()
                .expect(expectedText)
                .expectErrorCount(1)
                .expectWarningCount(0);
    }
}
