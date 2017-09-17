package permissions.dispatcher;

import org.intellij.lang.annotations.Language;
import org.junit.Test;

import static com.android.tools.lint.checks.infrastructure.TestFiles.java;
import static com.android.tools.lint.checks.infrastructure.TestLintTask.lint;
import static permissions.dispatcher.Utils.SOURCE_PATH;
import static permissions.dispatcher.Utils.getOnNeedsPermission;

public final class CallNeedsPermissionDetectorTest {

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
                + CallNeedsPermissionDetector.ISSUE.getId()
                + "]\n"
                + "fooBar();\n"
                + "~~~~~~~~\n"
                + "1 errors, 0 warnings\n";

        lint()
                .files(
                        java(SOURCE_PATH + "NeedsPermission.java", onNeeds),
                        java("src/com/example/Foo.java", foo))
                .issues(CallNeedsPermissionDetector.ISSUE)
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
                .issues(CallNeedsPermissionDetector.ISSUE)
                .run()
                .expectClean();
    }
}
