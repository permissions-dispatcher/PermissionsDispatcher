package permissions.dispatcher.processor

import com.squareup.kotlinpoet.KotlinFile
import javax.lang.model.type.TypeMirror

interface KtProcessorUnit {
    fun getTargetType(): TypeMirror

    fun createKotlinFile(rpe: RuntimePermissionsElement, requestCodeProvider: RequestCodeProvider): KotlinFile
}