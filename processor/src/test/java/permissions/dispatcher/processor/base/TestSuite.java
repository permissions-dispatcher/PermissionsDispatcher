package permissions.dispatcher.processor.base;

import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 * Created by marcel on 04.09.15.
 */
public abstract class TestSuite {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();
}
