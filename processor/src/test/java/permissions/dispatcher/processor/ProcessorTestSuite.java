package permissions.dispatcher.processor;

import org.junit.Test;

import permissions.dispatcher.processor.base.TestSuite;
import permissions.dispatcher.processor.data.Source;

public class ProcessorTestSuite extends TestSuite {

    @Test public void nativeFragmentNotSupported() {
        expectRuntimeException("PermissionsDispatcher for annotated class 'MyFragment' can't be generated, because the support-v13 dependency is missing on your project");
        assertJavaSource(Source.NativeFragmentNotSupported);
    }

    @Test public void noPermissionActivity() {
        expectRuntimeException("Annotated class 'MyActivity' doesn't have any method annotated with '@NeedsPermission'");
        assertJavaSource(Source.NoPermissionActivity);
    }

    @Test public void noPermissionFragment() {
        expectRuntimeException("Annotated class 'MyFragment' doesn't have any method annotated with '@NeedsPermission'");
        assertJavaSource(Source.NoPermissionSupportFragment);
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

    @Test public void rationaleWithWrongParameters1() {
        expectRuntimeException("Method 'cameraRationale()' must declare parameters of type 'PermissionRequest'");
        assertJavaSource(Source.RationaleWithWrongParameters1);
    }

    @Test public void rationaleWithWrongParameters2() {
        expectRuntimeException("Method 'cameraRationale()' must declare parameters of type 'PermissionRequest'");
        assertJavaSource(Source.RationaleWithWrongParameters2);
    }

    @Test public void rationaleWithoutParameters() {
        expectRuntimeException("Method 'cameraRationale()' must declare parameters of type 'PermissionRequest'");
        assertJavaSource(Source.RationaleWithoutParameters);
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

    @Test public void duplicatesInListsActivity() {
        expectRuntimeException("[android.permission.CAMERA, android.permission.WRITE_EXTERNAL_STORAGE] is duplicated in 'onCameraDenied2()' annotated with '@OnPermissionDenied'");
        assertJavaSource(Source.DuplicatesInListsActivity);
    }

    @Test public void onePermissionActivity() {
        assertJavaSource(Source.OnePermissionActivity);
    }

    @Test public void onePermissionFragment() {
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

    @Test public void onePermissionWithParametersSupportFragment() {
        assertJavaSource(Source.OnePermissionWithParametersSupportFragment);
    }

    @Test public void onePermissionWithParametersAndRationaleSupportFragment() {
        assertJavaSource(Source.OnePermissionWithParametersAndRationaleSupportFragment);
    }

    @Test public void onePermissionWithParametersAndDeniedSupportFragment() {
        assertJavaSource(Source.OnePermissionWithParametersAndDeniedSupportFragment);
    }

    @Test public void onePermissionWithParametersRationaleAndDeniedSupportFragment() {
        assertJavaSource(Source.OnePermissionWithParametersRationaleAndDeniedSupportFragment);
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

    @Test public void twoPermissionsWithSameSignatureSupportFragment() {
        assertJavaSource(Source.TwoPermissionsWithSameSignatureSupportFragment);
    }

    @Test public void twoPermissionsWithSameSignatureAndRationaleSupportFragment() {
        assertJavaSource(Source.TwoPermissionsWithSameSignatureAndRationaleSupportFragment);
    }

    @Test public void twoPermissionsWithSameSignatureAndDeniedSupportFragment() {
        assertJavaSource(Source.TwoPermissionsWithSameSignatureAndDeniedSupportFragment);
    }

    @Test public void twoPermissionsWithSameSignatureRationaleAndDeniedSupportFragment() {
        assertJavaSource(Source.TwoPermissionsWithSameSignatureRationaleAndDeniedSupportFragment);
    }

    @Test public void twoPermissionsAtOnceActivity() {
        assertJavaSource(Source.TwoPermissionsAtOnceActivity);
    }

    @Test public void twoPermissionsSupportFragment() {
        assertJavaSource(Source.TwoPermissionsSupportFragment);
    }

    @Test public void onePermissionWithRationaleActivity() {
        assertJavaSource(Source.OnePermissionWithRationaleActivity);
    }

    @Test public void onePermissionWithRationaleSupportFragment() {
        assertJavaSource(Source.OnePermissionWithRationaleSupportFragment);
    }

    @Test public void onePermissionWithNeverAskAndRationaleSupportFragment() {
        assertJavaSource(Source.OnePermissionWithNeverAskAndRationaleSupportFragment);
    }

    @Test public void onePermissionWithNeverAskRationaleAndDeniedSupportFragment() {
        assertJavaSource(Source.OnePermissionWithNeverAskRationaleAndDeniedSupportFragment);
    }

    @Test public void onePermissionWithNeverAskSupportFragment() {
        assertJavaSource(Source.OnePermissionWithNeverAskSupportFragment);
    }

    @Test public void onePermissionWithNeverAskAndDeniedSupportFragment() {
        assertJavaSource(Source.OnePermissionWithNeverAskAndDeniedSupportFragment);
    }

    @Test public void twoPermissionsWithOneRationaleActivity() {
        assertJavaSource(Source.TwoPermissionsWithOneRationaleActivity);
    }

    @Test public void twoPermissionsWithOneRationaleSupportFragment() {
        assertJavaSource(Source.TwoPermissionsWithOneRationaleSupportFragment);
    }

    @Test public void twoPermissionsWithTwoRationalesActivity() {
        assertJavaSource(Source.TwoPermissionsWithTwoRationalesActivity);
    }

    @Test public void twoPermissionsWithTwoRationalesSupportFragment() {
        assertJavaSource(Source.TwoPermissionsWithTwoRationalesSupportFragment);
    }

    @Test public void onePermissionWithOtherRationaleActivity() {
        assertJavaSource(Source.OnePermissionWithOtherRationaleActivity);
    }

    @Test public void onePermissionWithOtherRationaleSupportFragment() {
        assertJavaSource(Source.OnePermissionWithOtherRationaleSupportFragment);
    }

    @Test public void onePermissionWithDeniedActivity() {
        assertJavaSource(Source.OnePermissionWithDeniedActivity);
    }

    @Test public void onePermissionWithDeniedFragment() {
        assertJavaSource(Source.OnePermissionWithDeniedSupportFragment);
    }

    @Test public void onePermissionWithOtherDeniedActivity() {
        assertJavaSource(Source.OnePermissionWithOtherDeniedActivity);
    }

    @Test public void onePermissionWithOtherDeniedFragment() {
        assertJavaSource(Source.OnePermissionWithOtherDeniedSupportFragment);
    }

    @Test public void onePermissionWithRationaleAndDeniedActivity() {
        assertJavaSource(Source.OnePermissionWithRationaleAndDeniedActivity);
    }

    @Test public void onePermissionWithRationaleAndDeniedSupportFragment() {
        assertJavaSource(Source.OnePermissionWithRationaleAndDeniedSupportFragment);
    }

    @Test public void noDuplicatesDespiteRepeatedValuesActivity() {
        // Issue 63: https://github.com/hotchemi/PermissionsDispatcher/issues/63
        assertJavaSource(Source.NoDuplicatesDespiteRepeatedValuesActivity);
    }

    @Test public void validMaxSdkVersion() {
        assertJavaSource(Source.ValidMaxSdkVersion);
    }

    @Test public void invalidMaxSdkVersion() {
        assertJavaSource(Source.InValidMaxSdkVersion);
    }

    @Test public void writeSettingsSupportFragment() {
        assertJavaSource(Source.WriteSettingsSupportFragment);
    }

    @Test public void writeSettingsActivity() {
        assertJavaSource(Source.WriteSettingsActivity);
    }

    @Test public void systemAlertWindowSupportFragment() {
        assertJavaSource(Source.SystemAlertWindowSupportFragment);
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

}
