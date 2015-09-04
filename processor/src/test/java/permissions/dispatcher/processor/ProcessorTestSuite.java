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

    static void assertJavaSource(BaseTest test) {
        ASSERT
                .about(javaSource())
                .that(test.actual())
                .processedWith(new PermissionsProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(test.expect());
    }

    @Test public void noPermissionActivity() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Annotated class 'MyActivity' doesn't have any method annotated with '@Needs'");
        assertJavaSource(Source.NoPermissionActivity);
    }

    @Test public void noPermissionFragment() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Annotated class 'MyFragment' doesn't have any method annotated with '@Needs'");
        assertJavaSource(Source.NoPermissionSupportFragment);
    }

    @Test public void permissionWithNonVoidReturnType() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Method 'showCamera()' must specify return type 'void', not 'int'");
        assertJavaSource(Source.PermissionWithNonVoidReturnType);
    }

    @Test public void rationaleWithNonVoidReturnType() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Method 'cameraRationale()' must specify return type 'void', not 'int'");
        assertJavaSource(Source.RationaleWithNonVoidReturnType);
    }

    @Test public void deniedWithNonVoidReturnType() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Method 'onCameraDenied()' must specify return type 'void', not 'int'");
        assertJavaSource(Source.DeniedWithNonVoidReturnType);
    }

    @Test public void permissionWithParameters() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Method 'showCamera()' must not have any parameters");
        assertJavaSource(Source.PermissionWithParameters);
    }

    @Test public void rationaleWithParameters() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Method 'cameraRationale()' must not have any parameters");
        assertJavaSource(Source.RationaleWithParameters);
    }

    @Test public void deniedWithParameters() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Method 'onCameraDenied()' must not have any parameters");
        assertJavaSource(Source.DeniedWithParameters);
    }

    @Test public void wrongAnnotatedClass() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Class 'tests.MyService' can't be annotated with '@RuntimePermissions'");
        assertJavaSource(Source.WrongAnnotatedClass);
    }

    @Test public void duplicatedPermission() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("[android.permission.CAMERA] is duplicated in 'showCamera2()' annotated with '@interface permissions.dispatcher.Needs'");
        assertJavaSource(Source.DuplicatedPermission);
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
