package permissions.dispatcher.processor;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import permissions.dispatcher.*;
import permissions.dispatcher.processor.data.Source;

import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.ASSERT;
import static com.google.testing.compile.JavaFileObjects.forSourceLines;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static permissions.dispatcher.processor.ConstantsProvider.CLASS_SUFFIX;

/**
 * Unit test for {@link PermissionsProcessor}.
 */
public class PermissionsProcessorTest {

    private static final String DEFAULT_CLASS = "MainActivity";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private static void assertJavaSource(JavaFileObject actual, JavaFileObject expect) {
        ASSERT
                .about(javaSource())
                .that(actual)
                .processedWith(new PermissionsProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(expect);
    }

    @Test
    public void onePermissionActivity() {
        JavaFileObject actual = forSourceLines(DEFAULT_CLASS, Source.OnePermissionActivity.ACTUAL);
        JavaFileObject expect = forSourceLines(DEFAULT_CLASS + CLASS_SUFFIX, Source.OnePermissionActivity.EXPECT);
        assertJavaSource(actual, expect);
    }

    @Test
    public void onePermissionFragment() {
        String className = "MainFragment";
        JavaFileObject actual = forSourceLines(className, Source.OnePermissionFragment.ACTUAL);
        JavaFileObject expect = forSourceLines(className + CLASS_SUFFIX, Source.OnePermissionFragment.EXPECT);
        assertJavaSource(actual, expect);
    }

    @Test
    public void twoPermissions() {
        JavaFileObject actual = forSourceLines(DEFAULT_CLASS, Source.TwoPermissions.ACTUAL);
        JavaFileObject expect = forSourceLines(DEFAULT_CLASS + CLASS_SUFFIX, Source.TwoPermissions.EXPECT);
        assertJavaSource(actual, expect);
    }

    @Test
    public void noShowRationale() {
        JavaFileObject actual = forSourceLines(DEFAULT_CLASS, Source.NoShowRationale.ACTUAL);
        JavaFileObject expect = forSourceLines(DEFAULT_CLASS + CLASS_SUFFIX, Source.NoShowRationale.EXPECT);
        assertJavaSource(actual, expect);
    }

    @Test
    public void onePermissionAndOtherRationale() {
        JavaFileObject actual = forSourceLines(DEFAULT_CLASS, Source.OnePermissionAndOtherRationale.ACTUAL);
        JavaFileObject expect = forSourceLines(DEFAULT_CLASS + CLASS_SUFFIX, Source.OnePermissionAndOtherRationale.EXPECT);
        assertJavaSource(actual, expect);
    }

    @Test
    public void onePermissionAndOtherRationales() {
        JavaFileObject actual = forSourceLines(DEFAULT_CLASS, Source.OnePermissionAndOtherRationales.ACTUAL);
        JavaFileObject expect = forSourceLines(DEFAULT_CLASS + CLASS_SUFFIX, Source.OnePermissionAndOtherRationales.EXPECT);
        assertJavaSource(actual, expect);
    }

    @Test
    public void oneDeniedPermission() {
        JavaFileObject actual = forSourceLines(DEFAULT_CLASS, Source.OneDeniedPermission.ACTUAL);
        JavaFileObject expect = forSourceLines(DEFAULT_CLASS + CLASS_SUFFIX, Source.OneDeniedPermission.EXPECT);
        assertJavaSource(actual, expect);
    }

    @Test
    public void oneDeniedPermissions() {
        JavaFileObject actual = forSourceLines(DEFAULT_CLASS, Source.OneDeniedPermissions.ACTUAL);
        JavaFileObject expect = forSourceLines(DEFAULT_CLASS + CLASS_SUFFIX, Source.OneDeniedPermissions.EXPECT);
        assertJavaSource(actual, expect);
    }

    @Test
    public void twoDeniedPermission() {
        JavaFileObject actual = forSourceLines(DEFAULT_CLASS, Source.TwoDeniedPermission.ACTUAL);
        JavaFileObject expect = forSourceLines(DEFAULT_CLASS + CLASS_SUFFIX, Source.TwoDeniedPermission.EXPECT);
        assertJavaSource(actual, expect);
    }

    @Test
    public void nestedSuperClassActivity() {
        JavaFileObject actual = forSourceLines(DEFAULT_CLASS, Source.NestedSuperClassActivity.ACTUAL);
        JavaFileObject expect = forSourceLines(DEFAULT_CLASS + CLASS_SUFFIX, Source.NestedSuperClassActivity.EXPECT);
        assertJavaSource(actual, expect);
    }

    @Test
    public void nestedSuperClassFragment() {
        String className = "MainFragment";
        JavaFileObject actual = forSourceLines(className, Source.NestedSuperClassFragment.ACTUAL);
        JavaFileObject expect = forSourceLines(className + CLASS_SUFFIX, Source.NestedSuperClassFragment.EXPECT);
        assertJavaSource(actual, expect);
    }

    @Test
    public void deniedPermissionWithoutNeedsPermission() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("@DeniedPermissions for [android.permission.CAMERA] doesn't have a matching @NeedsPermissions method");
        JavaFileObject actual = forSourceLines(DEFAULT_CLASS, Source.DeniedPermissionWithoutNeedsPermission.ACTUAL);
        JavaFileObject expect = forSourceLines(DEFAULT_CLASS + CLASS_SUFFIX, Source.EMPTY);
        assertJavaSource(actual, expect);
    }

