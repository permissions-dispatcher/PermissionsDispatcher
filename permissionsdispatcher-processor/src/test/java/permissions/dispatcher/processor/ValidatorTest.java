package permissions.dispatcher.processor;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import javax.lang.model.element.ExecutableElement;

import static permissions.dispatcher.processor.Validator.checkNeedsPermissionsSize;

/**
 * Unit test for {@link Validator}.
 */
public class ValidatorTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void validEmpty() {
        List<ExecutableElement> list = Collections.singletonList(Mockito.mock(ExecutableElement.class));
        checkNeedsPermissionsSize(list, list);
    }

    @Test
    public void invalidEmpty() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("@NeedsPermission or @NeedsPermissions are not defined");
        checkNeedsPermissionsSize(null, null);
    }
}
