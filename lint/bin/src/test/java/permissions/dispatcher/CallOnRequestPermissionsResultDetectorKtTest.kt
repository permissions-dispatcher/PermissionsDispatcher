package permissions.dispatcher

import org.intellij.lang.annotations.Language
import org.junit.Test

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestFiles.kt
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import permissions.dispatcher.Utils.onNeedsPermission
import permissions.dispatcher.Utils.onRationaleAnnotation
import permissions.dispatcher.Utils.runtimePermission

class CallOnRequestPermissionsResultDetectorKtTest {

    @Test
    @Throws(Exception::class)
    fun callOnRequestPermissionsResultDetectorNoErrorForKotlin() {
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
                        java(runtimePermission),
                        java(onNeedsPermission),
                        java(onRationaleAnnotation),
                        kt(generatedClass),
                        kt(foo))
                .issues(CallOnRequestPermissionsResultDetector.ISSUE)
                .run()
                .expectClean()
    }

    @Test
    @Throws(Exception::class)
    fun callOnRequestPermissionsResultDetector() {
        @Language("kotlin") val foo = """
                package permissions.dispatcher

                @RuntimePermissions
                class Foo: android.app.Activity {
                    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
                        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
                    }

                    @NeedsPermission("Camera")
                    fun showCamera() {
                    }

                    @OnShowRationale("Camera")
                    fun someMethod() {
                    }
                }
                """.trimMargin()

        val expectedText = """
                |src/permissions/dispatcher/Foo.kt:5: Error: Generated onRequestPermissionsResult method not called [NeedOnRequestPermissionsResult]
                |                    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
                |                    ^
                |1 errors, 0 warnings
                """.trimMargin()

        lint()
                .files(
                        java(runtimePermission),
                        java(onNeedsPermission),
                        java(onRationaleAnnotation),
                        kt(foo))
                .issues(CallOnRequestPermissionsResultDetector.ISSUE)
                .run()
                .expect(expectedText)
                .expectErrorCount(1)
                .expectWarningCount(0)
    }
}
