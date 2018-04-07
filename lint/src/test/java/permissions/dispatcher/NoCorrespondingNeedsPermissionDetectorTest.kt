package permissions.dispatcher

import org.intellij.lang.annotations.Language
import org.junit.Test

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import permissions.dispatcher.Utils.PACKAGE
import permissions.dispatcher.Utils.SOURCE_PATH
import permissions.dispatcher.Utils.onNeedsPermission
import permissions.dispatcher.Utils.onRationaleAnnotation

class NoCorrespondingNeedsPermissionDetectorTest {

    @Test
    @Throws(Exception::class)
    fun noNeedsPermissionAnnotationNoErrors() {
        @Language("JAVA") val onNeeds = onNeedsPermission

        @Language("JAVA") val onShow = onRationaleAnnotation

        @Language("JAVA") val foo = (""
                + PACKAGE
                + "public class Foo {\n"
                + "@NeedsPermission(\"Camera\")\n"
                + "public void showCamera() {\n"
                + "}\n"
                + "@OnShowRationale(\"Camera\")\n"
                + "public void someMethod() {\n"
                + "}\n"
                + "}")

        lint()
                .files(
                        java(SOURCE_PATH + "NeedsPermission.java", onNeeds),
                        java(SOURCE_PATH + "OnShowRationale.java", onShow),
                        java(SOURCE_PATH + "Foo.java", foo))
                .issues(NoCorrespondingNeedsPermissionDetector.ISSUE)
                .run()
                .expectClean()
    }

    @Test
    @Throws(Exception::class)
    fun noNeedsPermissionAnnotation() {

        @Language("JAVA") val onShow = onRationaleAnnotation

        @Language("JAVA") val foo = (""
                + PACKAGE
                + "public class Foo {\n"
                + "@OnShowRationale(\"Camera\")\n"
                + "public void someMethod() {\n"
                + "}\n"
                + "}")

        val expectedText = (""
                + SOURCE_PATH + "Foo.java:3: Error: Useless @OnShowRationale declaration "
                + "["
                + NoCorrespondingNeedsPermissionDetector.ISSUE.id
                + "]\n"
                + "@OnShowRationale(\"Camera\")\n"
                + "~~~~~~~~~~~~~~~~~~~~~~~~~~\n"
                + "1 errors, 0 warnings\n")

        lint()
                .files(
                        java(SOURCE_PATH + "OnShowRationale.java", onShow),
                        java(SOURCE_PATH + "Foo.java", foo))
                .issues(NoCorrespondingNeedsPermissionDetector.ISSUE)
                .run()
                .expect(expectedText)
                .expectErrorCount(1)
                .expectWarningCount(0)
    }
}
