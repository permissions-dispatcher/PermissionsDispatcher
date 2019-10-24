package permissions.dispatcher.processor

import permissions.dispatcher.RuntimePermissions
import permissions.dispatcher.processor.impl.java.JavaActivityProcessorUnit
import permissions.dispatcher.processor.impl.java.JavaFragmentProcessorUnit
import permissions.dispatcher.processor.impl.kotlin.KotlinActivityProcessorUnit
import permissions.dispatcher.processor.impl.kotlin.KotlinFragmentProcessorUnit
import permissions.dispatcher.processor.util.findAndValidateProcessorUnit
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import kotlin.properties.Delegates

/** Element Utilities, obtained from the processing environment */
var ELEMENT_UTILS: Elements by Delegates.notNull()
/** Type Utilities, obtained from the processing environment */
var TYPE_UTILS: Types by Delegates.notNull()

class PermissionsProcessor : AbstractProcessor() {
    private val javaProcessorUnits = listOf(JavaActivityProcessorUnit(), JavaFragmentProcessorUnit())
    private val kotlinProcessorUnits = listOf(KotlinActivityProcessorUnit(), KotlinFragmentProcessorUnit())
    /* Processing Environment helpers */
    private var filer: Filer by Delegates.notNull()

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        filer = processingEnv.filer
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
                    val rpe = RuntimePermissionsElement(it as TypeElement)
                    val kotlinMetadata = it.getAnnotation(Metadata::class.java)
                    if (kotlinMetadata != null) {
                        processKotlin(it, rpe, requestCodeProvider)
                    } else {
                        processJava(it, rpe, requestCodeProvider)
                    }
                }
        return true
    }

    private fun processKotlin(element: Element, rpe: RuntimePermissionsElement, requestCodeProvider: RequestCodeProvider) {
        val processorUnit = findAndValidateProcessorUnit(kotlinProcessorUnits, element)
        val kotlinFile = processorUnit.createFile(rpe, requestCodeProvider)
        kotlinFile.writeTo(filer)
    }

    private fun processJava(element: Element, rpe: RuntimePermissionsElement, requestCodeProvider: RequestCodeProvider) {
        val processorUnit = findAndValidateProcessorUnit(javaProcessorUnits, element)
        val javaFile = processorUnit.createFile(rpe, requestCodeProvider)
        javaFile.writeTo(filer)
    }
}
