package permissions.dispatcher.processor

import com.squareup.javapoet.JavaFile
import permissions.dispatcher.RuntimePermissions
import permissions.dispatcher.processor.impl.ActivityProcessorUnit
import permissions.dispatcher.processor.impl.NativeFragmentProcessorUnit
import permissions.dispatcher.processor.impl.SupportFragmentProcessorUnit
import permissions.dispatcher.processor.util.findAndValidateProcessorUnit
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.Diagnostic
import kotlin.properties.Delegates

/** Element Utilities, obtained from the processing environment */
public var ELEMENT_UTILS: Elements by Delegates.notNull()
/** Type Utilities, obtained from the processing environment */
public var TYPE_UTILS: Types by Delegates.notNull()

class PermissionsProcessor : AbstractProcessor() {

    /* Processing Environment helpers */

    var filer: Filer by Delegates.notNull()
    var messager: Messager by Delegates.notNull()

    /* List of available ProcessorUnits */
    var processorUnits: List<ProcessorUnit> by Delegates.notNull()

    /**
     * Initialization method
     */
    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)

        // Setup helper objects
        filer = processingEnv.getFiler()
        messager = processingEnv.getMessager()
        ELEMENT_UTILS = processingEnv.getElementUtils()
        TYPE_UTILS = processingEnv.getTypeUtils()

        // Setup the list of ProcessorUnits to handle code generation with
        processorUnits = listOf(
                ActivityProcessorUnit(),
                SupportFragmentProcessorUnit(),
                NativeFragmentProcessorUnit()
        )
    }

    override fun getSupportedSourceVersion(): SourceVersion? {
        return SourceVersion.latestSupported()
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return hashSetOf(javaClass<RuntimePermissions>().getCanonicalName())
    }

    /**
     * Main processing method
     */
    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
        roundEnv!!.getElementsAnnotatedWith(javaClass<RuntimePermissions>()).forEach {
            // Find a suitable ProcessorUnit for this element
            val processorUnit = findAndValidateProcessorUnit(processorUnits, it)

            // Create a RuntimePermissionsElement for this value
            val rpe: RuntimePermissionsElement = RuntimePermissionsElement(it as TypeElement)

            // Create a JavaFile for this element and write it out
            val javaFile: JavaFile = processorUnit.createJavaFile(rpe)
            javaFile.writeTo(filer)
        }

        return true
    }
}