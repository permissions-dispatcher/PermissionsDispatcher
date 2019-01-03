package permissions.dispatcher.processor.kotlin

import junit.framework.Assert
import java.io.File

fun File.hasSourceEquivalentTo(source: String) {
    Assert.assertEquals(this.readText(), source)
}

fun File.hasSourceEquivalentTo(file: File) {
    Assert.assertEquals(this.readText(), file.readText())
}
