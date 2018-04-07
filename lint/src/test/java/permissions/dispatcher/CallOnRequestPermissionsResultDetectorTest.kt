package permissions.dispatcher

import org.intellij.lang.annotations.Language
import org.junit.Test

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestFiles.kt
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import permissions.dispatcher.Utils.SOURCE_PATH
import permissions.dispatcher.Utils.onNeedsPermission
import permissions.dispatcher.Utils.onRationaleAnnotation
import permissions.dispatcher.Utils.runtimePermission

class CallOnRequestPermissionsResultDetectorTest {

    @Test
    @Throws(Exception::class)
    fun callOnRequestPermissionsResultDetectorNoError() {
        @Language("JAVA") val runtimePerms = runtimePermission

        @Language("JAVA") val onNeeds = onNeedsPermission

        @Language("JAVA") val onShow = onRationaleAnnotation

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
                        java("src/permissions/dispatcher/RuntimePermissions.java", runtimePerms),
                        java("src/permissions/dispatcher/NeedsPermission.java", onNeeds),
                        java("src/permissions/dispatcher/OnShowRationale.java", onShow),
                        java("src/permissions/dispatcher/FooPermissionsDispatcher.java", generatedClass),
                        java("src/permissions/dispatcher/Foo.java", foo))
                .issues(CallOnRequestPermissionsResultDetector.ISSUE)
                .run()
                .expectClean()
    }

    @Test
    @Throws(Exception::class)
    fun callOnRequestPermissionsResultDetector() {
        @Language("JAVA") val runtimePerms = runtimePermission

        @Language("JAVA") val onNeeds = onNeedsPermission

        @Language("JAVA") val onShow = onRationaleAnnotation

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
                |${SOURCE_PATH}Foo.java:5: Error: Generated onRequestPermissionsResult method not called [NeedOnRequestPermissionsResult]
                |                    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
                |                    ^
                |1 errors, 0 warnings
                """.trimMargin()

        lint()
                .files(
                        java(SOURCE_PATH + "RuntimePermissions.java", runtimePerms),
                        java(SOURCE_PATH + "NeedsPermission.java", onNeeds),
                        java(SOURCE_PATH + "OnShowRationale.java", onShow),
                        java(SOURCE_PATH + "Foo.java", foo))
                .issues(CallOnRequestPermissionsResultDetector.ISSUE)
                .run()
                .expect(expectedText)
                .expectErrorCount(1)
                .expectWarningCount(0)
    }

    @Test
    @Throws(Exception::class)
    fun callOnRequestPermissionsResultDetectorNoErrorForKotlin() {
        @Language("JAVA") val runtimePerms = runtimePermission

        @Language("JAVA") val onNeeds = onNeedsPermission

        @Language("JAVA") val onShow = onRationaleAnnotation

        @Language("kotlin") val foo = """
                package permissions.dispatcher

                @RuntimePermissions
                class Foo : android.app.Activity {
                    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
                        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
                        onRequestPermissionsResult(requestCode, grantResults)
                    }

                    @NeedsPermission("Camera")
                    fun showCamera() {
                    }

                    @OnShowRationale("Camera")
                    fun someMethod() {
                    }
                }
                """.trimMargin()

        @Language("kotlin") val generatedClass = """
                package permissions.dispatcher

                fun Foo.onRequestPermissionsResult(requestCode: Int, grantResults: IntArray) {
                }
                """.trimMargin()

        lint()
                .files(
                        java("src/permissions/dispatcher/RuntimePermissions.java", runtimePerms),
                        java("src/permissions/dispatcher/NeedsPermission.java", onNeeds),
                        java("src/permissions/dispatcher/OnShowRationale.java", onShow),
                        kt("src/permissions/dispatcher/FooPermissionsDispatcher.kt", generatedClass),
                        kt("src/permissions/dispatcher/Foo.kt", foo))
                .issues(CallOnRequestPermissionsResultDetector.ISSUE)
                .run()
                .expectClean()
    }
}
