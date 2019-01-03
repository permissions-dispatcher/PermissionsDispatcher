package permissions.dispatcher.processor.kotlin

import org.jetbrains.kotlin.cli.common.ExitCode
import java.io.File

data class KotlinCompilerResult(val systemErr: String, val exitCode: ExitCode) {
    fun succeededWithoutWarnings(): Boolean {
        return exitCode == ExitCode.OK
    }

    fun generatedFile(qualifiedName: String): File {
        val kaptSourceDir = File(rootDir, "generated/source/kapt")
        return File(kaptSourceDir, qualifiedName)
    }
}
