package permissions.dispatcher.processor.kotlin

import junit.framework.Assert.assertEquals
import java.io.File

fun File.hasSourceEquivalentTo(source: String) {
    assertEquals(this.readText(), source)
}

fun File.hasSourceEquivalentTo(file: File) {
    assertEquals(this.readText(), file.readText())
}
