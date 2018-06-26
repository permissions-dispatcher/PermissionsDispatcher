package permissions.dispatcher

import org.intellij.lang.annotations.Language
import org.junit.Test

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import permissions.dispatcher.Utils.onNeedsPermission
import permissions.dispatcher.Utils.onRationaleAnnotation
import permissions.dispatcher.Utils.runtimePermission

class CallOnRequestPermissionsResultDetectorTest {

    @Test
    @Throws(Exception::class)
    fun callOnRequestPermissionsResultDetectorNoError() {
        @Language("JAVA") val foo = """
                package permissions.dispatcher;

                @RuntimePermissions
                public class Foo extends android.app.Activity {
                    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
                        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                        FooPermissionsDispatcher.onRequestPermissionsResult(requestCode, grantResults);
                    }

                    @NeedsPermission("Camera")
                    public void showCamera() {
                    }

                    @OnShowRationale("Camera")
                    public void someMethod() {
                    }
                }
                """.trimMargin()

        @Language("JAVA") val generatedClass = """
                package permissions.dispatcher;

                public class FooPermissionsDispatcher {
                    public static void onRequestPermissionsResult(int requestCode, int[] grantResults) {
                    }
                }
                """.trimMargin()

        lint()
                .files(
                        java(runtimePermission),
                        java(onNeedsPermission),
                        java(onRationaleAnnotation),
                        java(generatedClass),
                        java(foo))
                .issues(CallOnRequestPermissionsResultDetector.ISSUE)
                .run()
                .expectClean()
    }

    @Test
    @Throws(Exception::class)
    fun callOnRequestPermissionsResultDetector() {
        @Language("JAVA") val foo = """
                package permissions.dispatcher;

                @RuntimePermissions
                public class Foo extends android.app.Activity {
                    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
                        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                    }

                    @NeedsPermission("Camera")
                    public void showCamera() {
                    }

                    @OnShowRationale("Camera")
                    public void someMethod() {
                    }
                }
                """.trimMargin()

        val expectedText = """
                |src/permissions/dispatcher/Foo.java:5: Error: Generated onRequestPermissionsResult method not called [NeedOnRequestPermissionsResult]
                |                    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
                |                    ^
                |1 errors, 0 warnings
                """.trimMargin()

        lint()
                .files(
                        java(runtimePermission),
                        java(onNeedsPermission),
                        java(onRationaleAnnotation),
                        java(foo))
                .issues(CallOnRequestPermissionsResultDetector.ISSUE)
                .run()
                .expect(expectedText)
                .expectErrorCount(1)
                .expectWarningCount(0)
    }
}
