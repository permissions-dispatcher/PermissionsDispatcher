package permissions.dispatcher.processor

import permissions.dispatcher.RuntimePermissions
import permissions.dispatcher.processor.impl.javaProcessorUnits
import permissions.dispatcher.processor.impl.kotlinProcessorUnits
import permissions.dispatcher.processor.util.findAndValidateProcessorUnit
import permissions.dispatcher.processor.util.kotlinMetadataClass
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
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
        // processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME] returns generated/source/kaptKotlin/$sourceSetName
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
                    val rpe = RuntimePermissionsElement(it as TypeElement)
                    val kotlinMetadata = it.getAnnotation(kotlinMetadataClass)
                    if (kotlinMetadata != null) {
                        processKotlin(it, rpe, requestCodeProvider)
                    } else {
                        processJava(it, rpe, requestCodeProvider)
                    }
               }
        return true
    }

    private fun processKotlin(element: Element, rpe: RuntimePermissionsElement, requestCodeProvider: RequestCodeProvider) {
        // FIXME: weirdly under kaptKotlin files is not recognized as source file on AS or IntelliJ
        // so as a workaround we generate .kt file in generated/source/kapt/$sourceSetName
        // ref: https://github.com/hotchemi/PermissionsDispatcher/issues/320#issuecomment-316175775
        val kaptGeneratedDirPath = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]?.replace("kaptKotlin", "kapt") ?: run {
            processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, "Can't find the target directory for generated Kotlin files.")
            return
        }

        val kaptGeneratedDir = File(kaptGeneratedDirPath)
        if (!kaptGeneratedDir.parentFile.exists()) {
            kaptGeneratedDir.parentFile.mkdirs()
        }

        val processorUnit = findAndValidateProcessorUnit(kotlinProcessorUnits(messager), element)
        val kotlinFile = processorUnit.createFile(rpe, requestCodeProvider)
        kotlinFile.writeTo(kaptGeneratedDir)
    }

    private fun processJava(element: Element, rpe: RuntimePermissionsElement, requestCodeProvider: RequestCodeProvider) {
        val processorUnit = findAndValidateProcessorUnit(javaProcessorUnits(messager), element)
        val javaFile = processorUnit.createFile(rpe, requestCodeProvider)
        javaFile.writeTo(filer)
    }
}
