package permissions.dispatcher.processor

import org.hamcrest.CoreMatchers.containsString
import org.intellij.lang.annotations.Language
import org.jetbrains.kotlin.cli.common.ExitCode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import permissions.dispatcher.processor.kotlin.KotlinCompilerCall
import javax.annotation.processing.Processor

// TODO: Add regular test cases somehow!
class KtProcessorTestSuite {
    @Rule
    @JvmField
    var temporaryFolder: TemporaryFolder = TemporaryFolder()

    @Test
    fun noPermissionActivity() {
        val call = KotlinCompilerCall(temporaryFolder.root)
        call.inheritClasspath = true
        call.addService(Processor::class, PermissionsProcessor::class)
        @Language("kotlin") val source = """
        import android.Manifest
        import android.app.Activity
        import permissions.dispatcher.RuntimePermissions

        @RuntimePermissions
        class MyActivity: Activity {
        }
        """.trimMargin()
        call.addKt(source = source)

        val result = call.execute()
        assertEquals(result.exitCode, ExitCode.COMPILATION_ERROR)
        assertThat(result.systemErr, containsString("Annotated class 'MyActivity' doesn't have any method annotated with '@NeedsPermission'"))
    }
}
