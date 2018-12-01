package permissions.dispatcher

import org.intellij.lang.annotations.Language
import org.junit.Test

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestFiles.kt
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import permissions.dispatcher.Utils.onNeedsPermission
import permissions.dispatcher.Utils.onRationaleAnnotation

class NoCorrespondingNeedsPermissionDetectorKtTest {

    @Test
    @Throws(Exception::class)
    fun noNeedsPermissionAnnotationNoErrors() {
        @Language("kotlin") val foo = """
                package permissions.dispatcher

                class Foo {
                    @NeedsPermission("Camera")
                    fun showCamera() {
                    }

                    @OnShowRationale("Camera")
                    fun someMethod() {
                    }
                }
                """.trimMargin()

        lint()
                .files(
                        java(onNeedsPermission),
                        java(onRationaleAnnotation),
                        kt(foo))
                .issues(NoCorrespondingNeedsPermissionDetector.ISSUE)
                .run()
                .expectClean()
    }

    @Test
    @Throws(Exception::class)
    fun noNeedsPermissionAnnotationNoErrorsOrderNotMatter() {
        @Language("kotlin") val foo = """
                package permissions.dispatcher

                class Foo {
                    @OnShowRationale("Camera")
                    fun someMethod() {
                    }

                    @NeedsPermission("Camera")
                    fun showCamera() {
                    }
                }
                """.trimMargin()

        lint()
                .files(
                        java(onNeedsPermission),
                        java(onRationaleAnnotation),
                        kt(foo))
                .issues(NoCorrespondingNeedsPermissionDetector.ISSUE)
                .run()
                .expectClean()
    }

    @Test
    @Throws(Exception::class)
    fun noNeedsPermissionAnnotation() {
        @Language("kotlin") val foo = """
                package permissions.dispatcher

                class Foo {
                    @OnShowRationale("Camera")
                    fun someMethod() {
                    }
                }
                """.trimMargin()

        val expectedText = """
                |src/permissions/dispatcher/Foo.kt:4: Error: Useless @OnShowRationale declaration [NoCorrespondingNeedsPermission]
                |                    @OnShowRationale("Camera")
                |                    ~~~~~~~~~~~~~~~~~~~~~~~~~~
                |1 errors, 0 warnings
                """.trimMargin()

        lint()
                .files(
                        java(onRationaleAnnotation),
                        kt(foo))
                .issues(NoCorrespondingNeedsPermissionDetector.ISSUE)
                .run()
                .expect(expectedText)
                .expectErrorCount(1)
                .expectWarningCount(0)
    }
}
