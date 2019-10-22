package permissions.dispatcher.processor;

import org.junit.Test;
import permissions.dispatcher.processor.base.TestSuite;
import permissions.dispatcher.processor.data.Source;

public class ProcessorTestSuite extends TestSuite {
    @Test public void noPermissionActivity() {
        expectRuntimeException("Annotated class 'MyActivity' doesn't have any method annotated with '@NeedsPermission'");
        assertJavaSource(Source.NoPermissionActivity);
    }

    @Test public void permissionWithNonVoidReturnType() {
        expectRuntimeException("Method 'showCamera()' must specify return type 'void', not 'int'");
        assertJavaSource(Source.PermissionWithNonVoidReturnType);
    }

    @Test public void rationaleWithNonVoidReturnType() {
        expectRuntimeException("Method 'cameraRationale()' must specify return type 'void', not 'int'");
        assertJavaSource(Source.RationaleWithNonVoidReturnType);
    }

    @Test public void deniedWithNonVoidReturnType() {
        expectRuntimeException("Method 'onCameraDenied()' must specify return type 'void', not 'int'");
        assertJavaSource(Source.DeniedWithNonVoidReturnType);
    }

    @Test public void neverAskWithNonVoidReturnType() {
        expectRuntimeException("Method 'onNeverAskForCamera()' must specify return type 'void', not 'int'");
        assertJavaSource(Source.NeverAskWithNonVoidReturnType);
    }

    @Test public void rationaleWithWrongParameters() {
        expectRuntimeException("Method 'cameraRationale()' must has less than or equal to 1 size parameter and type of it is supposed to be 'PermissionRequest'");
        assertJavaSource(Source.RationaleWithWrongParameters);
    }

    @Test public void rationaleWithOneMoreParameters() {
        expectRuntimeException("Method 'cameraRationale()' must has less than or equal to 1 size parameter and type of it is supposed to be 'PermissionRequest'");
        assertJavaSource(Source.RationaleWithOneMoreParameters);
    }

    @Test public void deniedWithParameters() {
        expectRuntimeException("Method 'onCameraDenied()' must not have any parameters");
        assertJavaSource(Source.DeniedWithParameters);
    }

    @Test public void neverAskWithParameters() {
        expectRuntimeException("Method 'onNeverAskForCamera()' must not have any parameters");
        assertJavaSource(Source.NeverAskWithParameters);
    }

    @Test public void permissionWithThrows() {
        expectRuntimeException("Method 'showCamera()' must not have any 'throws' declaration in its signature");
        assertJavaSource(Source.PermissionWithThrows);
    }

    @Test public void rationaleWithThrows() {
        expectRuntimeException("Method 'cameraRationale()' must not have any 'throws' declaration in its signature");
        assertJavaSource(Source.RationaleWithThrows);
    }

    @Test public void deniedWithThrows() {
        expectRuntimeException("Method 'onCameraDenied()' must not have any 'throws' declaration in its signature");
        assertJavaSource(Source.DeniedWithThrows);
    }

    @Test public void neverAskWithThrows() {
        expectRuntimeException("Method 'onNeverAskForCamera()' must not have any 'throws' declaration in its signature");
        assertJavaSource(Source.NeverAskWithThrows);
    }

    @Test public void privatePermission() {
        expectRuntimeException("Method 'showCamera()' annotated with '@NeedsPermission' must not be private");
        assertJavaSource(Source.PrivatePermission);
    }

    @Test public void privateRationale() {
        expectRuntimeException("Method 'cameraRationale()' annotated with '@OnShowRationale' must not be private");
        assertJavaSource(Source.PrivateRationale);
    }

    @Test public void privateDenied() {
        expectRuntimeException("Method 'onCameraDenied()' annotated with '@OnPermissionDenied' must not be private");
        assertJavaSource(Source.PrivateDenied);
    }

    @Test public void privateNeverAsk() {
        expectRuntimeException("Method 'onNeverAskForCamera()' annotated with '@OnNeverAskAgain' must not be private");
        assertJavaSource(Source.PrivateNeverAsk);
    }

