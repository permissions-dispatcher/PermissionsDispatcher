package permissions.dispatcher

import com.android.tools.lint.checks.infrastructure.TestFiles
import com.android.tools.lint.checks.infrastructure.TestLintTask
import org.intellij.lang.annotations.Language
import org.junit.Test

import permissions.dispatcher.Utils.runtimePermission

class NoDelegateOnResumeDetectorKtTest {
    @Test
    @Throws(Exception::class)
    fun `WithPermissionCheck call inside onResume`() {
        @Language("kotlin") val foo = """
                package permissions.dispatcher

                @RuntimePermissions
                class Foo : android.app.Activity {
                    @NeedsPermission("Camera")
                    fun showCamera() {
                    }

                    fun onResume() {
                        super.onResume()
                        showCameraWithPermissionCheck()
                    }
                }
                """.trimMargin()

        val expectedText = """
                |src/permissions/dispatcher/Foo.kt:11: Error: Asking permission inside onResume() [NoDelegateOnResumeDetector]
                |                        showCameraWithPermissionCheck()
                |                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                |1 errors, 0 warnings
                """.trimMargin()

        TestLintTask.lint()
                .files(
                        TestFiles.java(runtimePermission),
                        TestFiles.java(Utils.onNeedsPermission),
                        TestFiles.kt(foo))
                .issues(NoDelegateOnResumeDetector.ISSUE)
                .run()
                .expect(expectedText)
                .expectErrorCount(1)
                .expectWarningCount(0)
    }

    @Test
    @Throws(Exception::class)
    fun `WithPermissionCheck call inside onResume which the method is before NeedsPermission`() {
        @Language("kotlin") val foo = """
                package permissions.dispatcher

                @RuntimePermissions
                class Foo : android.app.Activity {
                    fun onResume() {
                        super.onResume()
                        showCameraWithPermissionCheck()
                    }

                    @NeedsPermission("Camera")
                    fun showCamera() {
                    }
                }
                """.trimMargin()

        val expectedText = """
                |src/permissions/dispatcher/Foo.kt:7: Error: Asking permission inside onResume() [NoDelegateOnResumeDetector]
                |                        showCameraWithPermissionCheck()
                |                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                |1 errors, 0 warnings
                """.trimMargin()

        TestLintTask.lint()
                .files(
                        TestFiles.java(runtimePermission),
                        TestFiles.java(Utils.onNeedsPermission),
                        TestFiles.kt(foo))
                .issues(NoDelegateOnResumeDetector.ISSUE)
                .run()
                .expect(expectedText)
                .expectErrorCount(1)
                .expectWarningCount(0)
    }

    @Test
    @Throws(Exception::class)
    fun `No WithPermissionCheck inside onResume`() {
        @Language("kotlin") val foo = """
                package permissions.dispatcher

                @RuntimePermissions
                class Foo : android.app.Activity {
                    @NeedsPermission("Camera")
                    fun showCamera() {
                    }

                    fun onStart() {
                        super.onStart()
                        showCameraWithPermissionCheck()
                    }
                }
                """.trimMargin()

        TestLintTask.lint()
                .files(
                        TestFiles.java(runtimePermission),
                        TestFiles.java(Utils.onNeedsPermission),
                        TestFiles.kt(foo))
                .issues(NoDelegateOnResumeDetector.ISSUE)
                .run()
                .expectClean()
    }

    @Test
    @Throws(Exception::class)
    fun `WithPermissionCheck call inside onResume with argument`() {
        @Language("kotlin") val foo = """
                package permissions.dispatcher

                @RuntimePermissions
                class Foo : android.app.Activity {
                    @NeedsPermission("Camera")
                    fun showCamera(value: Int) {
                    }

                    fun onResume() {
                        super.onResume()
                        showCameraWithPermissionCheck(1)
                    }

                }
                """.trimMargin()

        val expectedText = """
                |src/permissions/dispatcher/Foo.kt:11: Error: Asking permission inside onResume() [NoDelegateOnResumeDetector]
                |                        showCameraWithPermissionCheck(1)
                |                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                |1 errors, 0 warnings
                """.trimMargin()

        TestLintTask.lint()
                .files(
                        TestFiles.java(runtimePermission),
                        TestFiles.java(Utils.onNeedsPermission),
                        TestFiles.kt(foo))
                .issues(NoDelegateOnResumeDetector.ISSUE)
                .run()
                .expect(expectedText)
                .expectErrorCount(1)
                .expectWarningCount(0)
    }

    @Test
    @Throws(Exception::class)
    fun `WithPermissionCheck call inside onResume but there is receiver`() {
        @Language("kotlin") val foo = """
                package permissions.dispatcher

                @RuntimePermissions
                class Foo : android.app.Activity {
                    @NeedsPermission("Camera")
                    fun showCamera() {
                    }

                    fun onResume() {
                        super.onResume()
                        Bar.showCameraWithPermissionCheck()
                    }
                }
                """.trimMargin()

        TestLintTask.lint()
                .files(
                        TestFiles.java(runtimePermission),
                        TestFiles.java(Utils.onNeedsPermission),
                        TestFiles.kt(foo))
                .issues(NoDelegateOnResumeDetector.ISSUE)
                .run()
                .expectClean()
    }

    @Test
    @Throws(Exception::class)
    fun `WithPermissionCheck call inside onResume but its visibility is private`() {
        @Language("kotlin") val foo = """
                package permissions.dispatcher

                @RuntimePermissions
                class Foo : android.app.Activity {
                    @NeedsPermission("Camera")
                    fun showCamera() {
                    }

                    private fun onResume() {
                        super.onResume()
                        showCameraWithPermissionCheck()
                    }
                }
                """.trimMargin()

        TestLintTask.lint()
                .files(
                        TestFiles.java(runtimePermission),
                        TestFiles.java(Utils.onNeedsPermission),
                        TestFiles.kt(foo))
                .issues(NoDelegateOnResumeDetector.ISSUE)
                .run()
                .expectClean()
    }

}