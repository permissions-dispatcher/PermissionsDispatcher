package permissions.dispatcher

import org.intellij.lang.annotations.Language
import org.junit.Test

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestFiles.kt
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import permissions.dispatcher.Utils.onNeedsPermission

class CallNeedsPermissionDetectorKtTest {

    @Test
    @Throws(Exception::class)
    fun callNeedsPermissionMethod() {
        @Language("kotlin") val foo = """
                package com.example

                import permissions.dispatcher.NeedsPermission

                class Foo {
                    @NeedsPermission("Test")
                    fun fooBar() {
                    }

                    fun hoge() {
                        fooBar()
                    }
                }
                """.trimMargin()

        val expectedText = """
            |src/com/example/Foo.kt:11: Error: Trying to access permission-protected method directly [CallNeedsPermission]
            |                        fooBar()
            |                        ~~~~~~~~
            |1 errors, 0 warnings
            """.trimMargin()

        lint()
                .files(
                        java(onNeedsPermission),
                        kt(foo))
                .issues(CallNeedsPermissionDetector.ISSUE)
                .run()
                .expect(expectedText)
                .expectErrorCount(1)
                .expectWarningCount(0)
    }

    @Test
    @Throws(Exception::class)
    fun callNeedsPermissionMethodNoError() {
        @Language("kotlin") val foo = """
                package com.example

                class Foo {
                    fun someMethod() {
                        val baz = Baz()
                        baz.noFooBar()
                    }
                }
                """.trimMargin()

        @Language("kotlin") val baz = """
                package com.example

                import permissions.dispatcher.NeedsPermission

                class Baz {
                    @NeedsPermission("Test")
                    fun fooBar() {
                    }

                    fun noFooBar() {
                    }
                }
                """.trimMargin()

        lint()
                .files(
                        java(onNeedsPermission),
                        kt(foo),
                        kt(baz))
                .issues(CallNeedsPermissionDetector.ISSUE)
                .run()
                .expectClean()
    }
}
