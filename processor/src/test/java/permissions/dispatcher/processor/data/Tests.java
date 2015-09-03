package permissions.dispatcher.processor.data;

import android.support.v4.app.ActivityCompat;

/**
 * Created by marcel on 03.09.15.
 */
public final class Tests {

    private Tests() {
    }

    private static final String[] EMPTY_SOURCE = {};

    public static final BaseTest FirstTest = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[] {
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.Needs;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @Needs(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[] {
                    "package tests;",
                    "import android.support.v4.app.ActivityCompat;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "    private static final int REQUEST_SHOWCAMERA = 0;",
                    "    private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "    private MyActivityPermissionsDispatcher() {",
                    "    }",
                    "    static void showCameraWithCheck(MyActivity target) {",
                    "        if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "            target.showCamera();",
                    "        } else {",
                    "            ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "        }",
                    "    }",
                    "    static void onRequestPermissionsResult(MyActivity target, int requestCode, int[] grantResults) {",
                    "        switch (requestCode) {",
                    "            case REQUEST_SHOWCAMERA:",
                    "                if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                    target.showCamera();",
                    "                }",
                    "                break;",
                    "            default:",
                    "                break;",
                    "        }",
                    "    }",
                    "}"
            };
        }
    };
}
