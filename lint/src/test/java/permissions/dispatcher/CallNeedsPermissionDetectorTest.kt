package permissions.dispatcher

import org.intellij.lang.annotations.Language
import org.junit.Test

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import permissions.dispatcher.Utils.onNeedsPermission

class CallNeedsPermissionDetectorTest {

    @Test
    @Throws(Exception::class)
    fun callNeedsPermissionMethod() {
        @Language("JAVA") val foo = """
                package com.example;

                import permissions.dispatcher.NeedsPermission;

                public class Foo {
                    @NeedsPermission("Test")
                    void fooBar() {
                    }

                    public void hoge() {
                        fooBar();
                    }
                }
                """.trimMargin()

        val expectedText = """
            |src/com/example/Foo.java:11: Error: Trying to access permission-protected method directly [CallNeedsPermission]
            |                        fooBar();
            |                        ~~~~~~~~
            |1 errors, 0 warnings
            """.trimMargin()

        lint()
                .files(
                        java(onNeedsPermission),
                        java(foo))
                .issues(CallNeedsPermissionDetector.ISSUE)
                .run()
                .expect(expectedText)
                .expectErrorCount(1)
                .expectWarningCount(0)
    }

    @Test
    @Throws(Exception::class)
    fun callNeedsPermissionMethodNoError() {
        @Language("JAVA") val foo = """
                package com.example;

                public class Foo {
                    void someMethod() {
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
                    void fooBar() {
                    }

                    void noFooBar() {
                    }
                }
                """.trimMargin()

        lint()
                .files(
                        java(onNeedsPermission),
                        java(foo),
                        java(baz))
                .issues(CallNeedsPermissionDetector.ISSUE)
                .run()
                .expectClean()
    }

    @Test
    @Throws(Exception::class)
    fun callNeedsPermissionMethodForIssues502() {
        @Language("JAVA") val foo = """
            package com.example;

            import permissions.dispatcher.NeedsPermission;

            public class Foo extends AppCompatActivity  {
                @NeedsPermission({Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS})
                void requestOTP() {
                    new PhoneVerificationInputFragment().requestOTP();
                }
            }

            class FooFragment extends Fragment {
                public void resendOTP() {
                    requestOTP();
                }
                private void requestOTP() {
                }
            }
        """.trimMargin()

        val expectedText = """
            |src/com/example/Foo.java:14: Error: Trying to access permission-protected method directly [CallNeedsPermission]
            |                    requestOTP();
            |                    ~~~~~~~~~~~~
            |1 errors, 0 warnings
            """.trimMargin()

        lint()
                .files(
                        java(onNeedsPermission),
                        java(foo))
                .issues(CallNeedsPermissionDetector.ISSUE)
                .run()
                .expect(expectedText)
                .expectErrorCount(1)
                .expectWarningCount(0)
    }
}
