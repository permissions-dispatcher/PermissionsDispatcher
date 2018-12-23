package permissions.dispatcher.processor.kotlin

import org.jetbrains.kotlin.cli.common.ExitCode

data class KotlinCompilerResult(val systemErr: String, var exitCode: ExitCode)