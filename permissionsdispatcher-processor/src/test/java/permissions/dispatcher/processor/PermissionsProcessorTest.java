package permissions.dispatcher.processor;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.tools.JavaFileObject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.ShowsRationale;
import permissions.dispatcher.processor.data.Source;

import static com.google.common.truth.Truth.assert_;
import static com.google.testing.compile.JavaFileObjects.forSourceLines;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

import static permissions.dispatcher.processor.ConstantsProvider.*;

/**
 * Unit test for {@link PermissionsProcessor}.
 */
public class PermissionsProcessorTest {

    private static final String CLASS_NAME = "MainActivity";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void onePermission() {
        JavaFileObject actual = forSourceLines(CLASS_NAME, Source.OnePermission.ACTUAL);
        JavaFileObject expect = forSourceLines(CLASS_NAME + CLASS_SUFFIX, Source.OnePermission.EXPECT);
        assertJavaSource(actual, expect);
    }

    @Test
    public void twoPermissions() {
        JavaFileObject actual = forSourceLines(CLASS_NAME, Source.TwoPermissions.ACTUAL);
        JavaFileObject expect = forSourceLines(CLASS_NAME + CLASS_SUFFIX, Source.TwoPermissions.EXPECT);
        assertJavaSource(actual, expect);
    }

    @Test
    public void noShowRationale() {
        JavaFileObject actual = forSourceLines(CLASS_NAME, Source.NoShowRationale.ACTUAL);
        JavaFileObject expect = forSourceLines(CLASS_NAME + CLASS_SUFFIX, Source.NoShowRationale.EXPECT);
        assertJavaSource(actual, expect);
    }

    @Test
    public void zeroPermission() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("@NeedsPermission is not defined");
        JavaFileObject actual = forSourceLines(CLASS_NAME, Source.ZeroPermission.ACTUAL);
        JavaFileObject expect = forSourceLines(CLASS_NAME + CLASS_SUFFIX, Source.EMPTY);
        assertJavaSource(actual, expect);
    }

    @Test
    public void wrongClassName() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Annotated class must be finished with 'Activity' or 'Fragment'");
        String className = "MainUtils";
        JavaFileObject actual = forSourceLines(className, Source.WrongFileName.ACTUAL);
        JavaFileObject expect = forSourceLines(className + CLASS_SUFFIX, Source.EMPTY);
        assertJavaSource(actual, expect);
    }

    @Test
    public void duplicatedPermission() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("android.permission.CAMERA is duplicated in " + NeedsPermission.class);
        JavaFileObject actual = forSourceLines(CLASS_NAME, Source.DuplicatedPermission.ACTUAL);
        JavaFileObject expect = forSourceLines(CLASS_NAME + CLASS_SUFFIX, Source.EMPTY);
        assertJavaSource(actual, expect);
    }

    @Test
    public void duplicatedRationale() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("android.permission.READ_CONTACTS is duplicated in " + ShowsRationale.class);
        JavaFileObject actual = forSourceLines(CLASS_NAME, Source.DuplicatedRationale.ACTUAL);
        JavaFileObject expect = forSourceLines(CLASS_NAME + CLASS_SUFFIX, Source.EMPTY);
        assertJavaSource(actual, expect);
    }

    @Test
    public void needsPermissionIsPrivate() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Annotated method must be package private or above");
        JavaFileObject actual = forSourceLines(CLASS_NAME, Source.NeedsPermissionIsPrivate.ACTUAL);
        JavaFileObject expect = forSourceLines(CLASS_NAME + CLASS_SUFFIX, Source.EMPTY);
        assertJavaSource(actual, expect);
    }

    @Test
    public void showsRationaleIsPrivate() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Annotated method must be package private or above");
        JavaFileObject actual = forSourceLines(CLASS_NAME, Source.ShowsRationaleIsPrivate.ACTUAL);
        JavaFileObject expect = forSourceLines(CLASS_NAME + CLASS_SUFFIX, Source.EMPTY);
        assertJavaSource(actual, expect);
    }

    private static void assertJavaSource(JavaFileObject actual, JavaFileObject expect) {
        assert_()
                .about(javaSource())
                .that(actual)
                .processedWith(new PermissionsProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(expect);
    }

}
