package permissions.dispatcher

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * This plugin creates a convenient dependency handler
 * that allows a shorthand way to access an "android.jar" file
 * of the environment's platform.
 *
 * It also adds an extension named "androidJar" to the project,
 * with which the default directory to search can be overridden.
 * If no value is specified explicitly, the plugin performs a
 * lookup of the "sdk.dir" property in the user's local.properties
 * file, and derives the Android version to include from the
 * compile SDK version specified in the root-level gradle.properties.
 */
@SuppressWarnings("GroovyUnusedDeclaration")
class AndroidJarDependencyPlugin implements Plugin<Project> {

    private static final EXTENSION_NAME = "androidJar"

    private static final SDK_DIR_PROPERTY = "sdk.dir"
    private static final PLATFORM_DIR_PROPERTY = "COMPILE_SDK_VERSION"
    private static final ANDROID_JAR_FILENAME = "android.jar"

    @Override
    void apply(Project project) {
        def extension = project.extensions.create(EXTENSION_NAME, AndroidJarDependencyExtension)

        project.dependencies.ext.androidJar = {
            def directory = extension.directory
            if (!directory) {
                // By default, concatenate android.jar path from build environment variables
                def sdkDir = loadProperties(project.rootProject.file("local.properties"))
                        .getProperty(SDK_DIR_PROPERTY)
                def platformDir = loadProperties(project.rootProject.file("gradle.properties"))
                        .getProperty(PLATFORM_DIR_PROPERTY)

                directory = "$sdkDir/platforms/$platformDir"
            }

            if (!new File(directory, ANDROID_JAR_FILENAME).exists()) {
                throw new RuntimeException("can't find android.jar in folder '$directory'!")
            }

            def dependency = project.fileTree(dir: directory, includes: [ANDROID_JAR_FILENAME])
            return project.dependencies.create(dependency)
        }
    }

    private static Properties loadProperties(File file) {
        def properties = new Properties()
        file.withReader { properties.load(it) }
        return properties
    }
}
