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

        @Language("JAVA") val foo = """
                package com.example;

                import permissions.dispatcher.NeedsPermission;
                public class Foo {
                    @NeedsPermission("Test")
                    public void fooBar() {
                    }

                    public void hoge() {
                        fooBar();
                    }
                }
                """.trimMargin()

        val expectedText = """
            |src/com/example/Foo.java:10: Error: Trying to access permission-protected method directly [CallNeedsPermission]
            |                        fooBar();
            |                        ~~~~~~~~
            |1 errors, 0 warnings
            """.trimMargin()

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

        @Language("JAVA") val foo = """
                package com.example;

                public class Foo {
                    public void someMethod() {
                        Baz baz = new Baz();
                        baz.noFooBar();
                    }
                }
                """.trimMargin()

        @Language("JAVA") val baz = """
                package com.example;

                import permissions.dispatcher.NeedsPermission;

                public class Baz {
                    @NeedsPermission("Test")
                    public void fooBar() {
                    }

                    public void noFooBar() {
                    }
                }
                """.trimMargin()

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
