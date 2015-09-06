package permissions.dispatcher.processor;

import org.junit.Test;
import permissions.dispatcher.processor.base.TestSuite;

public class ClasspathTestSuite extends TestSuite {

    @Test
    public void hasSupportV4() throws Exception {
        Class.forName("android.support.v4.app.ActivityCompat");
    }

    @Test public void doesntHaveSupportV13() throws Exception {
        expectedException.expect(ClassNotFoundException.class);
        Class.forName("android.support.v13.app.FragmentCompat");
    }
}
