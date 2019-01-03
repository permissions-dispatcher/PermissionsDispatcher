package permissions.dispatcher.processor.kotlin

import org.jetbrains.kotlin.cli.common.ExitCode

class CompilationFailureException(code: ExitCode) : RuntimeException("Compilation has failed for an unknown reason. ExitCode: ${code.name}")

class CompilationWithWarningException(warnings: String) : RuntimeException("Compilation has done with the following warnings. \nwarnings: $warnings")
