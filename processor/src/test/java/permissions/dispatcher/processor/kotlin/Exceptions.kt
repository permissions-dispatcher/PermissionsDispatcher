package permissions.dispatcher.processor.kotlin

class CompilationWithWarningException(warnings: String) : RuntimeException("Compilation has been done with the following error. \nError: $warnings")
