package permissions.dispatcher.processor;

import org.junit.Test;
import permissions.dispatcher.processor.base.TestSuite;
import permissions.dispatcher.processor.data.V13Source;

public class V13ProcessorTestSuite extends TestSuite {

    @Test public void noPermissionNativeFragment() {
        expectRuntimeException("Annotated class 'MyFragment' doesn't have any method annotated with '@NeedsPermission'");
        assertJavaSource(V13Source.NativeFragmentWithoutNeeds);
    }

    @Test public void onePermissionNativeFragment() {
        assertJavaSource(V13Source.OnePermissionNativeFragment);
    }

    @Test public void twoPermissionsNativeFragment() {
        assertJavaSource(V13Source.TwoPermissionsNativeFragment);
    }

    @Test public void onePermissionWithRationaleNativeFragment() {
        assertJavaSource(V13Source.OnePermissionWithRationaleNativeFragment);
    }

    @Test public void onePermissionWithDeniedNativeFragment() {
        assertJavaSource(V13Source.OnePermissionWithDeniedNativeFragment);
    }

    @Test public void onePermissionWithRationaleAndDeniedNativeFragment() {
        assertJavaSource(V13Source.OnePermissionWithRationaleAndDeniedNativeFragment);
    }
}
