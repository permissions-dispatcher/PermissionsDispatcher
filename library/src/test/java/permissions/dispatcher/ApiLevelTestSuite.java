package permissions.dispatcher;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.PermissionChecker;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static android.os.Build.VERSION_CODES.GINGERBREAD;
import static android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH;
import static android.os.Build.VERSION_CODES.JELLY_BEAN;
import static android.os.Build.VERSION_CODES.KITKAT_WATCH;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

/**
 * Test suite related to permissions that were added to Android at a later point in the platform's lifecycle.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({PermissionChecker.class})
@SuppressLint("NewApi")
public class ApiLevelTestSuite {

    private static final int MOST_RECENT_API_LEVEL = Build.VERSION_CODES.M;
    private static final int NEEDS_PERMISSION_CHECK = 1024;

    private final Context mockContext;

    public ApiLevelTestSuite() {
        // Mock out Context
        mockContext = Mockito.mock(Context.class);
    }

    @Before
    public void beforeTest() throws Exception {
        // Reset the API level assumption
        this.resetApiLevel();

        // Mock out PermissionChecker, so that "checkSelfPermission"
        // always returns NEEDS_PERMISSION_CHECK.
        // This way, we can distinguish between auto-grants and permission-checks
        PowerMockito.mockStatic(PermissionChecker.class);
        BDDMockito.given(PermissionChecker.checkSelfPermission(any(Context.class), anyString())).willReturn(NEEDS_PERMISSION_CHECK);
    }

    @Test
    public void testAssumeApiLevelWorking() throws Exception {
        // Check that manually setting the API level to a value works
        assumeApiLevel(ICE_CREAM_SANDWICH);
        assertEquals(ICE_CREAM_SANDWICH, Build.VERSION.SDK_INT);

        // Check that resetting the API level works
        resetApiLevel();
        assertEquals(0, Build.VERSION.SDK_INT);
    }

    @Test
    public void testCheckSelfPermissionMockWorking() throws Exception {
        // Check that mocking out PermissionChecker works
        assertEquals(NEEDS_PERMISSION_CHECK, PermissionChecker.checkSelfPermission(mockContext, "permission"));
    }

    @Test
    public void testAddVoicemailPermission() throws Exception {
        // ADD_VOICEMAIL:
        // Added in API level 14 ("Ice Cream Sandwich")
        iteratePermissionCheck(Manifest.permission.ADD_VOICEMAIL, ICE_CREAM_SANDWICH);
    }

    @Test
    public void testBodySensorsPermission() throws Exception {
        // BODY_SENSORS:
        // Added in API level 20 ("KitKat Watch")
        iteratePermissionCheck(Manifest.permission.BODY_SENSORS, KITKAT_WATCH);
    }

    @Test
    public void testReadCallLogPermission() throws Exception {
        // READ_CALL_LOG:
        // Added in API level 16 ("Jelly Bean")
        iteratePermissionCheck(Manifest.permission.READ_CALL_LOG, JELLY_BEAN);
    }

    @Test
    public void testReadExternalStoragePermission() throws Exception {
        // READ_EXTERNAL_STORAGE:
        // Added in API level 16 ("Jelly Bean")
        iteratePermissionCheck(Manifest.permission.READ_EXTERNAL_STORAGE, JELLY_BEAN);
    }

    @Test
    public void testUseSipPermission() throws Exception {
        // USE_SIP:
        // Added in API level 9 ("Gingerbread")
        iteratePermissionCheck(Manifest.permission.USE_SIP, GINGERBREAD);
    }

    @Test
    public void testWriteCallLogPermission() throws Exception {
        // WRITE_CALL_LOG:
        // Added in API level 16 ("Jelly Bean")
        iteratePermissionCheck(Manifest.permission.WRITE_CALL_LOG, JELLY_BEAN);
    }

	/* Begin private */

    private void iteratePermissionCheck(String permission, int permissionMinLevel) throws Exception {
        for (int apiLevel = 0; apiLevel <= MOST_RECENT_API_LEVEL; apiLevel++) {
            // Adjust the current API level
            assumeApiLevel(apiLevel);

            // Iterate over all API levels and verify that the permission is auto-granted
            // below the minimum available level. For all other API levels, the permission
            // shouldn't be auto-granted.
            boolean shouldAutoGrantPermission = apiLevel < permissionMinLevel;
            boolean hasPermission = PermissionUtils.hasSelfPermissions(mockContext, permission);

            if (shouldAutoGrantPermission != hasPermission) {
                // Mismatch, because the permission shouldn't be granted because the API level requires a check,
                // OR the permission should be granted because it doesn't exist on the current API level
                throw new AssertionError(permission + " check on API level " + apiLevel + " shouldn't return auto-grant=" + shouldAutoGrantPermission + " amd has-permission=" + hasPermission);
            }
        }
    }

    private void assumeApiLevel(int apiLevel) throws Exception {
        // Adjust the value of Build.VERSION.SDK_INT statically using reflection
        Field sdkIntField = Build.VERSION.class.getDeclaredField("SDK_INT");
        sdkIntField.setAccessible(true);

        // Temporarily remove the SDK_INT's "final" modifier
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(sdkIntField, sdkIntField.getModifiers() & ~Modifier.FINAL);

        // Update the SDK_INT value, re-finalize the field, and lock it again
        sdkIntField.set(null, apiLevel);
        modifiersField.setInt(sdkIntField, sdkIntField.getModifiers() | Modifier.FINAL);
        sdkIntField.setAccessible(false);
    }

    private void resetApiLevel() throws Exception {
        this.assumeApiLevel(0);
    }
}