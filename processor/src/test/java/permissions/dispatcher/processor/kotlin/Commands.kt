package permissions.dispatcher.processor.kotlin

import java.io.File

/** Returns the kotlinc compiler. */
fun kotlinc(rootDir: File = createTempDir()): Compiler {
    rootDir.deleteOnExit() // TODO: replace File I/O based logic with onMemory FileManager
    return Compiler(rootDir)
}
