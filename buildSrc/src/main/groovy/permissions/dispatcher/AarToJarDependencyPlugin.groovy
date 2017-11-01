package permissions.dispatcher

import org.apache.commons.io.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

import java.util.zip.ZipFile

/**
 * Custom plugin for creating "broken" JARs from AAR files.
 *
 * It allows an annotation processor's code generation test
 * to resolve Android-specific classes on the classpath.
 *
 * Of course, the generated JARs aren't able to be loaded in by a ClassLoader,
 * since they reference Android classes absent in a pure-JVM environment.
 * However the generated code will compile and serve as the cornerstone
 * for assertions of an annotation processor that interfaces with Android APIs.
 *
 * To do this, a special configuration is created when the plugin is applied.
 * Any dependency specified in this configuration will be written into
 * a specific resource file, then loaded into a custom ClassLoader when
 * the unit tests are executed. Since AARs are not readable by a pure Java
 * runtime environment, their classes need to be extracted, and converted
 * into the JAR format.
 */
class AarToJarConversionPlugin implements Plugin<Project> {

    private static final CONFIGURATION_NAME = "testCompileAar"
    private static final TASK_NAME = "convertAarsToJar"
    private static final CLASSPATH_FILE_NAME = "additional-test-classpath.txt"

    @Override
    void apply(Project project) {
        // Custom Configuration
        def configuration = project.configurations.create(CONFIGURATION_NAME)

        // Conversion Task
        def conversionTask = project.tasks.create(TASK_NAME, ConvertAarToJarTask) {
            inputFiles = configuration
            jarOutputDir = project.file("$project.buildDir/tmp/converted-aars")
            classpathOutputDir = project.file("$project.buildDir/resources/test")
        }

        // Hook into the task dependency chain
        project.tasks.getByName("classes").dependsOn conversionTask
    }

    static class ConvertAarToJarTask extends DefaultTask {

        @Input
        FileCollection inputFiles

        @OutputDirectory
        File jarOutputDir

        @OutputDirectory
        File classpathOutputDir

        @TaskAction
        def convert() {
            def paths = []

            inputFiles.each { file ->
                // AARs need to be converted to JAR,
                // other files are piped through to the output folder
                def destinationFile

                if (file.name.endsWith(".aar")) {
                    // Classes are contained inside a file named classes.jar,
                    // which resides in an AAR.
                    // Extract & rename it according to the artifact in question,
                    // then copy it over to the output
                    def zipFile = new ZipFile(file)
                    def classesEntry = zipFile.getEntry("classes.jar")
                    def newFileName = file.name.replace(".aar", ".jar")
                    def sourceInputStream = zipFile.getInputStream(classesEntry)

                    destinationFile = new File(jarOutputDir, newFileName)
                    FileUtils.copyInputStreamToFile(sourceInputStream, destinationFile)

                    project.logger.info("Converted AAR '$file.name' to JAR")

                } else {
                    destinationFile = new File(jarOutputDir, file.name)
                    FileUtils.copyFile(file, destinationFile)
                }

                paths += destinationFile.toURI().toURL().toString()
            }

            def classpathFile = new File(classpathOutputDir, CLASSPATH_FILE_NAME)
            classpathFile.withWriter { it.write(paths.join("\n")) }
        }
    }
}
