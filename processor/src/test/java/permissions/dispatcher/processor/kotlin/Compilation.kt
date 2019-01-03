package permissions.dispatcher.processor.kotlin

import org.jetbrains.kotlin.cli.common.ExitCode
import java.io.File

class Compilation(val warnings: String, val exitCode: ExitCode, private val rootDir: File) {
    fun succeeded() = apply {
        if (exitCode != ExitCode.OK) {
            throw CompilationFailureException(exitCode)
        }
    }

    fun succeededWithoutWarnings() = apply {
        succeeded()
        if (warnings.isNotEmpty()) {
            throw CompilationWithWarningException(warnings)
        }
    }

    fun failed() = apply {
//        if (exitCode != ExitCode.OK) {
//            throw CompilationFailureException(exitCode)
//        }
    }

    fun generatedFile(qualifiedName: String): File {
        val kaptSourceDir = File(rootDir, "generated/source/kapt")
        return File(kaptSourceDir, qualifiedName)
    }
}
