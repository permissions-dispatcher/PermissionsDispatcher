package permissions.dispatcher

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.intellij.lang.annotations.Language
import org.junit.Test
import permissions.dispatcher.Utils.onNeedsPermission
import permissions.dispatcher.Utils.runtimePermission

class CallNeedsPermissionDetectorTest {

    @Test
    @Throws(Exception::class)
    fun callNeedsPermissionMethod() {
        @Language("JAVA") val foo = """
                package com.example;

                import permissions.dispatcher.NeedsPermission;
                import permissions.dispatcher.RuntimePermissions;

                @RuntimePermissions
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
            |src/com/example/Foo.java:13: Error: Trying to access permission-protected method directly [CallNeedsPermission]
            |                        fooBar();
            |                        ~~~~~~~~
            |1 errors, 0 warnings
            """.trimMargin()

        lint()
                .files(
                        java(runtimePermission),
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
                import permissions.dispatcher.RuntimePermissions;

                @RuntimePermissions
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
    fun issues502() {
        @Language("JAVA") val foo = """
            package com.example;

            import permissions.dispatcher.NeedsPermission;
            import permissions.dispatcher.RuntimePermissions;

            @RuntimePermissions
            public class Foo extends AppCompatActivity  {
                @NeedsPermission({Manifest.permission.READ_SMS})
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

        lint()
                .files(
                        java(runtimePermission),
                        java(onNeedsPermission),
                        java(foo))
                .issues(CallNeedsPermissionDetector.ISSUE)
                .run()
                .expectClean()
    }

    @Test
    fun `same name methods in different class(issue602)`() {
        @Language("java") val foo = """
            package com.example;

            import permissions.dispatcher.NeedsPermission;
            import permissions.dispatcher.RuntimePermissions;

            @RuntimePermissions
            public class FirstActivity extends AppCompatActivity  {
                @NeedsPermission({Manifest.permission.READ_SMS})
                void someFun() {
                }
            }

            @RuntimePermissions
            public class SecondActivity extends AppCompatActivity  {
                @Override
                protected void onCreate(@Nullable Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    someFun();
                }

                void someFun() {
                }

                @NeedsPermission({Manifest.permission.READ_SMS})
                void otherFun() {
                }
            }
        """.trimMargin()

        lint()
                .files(java(runtimePermission), java(onNeedsPermission), java(foo))
                .issues(CallNeedsPermissionDetector.ISSUE)
                .run()
                .expectClean()
    }
}
