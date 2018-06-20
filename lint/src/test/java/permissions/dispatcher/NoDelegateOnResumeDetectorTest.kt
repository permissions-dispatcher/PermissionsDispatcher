package permissions.dispatcher

import com.android.tools.lint.checks.infrastructure.TestFiles
import com.android.tools.lint.checks.infrastructure.TestLintTask
import org.intellij.lang.annotations.Language
import org.junit.Test

import permissions.dispatcher.Utils.runtimePermission

class NoDelegateOnResumeDetectorTest {
    @Test
    @Throws(Exception::class)
    fun `WithPermissionCheck call inside onResume`() {
        @Language("JAVA") val foo = """
                package permissions.dispatcher;

                @RuntimePermissions
                public class Foo {
                    @NeedsPermission("Camera")
                    void showCamera() {
                    }

                    protected void onResume() {
                        super.onResume();
                        FooPermissionsDispatcher.showCameraWithPermissionCheck(this);
                    }
                }
                """.trimMargin()

        val expectedText = """
                |src/permissions/dispatcher/Foo.java:11: Error: Asking permission inside onResume() [NoDelegateOnResumeDetector]
                |                        FooPermissionsDispatcher.showCameraWithPermissionCheck(this);
                |                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                |1 errors, 0 warnings
                """.trimMargin()

        TestLintTask.lint()
                .files(
                        TestFiles.java(runtimePermission),
                        TestFiles.java(Utils.onNeedsPermission),
                        TestFiles.java(foo))
                .issues(NoDelegateOnResumeDetector.ISSUE)
                .run()
                .expect(expectedText)
                .expectErrorCount(1)
                .expectWarningCount(0)
    }

    @Test
    @Throws(Exception::class)
    fun `WithPermissionCheck call inside onResume which the method is before NeedsPermission`() {
        @Language("JAVA") val foo = """
                package permissions.dispatcher;

                @RuntimePermissions
                public class Foo {
                    protected void onResume() {
                        super.onResume();
                        FooPermissionsDispatcher.showCameraWithPermissionCheck(this);
                    }

                    @NeedsPermission("Camera")
                    void showCamera() {
                    }
                }
                """.trimMargin()

        val expectedText = """
                |src/permissions/dispatcher/Foo.java:7: Error: Asking permission inside onResume() [NoDelegateOnResumeDetector]
                |                        FooPermissionsDispatcher.showCameraWithPermissionCheck(this);
                |                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                |1 errors, 0 warnings
                """.trimMargin()

        TestLintTask.lint()
                .files(
                        TestFiles.java(runtimePermission),
                        TestFiles.java(Utils.onNeedsPermission),
                        TestFiles.java(foo))
                .issues(NoDelegateOnResumeDetector.ISSUE)
                .run()
                .expect(expectedText)
                .expectErrorCount(1)
                .expectWarningCount(0)
    }

    @Test
    @Throws(Exception::class)
    fun `No WithPermissionCheck inside onResume`() {
        @Language("JAVA") val foo = """
                package permissions.dispatcher;

                @RuntimePermissions
                public class Foo {
                    public void onStart() {
                        super.onStart();
                        FooPermissionsDispatcher.showCameraWithPermissionCheck(this);
                    }

                    @NeedsPermission("Camera")
                    void showCamera() {
                    }
                }
                """.trimMargin()

        TestLintTask.lint()
                .files(
                        TestFiles.java(runtimePermission),
                        TestFiles.java(Utils.onNeedsPermission),
                        TestFiles.java(foo))
                .issues(NoDelegateOnResumeDetector.ISSUE)
                .run()
                .expectClean()
    }

    @Test
    @Throws(Exception::class)
    fun `WithPermissionCheck call inside onResume but there is no argument`() {
        @Language("JAVA") val foo = """
                package permissions.dispatcher;

                @RuntimePermissions
                public class Foo {
                    protected void onResume() {
                        super.onResume();
                        // this should not be generated...
                        FooPermissionsDispatcher.showCameraWithPermissionCheck();
                    }

                    @NeedsPermission("Camera")
                    void showCamera() {
                    }
                }
                """.trimMargin()

        TestLintTask.lint()
                .files(
                        TestFiles.java(runtimePermission),
                        TestFiles.java(Utils.onNeedsPermission),
                        TestFiles.java(foo))
                .issues(NoDelegateOnResumeDetector.ISSUE)
                .run()
                .expectClean()
    }

    @Test
    @Throws(Exception::class)
    fun `WithPermissionCheck call inside onResume but there is no receiver`() {
        @Language("JAVA") val foo = """
                package permissions.dispatcher;

                @RuntimePermissions
                public class Foo {
                    protected void onResume() {
                        super.onResume();
                        showCameraWithPermissionCheck(this);
                    }

                    @NeedsPermission("Camera")
                    void showCamera() {
                    }

                    void showCameraWithPermissionCheck(Foo foo) {

                    }
                }
                """.trimMargin()

        TestLintTask.lint()
                .files(
                        TestFiles.java(runtimePermission),
                        TestFiles.java(Utils.onNeedsPermission),
                        TestFiles.java(foo))
                .issues(NoDelegateOnResumeDetector.ISSUE)
                .run()
                .expectClean()
    }

    @Test
    @Throws(Exception::class)
    fun `WithPermissionCheck call inside onResume but its visibility is package private`() {
        @Language("JAVA") val foo = """
                package permissions.dispatcher;

                @RuntimePermissions
                public class Foo {
                    void onResume() {
                        super.onResume();
                        FooPermissionsDispatcher.showCameraWithPermissionCheck(this);
                    }

                    @NeedsPermission("Camera")
                    void showCamera() {
                    }
                }
                """.trimMargin()

        TestLintTask.lint()
                .files(
                        TestFiles.java(runtimePermission),
                        TestFiles.java(Utils.onNeedsPermission),
                        TestFiles.java(foo))
                .issues(NoDelegateOnResumeDetector.ISSUE)
                .run()
                .expectClean()
    }

}