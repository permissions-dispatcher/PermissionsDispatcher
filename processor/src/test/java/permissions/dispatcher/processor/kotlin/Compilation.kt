package permissions.dispatcher.processor.kotlin

import org.hamcrest.CoreMatchers.containsString
import org.jetbrains.kotlin.cli.common.ExitCode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import java.io.File

class Compilation(val error: String, val exitCode: ExitCode, private val generatedKtDir: File) {
    fun succeeded() = apply {
        if (exitCode != ExitCode.OK) {
            throw CompilationFailureException(exitCode)
        }
    }

    fun succeededWithoutWarnings() = apply {
        succeeded()
        if (error.isNotEmpty()) {
            throw CompilationWithWarningException(error)
        }
    }

    fun failed() = apply {
        assertEquals(ExitCode.COMPILATION_ERROR, exitCode)
    }

    fun withErrorContaining(message: String) {
        assertThat(error, containsString(message))
    }

    fun generatedFile(qualifiedName: String): File {
        return File(generatedKtDir, qualifiedName)
    }
}