    @Test
    public void duplicatedDeniedPermission() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("[android.permission.CAMERA] is duplicated in " + DeniedPermission.class);
        JavaFileObject actual = forSourceLines(DEFAULT_CLASS, Source.DuplicatedDeniedPermission.ACTUAL);
        JavaFileObject expect = forSourceLines(DEFAULT_CLASS + CLASS_SUFFIX, Source.EMPTY);
        assertJavaSource(actual, expect);
    }

    @Test
    public void duplicatedDeniedPermissions() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("[android.permission.CAMERA] is duplicated in " + DeniedPermissions.class);
        JavaFileObject actual = forSourceLines(DEFAULT_CLASS, Source.DuplicatedDeniedPermissions.ACTUAL);
        JavaFileObject expect = forSourceLines(DEFAULT_CLASS + CLASS_SUFFIX, Source.EMPTY);
        assertJavaSource(actual, expect);
    }

    @Test
    public void deniedPermissionIsPrivate() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Annotated method must be package private or above");
        JavaFileObject actual = forSourceLines(DEFAULT_CLASS, Source.DeniedPermissionIsPrivate.ACTUAL);
        JavaFileObject expect = forSourceLines(DEFAULT_CLASS + CLASS_SUFFIX, Source.EMPTY);
        assertJavaSource(actual, expect);
    }

    @Test
    public void deniedPermissionsIsPrivate() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Annotated method must be package private or above");
        JavaFileObject actual = forSourceLines(DEFAULT_CLASS, Source.DeniedPermissionsIsPrivate.ACTUAL);
        JavaFileObject expect = forSourceLines(DEFAULT_CLASS + CLASS_SUFFIX, Source.EMPTY);
        assertJavaSource(actual, expect);
    }

    @Test
    public void zeroPermission() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("@NeedsPermission or @NeedsPermissions are not defined");
        JavaFileObject actual = forSourceLines(DEFAULT_CLASS, Source.ZeroPermission.ACTUAL);
        JavaFileObject expect = forSourceLines(DEFAULT_CLASS + CLASS_SUFFIX, Source.EMPTY);
        assertJavaSource(actual, expect);
    }

    @Test
    public void wrongSuperClassType() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Annotated class must be a subclass of 'android.app.Activity' or 'android.support.v4.app.Fragment'");
        String className = "MainUtils";
        JavaFileObject actual = forSourceLines(className, Source.WrongSuperClassType.ACTUAL);
        JavaFileObject expect = forSourceLines(className + CLASS_SUFFIX, Source.EMPTY);
        assertJavaSource(actual, expect);
    }

    @Test
    public void duplicatedPermission() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("[android.permission.CAMERA] is duplicated in " + NeedsPermission.class);
        JavaFileObject actual = forSourceLines(DEFAULT_CLASS, Source.DuplicatedPermission.ACTUAL);
        JavaFileObject expect = forSourceLines(DEFAULT_CLASS + CLASS_SUFFIX, Source.EMPTY);
        assertJavaSource(actual, expect);
    }

    @Test
    public void duplicatedPermissions() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("[android.permission.CAMERA] is duplicated in " + NeedsPermissions.class);
        JavaFileObject actual = forSourceLines(DEFAULT_CLASS, Source.DuplicatedPermissions.ACTUAL);
        JavaFileObject expect = forSourceLines(DEFAULT_CLASS + CLASS_SUFFIX, Source.EMPTY);
        assertJavaSource(actual, expect);
    }

    @Test
    public void duplicatedRationale() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("[android.permission.READ_CONTACTS] is duplicated in " + ShowsRationale.class);
        JavaFileObject actual = forSourceLines(DEFAULT_CLASS, Source.DuplicatedRationale.ACTUAL);
        JavaFileObject expect = forSourceLines(DEFAULT_CLASS + CLASS_SUFFIX, Source.EMPTY);
        assertJavaSource(actual, expect);
    }

    @Test
    public void duplicatedRationales() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("[android.permission.READ_CONTACTS] is duplicated in " + ShowsRationales.class);
        JavaFileObject actual = forSourceLines(DEFAULT_CLASS, Source.DuplicatedRationales.ACTUAL);
        JavaFileObject expect = forSourceLines(DEFAULT_CLASS + CLASS_SUFFIX, Source.EMPTY);
        assertJavaSource(actual, expect);
    }

    @Test
    public void needsPermissionIsPrivate() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Annotated method must be package private or above");
        JavaFileObject actual = forSourceLines(DEFAULT_CLASS, Source.NeedsPermissionIsPrivate.ACTUAL);
        JavaFileObject expect = forSourceLines(DEFAULT_CLASS + CLASS_SUFFIX, Source.EMPTY);
        assertJavaSource(actual, expect);
    }

    @Test
    public void needsPermissionsIsPrivate() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Annotated method must be package private or above");
        JavaFileObject actual = forSourceLines(DEFAULT_CLASS, Source.NeedsPermissionsIsPrivate.ACTUAL);
        JavaFileObject expect = forSourceLines(DEFAULT_CLASS + CLASS_SUFFIX, Source.EMPTY);
        assertJavaSource(actual, expect);
    }

    @Test
    public void showsRationaleIsPrivate() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Annotated method must be package private or above");
        JavaFileObject actual = forSourceLines(DEFAULT_CLASS, Source.ShowsRationaleIsPrivate.ACTUAL);
        JavaFileObject expect = forSourceLines(DEFAULT_CLASS + CLASS_SUFFIX, Source.EMPTY);
        assertJavaSource(actual, expect);
    }

    @Test
    public void showsRationalesIsPrivate() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Annotated method must be package private or above");
        JavaFileObject actual = forSourceLines(DEFAULT_CLASS, Source.ShowsRationalesIsPrivate.ACTUAL);
        JavaFileObject expect = forSourceLines(DEFAULT_CLASS + CLASS_SUFFIX, Source.EMPTY);
        assertJavaSource(actual, expect);
    }

}
