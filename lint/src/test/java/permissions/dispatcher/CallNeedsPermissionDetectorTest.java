package permissions.dispatcher;

import org.intellij.lang.annotations.Language;
import org.junit.Test;

import java.util.Collections;

import static com.android.tools.lint.checks.infrastructure.TestFiles.java;
import static com.android.tools.lint.checks.infrastructure.TestLintTask.lint;

public final class CallNeedsPermissionDetectorTest {

    @Test
    public void callNeedsPermissionMethod() throws Exception {
        CallNeedsPermissionDetector.methods = Collections.singletonList("fooBar");

        @Language("JAVA") String foo = ""
                + "package com.example;\n"
                + "public class Foo {\n"
                + "public void someMethod() {"
                + "Baz baz = new Baz();\n"
                + "baz.fooBar();  "
                + "}\n"
                + "}";

        @Language("JAVA") String baz = ""
                + "package com.example;\n"
                + "public class Baz {\n"
                + "public void fooBar() {\n"
                + "}\n"
                + "}";

        String expectedText = ""
                + "src/com/example/Foo.java:4: Error: Trying to access permission-protected method directly "
                + "["
                + CallNeedsPermissionDetector.ISSUE.getId()
                + "]\n"
                + "baz.fooBar();  }\n"
                + "~~~~~~~~~~~~\n"
                + "1 errors, 0 warnings\n";

        lint()
                .files(
                        java("src/com/example/Foo.java", foo),
                        java("src/com/example/Baz.java", baz))
                .issues(CallNeedsPermissionDetector.ISSUE)
                .run()
                .expect(expectedText)
                .expectErrorCount(1)
                .expectWarningCount(0);
    }

    @Test
    public void callNeedsPermissionMethodNoError() throws Exception {
        CallNeedsPermissionDetector.methods = Collections.singletonList("fooBar");

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
                + "public class Baz {\n"
                + "public void noFooBar() {\n"
                + "}\n"
                + "}";

        lint()
                .files(
                        java("src/com/example/Foo.java", foo),
                        java("src/com/example/Baz.java", baz))
                .issues(CallNeedsPermissionDetector.ISSUE)
                .run()
                .expectClean();
    }
}
