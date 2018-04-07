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

        @Language("JAVA") val foo = (""
                + "package permissions.dispatcher;\n"
                + "@RuntimePermissions\n"
                + "public class Foo extends android.app.Activity {\n"
                + "public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {\n"
                + "super.onRequestPermissionsResult(requestCode, permissions, grantResults);\n"
                + "FooPermissionsDispatcher.onRequestPermissionsResult(requestCode, grantResults);\n"
                + "}\n"
                + "@NeedsPermission(\"Camera\")"
                + "public void showCamera() {"
                + "}\n"
                + "@OnShowRationale(\"Camera\")"
                + "public void someMethod() {"
                + "}\n"
                + "}")

        @Language("JAVA") val generatedClass = (""
                + "package permissions.dispatcher;\n"
                + "public class FooPermissionsDispatcher {\n"
                + "public static void onRequestPermissionsResult(int requestCode, int[] grantResults) {\n"
                + "}\n"
                + "}")

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

        @Language("JAVA") val foo = (""
                + "package permissions.dispatcher;\n"
                + "@RuntimePermissions\n"
                + "public class Foo extends android.app.Activity {\n"
                + "public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {\n"
                + "super.onRequestPermissionsResult(requestCode, permissions, grantResults);\n"
                + "}\n"
                + "@NeedsPermission(\"Camera\")"
                + "public void showCamera() {"
                + "}\n"
                + "@OnShowRationale(\"Camera\")"
                + "public void someMethod() {"
                + "}\n"
                + "}")

        val expectedText = (""
                + SOURCE_PATH + "Foo.java:4: Error: Generated onRequestPermissionsResult method not called [NeedOnRequestPermissionsResult]\n"
                + "public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {\n"
                + "^\n"
                + "1 errors, 0 warnings\n")

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

        @Language("kotlin") val foo = (""
                + "package permissions.dispatcher\n"
                + "@RuntimePermissions\n"
                + "class Foo : android.app.Activity {\n"
                + "fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {\n"
                + "super.onRequestPermissionsResult(requestCode, permissions, grantResults)\n"
                + "onRequestPermissionsResult(requestCode, grantResults)\n"
                + "}\n"
                + "@NeedsPermission(\"Camera\")"
                + "fun showCamera() {"
                + "}\n"
                + "@OnShowRationale(\"Camera\")"
                + "fun someMethod() {"
                + "}\n"
                + "}")

        @Language("kotlin") val generatedClass = (""
                + "package permissions.dispatcher\n"
                + "fun Foo.onRequestPermissionsResult(requestCode: Int, grantResults: IntArray) {"
                + "}")

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
