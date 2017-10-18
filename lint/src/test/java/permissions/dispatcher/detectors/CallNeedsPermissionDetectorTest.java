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
import static permissions.dispatcher.detectors.Utils.SOURCE_PATH;
import static permissions.dispatcher.detectors.Utils.getOnNeedsPermission;

@RunWith(Parameterized.class)
public final class CallNeedsPermissionDetectorTest {

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> issues() {
        return Arrays.asList(new Object[][]{
                {"Uast", UastCallNeedsPermissionDetector.ISSUE},
                {"Psi", PsiCallNeedsPermissionDetector.ISSUE}
        });
    }

    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private final String implName;
    private final Issue issue;

    public CallNeedsPermissionDetectorTest(String implName, Issue issue) {
        this.implName = implName;
        this.issue = issue;
    }

    @Test
    public void callNeedsPermissionMethod() throws Exception {

        @Language("JAVA") String onNeeds = getOnNeedsPermission();

        @Language("JAVA") String foo = ""
                + "package com.example;\n"
                + "import permissions.dispatcher.NeedsPermission;\n"
                + "public class Foo {\n"
                + "@NeedsPermission(\"Test\")\n"
                + "public void fooBar() {\n"
                + "}\n"
                + "public void hoge() {\n"
                + "fooBar();\n"
                + "}\n"
                + "}";

        String expectedText = ""
                + "src/com/example/Foo.java:8: Error: Trying to access permission-protected method directly "
                + "["
                + UastCallNeedsPermissionDetector.ISSUE.getId()
                + "]\n"
                + "fooBar();\n"
                + "~~~~~~~~\n"
                + "1 errors, 0 warnings\n";

        lint()
                .files(
                        java(SOURCE_PATH + "NeedsPermission.java", onNeeds),
                        java("src/com/example/Foo.java", foo))
                .issues(issue)
                .run()
                .expect(expectedText)
                .expectErrorCount(1)
                .expectWarningCount(0);
    }

    @Test
    public void callNeedsPermissionMethodNoError() throws Exception {

        @Language("JAVA") String onNeeds = getOnNeedsPermission();

        @Language("JAVA") String foo = ""
                + "package com.example;\n"
                + "public class Foo {\n"
                + "public void someMethod() {"
                + "Baz baz = new Baz();\n"
                + "baz.noFooBar();\n"
                + "}\n"
                + "}";

        @Language("JAVA") String baz = ""
                + "package com.example;\n"
                + "import permissions.dispatcher.NeedsPermission;\n"
                + "public class Baz {\n"
                + "@NeedsPermission(\"Test\")\n"
                + "public void fooBar() {\n"
                + "}\n"
                + "public void noFooBar() {\n"
                + "}\n"
                + "}";

        lint()
                .files(
                        java(SOURCE_PATH + "NeedsPermission.java", onNeeds),
                        java("src/com/example/Foo.java", foo),
                        java("src/com/example/Baz.java", baz))
                .issues(issue)
                .run()
                .expectClean();
    }
}
