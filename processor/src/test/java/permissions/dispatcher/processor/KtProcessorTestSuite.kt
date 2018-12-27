package permissions.dispatcher.processor

import org.intellij.lang.annotations.Language
import org.jetbrains.kotlin.cli.common.ExitCode
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import permissions.dispatcher.processor.kotlin.KotlinCompilerCall
import javax.annotation.processing.Processor

class KtProcessorTestSuite {
    @Rule
    @JvmField
    var temporaryFolder: TemporaryFolder = TemporaryFolder()

    @Test
    fun privateConstructor() {
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
        call.addKt("source.kt", source)

        val result = call.execute()
        assertEquals(result.exitCode, ExitCode.COMPILATION_ERROR)
        assertEquals(result.systemErr, "constructor is not internal or public")
    }
}
