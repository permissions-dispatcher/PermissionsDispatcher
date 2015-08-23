package permissions.dispatcher.processor;

import org.junit.Test;
import org.mockito.Mockito;

import javax.lang.model.element.ExecutableElement;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static permissions.dispatcher.processor.Utils.*;

/**
 * Unit test for {@link Utils}.
 */
public class UtilsTest {

    @Test
    public void testFindShowsRationaleFromValue() {
        List<ExecutableElement> list = Collections.emptyList();
        assertThat(findShowsRationaleFromValue("test", list)).isNull();
    }

    @Test
    public void testFindShowsRationalesFromValue() {
        List<ExecutableElement> list = Collections.emptyList();
        assertThat(findShowsRationalesFromValue(new String[]{"test"}, list)).isNull();
    }

    @Test
    public void testGetValueFromAnnotation() {
        ExecutableElement element = Mockito.mock(ExecutableElement.class);
        assertThat(getValueFromAnnotation(element, Annotation.class)).isEmpty();
    }

    @Test
    public void testGetPackageName() {
        {
            String name = "permissions.dispatcher.Activity";
            assertThat(getPackageName(name)).isEqualTo("permissions.dispatcher");
        }
        {
            String name = "...";
            assertThat(getPackageName(name)).isEqualTo("..");
        }
    }

    @Test
    public void testGetClassName() {
        {
            String name = "permissions.dispatcher.Activity";
            assertThat(getClassName(name)).isEqualTo("Activity");
        }
        {
            String name = "Activity";
            assertThat(getClassName(name)).isEqualTo("Activity");
        }
        {
            String name = "...";
            assertThat(getClassName(name)).isEmpty();
        }
    }

    @Test
    public void testGetFieldName() {
        {
            String name = "activity";
            assertThat(getFieldName(name)).isEqualTo("REQUEST_ACTIVITY");
        }
        {
            String name = "FRAGMENT";
            assertThat(getFieldName(name)).isEqualTo("REQUEST_FRAGMENT");
        }
    }

    @Test
    public void testGetIsEmpty() {
        {
            assertThat(isEmpty(null)).isTrue();
        }
        {
            assertThat(isEmpty(Collections.emptyList())).isTrue();
        }
        {
            assertThat(isEmpty(Collections.singletonList("android"))).isFalse();
        }
    }

    @Test
    public void testToString() {
        {
            assertThat(Utils.toString(null)).isNull();
        }
        {
            assertThat(Utils.toString(new String[]{""})).isEqualTo("{\"\"}");
        }
        {
            assertThat(Utils.toString(new String[]{"android"})).isEqualTo("{\"android\"}");
        }
        {
            assertThat(Utils.toString(new String[]{"android", "ios"})).isEqualTo("{\"android\", \"ios\"}");
        }
    }

}
