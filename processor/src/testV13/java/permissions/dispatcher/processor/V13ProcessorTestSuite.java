package permissions.dispatcher.processor;

import org.junit.Test;
import permissions.dispatcher.processor.base.TestSuite;
import permissions.dispatcher.processor.data.V13Tests;

import static permissions.dispatcher.processor.ProcessorTestSuite.assertJavaSource;

public class V13ProcessorTestSuite extends TestSuite {

    @Test public void v13NativeFragmentWithoutNeeds() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Annotated class 'MyFragment' doesn't have any method annotated with '@Needs'");
        assertJavaSource(V13Tests.NativeFragmentWithoutNeeds);
    }
}
