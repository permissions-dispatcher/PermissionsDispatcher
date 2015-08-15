package permissions.dispatcher.processor;

import org.junit.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static permissions.dispatcher.processor.Utils.getClassName;
import static permissions.dispatcher.processor.Utils.getFieldName;
import static permissions.dispatcher.processor.Utils.getPackageName;
import static permissions.dispatcher.processor.Utils.isEmpty;

/**
 * Unit test for {@link Utils}.
 */
public class UtilsTest {

    @Test
    public void testGetPackageName() {
        { String name = "permissions.dispatcher.Activity";
        assertThat(getPackageName(name)).isEqualTo("permissions.dispatcher"); }
        { String name = "...";
        assertThat(getPackageName(name)).isEqualTo(".."); }
    }

    @Test
    public void testGetClassName() {
        { String name = "permissions.dispatcher.Activity";
            assertThat(getClassName(name)).isEqualTo("Activity"); }
        { String name = "Activity";
            assertThat(getClassName(name)).isEqualTo("Activity"); }
        { String name = "...";
            assertThat(getClassName(name)).isEmpty(); }
    }

    @Test
    public void testGetFieldName() {
        { String name = "activity";
            assertThat(getFieldName(name)).isEqualTo("REQUEST_ACTIVITY"); }
        { String name = "FRAGMENT";
            assertThat(getFieldName(name)).isEqualTo("REQUEST_FRAGMENT"); }
    }

    @Test
    public void testGetIsEmpty() {
        { assertThat(isEmpty(null)).isTrue(); }
        { assertThat(isEmpty(Collections.emptyList())).isTrue(); }
        { assertThat(isEmpty(Collections.singletonList("android"))).isFalse(); }
    }

}
