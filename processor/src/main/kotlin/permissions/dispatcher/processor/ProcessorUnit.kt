package permissions.dispatcher.processor

import com.squareup.javapoet.JavaFile
import com.squareup.kotlinpoet.KotlinFile
import javax.lang.model.type.TypeMirror

interface ProcessorUnit {

    fun getTargetType(): TypeMirror

    fun createJavaFile(rpe: RuntimePermissionsElement, requestCodeProvider: RequestCodeProvider): JavaFile

    fun createKotlinFile(rpe: RuntimePermissionsElement, requestCodeProvider: RequestCodeProvider): KotlinFile? {
        return null
    }
}