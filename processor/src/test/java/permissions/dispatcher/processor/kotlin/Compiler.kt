package permissions.dispatcher.processor.kotlin

import com.google.common.collect.LinkedHashMultimap
import okio.Buffer
import okio.buffer
import okio.sink
import org.apache.commons.io.FileUtils
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectOutputStream
import java.io.PrintStream
import java.net.URL
import java.net.URLClassLoader
import java.net.URLDecoder
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream
import javax.annotation.processing.Processor
import kotlin.reflect.KClass

class Compiler(private val rootDir: File) {
    private val sourcesDir = File(rootDir, "sources")
    private val classesDir = File(rootDir, "classes")
    private val servicesJar = File(rootDir, "services.jar")
    private val services = LinkedHashMultimap.create<KClass<*>, KClass<*>>()!!

    /** Adds a source file to be compiled. */
    fun addKtFile(fileName: String, source: String) = apply {
        val sourceFile = File(sourcesDir, fileName)
        sourceFile.parentFile.mkdirs()
        sourceFile.sink().buffer().use {
            it.writeUtf8(source)
        }
    }

    /** Adds a service like an annotation processor to make available to the compiler. */
    fun withProcessors(vararg processors: Processor) = apply {
        processors.forEach {
            services.put(Processor::class, it::class)
        }
    }

    fun compile(): Compilation {
        val fullArgs = mutableListOf<String>()
        fullArgs.add("-d")
        fullArgs.add(classesDir.toString())
        fullArgs.add("-no-stdlib")

        val fullClasspath = fullClasspath()
        fullArgs.add("-classpath")
        fullArgs.add(fullClasspath.joinToString(separator = ":"))

        sourcesDir.listFiles().forEach {
            fullArgs.add(it.toString())
        }
        fullArgs.addAll(annotationProcessorArgs())

        val kaptArgs = mutableMapOf<String, String>()
        val generatedKtDir = File(rootDir, "generatedKt")
        kaptArgs["kapt.kotlin.generated"] = generatedKtDir.path
        fullArgs.add("-P")
        fullArgs.add("plugin:org.jetbrains.kotlin.kapt3:apoptions=${encodeOptions(kaptArgs)}")

        val systemErrBuffer = Buffer()
        val errStream = PrintStream(systemErrBuffer.outputStream())
        val args = fullArgs.toTypedArray()
        val exitCode = K2JVMCompiler().exec(errStream, *args)
        return Compilation(error = systemErrBuffer.readUtf8(), exitCode = exitCode, generatedKtDir = generatedKtDir)
    }

    private fun annotationProcessorArgs(): List<String> {
        val sourceDir = File(rootDir, "kapt/sources")
        val stubsDir = File(rootDir, "kapt/stubs")
        val plugin = kapt3Jar()
        return listOf(
                "-Xplugin=$plugin",
                "-P", "plugin:org.jetbrains.kotlin.kapt3:sources=$sourceDir",
                "-P", "plugin:org.jetbrains.kotlin.kapt3:classes=$classesDir",
                "-P", "plugin:org.jetbrains.kotlin.kapt3:stubs=$stubsDir",
                "-P", "plugin:org.jetbrains.kotlin.kapt3:apclasspath=$servicesJar",
                "-P", "plugin:org.jetbrains.kotlin.kapt3:aptMode=stubsAndApt",
                "-P", "plugin:org.jetbrains.kotlin.kapt3:correctErrorTypes=true"
        )
    }

    private fun fullClasspath(): List<String> {
        val result = mutableListOf<String>()
        classpathFiles().forEach {
            result.add(it.toString())
        }
        if (!services.isEmpty) {
            writeServicesJar()
            result.add(servicesJar.toString())
        }
        return result.toList()
    }

    private fun writeServicesJar() {
        ZipOutputStream(FileOutputStream(servicesJar)).use { zipOutputStream ->
            for (entry in services.asMap()) {
                zipOutputStream.putNextEntry(
                        ZipEntry("META-INF/services/${entry.key.qualifiedName}"))
                val serviceFile = zipOutputStream.sink().buffer()
                for (implementation in entry.value) {
                    serviceFile.writeUtf8(implementation.qualifiedName!!)
                    serviceFile.writeUtf8("\n")
                }
                serviceFile.emit() // Don't close the entry; that closes the file.
                zipOutputStream.closeEntry()
            }
        }
    }

    private fun classpathFiles(): List<File> {
        val classLoader = javaClass.classLoader
        if (classLoader !is URLClassLoader) {
            throw UnsupportedOperationException("unable to extract classpath from $classLoader")
        }
        val result = mutableListOf<File>()
        for (url in classLoader.urLs) {
            if (url.protocol != "file") {
                throw UnsupportedOperationException("unable to handle classpath element $url")
            }
            if (url.path.endsWith(".aar")) {
                result.add(extractjarFromAar(url))
            } else {
                result.add(File(URLDecoder.decode(url.path, "UTF-8")))
            }
        }
        return result.toList()
    }

    /** extract jar file from aar and add it to classpath */
    private fun extractjarFromAar(url: URL): File {
        val zipFile = ZipFile(url.path)
        val sourceInputStream = zipFile.getInputStream(zipFile.getEntry("classes.jar"))
        val newFileName = url.path.replace(".aar", ".jar")
        val jarFile = File(File(rootDir, "unzippedAar"), newFileName)
        FileUtils.copyInputStreamToFile(sourceInputStream, jarFile)
        return jarFile
    }

    private fun kapt3Jar(): File {
        for (file in classpathFiles()) {
            if (file.name.startsWith("kotlin-annotation-processing-embeddable")) return file
        }
        throw IllegalStateException("no kotlin-annotation-processing-embeddable jar on classpath:\n  " +
                "${classpathFiles().joinToString(separator = "\n  ")}}")
    }

    private fun encodeOptions(options: Map<String, String>): String {
        val buffer = Buffer()
        ObjectOutputStream(buffer.outputStream()).use {
            it.writeInt(options.size)
            for ((key, value) in options.entries) {
                it.writeUTF(key)
                it.writeUTF(value)
            }
        }
        return buffer.readByteString().base64()
    }
}
