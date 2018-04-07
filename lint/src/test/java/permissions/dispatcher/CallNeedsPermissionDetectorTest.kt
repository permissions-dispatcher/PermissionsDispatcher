package permissions.dispatcher

import org.intellij.lang.annotations.Language
import org.junit.Test

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import permissions.dispatcher.Utils.SOURCE_PATH
import permissions.dispatcher.Utils.onNeedsPermission

class CallNeedsPermissionDetectorTest {

    @Test
    @Throws(Exception::class)
    fun callNeedsPermissionMethod() {

        @Language("JAVA") val onNeeds = onNeedsPermission

        @Language("JAVA") val foo = (""
                + "package com.example;\n"
                + "import permissions.dispatcher.NeedsPermission;\n"
                + "public class Foo {\n"
                + "@NeedsPermission(\"Test\")\n"
                + "public void fooBar() {\n"
                + "}\n"
                + "public void hoge() {\n"
                + "fooBar();\n"
                + "}\n"
                + "}")

        val expectedText = (""
                + "src/com/example/Foo.java:8: Error: Trying to access permission-protected method directly "
                + "["
                + CallNeedsPermissionDetector.ISSUE.id
                + "]\n"
                + "fooBar();\n"
                + "~~~~~~~~\n"
                + "1 errors, 0 warnings\n")

        lint()
                .files(
                        java(SOURCE_PATH + "NeedsPermission.java", onNeeds),
                        java("src/com/example/Foo.java", foo))
                .issues(CallNeedsPermissionDetector.ISSUE)
                .run()
                .expect(expectedText)
                .expectErrorCount(1)
                .expectWarningCount(0)
    }

    @Test
    @Throws(Exception::class)
    fun callNeedsPermissionMethodNoError() {

        @Language("JAVA") val onNeeds = onNeedsPermission

        @Language("JAVA") val foo = (""
                + "package com.example;\n"
                + "public class Foo {\n"
                + "public void someMethod() {"
                + "Baz baz = new Baz();\n"
                + "baz.noFooBar();\n"
                + "}\n"
                + "}")

        @Language("JAVA") val baz = (""
                + "package com.example;\n"
                + "import permissions.dispatcher.NeedsPermission;\n"
                + "public class Baz {\n"
                + "@NeedsPermission(\"Test\")\n"
                + "public void fooBar() {\n"
                + "}\n"
                + "public void noFooBar() {\n"
                + "}\n"
                + "}")

        lint()
                .files(
                        java(SOURCE_PATH + "NeedsPermission.java", onNeeds),
                        java("src/com/example/Foo.java", foo),
                        java("src/com/example/Baz.java", baz))
                .issues(CallNeedsPermissionDetector.ISSUE)
                .run()
                .expectClean()
    }
}