    @Test public void wrongAnnotatedClass() {
        expectRuntimeException("Class 'tests.MyService' can't be annotated with '@RuntimePermissions'");
        assertJavaSource(Source.WrongAnnotatedClass);
    }

    @Test public void duplicatedRationale() {
        expectRuntimeException("[android.permission.CAMERA] is duplicated in 'cameraRationale2()' annotated with '@OnShowRationale'");
        assertJavaSource(Source.DuplicatedRationale);
    }

    @Test public void duplicatedDenied() {
        expectRuntimeException("[android.permission.CAMERA] is duplicated in 'onCameraDenied2()' annotated with '@OnPermissionDenied'");
        assertJavaSource(Source.DuplicatedDenied);
    }

    @Test public void duplicatedNeverAsk() {
        expectRuntimeException("[android.permission.CAMERA] is duplicated in 'onNeverAskForCamera2()' annotated with '@OnNeverAskAgain'");
        assertJavaSource(Source.DuplicatedNeverAsk);
    }

    @Test public void needsPermissionMethodOverload() {
        expectRuntimeException("'showCamera()' has duplicated '@NeedsPermission' method. The method annotated with '@NeedsPermission' must has the unique name.");
        assertJavaSource(Source.needsPermissionMethodOverload);
    }

    @Test public void systemAlertWindowWithOnNeverAskAgain() {
        expectRuntimeException("'@NeverAskAgain' annotated method never being called with 'WRITE_SETTINGS' or 'SYSTEM_ALERT_WINDOW' permission.");
        assertJavaSource(Source.systemAlertWindowWithOnNeverAskAgain);
    }

    @Test public void writeSettingsWithOnNeverAskAgain() {
        expectRuntimeException("'@NeverAskAgain' annotated method never being called with 'WRITE_SETTINGS' or 'SYSTEM_ALERT_WINDOW' permission.");
        assertJavaSource(Source.writeSettingsWithOnNeverAskAgain);
    }

    @Test public void onePermissionActivity() {
        assertJavaSource(Source.OnePermissionActivity);
    }

    @Test public void onePermissionSupportFragment() {
        assertJavaSource(Source.OnePermissionSupportFragment);
    }

    @Test public void onePermissionWithParametersActivity() {
        assertJavaSource(Source.OnePermissionWithParametersActivity);
    }

    @Test public void onePermissionWithParametersAndRationaleActivity() {
        assertJavaSource(Source.OnePermissionWithParametersAndRationaleActivity);
    }

    @Test public void onePermissionWithParametersAndDeniedActivity() {
        assertJavaSource(Source.OnePermissionWithParametersAndDeniedActivity);
    }

    @Test public void onePermissionWithParametersRationaleAndDeniedActivity() {
        assertJavaSource(Source.OnePermissionWithParametersRationaleAndDeniedActivity);
    }

    @Test public void onePermissionWithParametersFragment() {
        assertJavaSource(Source.OnePermissionWithParametersFragment);
    }

    @Test public void onePermissionWithParametersAndRationaleFragment() {
        assertJavaSource(Source.OnePermissionWithParametersAndRationaleFragment);
    }

    @Test public void onePermissionWithParametersAndDeniedFragment() {
        assertJavaSource(Source.OnePermissionWithParametersAndDeniedFragment);
    }

    @Test public void onePermissionWithParametersRationaleAndDeniedFragment() {
        assertJavaSource(Source.OnePermissionWithParametersRationaleAndDeniedFragment);
    }

    @Test public void onePermissionWithNeverAskActivity() {
        assertJavaSource(Source.OnePermissionWithNeverAskActivity);
    }

    @Test public void onePermissionWithNeverAskAndRationaleActivity() {
        assertJavaSource(Source.OnePermissionWithNeverAskAndRationaleActivity);
    }

    @Test public void onePermissionWithNeverAskRationaleAndDeniedActivity() {
        assertJavaSource(Source.OnePermissionWithNeverAskRationaleAndDeniedActivity);
    }

