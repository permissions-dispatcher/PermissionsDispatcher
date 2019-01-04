package permissions.dispatcher.processor.kotlin

import org.jetbrains.kotlin.cli.common.ExitCode
import org.junit.ComparisonFailure
import java.io.File

class Compilation(val error: String, val exitCode: ExitCode, private val generatedKtDir: File) {
    fun succeeded(): SuccessfulCompilationClause {
        if (exitCode != ExitCode.OK) {
            throw ComparisonFailure(null, ExitCode.OK.name, exitCode.name)
        }
        return SuccessfulCompilationClause(generatedKtDir)
    }

    fun succeededWithoutWarnings(): SuccessfulCompilationClause {
        if (error.isNotEmpty()) {
            throw CompilationWithWarningException(error)
        }
        return succeeded()
    }

    fun failed(): UnsuccessfulCompilationClause {
        if (exitCode == ExitCode.OK) {
            throw ComparisonFailure(null, ExitCode.COMPILATION_ERROR.name, exitCode.name)
        }
        return UnsuccessfulCompilationClause(error)
    }
}
