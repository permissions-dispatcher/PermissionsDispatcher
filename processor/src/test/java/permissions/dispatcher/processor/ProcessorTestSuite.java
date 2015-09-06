package permissions.dispatcher.processor;

import org.junit.Test;
import permissions.dispatcher.processor.base.TestSuite;
import permissions.dispatcher.processor.base.BaseTest;
import permissions.dispatcher.processor.data.Source;

import static com.google.common.truth.Truth.ASSERT;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

/**
 * Created by marcel on 03.09.15.
 */
public class ProcessorTestSuite extends TestSuite {

    @Test public void nativeFragmentNotSupported() {
        expectRuntimeException("PermissionsDispatcher for annotated class 'MyFragment' can't be generated, because the support-v13 dependency is missing on your project");
        assertJavaSource(Source.NativeFragmentNotSupported);
    }

    @Test public void noPermissionActivity() {
        expectRuntimeException("Annotated class 'MyActivity' doesn't have any method annotated with '@Needs'");
        assertJavaSource(Source.NoPermissionActivity);
    }

    @Test public void noPermissionFragment() {
        expectRuntimeException("Annotated class 'MyFragment' doesn't have any method annotated with '@Needs'");
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

    @Test public void rationaleWithParameters() {
        expectRuntimeException("Method 'cameraRationale()' must not have any parameters");
        assertJavaSource(Source.RationaleWithParameters);
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
        expectRuntimeException("Method 'showCamera()' annotated with '@Needs' must not be private");
        assertJavaSource(Source.PrivatePermission);
    }

    @Test public void privateRationale() {
        expectRuntimeException("Method 'cameraRationale()' annotated with '@OnRationale' must not be private");
        assertJavaSource(Source.PrivateRationale);
    }

    @Test public void privateDenied() {
        expectRuntimeException("Method 'onCameraDenied()' annotated with '@OnDenied' must not be private");
        assertJavaSource(Source.PrivateDenied);
    }

    @Test public void wrongAnnotatedClass() {
        expectRuntimeException("Class 'tests.MyService' can't be annotated with '@RuntimePermissions'");
        assertJavaSource(Source.WrongAnnotatedClass);
    }

    @Test public void duplicatedPermission() {
        expectRuntimeException("[android.permission.CAMERA] is duplicated in 'showCamera2()' annotated with '@Needs'");
        assertJavaSource(Source.DuplicatedPermission);
    }

    @Test public void duplicatedRationale() {
        expectRuntimeException("[android.permission.CAMERA] is duplicated in 'cameraRationale2()' annotated with '@OnRationale'");
        assertJavaSource(Source.DuplicatedRationale);
    }

    @Test public void duplicatedDenied() {
        expectRuntimeException("[android.permission.CAMERA] is duplicated in 'onCameraDenied2()' annotated with '@OnDenied'");
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
}