    @Test public void onePermissionWithNeverAskAndDeniedActivity() {
        assertJavaSource(Source.OnePermissionWithNeverAskAndDeniedActivity);
    }

    @Test public void twoPermissionsWithNeverAskActivity() {
        assertJavaSource(Source.TwoPermissionsWithNeverAskActivity);
    }

    @Test public void twoPermissionsActivity() {
        assertJavaSource(Source.TwoPermissionsActivity);
    }

    @Test public void twoPermissionsWithSameSignatureActivity() {
        assertJavaSource(Source.TwoPermissionsWithSameSignatureActivity);
    }

    @Test public void twoPermissionsWithSameSignatureAndRationaleActivity() {
        assertJavaSource(Source.TwoPermissionsWithSameSignatureAndRationaleActivity);
    }

    @Test public void twoPermissionsWithSameSignatureAndDeniedActivity() {
        assertJavaSource(Source.TwoPermissionsWithSameSignatureAndDeniedActivity);
    }

    @Test public void twoPermissionsWithSameSignatureRationaleAndDeniedActivity() {
        assertJavaSource(Source.TwoPermissionsWithSameSignatureRationaleAndDeniedActivity);
    }

    @Test public void twoPermissionsWithSameSignatureFragment() {
        assertJavaSource(Source.TwoPermissionsWithSameSignatureFragment);
    }

    @Test public void twoPermissionsWithSameSignatureAndRationaleFragment() {
        assertJavaSource(Source.TwoPermissionsWithSameSignatureAndRationaleFragment);
    }

    @Test public void twoPermissionsWithSameSignatureAndDeniedFragment() {
        assertJavaSource(Source.TwoPermissionsWithSameSignatureAndDeniedFragment);
    }

    @Test public void twoPermissionsWithSameSignatureRationaleAndDeniedFragment() {
        assertJavaSource(Source.TwoPermissionsWithSameSignatureRationaleAndDeniedFragment);
    }

    @Test public void twoPermissionsAtOnceActivity() {
        assertJavaSource(Source.TwoPermissionsAtOnceActivity);
    }

    @Test public void twoPermissionsFragment() {
        assertJavaSource(Source.TwoPermissionsFragment);
    }

    @Test public void onePermissionWithNeverAskAndRationaleFragment() {
        assertJavaSource(Source.OnePermissionWithNeverAskAndRationaleFragment);
    }

    @Test public void onePermissionWithNeverAskRationaleAndDeniedFragment() {
        assertJavaSource(Source.OnePermissionWithNeverAskRationaleAndDeniedFragment);
    }

    @Test public void onePermissionWithNeverAskFragment() {
        assertJavaSource(Source.OnePermissionWithNeverAskFragment);
    }

    @Test public void onePermissionWithNeverAskAndDeniedFragment() {
        assertJavaSource(Source.OnePermissionWithNeverAskAndDeniedFragment);
    }

    @Test public void twoPermissionsWithTwoRationalesActivity() {
        assertJavaSource(Source.TwoPermissionsWithTwoRationalesActivity);
    }

    @Test public void twoPermissionsWithTwoRationalesFragment() {
        assertJavaSource(Source.TwoPermissionsWithTwoRationalesFragment);
    }

    @Test public void onePermissionWithOtherRationaleActivity() {
        assertJavaSource(Source.OnePermissionWithOtherRationaleActivity);
    }

    @Test public void onePermissionWithOtherRationaleFragment() {
        assertJavaSource(Source.OnePermissionWithOtherRationaleFragment);
    }

    @Test public void onePermissionWithOtherDeniedActivity() {
        assertJavaSource(Source.OnePermissionWithOtherDeniedActivity);
    }

    @Test public void onePermissionWithOtherDeniedFragment() {
        assertJavaSource(Source.OnePermissionWithOtherDeniedFragment);
    }

    @Test public void onePermissionWithRationaleAndDeniedActivity() {
        assertJavaSource(Source.OnePermissionWithRationaleAndDeniedActivity);
    }

    @Test public void onePermissionWithRationaleAndDeniedFragment() {
        assertJavaSource(Source.OnePermissionWithRationaleAndDeniedFragment);
    }

