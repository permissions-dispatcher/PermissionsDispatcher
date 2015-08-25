package permissions.dispatcher.processor

import permissions.dispatcher.*

import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import java.util.*

import java.util.Arrays.asList
import java.util.Arrays.deepEquals
import java.util.Collections.emptyList
import java.util.Collections.singletonList
import kotlin.platform.platformStatic

object Utils {

    fun getAnnotatedClasses(env: RoundEnvironment): List<RuntimePermissionsAnnotatedElement> {
        val models = ArrayList<RuntimePermissionsAnnotatedElement>()
        val elements = env.getElementsAnnotatedWith(javaClass<RuntimePermissions>())
        for (element in elements) {
            models.add(RuntimePermissionsAnnotatedElement(element as TypeElement))
        }
        return models
    }

    fun findMethods(element: Element, clazz: Class<out Annotation>): List<ExecutableElement> {
        val methods = ArrayList<ExecutableElement>()
        for (enclosedElement in element.getEnclosedElements()) {
            val annotation = enclosedElement.getAnnotation(clazz)
            if (annotation != null) {
                methods.add(enclosedElement as ExecutableElement)
            }
        }
        return methods
    }

    jvmStatic fun findShowsRationaleFromValue(value: String, elements: List<ExecutableElement>): ExecutableElement? {
        for (element in elements) {
            val annotation = element.getAnnotation(javaClass<ShowsRationale>())
            if (value == annotation.value) {
                return element
            }
        }
        return null
    }

    jvmStatic fun findShowsRationalesFromValue(value: Array<String>, elements: List<ExecutableElement>): ExecutableElement? {
        for (element in elements) {
            val annotation = element.getAnnotation(javaClass<ShowsRationales>())
            if (deepEquals(value, annotation.value)) {
                return element
            }
        }
        return null
    }

    fun <A : Annotation> getValueFromAnnotation(element: ExecutableElement, clazz: Class<A>): List<String> {
        if (clazz == javaClass<NeedsPermission>()) {
            return listOf(element.getAnnotation(javaClass<NeedsPermission>()).value)
        } else if (clazz == javaClass<NeedsPermissions>()) {
            return asList(*element.getAnnotation(javaClass<NeedsPermissions>()).value)
        } else if (clazz == javaClass<ShowsRationale>()) {
            return listOf(element.getAnnotation(javaClass<ShowsRationale>()).value)
        } else if (clazz == javaClass<ShowsRationales>()) {
            return asList(*element.getAnnotation(javaClass<ShowsRationales>()).value)
        } else {
            return Collections.emptyList<String>()
        }
    }

    fun getPackageName(name: String): String {
        return name.substring(0, name.lastIndexOf("."))
    }

    fun getClassName(name: String): String {
        return name.substring(name.lastIndexOf(".") + 1)
    }

    fun getRequestCodeFieldName(name: String): String {
        return ConstantsProvider.REQUEST_CODE_PREFIX + name.toUpperCase()
    }

    fun getPermissionFieldName(name: String): String {
        return ConstantsProvider.PERMISSION_PREFIX + name.toUpperCase()
    }

    fun isEmpty(collection: Collection<Any>?): Boolean {
        return collection == null || collection.isEmpty()
    }

    fun toString(vararg array: String): String? {
        val max = array.size() - 1
        val b = StringBuilder()
        b.append('{')
        var i = 0
        while (true) {
            b.append("\"").append(array[i]).append("\"")
            if (i == max)
                return b.append('}').toString()
            b.append(", ")
            i++
        }
    }

}
