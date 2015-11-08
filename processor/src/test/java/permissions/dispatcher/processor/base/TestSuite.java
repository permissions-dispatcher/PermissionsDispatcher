package permissions.dispatcher.processor.base;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import permissions.dispatcher.processor.PermissionsProcessor;

import static com.google.common.truth.Truth.ASSERT;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public abstract class TestSuite {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    protected final void expectRuntimeException(final String message) {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(newEqualsMatcher(message));
    }

    protected final void assertJavaSource(BaseTest test) {
        ASSERT
                .about(javaSource())
                .that(test.actual())
                .processedWith(new PermissionsProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(test.expect());
    }

    /* Static */

    private static Matcher<String> newEqualsMatcher(String forString) {
        return new StringEquals(forString);
    }
}