    @Test public void noDuplicatesDespiteRepeatedValuesActivity() {
        // https://github.com/hotchemi/PermissionsDispatcher/issues/63
        assertJavaSource(Source.NoDuplicatesDespiteRepeatedValuesActivity);
    }

    @Test public void validMaxSdkVersion() {
        assertJavaSource(Source.ValidMaxSdkVersion);
    }

    @Test public void invalidMaxSdkVersion() {
        assertJavaSource(Source.InValidMaxSdkVersion);
    }

    @Test public void writeSettingsFragment() {
        assertJavaSource(Source.WriteSettingsFragment);
    }

    @Test public void writeSettingsActivity() {
        assertJavaSource(Source.WriteSettingsActivity);
    }

    @Test public void systemAlertWindowFragment() {
        assertJavaSource(Source.SystemAlertWindowFragment);
    }

    @Test public void systemAlertWindowActivity() {
        assertJavaSource(Source.SystemAlertWindowActivity);
    }

    @Test public void mixSystemAlertWindowAndNormalPermissionCompileError() {
        expectRuntimeException("Method 'systemAlertWindow()' defines 'android.permission.SYSTEM_ALERT_WINDOW' with other permissions at the same time.");
        assertJavaSource(Source.SystemAlertWindowMixPermissionCase);
    }

    @Test public void mixWriteSettingsAndNormalPermissionCompileError() {
        expectRuntimeException("Method 'systemAlertWindow()' defines 'android.permission.WRITE_SETTINGS' with other permissions at the same time.");
        assertJavaSource(Source.WriteSettingsMixPermissionCase);
    }

    @Test public void mixSystemAlertWindowAndWriteSettingsPermissionCompileError() {
        expectRuntimeException("Method 'systemAlertWindow()' defines 'android.permission.WRITE_SETTINGS' with other permissions at the same time.");
        assertJavaSource(Source.SystemAlertWindowAndWriteSettingsMixPermissionCase);
    }

    @Test public void systemAlertWindowGenericsActivity() {
        assertJavaSource(Source.SystemAlertWindowGenericsActivity);
    }

    @Test public void systemAlertWindowSupportGenericsFragment() {
        assertJavaSource(Source.SystemAlertWindowSupportGenericsFragment);
    }

    @Test public void nestedActivity() {
        assertJavaSource(Source.NestedActivity);
    }

    @Test
    public void nestedStaticActivity() {
        assertJavaSource(Source.NestedStaticActivity);
    }

    @Test
    public void nestedActivityWithDefaultPackage() {
        assertJavaSource(Source.NestedActivityWithDefaultPackage);
    }

    @Test
    public void nestedFragment() {
        assertJavaSource(Source.NestedFragment);
    }

    @Test
    public void nestedStaticFragment() {
        assertJavaSource(Source.NestedStaticFragment);
    }

    @Test public void onePermissionWithNoArgumentRationaleActivity() {
        assertJavaSource(Source.OnePermissionWithNoArgumentRationaleActivity);
    }

    @Test public void onePermissionWithNoArgumentRationaleAndDeniedActivity() {
        assertJavaSource(Source.OnePermissionWithNoArgumentRationaleAndDeniedActivity);
    }

    @Test public void onePermissionWithParamNoArgumentRationaleAndDeniedActivity() {
        assertJavaSource(Source.OnePermissionWithParamNoArgumentRationaleAndDeniedActivity);
    }

    @Test public void onePermissionWithNoArgumentRationaleFragment() {
        assertJavaSource(Source.OnePermissionWithNoArgumentRationaleFragment);
    }

    @Test public void onePermissionWithNoArgumentRationaleAndDeniedFragment() {
        assertJavaSource(Source.OnePermissionWithNoArgumentRationaleAndDeniedFragment);
    }

    @Test public void onePermissionWithParamNoArgumentRationaleAndDeniedFragment() {
        assertJavaSource(Source.OnePermissionWithParamNoArgumentRationaleAndDeniedFragment);
    }
}
