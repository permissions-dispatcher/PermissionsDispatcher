package permissions.dispatcher

import org.intellij.lang.annotations.Language
import org.junit.Test

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import permissions.dispatcher.Utils.onNeedsPermission
import permissions.dispatcher.Utils.onRationaleAnnotation

class NoCorrespondingNeedsPermissionDetectorTest {

    @Test
    @Throws(Exception::class)
    fun noNeedsPermissionAnnotationNoErrors() {
        @Language("JAVA") val foo = """
                package permissions.dispatcher;

                public class Foo {
                    @NeedsPermission("Camera")
                    void showCamera() {
                    }

                    @OnShowRationale("Camera")
                    void someMethod() {
                    }
                }
                """.trimMargin()

        lint()
                .files(
                        java(onNeedsPermission),
                        java(onRationaleAnnotation),
                        java(foo))
                .issues(NoCorrespondingNeedsPermissionDetector.ISSUE)
                .run()
                .expectClean()
    }

    @Test
    @Throws(Exception::class)
    fun noNeedsPermissionAnnotationNoErrorsOrderNoTMatter() {
        @Language("JAVA") val foo = """
                package permissions.dispatcher;

                public class Foo {
                    @OnShowRationale("Camera")
                    void someMethod() {
                    }

                    @NeedsPermission("Camera")
                    void showCamera() {
                    }
                }
                """.trimMargin()

        lint()
                .files(
                        java(onNeedsPermission),
                        java(onRationaleAnnotation),
                        java(foo))
                .issues(NoCorrespondingNeedsPermissionDetector.ISSUE)
                .run()
                .expectClean()
    }

    @Test
    @Throws(Exception::class)
    fun noNeedsPermissionAnnotation() {
        @Language("JAVA") val foo = """
                package permissions.dispatcher;

                public class Foo {
                    @OnShowRationale("Camera")
                    void someMethod() {
                    }
                }
                """.trimMargin()

        val expectedText = """
                |src/permissions/dispatcher/Foo.java:4: Error: Useless @OnShowRationale declaration [NoCorrespondingNeedsPermission]
                |                    @OnShowRationale("Camera")
                |                    ~~~~~~~~~~~~~~~~~~~~~~~~~~
                |1 errors, 0 warnings
                """.trimMargin()

        lint()
                .files(
                        java(onRationaleAnnotation),
                        java(foo))
                .issues(NoCorrespondingNeedsPermissionDetector.ISSUE)
                .run()
                .expect(expectedText)
                .expectErrorCount(1)
                .expectWarningCount(0)
    }
}
