package permissions.dispatcher.processor.exception

import com.squareup.javapoet.TypeName
import javax.lang.model.type.TypeMirror

class WrongClassException(type: TypeMirror) : RuntimeException("Class '${TypeName.get(type).toString()}' can't be annotated with '@RuntimePermissions'")