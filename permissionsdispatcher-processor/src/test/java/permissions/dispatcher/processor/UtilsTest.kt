package permissions.dispatcher.processor

import org.junit.Test
import org.mockito.Mockito

import javax.lang.model.element.ExecutableElement
import java.util.Collections

import org.assertj.core.api.Assertions.assertThat

/**
 * Unit test for [Utils].
 */
public class UtilsTest {

    Test
    public fun testFindShowsRationaleFromValue() {
        val list = emptyList<ExecutableElement>()
        assertThat<ExecutableElement>(Utils.findShowsRationaleFromValue("test", list)).isNull()
    }

    Test
    public fun testFindShowsRationalesFromValue() {
        val list = emptyList<ExecutableElement>()
        assertThat(findShowsRationalesFromValue(arrayOf("test"), list)).isNull()
    }

    Test
    public fun testGetValueFromAnnotation() {
        val element = Mockito.mock(javaClass<ExecutableElement>())
        assertThat(getValueFromAnnotation(element, javaClass<Annotation>())).isEmpty()
    }

    Test
    public fun testGetPackageName() {
        run {
            val name = "permissions.dispatcher.Activity"
            assertThat(getPackageName(name)).isEqualTo("permissions.dispatcher")
        }
        run {
            val name = "..."
            assertThat(getPackageName(name)).isEqualTo("..")
        }
    }

    Test
    public fun testGetClassName() {
        run {
            val name = "permissions.dispatcher.Activity"
            assertThat(getClassName(name)).isEqualTo("Activity")
        }
        run {
            val name = "Activity"
            assertThat(getClassName(name)).isEqualTo("Activity")
        }
        run {
            val name = "..."
            assertThat(getClassName(name)).isEmpty()
        }
    }

    Test
    public fun testGetFieldName() {
        run {
            val name = "activity"
            assertThat(getRequestCodeFieldName(name)).isEqualTo("REQUEST_ACTIVITY")
        }
        run {
            val name = "FRAGMENT"
            assertThat(getRequestCodeFieldName(name)).isEqualTo("REQUEST_FRAGMENT")
        }
    }

    Test
    public fun testGetIsEmpty() {
        run { assertThat(isEmpty(null)).isTrue() }
        run { assertThat(isEmpty(emptyList<Any>())).isTrue() }
        run { assertThat(isEmpty(listOf("android"))).isFalse() }
    }

    Test
    public fun testToString() {
        run { //noinspection NullArgumentToVariableArgMethod
            assertThat(Utils.toString(null)).isNull()
        }
        run { assertThat(Utils.toString(*arrayOf(""))).isEqualTo("{\"\"}") }
        run { assertThat(Utils.toString(*arrayOf("android"))).isEqualTo("{\"android\"}") }
        run { assertThat(Utils.toString(*arrayOf("android", "ios"))).isEqualTo("{\"android\", \"ios\"}") }
    }

}
