package permissions.dispatcher

import org.intellij.lang.annotations.Language
import org.junit.Test

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestFiles.kt
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import permissions.dispatcher.Utils.onNeedsPermission
import permissions.dispatcher.Utils.runtimePermission

class CallNeedsPermissionDetectorKtTest {

    @Test
    @Throws(Exception::class)
    fun callNeedsPermissionMethod() {
        @Language("kotlin") val foo = """
                package com.example

                import permissions.dispatcher.NeedsPermission
                import permissions.dispatcher.RuntimePermissions

                @RuntimePermissions
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
            |src/com/example/Foo.kt:13: Error: Trying to access permission-protected method directly [CallNeedsPermission]
            |                        fooBar()
            |                        ~~~~~~~~
            |1 errors, 0 warnings
            """.trimMargin()

        lint()
                .files(
                        java(runtimePermission),
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
                import permissions.dispatcher.RuntimePermissions

                @RuntimePermissions
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
                        java(runtimePermission),
                        java(onNeedsPermission),
                        kt(foo),
                        kt(baz))
                .issues(CallNeedsPermissionDetector.ISSUE)
                .run()
                .expectClean()
    }

    @Test
    @Throws(Exception::class)
    fun issues502() {
        @Language("kotlin") val foo = """
            package com.example

            import permissions.dispatcher.NeedsPermission
            import permissions.dispatcher.RuntimePermissions

            @RuntimePermissions
            class Foo: AppCompatActivity  {
                @NeedsPermission(Manifest.permission.READ_SMS)
                fun requestOTP() {
                    PhoneVerificationInputFragment().requestOTP()
                }
            }

            class FooFragment: Fragment {
                fun resendOTP() {
                    requestOTP()
                }
                private fun requestOTP() {
                }
            }
        """.trimMargin()

        lint()
                .files(
                        java(runtimePermission),
                        java(onNeedsPermission),
                        kt(foo))
                .issues(CallNeedsPermissionDetector.ISSUE)
                .run()
                .expectClean()
    }
}
