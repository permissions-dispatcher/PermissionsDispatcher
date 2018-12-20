package permissions.dispatcher.processor

import com.squareup.javapoet.JavaFile
import com.squareup.kotlinpoet.FileSpec
import javax.lang.model.type.TypeMirror

interface ProcessorUnit<out K> {
    fun getTargetType(): TypeMirror
    /**
     * Creates the File for the provided @RuntimePermissions element.
     * <p>
     * This will delegate to other methods that compose generated code.
     */
    fun createFile(rpe: RuntimePermissionsElement, requestCodeProvider: RequestCodeProvider): K
}

interface JavaProcessorUnit : ProcessorUnit<JavaFile>
interface KtProcessorUnit : ProcessorUnit<FileSpec>
