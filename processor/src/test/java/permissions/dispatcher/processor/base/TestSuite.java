package permissions.dispatcher.processor.base;

import org.junit.Rule;
import org.junit.rules.ExpectedException;
import permissions.dispatcher.processor.PermissionsProcessor;

import static com.google.common.truth.Truth.assert_;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public abstract class TestSuite {

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
                .processedWith(new PermissionsProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(test.expect());
    }
}
