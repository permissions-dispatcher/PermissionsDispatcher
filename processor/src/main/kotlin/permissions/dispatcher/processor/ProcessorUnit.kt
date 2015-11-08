package permissions.dispatcher.processor

import com.squareup.javapoet.JavaFile
import javax.lang.model.type.TypeMirror

interface ProcessorUnit {

    fun getTargetType(): TypeMirror

    fun createJavaFile(rpe: RuntimePermissionsElement, requestCodeProvider: RequestCodeProvider): JavaFile
}