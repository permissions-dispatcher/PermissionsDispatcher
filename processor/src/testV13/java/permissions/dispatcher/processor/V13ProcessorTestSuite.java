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

    @Test public void onePermissionWithNeverAskAndRationaleNativeFragment() {
        assertJavaSource(V13Source.OnePermissionWithNeverAskAndRationaleNativeFragment);
    }

    @Test public void onePermissionWithNeverAskRationaleAndDeniedNativeFragment() {
        assertJavaSource(V13Source.OnePermissionWithNeverAskRationaleAndDeniedNativeFragment);
    }

    @Test public void onePermissionWithNeverAskNativeFragment() {
        assertJavaSource(V13Source.OnePermissionWithNeverAskNativeFragment);
    }

    @Test public void onePermissionWithNeverAskAndDeniedNativeFragment() {
        assertJavaSource(V13Source.OnePermissionWithNeverAskAndDeniedNativeFragment);
    }

    @Test public void onePermissionWithDeniedNativeFragment() {
        assertJavaSource(V13Source.OnePermissionWithDeniedNativeFragment);
    }

    @Test public void onePermissionWithRationaleAndDeniedNativeFragment() {
        assertJavaSource(V13Source.OnePermissionWithRationaleAndDeniedNativeFragment);
    }

    @Test public void onePermissionWithParametersNativeFragment() {
        assertJavaSource(V13Source.OnePermissionWithParametersNativeFragment);
    }

    @Test public void onePermissionWithParametersAndRationaleNativeFragment() {
        assertJavaSource(V13Source.OnePermissionWithParametersAndRationaleNativeFragment);
    }

    @Test public void onePermissionWithParametersAndDeniedNativeFragment() {
        assertJavaSource(V13Source.OnePermissionWithParametersAndDeniedNativeFragment);
    }

    @Test public void onePermissionWithParametersRationaleAndDeniedNativeFragment() {
        assertJavaSource(V13Source.OnePermissionWithParametersRationaleAndDeniedNativeFragment);
    }

    @Test public void twoPermissionsWithSameSignatureNativeFragment() {
        assertJavaSource(V13Source.TwoPermissionsWithSameSignatureNativeFragment);
    }

    @Test public void twoPermissionsWithSameSignatureAndRationaleNativeFragment() {
        assertJavaSource(V13Source.TwoPermissionsWithSameSignatureAndRationaleNativeFragment);
    }

    @Test public void twoPermissionsWithSameSignatureAndDeniedNativeFragment() {
        assertJavaSource(V13Source.TwoPermissionsWithSameSignatureAndDeniedNativeFragment);
    }

    @Test public void twoPermissionsWithSameSignatureRationaleAndDeniedNativeFragment() {
        assertJavaSource(V13Source.TwoPermissionsWithSameSignatureRationaleAndDeniedNativeFragment);
    }

    @Test public void writeSettingsFragment() {
        assertJavaSource(V13Source.WriteSettingsFragment);
    }

    @Test public void SystemAlertWindowFragment() {
        assertJavaSource(V13Source.SystemAlertWindowFragment);
    }

    @Test
    public void nestedFragment() {
        assertJavaSource(V13Source.NestedFragment);
    }

    @Test
    public void nestedStaticFragment() {
        assertJavaSource(V13Source.NestedStaticFragment);
    }
}
