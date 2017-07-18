package permissions.dispatcher.processor

import permissions.dispatcher.RuntimePermissions
import permissions.dispatcher.processor.util.findAndValidateProcessorUnit
import permissions.dispatcher.processor.util.getProcessorUnits
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
                    val isKotlin = it.getAnnotation(RuntimePermissions::class.java).isKotlin()
                    val processorUnits = getProcessorUnits(isKotlin)

                    // Find a suitable ProcessorUnit for this element
                    val processorUnit = findAndValidateProcessorUnit(processorUnits, it)

                    // Create a RuntimePermissionsElement for this value
                    val rpe = RuntimePermissionsElement(it as TypeElement)

                    if (isKotlin) {
                        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME] ?: run {
                            processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, "Can't find the target directory for generated Kotlin files.")
                            return false
                        }
//                        val file = File(kaptKotlinGeneratedDir, "testGenerated.kt")
//                        val kotlinFile = processorUnit.createKotlinFile(rpe, requestCodeProvider)
//                        kotlinFile?.writeTo(file)
                    } else {
                        val javaFile = processorUnit.createJavaFile(rpe, requestCodeProvider)
                        javaFile.writeTo(filer)
                    }
               }
        return true
    }
}