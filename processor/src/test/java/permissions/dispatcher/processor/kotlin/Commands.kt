package permissions.dispatcher.processor.kotlin

/** Returns the kotlinc compiler. */
fun kotlinc(): Compiler {
    // TODO: replace File I/O based logic with onMemory FileManager
    val rootDir = createTempDir()
    rootDir.deleteOnExit()
    return Compiler(rootDir)
}
