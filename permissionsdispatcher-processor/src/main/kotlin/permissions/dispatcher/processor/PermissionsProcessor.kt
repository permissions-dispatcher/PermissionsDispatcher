package permissions.dispatcher.processor

import com.google.auto.service.AutoService
import com.google.common.collect.ImmutableSet
import com.squareup.javapoet.JavaFile

import java.io.IOException

import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

import permissions.dispatcher.RuntimePermissions

import permissions.dispatcher.processor.JavaFileBuilder.createJavaFile
import permissions.dispatcher.processor.Utils.getAnnotatedClasses

AutoService(Processor::class)
public class PermissionsProcessor : AbstractProcessor() {

    private var filer: Filer? = null

    private var messager: Messager? = null

    override fun getSupportedAnnotationTypes(): Set<String> {
        return ImmutableSet.of(javaClass<RuntimePermissions>().getCanonicalName())
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    synchronized override fun init(env: ProcessingEnvironment) {
        super.init(env)
        this.filer = env.getFiler()
        this.messager = env.getMessager()
    }

    override fun process(annotations: Set<TypeElement>, env: RoundEnvironment): Boolean {
        val classes = getAnnotatedClasses(env)
        for (clazz in classes) {
            val javaFile = createJavaFile(clazz)
            try {
                javaFile.writeTo(filer)
            } catch (e: IOException) {
                messager!!.printMessage(Diagnostic.Kind.ERROR, e.getMessage())
            }

        }
        return true
    }

}
