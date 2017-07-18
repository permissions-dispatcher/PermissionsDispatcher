package permissions.dispatcher.processor

import permissions.dispatcher.RuntimePermissions
import permissions.dispatcher.processor.impl.java.ActivityProcessorUnit
import permissions.dispatcher.processor.impl.java.NativeFragmentProcessorUnit
import permissions.dispatcher.processor.impl.java.SupportFragmentProcessorUnit
import permissions.dispatcher.processor.impl.kotlin.ActivityKtProcessorUnit
import permissions.dispatcher.processor.impl.kotlin.NativeFragmentKtProcessorUnit
import permissions.dispatcher.processor.impl.kotlin.SupportFragmentKtProcessorUnit
import permissions.dispatcher.processor.util.findAndValidateKtProcessorUnit
import permissions.dispatcher.processor.util.findAndValidateProcessorUnit
import permissions.dispatcher.processor.util.isKotlin
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.Diagnostic
import kotlin.properties.Delegates

/** Element Utilities, obtained from the processing environment */
var ELEMENT_UTILS: Elements by Delegates.notNull()
/** Type Utilities, obtained from the processing environment */
var TYPE_UTILS: Types by Delegates.notNull()

@SupportedOptions(PermissionsProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class PermissionsProcessor : AbstractProcessor() {

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    /* Processing Environment helpers */
    var filer: Filer by Delegates.notNull()
    var messager: Messager by Delegates.notNull()

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        filer = processingEnv.filer
        messager = processingEnv.messager
        ELEMENT_UTILS = processingEnv.elementUtils
        TYPE_UTILS = processingEnv.typeUtils
    }

    override fun getSupportedSourceVersion(): SourceVersion? {
        return SourceVersion.latestSupported()
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        return hashSetOf(RuntimePermissions::class.java.canonicalName)
    }

    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
        // Create a RequestCodeProvider which guarantees unique request codes for each permission request
        val requestCodeProvider = RequestCodeProvider()

        // The Set of annotated elements needs to be ordered
        // in order to achieve Deterministic, Reproducible Builds
        roundEnv.getElementsAnnotatedWith(RuntimePermissions::class.java)
                .sortedBy { it.simpleName.toString() }
                .forEach {
                    // Create a RuntimePermissionsElement for this value
                    val rpe = RuntimePermissionsElement(it as TypeElement)
                    val isKotlin = it.getAnnotation(RuntimePermissions::class.java).isKotlin()
                    if (isKotlin) {
                        val processorUnits = listOf(ActivityKtProcessorUnit(), SupportFragmentKtProcessorUnit(), NativeFragmentKtProcessorUnit())
                        // Find a suitable ProcessorUnit for this element
                        val processorUnit = findAndValidateKtProcessorUnit(processorUnits, it)

                        // refer to official sample but seems there's more clever way...
                        // https://github.com/JetBrains/kotlin-examples/blob/master/gradle/kotlin-code-generation/annotation-processor/src/main/java/TestAnnotationProcessor.kt#L26
                        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME] ?: run {
                            processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, "Can't find the target directory for generated Kotlin files.")
                            return false
                        }

                        // FIXME: KAPT_KOTLIN_GENERATED_OPTION_NAME creates source code under kaptKotlin dir
                        // but it seems the dir is not managed by build variants so it will cause compile error
                        // this is pretty much ad hoc address...
                        val kaptDir = kaptKotlinGeneratedDir.replace("kaptKotlin", "kapt")
                        val file = File(kaptDir)
                        if (!file.parentFile.exists()) {
                            file.parentFile.mkdirs()
                        }
                        val kotlinFile = processorUnit.createKotlinFile(rpe, requestCodeProvider)
                        kotlinFile.writeTo(file)
                    } else {
                        val processorUnits = listOf(ActivityProcessorUnit(), SupportFragmentProcessorUnit(), NativeFragmentProcessorUnit())
                        // Find a suitable ProcessorUnit for this element
                        val processorUnit = findAndValidateProcessorUnit(processorUnits, it)
                        val javaFile = processorUnit.createJavaFile(rpe, requestCodeProvider)
                        javaFile.writeTo(filer)
                    }
               }
        return true
    }
}