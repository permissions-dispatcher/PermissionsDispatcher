package permissions.dispatcher.processor.data;

import permissions.dispatcher.processor.base.BaseTest;

public final class Source {

    private Source() {
    }

    static final String[] EMPTY_SOURCE = {};

    public static final BaseTest NativeFragmentNotSupported = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Fragment;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "@RuntimePermissions",
                    "public class MyFragment extends Fragment {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return EMPTY_SOURCE;
        }
    };

    public static final BaseTest NoPermissionActivity = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return EMPTY_SOURCE;
        }
    };

    public static final BaseTest NoPermissionSupportFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.support.v4.app.Fragment;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "@RuntimePermissions",
                    "public class MyFragment extends Fragment {",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return EMPTY_SOURCE;
        }
    };

    public static final BaseTest PermissionWithNonVoidReturnType = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   int showCamera() {",
                    "       return 0;",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return EMPTY_SOURCE;
        }
    };

    public static final BaseTest RationaleWithNonVoidReturnType = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnShowRationale(Manifest.permission.CAMERA)",
                    "   int cameraRationale(PermissionRequest request) {",
                    "       return 0;",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return EMPTY_SOURCE;
        }
    };

    public static final BaseTest NeverAskWithNonVoidReturnType = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnNeverAskAgain;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnNeverAskAgain(Manifest.permission.CAMERA)",
                    "   int onNeverAskForCamera() {",
                    "       return 0;",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return EMPTY_SOURCE;
        }
    };

    public static final BaseTest DeniedWithNonVoidReturnType = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnPermissionDenied;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnPermissionDenied(Manifest.permission.CAMERA)",
                    "   int onCameraDenied() {",
                    "       return 0;",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return EMPTY_SOURCE;
        }
    };

    public static final BaseTest RationaleWithWrongParameters1 = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnShowRationale(Manifest.permission.CAMERA)",
                    "   void cameraRationale(int value) {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return EMPTY_SOURCE;
        }
    };

    public static final BaseTest RationaleWithWrongParameters2 = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera(MyPermissionRequest request) {",
                    "   }",
                    "   @OnShowRationale(Manifest.permission.CAMERA)",
                    "   void cameraRationale(int value) {",
                    "   }",
                    "   private static interface MyPermissionRequest extends PermissionRequest {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return EMPTY_SOURCE;
        }
    };

    public static final BaseTest RationaleWithoutParameters = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnShowRationale(Manifest.permission.CAMERA)",
                    "   void cameraRationale() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return EMPTY_SOURCE;
        }
    };

    public static final BaseTest DeniedWithParameters = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnPermissionDenied;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnPermissionDenied(Manifest.permission.CAMERA)",
                    "   void onCameraDenied(int value) {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return EMPTY_SOURCE;
        }
    };

    public static final BaseTest NeverAskWithParameters = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnNeverAskAgain;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnNeverAskAgain(Manifest.permission.CAMERA)",
                    "   void onNeverAskForCamera(int value) {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return EMPTY_SOURCE;
        }
    };

    public static final BaseTest PermissionWithThrows = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() throws Exception {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return EMPTY_SOURCE;
        }
    };

    public static final BaseTest RationaleWithThrows = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnShowRationale(Manifest.permission.CAMERA)",
                    "   void cameraRationale(PermissionRequest request) throws Exception {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return EMPTY_SOURCE;
        }
    };

    public static final BaseTest DeniedWithThrows = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnPermissionDenied;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnPermissionDenied(Manifest.permission.CAMERA)",
                    "   void onCameraDenied() throws Exception {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return EMPTY_SOURCE;
        }
    };

    public static final BaseTest NeverAskWithThrows = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnNeverAskAgain;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnNeverAskAgain(Manifest.permission.CAMERA)",
                    "   void onNeverAskForCamera() throws Exception {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return EMPTY_SOURCE;
        }
    };

    public static final BaseTest PrivatePermission = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   private void showCamera() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return EMPTY_SOURCE;
        }
    };

    public static final BaseTest PrivateRationale = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnShowRationale(Manifest.permission.CAMERA)",
                    "   private void cameraRationale(PermissionRequest request) {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return EMPTY_SOURCE;
        }
    };

    public static final BaseTest PrivateDenied = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnPermissionDenied;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnPermissionDenied(Manifest.permission.CAMERA)",
                    "   private void onCameraDenied() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return EMPTY_SOURCE;
        }
    };

    public static final BaseTest PrivateNeverAsk = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnNeverAskAgain;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnNeverAskAgain(Manifest.permission.CAMERA)",
                    "   private void onNeverAskForCamera() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return EMPTY_SOURCE;
        }
    };

    public static final BaseTest WrongAnnotatedClass = new BaseTest() {
        @Override
        protected String getName() {
            return "MyService";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Service;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "@RuntimePermissions",
                    "public class MyService extends Service {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return EMPTY_SOURCE;
        }
    };

    public static final BaseTest DuplicatedRationale = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnShowRationale(Manifest.permission.CAMERA)",
                    "   void cameraRationale(PermissionRequest request) {",
                    "   }",
                    "   @OnShowRationale(Manifest.permission.CAMERA)",
                    "   void cameraRationale2(PermissionRequest request) {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return EMPTY_SOURCE;
        }
    };

    public static final BaseTest DuplicatedDenied = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnPermissionDenied;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnPermissionDenied(Manifest.permission.CAMERA)",
                    "   void onCameraDenied() {",
                    "   }",
                    "   @OnPermissionDenied(Manifest.permission.CAMERA)",
                    "   void onCameraDenied2() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return EMPTY_SOURCE;
        }
    };

    public static final BaseTest DuplicatedNeverAsk = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnNeverAskAgain;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnNeverAskAgain(Manifest.permission.CAMERA)",
                    "   void onNeverAskForCamera() {",
                    "   }",
                    "   @OnNeverAskAgain(Manifest.permission.CAMERA)",
                    "   void onNeverAskForCamera2() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return EMPTY_SOURCE;
        }
    };

    public static final BaseTest OnePermissionActivity = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.support.v4.app.ActivityCompat;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyActivity target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               }",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest OnePermissionSupportFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.support.v4.app.Fragment;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "@RuntimePermissions",
                    "public class MyFragment extends Fragment {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.app.Activity;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyFragment target) {",
                    "       Activity activity = target.getActivity();",
                    "       if (PermissionUtils.hasSelfPermissions(activity, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyFragment target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               }",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest OnePermissionWithParametersActivity = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera(int value) {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.support.v4.app.ActivityCompat;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.GrantableRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static GrantableRequest PENDING_SHOWCAMERA;",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyActivity target, int value) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera(value);",
                    "       } else {",
                    "           PENDING_SHOWCAMERA = new ShowCameraPermissionRequest(target, value);",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyActivity target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   if (PENDING_SHOWCAMERA != null) {",
                    "                       PENDING_SHOWCAMERA.grant();",
                    "                   }",
                    "               }",
                    "               PENDING_SHOWCAMERA = null;",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "   private static final class ShowCameraPermissionRequest implements GrantableRequest {",
                    "        private final WeakReference<MyActivity> weakTarget;",
                    "        private final int value;",
                    "        private ShowCameraPermissionRequest(MyActivity target, int value) {",
                    "            this.weakTarget = new WeakReference<>(target);",
                    "            this.value = value;",
                    "        }",
                    "        @Override",
                    "        public void proceed() {",
                    "            MyActivity target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "        }",
                    "        @Override",
                    "        public void cancel() {",
                    "        }",
                    "        @Override",
                    "        public void grant() {",
                    "            MyActivity target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            target.showCamera(value);",
                    "        }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest OnePermissionWithParametersAndRationaleActivity = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera(int value) {",
                    "   }",
                    "   @OnShowRationale(Manifest.permission.CAMERA)",
                    "   void cameraRationale(PermissionRequest request) {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.support.v4.app.ActivityCompat;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.GrantableRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static GrantableRequest PENDING_SHOWCAMERA;",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyActivity target, int value) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera(value);",
                    "       } else {",
                    "           PENDING_SHOWCAMERA = new ShowCameraPermissionRequest(target, value);",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(PENDING_SHOWCAMERA);",
                    "           } else {",
                    "               ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyActivity target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   if (PENDING_SHOWCAMERA != null) {",
                    "                       PENDING_SHOWCAMERA.grant();",
                    "                   }",
                    "               }",
                    "               PENDING_SHOWCAMERA = null;",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "   private static final class ShowCameraPermissionRequest implements GrantableRequest {",
                    "        private final WeakReference<MyActivity> weakTarget;",
                    "        private final int value;",
                    "        private ShowCameraPermissionRequest(MyActivity target, int value) {",
                    "            this.weakTarget = new WeakReference<>(target);",
                    "            this.value = value;",
                    "        }",
                    "        @Override",
                    "        public void proceed() {",
                    "            MyActivity target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "        }",
                    "        @Override",
                    "        public void cancel() {",
                    "        }",
                    "        @Override",
                    "        public void grant() {",
                    "            MyActivity target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            target.showCamera(value);",
                    "        }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest OnePermissionWithParametersAndDeniedActivity = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnPermissionDenied;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera(int value) {",
                    "   }",
                    "   @OnPermissionDenied(Manifest.permission.CAMERA)",
                    "   void cameraDenied() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.support.v4.app.ActivityCompat;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.GrantableRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static GrantableRequest PENDING_SHOWCAMERA;",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyActivity target, int value) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera(value);",
                    "       } else {",
                    "           PENDING_SHOWCAMERA = new ShowCameraPermissionRequest(target, value);",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyActivity target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   if (PENDING_SHOWCAMERA != null) {",
                    "                       PENDING_SHOWCAMERA.grant();",
                    "                   }",
                    "               } else {",
                    "                   target.cameraDenied();",
                    "               }",
                    "               PENDING_SHOWCAMERA = null;",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "   private static final class ShowCameraPermissionRequest implements GrantableRequest {",
                    "        private final WeakReference<MyActivity> weakTarget;",
                    "        private final int value;",
                    "        private ShowCameraPermissionRequest(MyActivity target, int value) {",
                    "            this.weakTarget = new WeakReference<>(target);",
                    "            this.value = value;",
                    "        }",
                    "        @Override",
                    "        public void proceed() {",
                    "            MyActivity target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "        }",
                    "        @Override",
                    "        public void cancel() {",
                    "            MyActivity target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            target.cameraDenied();",
                    "        }",
                    "        @Override",
                    "        public void grant() {",
                    "            MyActivity target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            target.showCamera(value);",
                    "        }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest OnePermissionWithParametersRationaleAndDeniedActivity = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.OnPermissionDenied;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera(int value) {",
                    "   }",
                    "   @OnShowRationale(Manifest.permission.CAMERA)",
                    "   void cameraRationale(PermissionRequest request) {",
                    "   }",
                    "   @OnPermissionDenied(Manifest.permission.CAMERA)",
                    "   void cameraDenied() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.support.v4.app.ActivityCompat;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.GrantableRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static GrantableRequest PENDING_SHOWCAMERA;",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyActivity target, int value) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera(value);",
                    "       } else {",
                    "           PENDING_SHOWCAMERA = new ShowCameraPermissionRequest(target, value);",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(PENDING_SHOWCAMERA);",
                    "           } else {",
                    "               ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyActivity target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   if (PENDING_SHOWCAMERA != null) {",
                    "                       PENDING_SHOWCAMERA.grant();",
                    "                   }",
                    "               } else {",
                    "                   target.cameraDenied();",
                    "               }",
                    "               PENDING_SHOWCAMERA = null;",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "   private static final class ShowCameraPermissionRequest implements GrantableRequest {",
                    "        private final WeakReference<MyActivity> weakTarget;",
                    "        private final int value;",
                    "        private ShowCameraPermissionRequest(MyActivity target, int value) {",
                    "            this.weakTarget = new WeakReference<>(target);",
                    "            this.value = value;",
                    "        }",
                    "        @Override",
                    "        public void proceed() {",
                    "            MyActivity target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "        }",
                    "        @Override",
                    "        public void cancel() {",
                    "            MyActivity target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            target.cameraDenied();",
                    "        }",
                    "        @Override",
                    "        public void grant() {",
                    "            MyActivity target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            target.showCamera(value);",
                    "        }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest OnePermissionWithNeverAskActivity = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnNeverAskAgain;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnNeverAskAgain(Manifest.permission.CAMERA)",
                    "   void onNeverAskForCamera() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.support.v4.app.ActivityCompat;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyActivity target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               } else {",
                    "                   if (!PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "                       target.onNeverAskForCamera();",
                    "                   }",
                    "               }",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest OnePermissionWithNeverAskAndRationaleActivity = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.OnNeverAskAgain;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnShowRationale(Manifest.permission.CAMERA)",
                    "   void cameraRationale(PermissionRequest request) {",
                    "   }",
                    "   @OnNeverAskAgain(Manifest.permission.CAMERA)",
                    "   void onNeverAskForCamera() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.support.v4.app.ActivityCompat;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(new ShowCameraPermissionRequest(target));",
                    "           } else {",
                    "               ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyActivity target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               } else {",
                    "                   if (!PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "                       target.onNeverAskForCamera();",
                    "                   }",
                    "               }",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "   private static final class ShowCameraPermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyActivity> weakTarget;",
                    "        private ShowCameraPermissionRequest(MyActivity target) {",
                    "            this.weakTarget = new WeakReference<>(target);",
                    "        }",
                    "        @Override",
                    "        public void proceed() {",
                    "            MyActivity target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "        }",
                    "        @Override",
                    "        public void cancel() {",
                    "        }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest OnePermissionWithNeverAskRationaleAndDeniedActivity = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.OnPermissionDenied;",
                    "import permissions.dispatcher.OnNeverAskAgain;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnShowRationale(Manifest.permission.CAMERA)",
                    "   void cameraRationale(PermissionRequest request) {",
                    "   }",
                    "   @OnPermissionDenied(Manifest.permission.CAMERA)",
                    "   void cameraDenied() {",
                    "   }",
                    "   @OnNeverAskAgain(Manifest.permission.CAMERA)",
                    "   void onNeverAskForCamera() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.support.v4.app.ActivityCompat;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(new ShowCameraPermissionRequest(target));",
                    "           } else {",
                    "               ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyActivity target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               } else {",
                    "                   if (!PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "                       target.onNeverAskForCamera();",
                    "                   } else {",
                    "                       target.cameraDenied();",
                    "                   }",
                    "               }",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "   private static final class ShowCameraPermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyActivity> weakTarget;",
                    "        private ShowCameraPermissionRequest(MyActivity target) {",
                    "            this.weakTarget = new WeakReference<>(target);",
                    "        }",
                    "        @Override",
                    "        public void proceed() {",
                    "            MyActivity target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "        }",
                    "        @Override",
                    "        public void cancel() {",
                    "            MyActivity target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            target.cameraDenied();",
                    "        }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest OnePermissionWithNeverAskAndDeniedActivity = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnPermissionDenied;",
                    "import permissions.dispatcher.OnNeverAskAgain;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnPermissionDenied(Manifest.permission.CAMERA)",
                    "   void cameraDenied() {",
                    "   }",
                    "   @OnNeverAskAgain(Manifest.permission.CAMERA)",
                    "   void onNeverAskForCamera() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.support.v4.app.ActivityCompat;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyActivity target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               } else {",
                    "                   if (!PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "                       target.onNeverAskForCamera();",
                    "                   } else {",
                    "                       target.cameraDenied();",
                    "                   }",
                    "               }",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest TwoPermissionsWithNeverAskActivity = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnNeverAskAgain;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnNeverAskAgain(Manifest.permission.CAMERA)",
                    "   void onNeverAskForCamera() {",
                    "   }",
                    "   @NeedsPermission(Manifest.permission.READ_CONTACTS)",
                    "   void accessContacts() {",
                    "   }",
                    "   @OnNeverAskAgain(Manifest.permission.READ_CONTACTS)",
                    "   void onNeverAskForContacts() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.support.v4.app.ActivityCompat;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static final int REQUEST_ACCESSCONTACTS = 1;",
                    "   private static final String[] PERMISSION_ACCESSCONTACTS = new String[] {\"android.permission.READ_CONTACTS\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void accessContactsWithCheck(MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_ACCESSCONTACTS)) {",
                    "           target.accessContacts();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_ACCESSCONTACTS, REQUEST_ACCESSCONTACTS);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyActivity target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               } else {",
                    "                   if (!PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "                       target.onNeverAskForCamera();",
                    "                   }",
                    "               }",
                    "               break;",
                    "           case REQUEST_ACCESSCONTACTS:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.accessContacts();",
                    "               } else {",
                    "                   if (!PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_ACCESSCONTACTS)) {",
                    "                       target.onNeverAskForContacts();",
                    "                   }",
                    "               }",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest TwoPermissionsActivity = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @NeedsPermission(Manifest.permission.READ_CONTACTS)",
                    "   void accessContacts() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.support.v4.app.ActivityCompat;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static final int REQUEST_ACCESSCONTACTS = 1;",
                    "   private static final String[] PERMISSION_ACCESSCONTACTS = new String[] {\"android.permission.READ_CONTACTS\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void accessContactsWithCheck(MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_ACCESSCONTACTS)) {",
                    "           target.accessContacts();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_ACCESSCONTACTS, REQUEST_ACCESSCONTACTS);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyActivity target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               }",
                    "               break;",
                    "           case REQUEST_ACCESSCONTACTS:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.accessContacts();",
                    "               }",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest TwoPermissionsAtOnceActivity = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_CONTACTS})",
                    "   void showCamera() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.support.v4.app.ActivityCompat;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\", \"android.permission.READ_CONTACTS\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyActivity target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               }",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest TwoPermissionsSupportFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.support.v4.app.Fragment;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "@RuntimePermissions",
                    "public class MyFragment extends Fragment {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @NeedsPermission(Manifest.permission.READ_CONTACTS)",
                    "   void accessContacts() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.app.Activity;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static final int REQUEST_ACCESSCONTACTS = 1;",
                    "   private static final String[] PERMISSION_ACCESSCONTACTS = new String[] {\"android.permission.READ_CONTACTS\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyFragment target) {",
                    "       Activity activity = target.getActivity();",
                    "       if (PermissionUtils.hasSelfPermissions(activity, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void accessContactsWithCheck(MyFragment target) {",
                    "       Activity activity = target.getActivity();",
                    "       if (PermissionUtils.hasSelfPermissions(activity, PERMISSION_ACCESSCONTACTS)) {",
                    "           target.accessContacts();",
                    "       } else {",
                    "           target.requestPermissions(PERMISSION_ACCESSCONTACTS, REQUEST_ACCESSCONTACTS);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyFragment target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               }",
                    "               break;",
                    "           case REQUEST_ACCESSCONTACTS:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.accessContacts();",
                    "               }",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest TwoPermissionsWithSameSignatureActivity = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera2() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.support.v4.app.ActivityCompat;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static final int REQUEST_SHOWCAMERA2 = 1;",
                    "   private static final String[] PERMISSION_SHOWCAMERA2 = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void showCamera2WithCheck(MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA2)) {",
                    "           target.showCamera2();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA2, REQUEST_SHOWCAMERA2);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyActivity target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               }",
                    "               break;",
                    "           case REQUEST_SHOWCAMERA2:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera2();",
                    "               }",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest TwoPermissionsWithSameSignatureAndRationaleActivity = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera2() {",
                    "   }",
                    "   @OnShowRationale(Manifest.permission.CAMERA)",
                    "   void cameraRationale(PermissionRequest request) {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.support.v4.app.ActivityCompat;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static final int REQUEST_SHOWCAMERA2 = 1;",
                    "   private static final String[] PERMISSION_SHOWCAMERA2 = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(new ShowCameraPermissionRequest(target));",
                    "           } else {",
                    "               ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void showCamera2WithCheck(MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA2)) {",
                    "           target.showCamera2();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA2)) {",
                    "               target.cameraRationale(new ShowCamera2PermissionRequest(target));",
                    "           } else {",
                    "               ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA2, REQUEST_SHOWCAMERA2);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyActivity target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               }",
                    "               break;",
                    "           case REQUEST_SHOWCAMERA2:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera2();",
                    "               }",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "   private static final class ShowCameraPermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyActivity> weakTarget;",
                    "        private ShowCameraPermissionRequest(MyActivity target) {",
                    "            this.weakTarget = new WeakReference<>(target);",
                    "        }",
                    "        @Override",
                    "        public void proceed() {",
                    "            MyActivity target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "        }",
                    "        @Override",
                    "        public void cancel() {",
                    "        }",
                    "   }",
                    "   private static final class ShowCamera2PermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyActivity> weakTarget;",
                    "        private ShowCamera2PermissionRequest(MyActivity target) {",
                    "            this.weakTarget = new WeakReference<>(target);",
                    "        }",
                    "        @Override",
                    "        public void proceed() {",
                    "            MyActivity target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA2, REQUEST_SHOWCAMERA2);",
                    "        }",
                    "        @Override",
                    "        public void cancel() {",
                    "        }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest TwoPermissionsWithSameSignatureAndDeniedActivity = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnPermissionDenied;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera2() {",
                    "   }",
                    "   @OnPermissionDenied(Manifest.permission.CAMERA)",
                    "   void cameraDenied() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.support.v4.app.ActivityCompat;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static final int REQUEST_SHOWCAMERA2 = 1;",
                    "   private static final String[] PERMISSION_SHOWCAMERA2 = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void showCamera2WithCheck(MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA2)) {",
                    "           target.showCamera2();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA2, REQUEST_SHOWCAMERA2);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyActivity target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               } else {",
                    "                   target.cameraDenied();",
                    "               }",
                    "               break;",
                    "           case REQUEST_SHOWCAMERA2:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera2();",
                    "               } else {",
                    "                   target.cameraDenied();",
                    "               }",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest TwoPermissionsWithSameSignatureRationaleAndDeniedActivity = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.OnPermissionDenied;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera2() {",
                    "   }",
                    "   @OnShowRationale(Manifest.permission.CAMERA)",
                    "   void cameraRationale(PermissionRequest request) {",
                    "   }",
                    "   @OnPermissionDenied(Manifest.permission.CAMERA)",
                    "   void cameraDenied() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.support.v4.app.ActivityCompat;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static final int REQUEST_SHOWCAMERA2 = 1;",
                    "   private static final String[] PERMISSION_SHOWCAMERA2 = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(new ShowCameraPermissionRequest(target));",
                    "           } else {",
                    "               ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void showCamera2WithCheck(MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA2)) {",
                    "           target.showCamera2();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA2)) {",
                    "               target.cameraRationale(new ShowCamera2PermissionRequest(target));",
                    "           } else {",
                    "               ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA2, REQUEST_SHOWCAMERA2);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyActivity target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               } else {",
                    "                   target.cameraDenied();",
                    "               }",
                    "               break;",
                    "           case REQUEST_SHOWCAMERA2:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera2();",
                    "               } else {",
                    "                   target.cameraDenied();",
                    "               }",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "   private static final class ShowCameraPermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyActivity> weakTarget;",
                    "        private ShowCameraPermissionRequest(MyActivity target) {",
                    "            this.weakTarget = new WeakReference<>(target);",
                    "        }",
                    "        @Override",
                    "        public void proceed() {",
                    "            MyActivity target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "        }",
                    "        @Override",
                    "        public void cancel() {",
                    "            MyActivity target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            target.cameraDenied();",
                    "        }",
                    "   }",
                    "   private static final class ShowCamera2PermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyActivity> weakTarget;",
                    "        private ShowCamera2PermissionRequest(MyActivity target) {",
                    "            this.weakTarget = new WeakReference<>(target);",
                    "        }",
                    "        @Override",
                    "        public void proceed() {",
                    "            MyActivity target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA2, REQUEST_SHOWCAMERA2);",
                    "        }",
                    "        @Override",
                    "        public void cancel() {",
                    "            MyActivity target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            target.cameraDenied();",
                    "        }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest OnePermissionWithRationaleActivity = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnShowRationale(Manifest.permission.CAMERA)",
                    "   void cameraRationale(PermissionRequest request) {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.support.v4.app.ActivityCompat;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(new ShowCameraPermissionRequest(target));",
                    "           } else {",
                    "               ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyActivity target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               }",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "   private static final class ShowCameraPermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyActivity> weakTarget;",
                    "        private ShowCameraPermissionRequest(MyActivity target) {",
                    "            this.weakTarget = new WeakReference<>(target);",
                    "        }",
                    "        @Override",
                    "        public void proceed() {",
                    "            MyActivity target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "        }",
                    "        @Override",
                    "        public void cancel() {",
                    "        }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest OnePermissionWithRationaleSupportFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.support.v4.app.Fragment;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "@RuntimePermissions",
                    "public class MyFragment extends Fragment {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnShowRationale(Manifest.permission.CAMERA)",
                    "   void cameraRationale(PermissionRequest request) {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.app.Activity;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyFragment target) {",
                    "       Activity activity = target.getActivity();",
                    "       if (PermissionUtils.hasSelfPermissions(activity, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(activity, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(new ShowCameraPermissionRequest(target));",
                    "           } else {",
                    "               target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyFragment target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               }",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "   private static final class ShowCameraPermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyFragment> weakTarget;",
                    "        private ShowCameraPermissionRequest(MyFragment target) {",
                    "            this.weakTarget = new WeakReference<>(target);",
                    "        }",
                    "        @Override",
                    "        public void proceed() {",
                    "            MyFragment target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "        }",
                    "        @Override",
                    "        public void cancel() {",
                    "        }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest OnePermissionWithNeverAskAndRationaleSupportFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.support.v4.app.Fragment;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.OnNeverAskAgain;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "@RuntimePermissions",
                    "public class MyFragment extends Fragment {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnShowRationale(Manifest.permission.CAMERA)",
                    "   void cameraRationale(PermissionRequest request) {",
                    "   }",
                    "   @OnNeverAskAgain(Manifest.permission.CAMERA)",
                    "   void onNeverAskForCamera() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.app.Activity;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyFragment target) {",
                    "       Activity activity = target.getActivity();",
                    "       if (PermissionUtils.hasSelfPermissions(activity, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(activity, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(new ShowCameraPermissionRequest(target));",
                    "           } else {",
                    "               target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyFragment target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               } else {",
                    "                   if (!PermissionUtils.shouldShowRequestPermissionRationale(target.getActivity(), PERMISSION_SHOWCAMERA)) {",
                    "                       target.onNeverAskForCamera();",
                    "                   }",
                    "               }",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "   private static final class ShowCameraPermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyFragment> weakTarget;",
                    "        private ShowCameraPermissionRequest(MyFragment target) {",
                    "            this.weakTarget = new WeakReference<>(target);",
                    "        }",
                    "        @Override",
                    "        public void proceed() {",
                    "            MyFragment target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "        }",
                    "        @Override",
                    "        public void cancel() {",
                    "        }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest OnePermissionWithNeverAskRationaleAndDeniedSupportFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.support.v4.app.Fragment;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.OnPermissionDenied;",
                    "import permissions.dispatcher.OnNeverAskAgain;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "@RuntimePermissions",
                    "public class MyFragment extends Fragment {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnShowRationale(Manifest.permission.CAMERA)",
                    "   void cameraRationale(PermissionRequest request) {",
                    "   }",
                    "   @OnPermissionDenied(Manifest.permission.CAMERA)",
                    "   void cameraDenied() {",
                    "   }",
                    "   @OnNeverAskAgain(Manifest.permission.CAMERA)",
                    "   void onNeverAskForCamera() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.app.Activity;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyFragment target) {",
                    "       Activity activity = target.getActivity();",
                    "       if (PermissionUtils.hasSelfPermissions(activity, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(activity, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(new ShowCameraPermissionRequest(target));",
                    "           } else {",
                    "               target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyFragment target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               } else {",
                    "                   if (!PermissionUtils.shouldShowRequestPermissionRationale(target.getActivity(), PERMISSION_SHOWCAMERA)) {",
                    "                       target.onNeverAskForCamera();",
                    "                   } else {",
                    "                       target.cameraDenied();",
                    "                   }",
                    "               }",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "   private static final class ShowCameraPermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyFragment> weakTarget;",
                    "        private ShowCameraPermissionRequest(MyFragment target) {",
                    "            this.weakTarget = new WeakReference<>(target);",
                    "        }",
                    "        @Override",
                    "        public void proceed() {",
                    "            MyFragment target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "        }",
                    "        @Override",
                    "        public void cancel() {",
                    "            MyFragment target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            target.cameraDenied();",
                    "        }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest OnePermissionWithNeverAskSupportFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.support.v4.app.Fragment;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnNeverAskAgain;",
                    "@RuntimePermissions",
                    "public class MyFragment extends Fragment {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnNeverAskAgain(Manifest.permission.CAMERA)",
                    "   void onNeverAskForCamera() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.app.Activity;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyFragment target) {",
                    "       Activity activity = target.getActivity();",
                    "       if (PermissionUtils.hasSelfPermissions(activity, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyFragment target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               } else {",
                    "                   if (!PermissionUtils.shouldShowRequestPermissionRationale(target.getActivity(), PERMISSION_SHOWCAMERA)) {",
                    "                       target.onNeverAskForCamera();",
                    "                   }",
                    "               }",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest OnePermissionWithNeverAskAndDeniedSupportFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.support.v4.app.Fragment;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnNeverAskAgain;",
                    "import permissions.dispatcher.OnPermissionDenied;",
                    "@RuntimePermissions",
                    "public class MyFragment extends Fragment {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnNeverAskAgain(Manifest.permission.CAMERA)",
                    "   void onNeverAskForCamera() {",
                    "   }",
                    "   @OnPermissionDenied(Manifest.permission.CAMERA)",
                    "   void cameraDenied() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.app.Activity;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyFragment target) {",
                    "       Activity activity = target.getActivity();",
                    "       if (PermissionUtils.hasSelfPermissions(activity, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyFragment target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               } else {",
                    "                   if (!PermissionUtils.shouldShowRequestPermissionRationale(target.getActivity(), PERMISSION_SHOWCAMERA)) {",
                    "                       target.onNeverAskForCamera();",
                    "                   } else {",
                    "                       target.cameraDenied();",
                    "                   }",
                    "               }",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest OnePermissionWithParametersSupportFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.support.v4.app.Fragment;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "@RuntimePermissions",
                    "public class MyFragment extends Fragment {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera(int value, String name) {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.app.Activity;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.GrantableRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static GrantableRequest PENDING_SHOWCAMERA;",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyFragment target, int value, String name) {",
                    "       Activity activity = target.getActivity();",
                    "       if (PermissionUtils.hasSelfPermissions(activity, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera(value, name);",
                    "       } else {",
                    "           PENDING_SHOWCAMERA = new ShowCameraPermissionRequest(target, value, name);",
                    "           target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyFragment target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   if (PENDING_SHOWCAMERA != null) {",
                    "                       PENDING_SHOWCAMERA.grant();",
                    "                   }",
                    "               }",
                    "               PENDING_SHOWCAMERA = null;",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "   private static final class ShowCameraPermissionRequest implements GrantableRequest {",
                    "        private final WeakReference<MyFragment> weakTarget;",
                    "        private final int value;",
                    "        private final String name;",
                    "        private ShowCameraPermissionRequest(MyFragment target, int value, String name) {",
                    "            this.weakTarget = new WeakReference<>(target);",
                    "            this.value = value;",
                    "            this.name = name;",
                    "        }",
                    "        @Override",
                    "        public void proceed() {",
                    "            MyFragment target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "        }",
                    "        @Override",
                    "        public void cancel() {",
                    "        }",
                    "        @Override",
                    "        public void grant() {",
                    "            MyFragment target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            target.showCamera(value, name);",
                    "        }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest OnePermissionWithParametersAndRationaleSupportFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.support.v4.app.Fragment;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "@RuntimePermissions",
                    "public class MyFragment extends Fragment {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera(int value, String name) {",
                    "   }",
                    "   @OnShowRationale(Manifest.permission.CAMERA)",
                    "   void cameraRationale(PermissionRequest request) {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.app.Activity;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.GrantableRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static GrantableRequest PENDING_SHOWCAMERA;",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyFragment target, int value, String name) {",
                    "       Activity activity = target.getActivity();",
                    "       if (PermissionUtils.hasSelfPermissions(activity, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera(value, name);",
                    "       } else {",
                    "           PENDING_SHOWCAMERA = new ShowCameraPermissionRequest(target, value, name);",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(activity, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(PENDING_SHOWCAMERA);",
                    "           } else {",
                    "               target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyFragment target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   if (PENDING_SHOWCAMERA != null) {",
                    "                       PENDING_SHOWCAMERA.grant();",
                    "                   }",
                    "               }",
                    "               PENDING_SHOWCAMERA = null;",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "   private static final class ShowCameraPermissionRequest implements GrantableRequest {",
                    "        private final WeakReference<MyFragment> weakTarget;",
                    "        private final int value;",
                    "        private final String name;",
                    "        private ShowCameraPermissionRequest(MyFragment target, int value, String name) {",
                    "            this.weakTarget = new WeakReference<>(target);",
                    "            this.value = value;",
                    "            this.name = name;",
                    "        }",
                    "        @Override",
                    "        public void proceed() {",
                    "            MyFragment target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "        }",
                    "        @Override",
                    "        public void cancel() {",
                    "        }",
                    "        @Override",
                    "        public void grant() {",
                    "            MyFragment target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            target.showCamera(value, name);",
                    "        }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest OnePermissionWithParametersAndDeniedSupportFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.support.v4.app.Fragment;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnPermissionDenied;",
                    "@RuntimePermissions",
                    "public class MyFragment extends Fragment {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera(int value, String name) {",
                    "   }",
                    "   @OnPermissionDenied(Manifest.permission.CAMERA)",
                    "   void cameraDenied() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.app.Activity;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.GrantableRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static GrantableRequest PENDING_SHOWCAMERA;",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyFragment target, int value, String name) {",
                    "       Activity activity = target.getActivity();",
                    "       if (PermissionUtils.hasSelfPermissions(activity, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera(value, name);",
                    "       } else {",
                    "           PENDING_SHOWCAMERA = new ShowCameraPermissionRequest(target, value, name);",
                    "           target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyFragment target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   if (PENDING_SHOWCAMERA != null) {",
                    "                       PENDING_SHOWCAMERA.grant();",
                    "                   }",
                    "               } else {",
                    "                   target.cameraDenied();",
                    "               }",
                    "               PENDING_SHOWCAMERA = null;",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "   private static final class ShowCameraPermissionRequest implements GrantableRequest {",
                    "        private final WeakReference<MyFragment> weakTarget;",
                    "        private final int value;",
                    "        private final String name;",
                    "        private ShowCameraPermissionRequest(MyFragment target, int value, String name) {",
                    "            this.weakTarget = new WeakReference<>(target);",
                    "            this.value = value;",
                    "            this.name = name;",
                    "        }",
                    "        @Override",
                    "        public void proceed() {",
                    "            MyFragment target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "        }",
                    "        @Override",
                    "        public void cancel() {",
                    "            MyFragment target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            target.cameraDenied();",
                    "        }",
                    "        @Override",
                    "        public void grant() {",
                    "            MyFragment target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            target.showCamera(value, name);",
                    "        }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest OnePermissionWithParametersRationaleAndDeniedSupportFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.support.v4.app.Fragment;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.OnPermissionDenied;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "@RuntimePermissions",
                    "public class MyFragment extends Fragment {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera(int value, String name) {",
                    "   }",
                    "   @OnShowRationale(Manifest.permission.CAMERA)",
                    "   void cameraRationale(PermissionRequest request) {",
                    "   }",
                    "   @OnPermissionDenied(Manifest.permission.CAMERA)",
                    "   void cameraDenied() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.app.Activity;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.GrantableRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static GrantableRequest PENDING_SHOWCAMERA;",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyFragment target, int value, String name) {",
                    "       Activity activity = target.getActivity();",
                    "       if (PermissionUtils.hasSelfPermissions(activity, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera(value, name);",
                    "       } else {",
                    "           PENDING_SHOWCAMERA = new ShowCameraPermissionRequest(target, value, name);",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(activity, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(PENDING_SHOWCAMERA);",
                    "           } else {",
                    "               target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyFragment target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   if (PENDING_SHOWCAMERA != null) {",
                    "                       PENDING_SHOWCAMERA.grant();",
                    "                   }",
                    "               } else {",
                    "                   target.cameraDenied();",
                    "               }",
                    "               PENDING_SHOWCAMERA = null;",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "   private static final class ShowCameraPermissionRequest implements GrantableRequest {",
                    "        private final WeakReference<MyFragment> weakTarget;",
                    "        private final int value;",
                    "        private final String name;",
                    "        private ShowCameraPermissionRequest(MyFragment target, int value, String name) {",
                    "            this.weakTarget = new WeakReference<>(target);",
                    "            this.value = value;",
                    "            this.name = name;",
                    "        }",
                    "        @Override",
                    "        public void proceed() {",
                    "            MyFragment target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "        }",
                    "        @Override",
                    "        public void cancel() {",
                    "            MyFragment target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            target.cameraDenied();",
                    "        }",
                    "        @Override",
                    "        public void grant() {",
                    "            MyFragment target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            target.showCamera(value, name);",
                    "        }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest TwoPermissionsWithSameSignatureSupportFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.support.v4.app.Fragment;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "@RuntimePermissions",
                    "public class MyFragment extends Fragment {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera2() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.app.Activity;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static final int REQUEST_SHOWCAMERA2 = 1;",
                    "   private static final String[] PERMISSION_SHOWCAMERA2 = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyFragment target) {",
                    "       Activity activity = target.getActivity();",
                    "       if (PermissionUtils.hasSelfPermissions(activity, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void showCamera2WithCheck(MyFragment target) {",
                    "       Activity activity = target.getActivity();",
                    "       if (PermissionUtils.hasSelfPermissions(activity, PERMISSION_SHOWCAMERA2)) {",
                    "           target.showCamera2();",
                    "       } else {",
                    "           target.requestPermissions(PERMISSION_SHOWCAMERA2, REQUEST_SHOWCAMERA2);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyFragment target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               }",
                    "               break;",
                    "           case REQUEST_SHOWCAMERA2:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera2();",
                    "               }",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest TwoPermissionsWithSameSignatureAndRationaleSupportFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.support.v4.app.Fragment;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "@RuntimePermissions",
                    "public class MyFragment extends Fragment {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera2() {",
                    "   }",
                    "   @OnShowRationale(Manifest.permission.CAMERA)",
                    "   void cameraRationale(PermissionRequest request) {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.app.Activity;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static final int REQUEST_SHOWCAMERA2 = 1;",
                    "   private static final String[] PERMISSION_SHOWCAMERA2 = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyFragment target) {",
                    "       Activity activity = target.getActivity();",
                    "       if (PermissionUtils.hasSelfPermissions(activity, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(activity, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(new ShowCameraPermissionRequest(target));",
                    "           } else {",
                    "               target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void showCamera2WithCheck(MyFragment target) {",
                    "       Activity activity = target.getActivity();",
                    "       if (PermissionUtils.hasSelfPermissions(activity, PERMISSION_SHOWCAMERA2)) {",
                    "           target.showCamera2();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(activity, PERMISSION_SHOWCAMERA2)) {",
                    "               target.cameraRationale(new ShowCamera2PermissionRequest(target));",
                    "           } else {",
                    "               target.requestPermissions(PERMISSION_SHOWCAMERA2, REQUEST_SHOWCAMERA2);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyFragment target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               }",
                    "               break;",
                    "           case REQUEST_SHOWCAMERA2:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera2();",
                    "               }",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "   private static final class ShowCameraPermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyFragment> weakTarget;",
                    "        private ShowCameraPermissionRequest(MyFragment target) {",
                    "            this.weakTarget = new WeakReference<>(target);",
                    "        }",
                    "        @Override",
                    "        public void proceed() {",
                    "            MyFragment target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "        }",
                    "        @Override",
                    "        public void cancel() {",
                    "        }",
                    "   }",
                    "   private static final class ShowCamera2PermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyFragment> weakTarget;",
                    "        private ShowCameraPermissionRequest(MyFragment target) {",
                    "            this.weakTarget = new WeakReference<>(target);",
                    "        }",
                    "        @Override",
                    "        public void proceed() {",
                    "            MyFragment target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            target.requestPermissions(PERMISSION_SHOWCAMERA2, REQUEST_SHOWCAMERA2);",
                    "        }",
                    "        @Override",
                    "        public void cancel() {",
                    "        }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest TwoPermissionsWithSameSignatureAndDeniedSupportFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.support.v4.app.Fragment;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnPermissionDenied;",
                    "@RuntimePermissions",
                    "public class MyFragment extends Fragment {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera2() {",
                    "   }",
                    "   @OnPermissionDenied(Manifest.permission.CAMERA)",
                    "   void cameraDenied() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.app.Activity;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static final int REQUEST_SHOWCAMERA2 = 1;",
                    "   private static final String[] PERMISSION_SHOWCAMERA2 = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyFragment target) {",
                    "       Activity activity = target.getActivity();",
                    "       if (PermissionUtils.hasSelfPermissions(activity, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void showCamera2WithCheck(MyFragment target) {",
                    "       Activity activity = target.getActivity();",
                    "       if (PermissionUtils.hasSelfPermissions(activity, PERMISSION_SHOWCAMERA2)) {",
                    "           target.showCamera2();",
                    "       } else {",
                    "           target.requestPermissions(PERMISSION_SHOWCAMERA2, REQUEST_SHOWCAMERA2);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyFragment target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               } else {",
                    "                   target.cameraDenied();",
                    "               }",
                    "               break;",
                    "           case REQUEST_SHOWCAMERA2:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera2();",
                    "               } else {",
                    "                   target.cameraDenied();",
                    "               }",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest TwoPermissionsWithSameSignatureRationaleAndDeniedSupportFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.support.v4.app.Fragment;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.OnPermissionDenied;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "@RuntimePermissions",
                    "public class MyFragment extends Fragment {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera2() {",
                    "   }",
                    "   @OnShowRationale(Manifest.permission.CAMERA)",
                    "   void cameraRationale(PermissionRequest request) {",
                    "   }",
                    "   @OnPermissionDenied(Manifest.permission.CAMERA)",
                    "   void cameraDenied() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.app.Activity;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static final int REQUEST_SHOWCAMERA2 = 1;",
                    "   private static final String[] PERMISSION_SHOWCAMERA2 = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyFragment target) {",
                    "       Activity activity = target.getActivity();",
                    "       if (PermissionUtils.hasSelfPermissions(activity, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(activity, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(new ShowCameraPermissionRequest(target));",
                    "           } else {",
                    "               target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void showCamera2WithCheck(MyFragment target) {",
                    "       Activity activity = target.getActivity();",
                    "       if (PermissionUtils.hasSelfPermissions(activity, PERMISSION_SHOWCAMERA2)) {",
                    "           target.showCamera2();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(activity, PERMISSION_SHOWCAMERA2)) {",
                    "               target.cameraRationale(new ShowCamera2PermissionRequest(target));",
                    "           } else {",
                    "               target.requestPermissions(PERMISSION_SHOWCAMERA2, REQUEST_SHOWCAMERA2);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyFragment target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               } else {",
                    "                   target.cameraDenied();",
                    "               }",
                    "               break;",
                    "           case REQUEST_SHOWCAMERA2:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera2();",
                    "               } else {",
                    "                   target.cameraDenied();",
                    "               }",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "   private static final class ShowCameraPermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyFragment> weakTarget;",
                    "        private ShowCameraPermissionRequest(MyFragment target) {",
                    "            this.weakTarget = new WeakReference<>(target);",
                    "        }",
                    "        @Override",
                    "        public void proceed() {",
                    "            MyFragment target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "        }",
                    "        @Override",
                    "        public void cancel() {",
                    "            MyFragment target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            target.cameraDenied();",
                    "        }",
                    "   }",
                    "   private static final class ShowCamera2PermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyFragment> weakTarget;",
                    "        private ShowCameraPermissionRequest(MyFragment target) {",
                    "            this.weakTarget = new WeakReference<>(target);",
                    "        }",
                    "        @Override",
                    "        public void proceed() {",
                    "            MyFragment target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            target.requestPermissions(PERMISSION_SHOWCAMERA2, REQUEST_SHOWCAMERA2);",
                    "        }",
                    "        @Override",
                    "        public void cancel() {",
                    "            MyFragment target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            target.cameraDenied();",
                    "        }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest TwoPermissionsWithOneRationaleActivity = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @NeedsPermission(Manifest.permission.READ_CONTACTS)",
                    "   void accessContacts() {",
                    "   }",
                    "   @OnShowRationale(Manifest.permission.CAMERA)",
                    "   void cameraRationale(PermissionRequest request) {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.support.v4.app.ActivityCompat;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static final int REQUEST_ACCESSCONTACTS = 1;",
                    "   private static final String[] PERMISSION_ACCESSCONTACTS = new String[] {\"android.permission.READ_CONTACTS\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(new ShowCameraPermissionRequest(target));",
                    "           } else {",
                    "               ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void accessContactsWithCheck(MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_ACCESSCONTACTS)) {",
                    "           target.accessContacts();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_ACCESSCONTACTS, REQUEST_ACCESSCONTACTS);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyActivity target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               }",
                    "               break;",
                    "           case REQUEST_ACCESSCONTACTS:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.accessContacts();",
                    "               }",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "   private static final class ShowCameraPermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyActivity> weakTarget;",
                    "        private ShowCameraPermissionRequest(MyActivity target) {",
                    "            this.weakTarget = new WeakReference<>(target);",
                    "        }",
                    "        @Override",
                    "        public void proceed() {",
                    "            MyActivity target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "        }",
                    "        @Override",
                    "        public void cancel() {",
                    "        }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest TwoPermissionsWithOneRationaleSupportFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.support.v4.app.Fragment;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "@RuntimePermissions",
                    "public class MyFragment extends Fragment {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @NeedsPermission(Manifest.permission.READ_CONTACTS)",
                    "   void accessContacts() {",
                    "   }",
                    "   @OnShowRationale(Manifest.permission.CAMERA)",
                    "   void cameraRationale(PermissionRequest request) {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.app.Activity;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static final int REQUEST_ACCESSCONTACTS = 1;",
                    "   private static final String[] PERMISSION_ACCESSCONTACTS = new String[] {\"android.permission.READ_CONTACTS\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyFragment target) {",
                    "       Activity activity = target.getActivity();",
                    "       if (PermissionUtils.hasSelfPermissions(activity, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(activity, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(new ShowCameraPermissionRequest(target));",
                    "           } else {",
                    "               target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void accessContactsWithCheck(MyFragment target) {",
                    "       Activity activity = target.getActivity();",
                    "       if (PermissionUtils.hasSelfPermissions(activity, PERMISSION_ACCESSCONTACTS)) {",
                    "           target.accessContacts();",
                    "       } else {",
                    "           target.requestPermissions(PERMISSION_ACCESSCONTACTS, REQUEST_ACCESSCONTACTS);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyFragment target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               }",
                    "               break;",
                    "           case REQUEST_ACCESSCONTACTS:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.accessContacts();",
                    "               }",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "   private static final class ShowCameraPermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyFragment> weakTarget;",
                    "        private ShowCameraPermissionRequest(MyFragment target) {",
                    "            this.weakTarget = new WeakReference<>(target);",
                    "        }",
                    "        @Override",
                    "        public void proceed() {",
                    "            MyFragment target = weakTarget.get();",
                    "            if (target == null) return;",
                    "           target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "        }",
                    "        @Override",
                    "        public void cancel() {",
                    "        }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest TwoPermissionsWithTwoRationalesActivity = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @NeedsPermission(Manifest.permission.READ_CONTACTS)",
                    "   void accessContacts() {",
                    "   }",
                    "   @OnShowRationale(Manifest.permission.CAMERA)",
                    "   void cameraRationale(PermissionRequest request) {",
                    "   }",
                    "   @OnShowRationale(Manifest.permission.READ_CONTACTS)",
                    "   void contactsRationale(PermissionRequest request) {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.support.v4.app.ActivityCompat;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static final int REQUEST_ACCESSCONTACTS = 1;",
                    "   private static final String[] PERMISSION_ACCESSCONTACTS = new String[] {\"android.permission.READ_CONTACTS\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(new ShowCameraPermissionRequest(target));",
                    "           } else {",
                    "               ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void accessContactsWithCheck(MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_ACCESSCONTACTS)) {",
                    "           target.accessContacts();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_ACCESSCONTACTS)) {",
                    "               target.contactsRationale(new AccessContactsPermissionRequest(target));",
                    "           } else {",
                    "               ActivityCompat.requestPermissions(target, PERMISSION_ACCESSCONTACTS, REQUEST_ACCESSCONTACTS);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyActivity target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               }",
                    "               break;",
                    "           case REQUEST_ACCESSCONTACTS:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.accessContacts();",
                    "               }",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "   private static final class ShowCameraPermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyActivity> weakTarget;",
                    "        private ShowCameraPermissionRequest(MyActivity target) {",
                    "            this.weakTarget = new WeakReference<>(target);",
                    "        }",
                    "        @Override",
                    "        public void proceed() {",
                    "            MyActivity target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "        }",
                    "        @Override",
                    "        public void cancel() {",
                    "        }",
                    "   }",
                    "   private static final class AccessContactsPermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyActivity> weakTarget;",
                    "        private ShowCameraPermissionRequest(MyActivity target) {",
                    "            this.weakTarget = new WeakReference<>(target);",
                    "        }",
                    "        @Override",
                    "        public void proceed() {",
                    "            MyActivity target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            ActivityCompat.requestPermissions(target, PERMISSION_ACCESSCONTACTS, REQUEST_ACCESSCONTACTS);",
                    "        }",
                    "        @Override",
                    "        public void cancel() {",
                    "        }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest TwoPermissionsWithTwoRationalesSupportFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.support.v4.app.Fragment;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "@RuntimePermissions",
                    "public class MyFragment extends Fragment {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @NeedsPermission(Manifest.permission.READ_CONTACTS)",
                    "   void accessContacts() {",
                    "   }",
                    "   @OnShowRationale(Manifest.permission.CAMERA)",
                    "   void cameraRationale(PermissionRequest request) {",
                    "   }",
                    "   @OnShowRationale(Manifest.permission.READ_CONTACTS)",
                    "   void contactsRationale(PermissionRequest request) {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.app.Activity;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static final int REQUEST_ACCESSCONTACTS = 1;",
                    "   private static final String[] PERMISSION_ACCESSCONTACTS = new String[] {\"android.permission.READ_CONTACTS\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyFragment target) {",
                    "       Activity activity = target.getActivity();",
                    "       if (PermissionUtils.hasSelfPermissions(activity, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(activity, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(new ShowCameraPermissionRequest(target));",
                    "           } else {",
                    "               target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void accessContactsWithCheck(MyFragment target) {",
                    "       Activity activity = target.getActivity();",
                    "       if (PermissionUtils.hasSelfPermissions(activity, PERMISSION_ACCESSCONTACTS)) {",
                    "           target.accessContacts();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(activity, PERMISSION_ACCESSCONTACTS)) {",
                    "               target.contactsRationale(new AccessContactsPermissionRequest(target));",
                    "           } else {",
                    "               target.requestPermissions(PERMISSION_ACCESSCONTACTS, REQUEST_ACCESSCONTACTS);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyFragment target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               }",
                    "               break;",
                    "           case REQUEST_ACCESSCONTACTS:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.accessContacts();",
                    "               }",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "   private static final class ShowCameraPermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyFragment> weakTarget;",
                    "        private ShowCameraPermissionRequest(MyFragment target) {",
                    "            this.weakTarget = new WeakReference<>(target);",
                    "        }",
                    "        @Override",
                    "        public void proceed() {",
                    "            MyFragment target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "        }",
                    "        @Override",
                    "        public void cancel() {",
                    "        }",
                    "   }",
                    "   private static final class AccessContactsPermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyFragment> weakTarget;",
                    "        private ShowCameraPermissionRequest(MyFragment target) {",
                    "            this.weakTarget = new WeakReference<>(target);",
                    "        }",
                    "        @Override",
                    "        public void proceed() {",
                    "            MyFragment target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            target.requestPermissions(PERMISSION_ACCESSCONTACTS, REQUEST_ACCESSCONTACTS);",
                    "        }",
                    "        @Override",
                    "        public void cancel() {",
                    "        }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest OnePermissionWithOtherRationaleActivity = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnShowRationale(Manifest.permission.READ_CONTACTS)",
                    "   void contactsRationale(PermissionRequest request) {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.support.v4.app.ActivityCompat;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyActivity target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               }",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest OnePermissionWithOtherRationaleSupportFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.support.v4.app.Fragment;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "@RuntimePermissions",
                    "public class MyFragment extends Fragment {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnShowRationale(Manifest.permission.READ_CONTACTS)",
                    "   void contactsRationale(PermissionRequest request) {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.app.Activity;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyFragment target) {",
                    "       Activity activity = target.getActivity();",
                    "       if (PermissionUtils.hasSelfPermissions(activity, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyFragment target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               }",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest OnePermissionWithDeniedActivity = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnPermissionDenied;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnPermissionDenied(Manifest.permission.CAMERA)",
                    "   void onCameraDenied() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.support.v4.app.ActivityCompat;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyActivity target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               } else {",
                    "                   target.onCameraDenied();",
                    "               }",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest OnePermissionWithDeniedSupportFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.support.v4.app.Fragment;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnPermissionDenied;",
                    "@RuntimePermissions",
                    "public class MyFragment extends Fragment {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnPermissionDenied(Manifest.permission.CAMERA)",
                    "   void onCameraDenied() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.app.Activity;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyFragment target) {",
                    "       Activity activity = target.getActivity();",
                    "       if (PermissionUtils.hasSelfPermissions(activity, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyFragment target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               } else {",
                    "                   target.onCameraDenied();",
                    "               }",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest OnePermissionWithOtherDeniedActivity = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnPermissionDenied;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnPermissionDenied(Manifest.permission.READ_CONTACTS)",
                    "   void onContactsDenied() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.support.v4.app.ActivityCompat;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyActivity target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               }",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest OnePermissionWithOtherDeniedSupportFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.support.v4.app.Fragment;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnPermissionDenied;",
                    "@RuntimePermissions",
                    "public class MyFragment extends Fragment {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnPermissionDenied(Manifest.permission.READ_CONTACTS)",
                    "   void onContactsDenied() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.app.Activity;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyFragment target) {",
                    "       Activity activity = target.getActivity();",
                    "       if (PermissionUtils.hasSelfPermissions(activity, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyFragment target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               }",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest OnePermissionWithRationaleAndDeniedActivity = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.OnPermissionDenied;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnShowRationale(Manifest.permission.CAMERA)",
                    "   void cameraRationale(PermissionRequest request) {",
                    "   }",
                    "   @OnPermissionDenied(Manifest.permission.CAMERA)",
                    "   void onCameraDenied() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.support.v4.app.ActivityCompat;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(new ShowCameraPermissionRequest(target));",
                    "           } else {",
                    "               ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyActivity target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               } else {",
                    "                   target.onCameraDenied();",
                    "               }",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "   private static final class ShowCameraPermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyActivity> weakTarget;",
                    "        private ShowCameraPermissionRequest(MyActivity target) {",
                    "            this.weakTarget = new WeakReference<>(target);",
                    "        }",
                    "        @Override",
                    "        public void proceed() {",
                    "            MyActivity target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "        }",
                    "        @Override",
                    "        public void cancel() {",
                    "            MyActivity target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            target.onCameraDenied();",
                    "        }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest OnePermissionWithRationaleAndDeniedSupportFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.support.v4.app.Fragment;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.OnPermissionDenied;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "@RuntimePermissions",
                    "public class MyFragment extends Fragment {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnShowRationale(Manifest.permission.CAMERA)",
                    "   void cameraRationale(PermissionRequest request) {",
                    "   }",
                    "   @OnPermissionDenied(Manifest.permission.CAMERA)",
                    "   void onCameraDenied() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.app.Activity;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyFragment target) {",
                    "       Activity activity = target.getActivity();",
                    "       if (PermissionUtils.hasSelfPermissions(activity, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(activity, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(new ShowCameraPermissionRequest(target));",
                    "           } else {",
                    "               target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyFragment target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               } else {",
                    "                   target.onCameraDenied();",
                    "               }",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "   private static final class ShowCameraPermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyFragment> weakTarget;",
                    "        private ShowCameraPermissionRequest(MyFragment target) {",
                    "            this.weakTarget = new WeakReference<>(target);",
                    "        }",
                    "        @Override",
                    "        public void proceed() {",
                    "            MyFragment target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "        }",
                    "        @Override",
                    "        public void cancel() {",
                    "            MyFragment target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            target.onCameraDenied();",
                    "        }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest NoDuplicatesDespiteRepeatedValuesActivity = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnPermissionDenied;",
                    "import permissions.dispatcher.OnNeverAskAgain;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission({Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE})",
                    "   public void showCamera() {",
                    "   }",
                    "   @OnPermissionDenied({Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE})",
                    "   void onShowCameraDenied() {",
                    "   }",
                    "   @OnNeverAskAgain({Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE})",
                    "   void onShowCameraNeverAsk() {",
                    "   }",
                    "   @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)",
                    "   void showGetStorage() {",
                    "   }",
                    "   @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)",
                    "   void onGetStorageDenied() {",
                    "   }",
                    "   @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)",
                    "   void onGetStorageNeverAsk() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import android.support.v4.app.ActivityCompat;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\", \"android.permission.WRITE_EXTERNAL_STORAGE\"};",
                    "   private static final int REQUEST_SHOWGETSTORAGE = 1;",
                    "   private static final String[] PERMISSION_SHOWGETSTORAGE = new String[] {\"android.permission.WRITE_EXTERNAL_STORAGE\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void showGetStorageWithCheck(MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWGETSTORAGE)) {",
                    "           target.showGetStorage();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWGETSTORAGE, REQUEST_SHOWGETSTORAGE);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyActivity target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               } else {",
                    "                   if (!PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "                       target.onShowCameraNeverAsk();",
                    "                   } else {",
                    "                       target.onShowCameraDenied();",
                    "                   }",
                    "               }",
                    "               break;",
                    "           case REQUEST_SHOWGETSTORAGE:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showGetStorage();",
                    "               } else {",
                    "                   if (!PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWGETSTORAGE)) {",
                    "                       target.onGetStorageNeverAsk();",
                    "                   } else {",
                    "                       target.onGetStorageDenied();",
                    "                   }",
                    "               }",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest DuplicatesInListsActivity = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnPermissionDenied;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission({Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE})",
                    "   public void showCamera() {",
                    "   }",
                    "   @OnPermissionDenied({Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE})",
                    "   public void onCameraDenied() {",
                    "   }",
                    "   @OnPermissionDenied({Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE})",
                    "   void onCameraDenied2() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return EMPTY_SOURCE;
        }
    };
}
