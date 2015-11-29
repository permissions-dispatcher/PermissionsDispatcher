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

    @Test public void permissionWithParameters() {
        expectRuntimeException("Method 'showCamera()' must not have any parameters");
        assertJavaSource(Source.PermissionWithParameters);
    }

    @Test public void rationaleWithWrongParameters() {
        expectRuntimeException("Method 'cameraRationale()' must declare parameters of type 'PermissionRequest'");
        assertJavaSource(Source.RationaleWithWrongParameters);
    }

    @Test public void rationaleWithoutParameters() {
        expectRuntimeException("Method 'cameraRationale()' must declare parameters of type 'PermissionRequest'");
        assertJavaSource(Source.RationaleWithoutParameters);
    }

    @Test public void deniedWithParameters() {
        expectRuntimeException("Method 'onCameraDenied()' must not have any parameters");
        assertJavaSource(Source.DeniedWithParameters);
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

    @Test public void onePermissionActivity() {
        assertJavaSource(Source.OnePermissionActivity);
    }

    @Test public void onePermissionFragment() {
        assertJavaSource(Source.OnePermissionSupportFragment);
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
}
