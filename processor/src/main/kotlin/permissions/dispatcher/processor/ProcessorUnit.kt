package permissions.dispatcher.processor

import com.squareup.javapoet.JavaFile
import javax.lang.model.type.TypeMirror

interface ProcessorUnit {

    fun checkSupported(): Boolean

    fun getTargetType(): TypeMirror

    fun createJavaFile(rpe: RuntimePermissionsElement): JavaFile
}