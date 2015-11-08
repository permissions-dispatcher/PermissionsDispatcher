package permissions.dispatcher.processor;

import org.junit.Test;

public class V13ClasspathTestSuite {

    @Test
    public void hasSupportV13() throws Exception {
        Class.forName("android.support.v13.app.FragmentCompat");
    }
}
