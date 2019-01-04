package permissions.dispatcher.processor.kotlin

import org.junit.ComparisonFailure
import java.io.File

class SuccessfulCompilationClause(private val generatedKtDir: File) {
    fun generatedFile(qualifiedName: String): FileClause {
        return FileClause(File(generatedKtDir, qualifiedName))
    }
}

class FileClause(private val generatedFile: File) {
    fun hasSourceEquivalentTo(expected: String) {
        val actual = generatedFile.readText()
        if (generatedFile.readText() != expected) {
            throw ComparisonFailure(null, expected, actual)
        }
    }

    fun hasSourceEquivalentTo(expected: Array<String>) {
        return hasSourceEquivalentTo(expected.joinToString(separator = "\n"))
    }

    fun hasSourceEquivalentTo(expected: File) {
        return hasSourceEquivalentTo(expected.readText())
    }
}

class UnsuccessfulCompilationClause(private val error: String) {
    fun withErrorContaining(message: String) {
        if (!error.contains(message)) {
            throw ComparisonFailure(null, message, error)
        }
    }
}
