package permissions.dispatcher.processor.base;

import org.junit.Rule;
import org.junit.rules.ExpectedException;

import permissions.dispatcher.processor.PermissionsProcessor;

import static com.google.common.truth.Truth.assert_;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public abstract class TestSuite {

    /* System ClassLoader, enhanced with Android support libraries */
    private static final ClassLoader ANDROID_AWARE_CLASSLOADER = AndroidAwareClassLoader.create();

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    protected final void expectRuntimeException(final String message) {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(new StringEquals(message));
    }

    protected final void assertJavaSource(BaseTest test) {
        assert_()
                .about(javaSource())
                .that(test.actual())
                .withClasspathFrom(ANDROID_AWARE_CLASSLOADER)
                .processedWith(new PermissionsProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(test.expect());
    }
}
