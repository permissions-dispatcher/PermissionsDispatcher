package permissions.dispatcher;

import android.Manifest;
import android.content.pm.PackageManager;
import android.test.AndroidTestCase;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static permissions.dispatcher.PermissionUtils.hasSelfPermissions;
import static permissions.dispatcher.PermissionUtils.verifyPermissions;

/**
 * Unit test for {@link PermissionUtils}.
 */
public class PermissionUtilsTest extends AndroidTestCase {

    private static final int GRANTED = PackageManager.PERMISSION_GRANTED;

    @Test
    public void testVerifyPermissions() {
        assertThat(verifyPermissions(GRANTED), is(true));
        assertThat(verifyPermissions(GRANTED, GRANTED), is(true));
        assertThat(verifyPermissions(1, 2, GRANTED), is(false));
        assertThat(verifyPermissions(1, 2, 3), is(false));
    }

    @Test
    public void testHasSelfPermissions() {
        assertThat(hasSelfPermissions(getContext(), Manifest.permission.CAMERA), is(false));
    }

}
