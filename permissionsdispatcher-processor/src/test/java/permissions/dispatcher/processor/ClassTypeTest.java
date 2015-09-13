package permissions.dispatcher.processor;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link ClassType}.
 */
public class ClassTypeTest {

    private static final TypeResolver MOCK_RESOLVER = new TypeResolver() {
        @Override
        public boolean isSubTypeOf(String subTypeClass, String superTypeClass) {
            String superTypeWithoutPackage = superTypeClass.substring(superTypeClass.lastIndexOf('.') + 1);
            return subTypeClass.contains(superTypeWithoutPackage);
        }
    };

    @Test
    public void getActivityTest() {
        assertThat(ClassType.ACTIVITY.getActivity()).isEqualTo("target");
        assertThat(ClassType.FRAGMENT.getActivity()).isEqualTo("target.getActivity()");
    }

    @Test
    public void getClassTypeTest() {
        assertThat(ClassType.getClassType("MainActivity", MOCK_RESOLVER)).isEqualTo(ClassType.ACTIVITY);
        assertThat(ClassType.getClassType("MainFragment", MOCK_RESOLVER)).isEqualTo(ClassType.FRAGMENT);
        assertThat(ClassType.getClassType("ActivityMain", MOCK_RESOLVER)).isEqualTo(ClassType.ACTIVITY);
        assertThat(ClassType.getClassType("FragmentMain", MOCK_RESOLVER)).isEqualTo(ClassType.FRAGMENT);
        assertThat(ClassType.getClassType("MyService", MOCK_RESOLVER)).isNull();
        assertThat(ClassType.getClassType("SomeBroadcastReceiver", MOCK_RESOLVER)).isNull();
    }

}
