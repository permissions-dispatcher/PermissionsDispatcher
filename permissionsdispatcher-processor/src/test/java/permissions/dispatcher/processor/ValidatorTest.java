package permissions.dispatcher.processor;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import java.util.Collections;

import javax.lang.model.element.ExecutableElement;

import static permissions.dispatcher.processor.Validator.checkClassName;
import static permissions.dispatcher.processor.Validator.checkNeedsPermissionSize;

/**
 * Unit test for {@link Validator}.
 */
public class ValidatorTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void validEmpty() {
        checkNeedsPermissionSize(Collections.singletonList(Mockito.mock(ExecutableElement.class)));
    }

    @Test
    public void invalidEmpty() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("@NeedsPermission is not defined");
        checkNeedsPermissionSize(null);
    }

    @Test
    public void validCheckClassName() {
        checkClassName("MainActivity");
        checkClassName("MainFragment");
    }

    @Test
    public void invalidCheckClassName1() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Annotated class must be finished with 'Activity' or 'Fragment'");
        checkClassName("ActivityMain");
    }

    @Test
    public void invalidCheckClassName2() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Annotated class must be finished with 'Activity' or 'Fragment'");
        checkClassName("FragmentMain");
    }

}
