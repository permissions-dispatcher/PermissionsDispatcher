package permissions.dispatcher;

import android.Manifest;
import android.content.Context;
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

    @Test
    public void testVerifyPermissions() {
        assertThat(verifyPermissions(PackageManager.PERMISSION_GRANTED), is(true));
        assertThat(verifyPermissions(PackageManager.PERMISSION_GRANTED, PackageManager.PERMISSION_GRANTED), is(true));
        assertThat(verifyPermissions(1, 2, PackageManager.PERMISSION_GRANTED), is(false));
        assertThat(verifyPermissions(1, 2, 3), is(false));
    }

    @Test
    public void testHasSelfPermissions() {
        Context context = getContext();
        assertThat(hasSelfPermissions(context, Manifest.permission.CAMERA), is(false));
    }

}
