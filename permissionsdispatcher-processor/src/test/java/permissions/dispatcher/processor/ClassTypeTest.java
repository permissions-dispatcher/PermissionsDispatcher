package permissions.dispatcher.processor;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link ClassType}.
 */
public class ClassTypeTest {

    @Test
    public void getActivityTest() {
        assertThat(ClassType.ACTIVITY.getActivity()).isEqualTo("target");
        assertThat(ClassType.FRAGMENT.getActivity()).isEqualTo("target.getActivity()");
    }

    @Test
    public void getClassTypeTest() {
        assertThat(ClassType.Companion.getClassType("MainActivity")).isEqualTo(ClassType.ACTIVITY);
        assertThat(ClassType.Companion.getClassType("MainFragment")).isEqualTo(ClassType.FRAGMENT);
        assertThat(ClassType.Companion.getClassType("FragmentMain")).isNull();
        assertThat(ClassType.Companion.getClassType("ActivityMain")).isNull();
    }

}
