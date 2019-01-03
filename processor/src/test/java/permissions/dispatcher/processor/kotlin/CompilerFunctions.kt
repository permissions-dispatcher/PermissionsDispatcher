package permissions.dispatcher.processor.kotlin

import junit.framework.Assert.assertEquals
import org.junit.rules.TemporaryFolder
import permissions.dispatcher.processor.PermissionsProcessor
import java.io.File
import javax.annotation.processing.Processor
import kotlin.reflect.KClass

internal val rootDir = TemporaryFolder().root

fun kotlinc(): KotlinCompilerCall {
    return KotlinCompilerCall(rootDir)
}

fun KotlinCompilerCall.withProcessors(processor: KClass<out Processor> = PermissionsProcessor::class): KotlinCompilerCall {
    return apply {
        services.put(Processor::class, processor)
    }
}

fun KotlinCompilerCall.compile(ktFile: KtFile): KotlinCompilerCall {
    return apply {
        addKt(ktFile.fullyQualifiedName, ktFile.source)
    }
}

fun File.hasSourceEquivalentTo(source: String) {
    assertEquals(this.readText(), source)
}

fun File.hasSourceEquivalentTo(file: File) {
    assertEquals(this.readText(), file.readText())
}
