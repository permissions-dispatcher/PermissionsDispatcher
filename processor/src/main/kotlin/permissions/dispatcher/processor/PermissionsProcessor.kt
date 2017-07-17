package permissions.dispatcher.processor

import permissions.dispatcher.RuntimePermissions
import permissions.dispatcher.processor.impl.ActivityProcessorUnit
import permissions.dispatcher.processor.impl.NativeFragmentProcessorUnit
import permissions.dispatcher.processor.impl.SupportFragmentProcessorUnit
import permissions.dispatcher.processor.util.findAndValidateProcessorUnit
import permissions.dispatcher.processor.util.getProcessorUnits
import permissions.dispatcher.processor.util.isKotlin
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import kotlin.properties.Delegates

/** Element Utilities, obtained from the processing environment */
var ELEMENT_UTILS: Elements by Delegates.notNull()
/** Type Utilities, obtained from the processing environment */
var TYPE_UTILS: Types by Delegates.notNull()

class PermissionsProcessor : AbstractProcessor() {

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

    /**
     * Main processing method
     */
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

                    // Create a JavaFile for this element and write it out
                    val javaFile = processorUnit.createJavaFile(rpe, requestCodeProvider)
                    javaFile.writeTo(filer)
                }

        return true
    }
}