package permissions.dispatcher.processor.kotlin

import com.google.common.collect.LinkedHashMultimap
import okio.Buffer
import okio.buffer
import okio.sink
import org.apache.commons.io.FileUtils
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import permissions.dispatcher.processor.KtProcessorTestSuite
import permissions.dispatcher.processor.PermissionsProcessor
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectOutputStream
import java.io.PrintStream
import java.net.URLClassLoader
import java.net.URLDecoder
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream
import javax.annotation.processing.Processor
import kotlin.reflect.KClass

class Compiler(private val scratchDir: File) {
    private val sourcesDir = File(scratchDir, "sources")
    private val classesDir = File(scratchDir, "classes")
    private val servicesJar = File(scratchDir, "services.jar")
    private val kaptArgs = mutableMapOf<String, String>()
    private val classpath = mutableListOf<String>()
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

        val fullClasspath = fullClasspath()
        if (fullClasspath.isNotEmpty()) {
            fullArgs.add("-classpath")
            fullArgs.add(fullClasspath.joinToString(separator = ":"))
        }

        fullArgs.add("-no-stdlib")

        for (source in sourcesDir.listFiles()) {
            fullArgs.add(source.toString())
        }

        fullArgs.addAll(annotationProcessorArgs())

        val kaptGeneratedSourcePath = File(scratchDir, "generated/source/kaptKotlin/")
        if (!kaptGeneratedSourcePath.exists()) {
            kaptGeneratedSourcePath.mkdirs()
        }
        kaptArgs[PermissionsProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME] = kaptGeneratedSourcePath.path
        fullArgs.apply {
            add("-P")
            add("plugin:org.jetbrains.kotlin.kapt3:apoptions=${encodeOptions(kaptArgs)}")
        }

        val systemErrBuffer = Buffer()
        val errStream = PrintStream(systemErrBuffer.outputStream())
        val args = fullArgs.toTypedArray()
        val exitCode = K2JVMCompiler().exec(errStream, *args)
        val systemErr = systemErrBuffer.readUtf8()
        return Compilation(systemErr, exitCode, scratchDir)
    }

    /** Returns arguments necessary to enable and configure kapt3. */
    private fun annotationProcessorArgs(): List<String> {
        val kaptSourceDir = File(scratchDir, "kapt/sources")
        val kaptStubsDir = File(scratchDir, "kapt/stubs")
        return listOf(
                "-Xplugin=${kapt3Jar()}",
                "-P", "plugin:org.jetbrains.kotlin.kapt3:sources=$kaptSourceDir",
                "-P", "plugin:org.jetbrains.kotlin.kapt3:classes=$classesDir",
                "-P", "plugin:org.jetbrains.kotlin.kapt3:stubs=$kaptStubsDir",
                "-P", "plugin:org.jetbrains.kotlin.kapt3:apclasspath=$servicesJar",
                "-P", "plugin:org.jetbrains.kotlin.kapt3:aptMode=stubsAndApt",
                "-P", "plugin:org.jetbrains.kotlin.kapt3:correctErrorTypes=true"
        )
    }

    /** Returns the classpath to use when compiling code. */
    private fun fullClasspath(): List<String> {
        val result = mutableListOf<String>()
        result.addAll(classpath)
        // Copy over the classpath of the running application.
        for (classpathFile in classpathFiles()) {
            result.add(classpathFile.toString())
        }
        if (!services.isEmpty) {
            writeServicesJar()
            result.add(servicesJar.toString())
        }
        return result.toList()
    }

    /**
     * Generate a .jar file that holds ServiceManager registrations. Necessary because AutoService's
     * results might not be visible to this test.
     */
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

    /** Returns the files on the host process' classpath. */
    private fun classpathFiles(): List<File> {
        val classLoader = KtProcessorTestSuite::class.java.classLoader
        if (classLoader !is URLClassLoader) {
            throw UnsupportedOperationException("unable to extract classpath from $classLoader")
        }
        val result = mutableListOf<File>()
        for (url in classLoader.urLs) {
            if (url.protocol != "file") {
                throw UnsupportedOperationException("unable to handle classpath element $url")
            }
            if (url.path.endsWith(".aar")) {
                // extract jar file from aar and add it to classpath
                val zipFile = ZipFile(url.path)
                val sourceInputStream = zipFile.getInputStream(zipFile.getEntry("classes.jar"))
                val newFileName = url.path.replace(".aar", ".jar")
                val destinationFile = File(File(scratchDir, "unzippedAar"), newFileName)
                FileUtils.copyInputStreamToFile(sourceInputStream, destinationFile)
                result.add(destinationFile)
            } else {
                result.add(File(URLDecoder.decode(url.path, "UTF-8")))
            }
        }
        return result.toList()
    }

    /** Returns the path to the kotlin-annotation-processing .jar file. */
    private fun kapt3Jar(): File {
        for (file in classpathFiles()) {
            if (file.name.startsWith("kotlin-annotation-processing-embeddable")) return file
        }
        throw IllegalStateException("no kotlin-annotation-processing-embeddable jar on classpath:\n  " +
                "${classpathFiles().joinToString(separator = "\n  ")}}")
    }

    /**
     * Base64 encodes a mapping of annotation processor args for kapt, as specified by
     * https://kotlinlang.org/docs/reference/kapt.html#apjavac-options-encoding
     */
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

