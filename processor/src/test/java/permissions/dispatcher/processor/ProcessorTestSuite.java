package permissions.dispatcher.processor;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import permissions.dispatcher.processor.data.BaseTest;
import permissions.dispatcher.processor.data.Tests;

import static com.google.common.truth.Truth.ASSERT;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

/**
 * Created by marcel on 03.09.15.
 */
public class ProcessorTestSuite {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private static void assertJavaSource(BaseTest test) {
        ASSERT
                .about(javaSource())
                .that(test.actual())
                .processedWith(new PermissionsProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(test.expect());
    }

    @Test public void firstTest() {
        assertJavaSource(Tests.FirstTest);
    }
}
