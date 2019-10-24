package permissions.dispatcher.processor.data;

import permissions.dispatcher.processor.base.BaseTest;

public final class Source {

    private Source() {
    }

    private static final String[] EMPTY_SOURCE = {};

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

    public static final BaseTest RationaleWithWrongParameters = new BaseTest() {
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

    public static final BaseTest RationaleWithOneMoreParameters = new BaseTest() {
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
                    "   void cameraRationale(PermissionRequest request, PermissionRequest request2) {",
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
                    "import androidx.annotation.NonNull;",
                    "import androidx.core.app.ActivityCompat;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyActivity target, int requestCode, int[] grantResults) {",
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
                    "import androidx.fragment.app.Fragment;",
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
                    "import androidx.annotation.NonNull;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private  MyFragmentPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull MyFragment target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.requireActivity(), PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyFragment target, int requestCode, int[] grantResults) {",
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
                    "import androidx.annotation.NonNull;",
                    "import androidx.core.app.ActivityCompat;",
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
                    "   static void showCameraWithPermissionCheck(@NonNull MyActivity target, int value) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera(value);",
                    "       } else {",
                    "           PENDING_SHOWCAMERA = new MyActivityShowCameraPermissionRequest(target, value);",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyActivity target, int requestCode, int[] grantResults) {",
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
                    "   private static final class MyActivityShowCameraPermissionRequest implements GrantableRequest {",
                    "        private final WeakReference<MyActivity> weakTarget;",
                    "        private final int value;",
                    "        private MyActivityShowCameraPermissionRequest(@NonNull MyActivity target, int value) {",
                    "            this.weakTarget = new WeakReference<MyActivity>(target);",
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
                    "import androidx.annotation.NonNull;",
                    "import androidx.core.app.ActivityCompat;",
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
                    "   static void showCameraWithPermissionCheck(@NonNull MyActivity target, int value) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera(value);",
                    "       } else {",
                    "           PENDING_SHOWCAMERA = new MyActivityShowCameraPermissionRequest(target, value);",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(PENDING_SHOWCAMERA);",
                    "           } else {",
                    "               ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyActivity target, int requestCode, int[] grantResults) {",
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
                    "   private static final class MyActivityShowCameraPermissionRequest implements GrantableRequest {",
                    "        private final WeakReference<MyActivity> weakTarget;",
                    "        private final int value;",
                    "        private MyActivityShowCameraPermissionRequest(@NonNull MyActivity target, int value) {",
                    "            this.weakTarget = new WeakReference<MyActivity>(target);",
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
                    "import androidx.annotation.NonNull;",
                    "import androidx.core.app.ActivityCompat;",
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
                    "   static void showCameraWithPermissionCheck(@NonNull MyActivity target, int value) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera(value);",
                    "       } else {",
                    "           PENDING_SHOWCAMERA = new MyActivityShowCameraPermissionRequest(target, value);",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyActivity target, int requestCode, int[] grantResults) {",
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
                    "   private static final class MyActivityShowCameraPermissionRequest implements GrantableRequest {",
                    "        private final WeakReference<MyActivity> weakTarget;",
                    "        private final int value;",
                    "        private MyActivityShowCameraPermissionRequest(@NonNull MyActivity target, int value) {",
                    "            this.weakTarget = new WeakReference<MyActivity>(target);",
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
                    "import androidx.annotation.NonNull;",
                    "import androidx.core.app.ActivityCompat;",
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
                    "   static void showCameraWithPermissionCheck(@NonNull MyActivity target, int value) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera(value);",
                    "       } else {",
                    "           PENDING_SHOWCAMERA = new MyActivityShowCameraPermissionRequest(target, value);",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(PENDING_SHOWCAMERA);",
                    "           } else {",
                    "               ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyActivity target, int requestCode, int[] grantResults) {",
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
                    "   private static final class MyActivityShowCameraPermissionRequest implements GrantableRequest {",
                    "        private final WeakReference<MyActivity> weakTarget;",
                    "        private final int value;",
                    "        private MyActivityShowCameraPermissionRequest(@NonNull MyActivity target, int value) {",
                    "            this.weakTarget = new WeakReference<MyActivity>(target);",
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
                    "import androidx.annotation.NonNull;",
                    "import androidx.core.app.ActivityCompat;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyActivity target, int requestCode, int[] grantResults) {",
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
                    "import androidx.annotation.NonNull;",
                    "import androidx.core.app.ActivityCompat;",
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
                    "   static void showCameraWithPermissionCheck(@NonNull MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(new MyActivityShowCameraPermissionRequest(target));",
                    "           } else {",
                    "               ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyActivity target, int requestCode, int[] grantResults) {",
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
                    "   private static final class MyActivityShowCameraPermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyActivity> weakTarget;",
                    "        private MyActivityShowCameraPermissionRequest(@NonNull MyActivity target) {",
                    "            this.weakTarget = new WeakReference<MyActivity>(target);",
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
                    "import androidx.annotation.NonNull;",
                    "import androidx.core.app.ActivityCompat;",
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
                    "   static void showCameraWithPermissionCheck(@NonNull MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(new MyActivityShowCameraPermissionRequest(target));",
                    "           } else {",
                    "               ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyActivity target, int requestCode, int[] grantResults) {",
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
                    "   private static final class MyActivityShowCameraPermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyActivity> weakTarget;",
                    "        private MyActivityShowCameraPermissionRequest(@NonNull MyActivity target) {",
                    "            this.weakTarget = new WeakReference<MyActivity>(target);",
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
                    "import androidx.annotation.NonNull;",
                    "import androidx.core.app.ActivityCompat;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyActivity target, int requestCode, int[] grantResults) {",
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
                    "import androidx.annotation.NonNull;",
                    "import androidx.core.app.ActivityCompat;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_ACCESSCONTACTS = 0;",
                    "   private static final String[] PERMISSION_ACCESSCONTACTS = new String[] {\"android.permission.READ_CONTACTS\"};",
                    "   private static final int REQUEST_SHOWCAMERA = 1;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void accessContactsWithPermissionCheck(@NonNull MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_ACCESSCONTACTS)) {",
                    "           target.accessContacts();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_ACCESSCONTACTS, REQUEST_ACCESSCONTACTS);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyActivity target, int requestCode, int[] grantResults) {",
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
                    "import androidx.annotation.NonNull;",
                    "import androidx.core.app.ActivityCompat;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_ACCESSCONTACTS = 0;",
                    "   private static final String[] PERMISSION_ACCESSCONTACTS = new String[] {\"android.permission.READ_CONTACTS\"};",
                    "   private static final int REQUEST_SHOWCAMERA = 1;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void accessContactsWithPermissionCheck(@NonNull MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_ACCESSCONTACTS)) {",
                    "           target.accessContacts();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_ACCESSCONTACTS, REQUEST_ACCESSCONTACTS);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyActivity target, int requestCode, int[] grantResults) {",
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
                    "import androidx.annotation.NonNull;",
                    "import androidx.core.app.ActivityCompat;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\", \"android.permission.READ_CONTACTS\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyActivity target, int requestCode, int[] grantResults) {",
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

    public static final BaseTest TwoPermissionsFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import androidx.fragment.app.Fragment;",
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
                    "import androidx.annotation.NonNull;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_ACCESSCONTACTS = 0;",
                    "   private static final String[] PERMISSION_ACCESSCONTACTS = new String[] {\"android.permission.READ_CONTACTS\"};",
                    "   private static final int REQUEST_SHOWCAMERA = 1;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private  MyFragmentPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull MyFragment target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.requireActivity(), PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void accessContactsWithPermissionCheck(@NonNull MyFragment target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.requireActivity(), PERMISSION_ACCESSCONTACTS)) {",
                    "           target.accessContacts();",
                    "       } else {",
                    "           target.requestPermissions(PERMISSION_ACCESSCONTACTS, REQUEST_ACCESSCONTACTS);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyFragment target, int requestCode, int[] grantResults) {",
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
                    "import androidx.annotation.NonNull;",
                    "import androidx.core.app.ActivityCompat;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static final int REQUEST_SHOWCAMERA2 = 1;",
                    "   private static final String[] PERMISSION_SHOWCAMERA2 = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void showCamera2WithPermissionCheck(@NonNull MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA2)) {",
                    "           target.showCamera2();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA2, REQUEST_SHOWCAMERA2);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyActivity target, int requestCode, int[] grantResults) {",
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
                    "import androidx.annotation.NonNull;",
                    "import androidx.core.app.ActivityCompat;",
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
                    "   static void showCameraWithPermissionCheck(@NonNull MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(new MyActivityShowCameraPermissionRequest(target));",
                    "           } else {",
                    "               ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void showCamera2WithPermissionCheck(@NonNull MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA2)) {",
                    "           target.showCamera2();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA2)) {",
                    "               target.cameraRationale(new MyActivityShowCamera2PermissionRequest(target));",
                    "           } else {",
                    "               ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA2, REQUEST_SHOWCAMERA2);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyActivity target, int requestCode, int[] grantResults) {",
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
                    "   private static final class MyActivityShowCameraPermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyActivity> weakTarget;",
                    "        private MyActivityShowCameraPermissionRequest(@NonNull MyActivity target) {",
                    "            this.weakTarget = new WeakReference<MyActivity>(target);",
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
                    "   private static final class MyActivityShowCamera2PermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyActivity> weakTarget;",
                    "        private MyActivityShowCamera2PermissionRequest(@NonNull MyActivity target) {",
                    "            this.weakTarget = new WeakReference<MyActivity>(target);",
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
                    "import androidx.annotation.NonNull;",
                    "import androidx.core.app.ActivityCompat;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static final int REQUEST_SHOWCAMERA2 = 1;",
                    "   private static final String[] PERMISSION_SHOWCAMERA2 = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void showCamera2WithPermissionCheck(@NonNull MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA2)) {",
                    "           target.showCamera2();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA2, REQUEST_SHOWCAMERA2);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyActivity target, int requestCode, int[] grantResults) {",
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
                    "import androidx.annotation.NonNull;",
                    "import androidx.core.app.ActivityCompat;",
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
                    "   static void showCameraWithPermissionCheck(@NonNull MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(new MyActivityShowCameraPermissionRequest(target));",
                    "           } else {",
                    "               ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void showCamera2WithPermissionCheck(@NonNull MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA2)) {",
                    "           target.showCamera2();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA2)) {",
                    "               target.cameraRationale(new MyActivityShowCamera2PermissionRequest(target));",
                    "           } else {",
                    "               ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA2, REQUEST_SHOWCAMERA2);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyActivity target, int requestCode, int[] grantResults) {",
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
                    "   private static final class MyActivityShowCameraPermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyActivity> weakTarget;",
                    "        private MyActivityShowCameraPermissionRequest(@NonNull MyActivity target) {",
                    "            this.weakTarget = new WeakReference<MyActivity>(target);",
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
                    "   private static final class MyActivityShowCamera2PermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyActivity> weakTarget;",
                    "        private MyActivityShowCamera2PermissionRequest(@NonNull MyActivity target) {",
                    "            this.weakTarget = new WeakReference<MyActivity>(target);",
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

    public static final BaseTest OnePermissionWithNeverAskAndRationaleFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import androidx.fragment.app.Fragment;",
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
                    "import androidx.annotation.NonNull;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyFragmentPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull MyFragment target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.requireActivity(), PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(new MyFragmentShowCameraPermissionRequest(target));",
                    "           } else {",
                    "               target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyFragment target, int requestCode, int[] grantResults) {",
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
                    "   private static final class MyFragmentShowCameraPermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyFragment> weakTarget;",
                    "        private MyFragmentShowCameraPermissionRequest(@NonNull MyFragment target) {",
                    "            this.weakTarget = new WeakReference<MyFragment>(target);",
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

    public static final BaseTest OnePermissionWithNeverAskRationaleAndDeniedFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import androidx.fragment.app.Fragment;",
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
                    "import androidx.annotation.NonNull;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyFragmentPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull MyFragment target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.requireActivity(), PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(new MyFragmentShowCameraPermissionRequest(target));",
                    "           } else {",
                    "               target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyFragment target, int requestCode, int[] grantResults) {",
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
                    "   private static final class MyFragmentShowCameraPermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyFragment> weakTarget;",
                    "        private MyFragmentShowCameraPermissionRequest(@NonNull MyFragment target) {",
                    "            this.weakTarget = new WeakReference<MyFragment>(target);",
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

    public static final BaseTest OnePermissionWithNeverAskFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import androidx.fragment.app.Fragment;",
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
                    "import androidx.annotation.NonNull;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyFragmentPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull MyFragment target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.requireActivity(), PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyFragment target, int requestCode, int[] grantResults) {",
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

    public static final BaseTest OnePermissionWithNeverAskAndDeniedFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import androidx.fragment.app.Fragment;",
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
                    "import androidx.annotation.NonNull;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyFragmentPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull MyFragment target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.requireActivity(), PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyFragment target, int requestCode, int[] grantResults) {",
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

    public static final BaseTest OnePermissionWithParametersFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import androidx.fragment.app.Fragment;",
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
                    "import androidx.annotation.NonNull;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.GrantableRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static GrantableRequest PENDING_SHOWCAMERA;",
                    "   private  MyFragmentPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull MyFragment target, int value, String name) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.requireActivity(), PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera(value, name);",
                    "       } else {",
                    "           PENDING_SHOWCAMERA = new MyFragmentShowCameraPermissionRequest(target, value, name);",
                    "           target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyFragment target, int requestCode, int[] grantResults) {",
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
                    "   private static final class MyFragmentShowCameraPermissionRequest implements GrantableRequest {",
                    "        private final WeakReference<MyFragment> weakTarget;",
                    "        private final int value;",
                    "        private final String name;",
                    "        private MyFragmentShowCameraPermissionRequest(@NonNull MyFragment target, int value, String name) {",
                    "            this.weakTarget = new WeakReference<MyFragment>(target);",
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

    public static final BaseTest OnePermissionWithParametersAndRationaleFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import androidx.fragment.app.Fragment;",
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
                    "import androidx.annotation.NonNull;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.GrantableRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static GrantableRequest PENDING_SHOWCAMERA;",
                    "   private  MyFragmentPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull MyFragment target, int value, String name) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.requireActivity(), PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera(value, name);",
                    "       } else {",
                    "           PENDING_SHOWCAMERA = new MyFragmentShowCameraPermissionRequest(target, value, name);",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(PENDING_SHOWCAMERA);",
                    "           } else {",
                    "               target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyFragment target, int requestCode, int[] grantResults) {",
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
                    "   private static final class MyFragmentShowCameraPermissionRequest implements GrantableRequest {",
                    "        private final WeakReference<MyFragment> weakTarget;",
                    "        private final int value;",
                    "        private final String name;",
                    "        private MyFragmentShowCameraPermissionRequest(@NonNull MyFragment target, int value, String name) {",
                    "            this.weakTarget = new WeakReference<MyFragment>(target);",
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

    public static final BaseTest OnePermissionWithParametersAndDeniedFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import androidx.fragment.app.Fragment;",
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
                    "import androidx.annotation.NonNull;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.GrantableRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static GrantableRequest PENDING_SHOWCAMERA;",
                    "   private MyFragmentPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull MyFragment target, int value, String name) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.requireActivity(), PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera(value, name);",
                    "       } else {",
                    "           PENDING_SHOWCAMERA = new MyFragmentShowCameraPermissionRequest(target, value, name);",
                    "           target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyFragment target, int requestCode, int[] grantResults) {",
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
                    "   private static final class MyFragmentShowCameraPermissionRequest implements GrantableRequest {",
                    "        private final WeakReference<MyFragment> weakTarget;",
                    "        private final int value;",
                    "        private final String name;",
                    "        private MyFragmentShowCameraPermissionRequest(@NonNull MyFragment target, int value, String name) {",
                    "            this.weakTarget = new WeakReference<MyFragment>(target);",
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

    public static final BaseTest OnePermissionWithParametersRationaleAndDeniedFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import androidx.fragment.app.Fragment;",
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
                    "import androidx.annotation.NonNull;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.GrantableRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static GrantableRequest PENDING_SHOWCAMERA;",
                    "   private  MyFragmentPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull MyFragment target, int value, String name) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.requireActivity(), PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera(value, name);",
                    "       } else {",
                    "           PENDING_SHOWCAMERA = new MyFragmentShowCameraPermissionRequest(target, value, name);",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(PENDING_SHOWCAMERA);",
                    "           } else {",
                    "               target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyFragment target, int requestCode, int[] grantResults) {",
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
                    "   private static final class MyFragmentShowCameraPermissionRequest implements GrantableRequest {",
                    "        private final WeakReference<MyFragment> weakTarget;",
                    "        private final int value;",
                    "        private final String name;",
                    "        private MyFragmentShowCameraPermissionRequest(@NonNull MyFragment target, int value, String name) {",
                    "            this.weakTarget = new WeakReference<MyFragment>(target);",
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

    public static final BaseTest TwoPermissionsWithSameSignatureFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import androidx.fragment.app.Fragment;",
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
                    "import androidx.annotation.NonNull;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static final int REQUEST_SHOWCAMERA2 = 1;",
                    "   private static final String[] PERMISSION_SHOWCAMERA2 = new String[] {\"android.permission.CAMERA\"};",
                    "   private  MyFragmentPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull MyFragment target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.requireActivity(), PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void showCamera2WithPermissionCheck(@NonNull MyFragment target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.requireActivity(), PERMISSION_SHOWCAMERA2)) {",
                    "           target.showCamera2();",
                    "       } else {",
                    "           target.requestPermissions(PERMISSION_SHOWCAMERA2, REQUEST_SHOWCAMERA2);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyFragment target, int requestCode, int[] grantResults) {",
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

    public static final BaseTest TwoPermissionsWithSameSignatureAndRationaleFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import androidx.fragment.app.Fragment;",
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
                    "import androidx.annotation.NonNull;",
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
                    "   private  MyFragmentPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull MyFragment target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.requireActivity(), PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(new MyFragmentShowCameraPermissionRequest(target));",
                    "           } else {",
                    "               target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void showCamera2WithPermissionCheck(@NonNull MyFragment target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.requireActivity(), PERMISSION_SHOWCAMERA2)) {",
                    "           target.showCamera2();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA2)) {",
                    "               target.cameraRationale(new MyFragmentShowCamera2PermissionRequest(target));",
                    "           } else {",
                    "               target.requestPermissions(PERMISSION_SHOWCAMERA2, REQUEST_SHOWCAMERA2);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyFragment target, int requestCode, int[] grantResults) {",
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
                    "   private static final class MyFragmentShowCameraPermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyFragment> weakTarget;",
                    "        private MyFragmentShowCameraPermissionRequest(@NonNull MyFragment target) {",
                    "            this.weakTarget = new WeakReference<MyFragment>(target);",
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
                    "   private static final class MyFragmentShowCamera2PermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyFragment> weakTarget;",
                    "        private MyFragmentShowCamera2PermissionRequest(@NonNull MyFragment target) {",
                    "            this.weakTarget = new WeakReference<MyFragment>(target);",
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

    public static final BaseTest TwoPermissionsWithSameSignatureAndDeniedFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import androidx.fragment.app.Fragment;",
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
                    "import androidx.annotation.NonNull;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static final int REQUEST_SHOWCAMERA2 = 1;",
                    "   private static final String[] PERMISSION_SHOWCAMERA2 = new String[] {\"android.permission.CAMERA\"};",
                    "   private  MyFragmentPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull MyFragment target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.requireActivity(), PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void showCamera2WithPermissionCheck(@NonNull MyFragment target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.requireActivity(), PERMISSION_SHOWCAMERA2)) {",
                    "           target.showCamera2();",
                    "       } else {",
                    "           target.requestPermissions(PERMISSION_SHOWCAMERA2, REQUEST_SHOWCAMERA2);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyFragment target, int requestCode, int[] grantResults) {",
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

    public static final BaseTest TwoPermissionsWithSameSignatureRationaleAndDeniedFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import androidx.fragment.app.Fragment;",
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
                    "import androidx.annotation.NonNull;",
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
                    "   private  MyFragmentPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull MyFragment target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.requireActivity(), PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(new MyFragmentShowCameraPermissionRequest(target));",
                    "           } else {",
                    "               target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void showCamera2WithPermissionCheck(@NonNull MyFragment target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.requireActivity(), PERMISSION_SHOWCAMERA2)) {",
                    "           target.showCamera2();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA2)) {",
                    "               target.cameraRationale(new MyFragmentShowCamera2PermissionRequest(target));",
                    "           } else {",
                    "               target.requestPermissions(PERMISSION_SHOWCAMERA2, REQUEST_SHOWCAMERA2);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyFragment target, int requestCode, int[] grantResults) {",
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
                    "   private static final class MyFragmentShowCameraPermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyFragment> weakTarget;",
                    "        private MyFragmentShowCameraPermissionRequest(@NonNull MyFragment target) {",
                    "            this.weakTarget = new WeakReference<MyFragment>(target);",
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
                    "   private static final class MyFragmentShowCamera2PermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyFragment> weakTarget;",
                    "        private MyFragmentShowCamera2PermissionRequest(@NonNull MyFragment target) {",
                    "            this.weakTarget = new WeakReference<MyFragment>(target);",
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
                    "import androidx.annotation.NonNull;",
                    "import androidx.core.app.ActivityCompat;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_ACCESSCONTACTS = 0;",
                    "   private static final String[] PERMISSION_ACCESSCONTACTS = new String[] {\"android.permission.READ_CONTACTS\"};",
                    "   private static final int REQUEST_SHOWCAMERA = 1;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(new MyActivityShowCameraPermissionRequest(target));",
                    "           } else {",
                    "               ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void accessContactsWithPermissionCheck(@NonNull MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_ACCESSCONTACTS)) {",
                    "           target.accessContacts();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_ACCESSCONTACTS)) {",
                    "               target.contactsRationale(new MyActivityAccessContactsPermissionRequest(target));",
                    "           } else {",
                    "               ActivityCompat.requestPermissions(target, PERMISSION_ACCESSCONTACTS, REQUEST_ACCESSCONTACTS);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyActivity target, int requestCode, int[] grantResults) {",
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
                    "   private static final class MyActivityShowCameraPermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyActivity> weakTarget;",
                    "        private MyActivityShowCameraPermissionRequest(@NonNull MyActivity target) {",
                    "            this.weakTarget = new WeakReference<MyActivity>(target);",
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
                    "   private static final class MyActivityAccessContactsPermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyActivity> weakTarget;",
                    "        private MyActivityAccessContactsPermissionRequest(@NonNull MyActivity target) {",
                    "            this.weakTarget = new WeakReference<MyActivity>(target);",
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

    public static final BaseTest TwoPermissionsWithTwoRationalesFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import androidx.fragment.app.Fragment;",
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
                    "import androidx.annotation.NonNull;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_ACCESSCONTACTS = 0;",
                    "   private static final String[] PERMISSION_ACCESSCONTACTS = new String[] {\"android.permission.READ_CONTACTS\"};",
                    "   private static final int REQUEST_SHOWCAMERA = 1;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private  MyFragmentPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull MyFragment target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.requireActivity(), PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(new MyFragmentShowCameraPermissionRequest(target));",
                    "           } else {",
                    "               target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void accessContactsWithPermissionCheck(@NonNull MyFragment target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.requireActivity(), PERMISSION_ACCESSCONTACTS)) {",
                    "           target.accessContacts();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_ACCESSCONTACTS)) {",
                    "               target.contactsRationale(new MyFragmentAccessContactsPermissionRequest(target));",
                    "           } else {",
                    "               target.requestPermissions(PERMISSION_ACCESSCONTACTS, REQUEST_ACCESSCONTACTS);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyFragment target, int requestCode, int[] grantResults) {",
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
                    "   private static final class MyFragmentShowCameraPermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyFragment> weakTarget;",
                    "        private MyFragmentShowCameraPermissionRequest(@NonNull MyFragment target) {",
                    "            this.weakTarget = new WeakReference<MyFragment>(target);",
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
                    "   private static final class MyFragmentAccessContactsPermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyFragment> weakTarget;",
                    "        private MyFragmentAccessContactsPermissionRequest(@NonNull MyFragment target) {",
                    "            this.weakTarget = new WeakReference<MyFragment>(target);",
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
                    "import androidx.annotation.NonNull;",
                    "import androidx.core.app.ActivityCompat;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyActivity target, int requestCode, int[] grantResults) {",
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

    public static final BaseTest OnePermissionWithOtherRationaleFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import androidx.fragment.app.Fragment;",
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
                    "import androidx.annotation.NonNull;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private  MyFragmentPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull MyFragment target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.requireActivity(), PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyFragment target, int requestCode, int[] grantResults) {",
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
                    "import androidx.annotation.NonNull;",
                    "import androidx.core.app.ActivityCompat;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyActivity target, int requestCode, int[] grantResults) {",
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

    public static final BaseTest OnePermissionWithOtherDeniedFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import androidx.fragment.app.Fragment;",
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
                    "import androidx.annotation.NonNull;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyFragmentPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull MyFragment target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.requireActivity(), PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyFragment target, int requestCode, int[] grantResults) {",
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
                    "import androidx.annotation.NonNull;",
                    "import androidx.core.app.ActivityCompat;",
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
                    "   static void showCameraWithPermissionCheck(@NonNull MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(new MyActivityShowCameraPermissionRequest(target));",
                    "           } else {",
                    "               ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyActivity target, int requestCode, int[] grantResults) {",
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
                    "   private static final class MyActivityShowCameraPermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyActivity> weakTarget;",
                    "        private MyActivityShowCameraPermissionRequest(@NonNull MyActivity target) {",
                    "            this.weakTarget = new WeakReference<MyActivity>(target);",
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

    public static final BaseTest OnePermissionWithRationaleAndDeniedFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import androidx.fragment.app.Fragment;",
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
                    "import androidx.annotation.NonNull;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private  MyFragmentPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull MyFragment target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.requireActivity(), PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(new MyFragmentShowCameraPermissionRequest(target));",
                    "           } else {",
                    "               target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyFragment target, int requestCode, int[] grantResults) {",
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
                    "   private static final class MyFragmentShowCameraPermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyFragment> weakTarget;",
                    "        private MyFragmentShowCameraPermissionRequest(@NonNull MyFragment target) {",
                    "            this.weakTarget = new WeakReference<MyFragment>(target);",
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
                    "import androidx.annotation.NonNull;",
                    "import androidx.core.app.ActivityCompat;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\", \"android.permission.WRITE_EXTERNAL_STORAGE\"};",
                    "   private static final int REQUEST_SHOWGETSTORAGE = 1;",
                    "   private static final String[] PERMISSION_SHOWGETSTORAGE = new String[] {\"android.permission.WRITE_EXTERNAL_STORAGE\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void showGetStorageWithPermissionCheck(@NonNull MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWGETSTORAGE)) {",
                    "           target.showGetStorage();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWGETSTORAGE, REQUEST_SHOWGETSTORAGE);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyActivity target, int requestCode, int[] grantResults) {",
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

    public static final BaseTest ValidMaxSdkVersion = new BaseTest() {
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
                    "   @NeedsPermission(value = Manifest.permission.WRITE_EXTERNAL_STORAGE, maxSdkVersion = 18)",
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
                    "import android.os.Build;",
                    "import androidx.annotation.NonNull;",
                    "import androidx.core.app.ActivityCompat;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWGETSTORAGE = 0;",
                    "   private static final String[] PERMISSION_SHOWGETSTORAGE = new String[] {\"android.permission.WRITE_EXTERNAL_STORAGE\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showGetStorageWithPermissionCheck(@NonNull MyActivity target) {",
                    "       if (Build.VERSION.SDK_INT > 18) {",
                    "           target.showGetStorage();",
                    "           return;",
                    "       }",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWGETSTORAGE)) {",
                    "           target.showGetStorage();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWGETSTORAGE, REQUEST_SHOWGETSTORAGE);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyActivity target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
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

    public static final BaseTest InValidMaxSdkVersion = new BaseTest() {
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
                    "   @NeedsPermission(value = Manifest.permission.WRITE_EXTERNAL_STORAGE, maxSdkVersion = 0)",
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
                    "import androidx.annotation.NonNull;",
                    "import androidx.core.app.ActivityCompat;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWGETSTORAGE = 0;",
                    "   private static final String[] PERMISSION_SHOWGETSTORAGE = new String[] {\"android.permission.WRITE_EXTERNAL_STORAGE\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showGetStorageWithPermissionCheck(@NonNull MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWGETSTORAGE)) {",
                    "           target.showGetStorage();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWGETSTORAGE, REQUEST_SHOWGETSTORAGE);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyActivity target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
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

    public static final BaseTest WriteSettingsFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package test;",
                    "import android.Manifest;",
                    "import androidx.fragment.app.Fragment;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnNeverAskAgain;",
                    "import permissions.dispatcher.OnPermissionDenied;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "@RuntimePermissions",
                    "public class MyFragment extends Fragment {",
                    "    @NeedsPermission(Manifest.permission.WRITE_SETTINGS)",
                    "    void writeSettings() {",
                    "    }",
                    "    @OnShowRationale(Manifest.permission.WRITE_SETTINGS)",
                    "    void writeSettingOnShowRationale(PermissionRequest request) {",
                    "    }",
                    "    @OnPermissionDenied(Manifest.permission.WRITE_SETTINGS)",
                    "    void writeSettingOnPermissionDenied() {",
                    "    }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package test;",
                    "import android.content.Intent;",
                    "import android.net.Uri;",
                    "import android.provider.Settings;",
                    "import androidx.annotation.NonNull;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "  private static final int REQUEST_WRITESETTINGS = 0;",
                    "  private static final String[] PERMISSION_WRITESETTINGS = new String[] {\"android.permission.WRITE_SETTINGS\"};",
                    "  private MyFragmentPermissionsDispatcher() {",
                    "  }",
                    "  static void writeSettingsWithPermissionCheck(@NonNull MyFragment target) {",
                    "    if (PermissionUtils.hasSelfPermissions(target.requireActivity(), PERMISSION_WRITESETTINGS) || Settings.System.canWrite(target.requireActivity())) {",
                    "      target.writeSettings();",
                    "    } else {",
                    "      if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_WRITESETTINGS)) {",
                    "        target.writeSettingOnShowRationale(new MyFragmentWriteSettingsPermissionRequest(target));",
                    "      } else {",
                    "        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse(\"package:\" + target.requireActivity().getPackageName()));",
                    "        target.startActivityForResult(intent, REQUEST_WRITESETTINGS);",
                    "      }",
                    "    }",
                    "  }",
                    "  static void onActivityResult(@NonNull MyFragment target, int requestCode) {",
                    "    switch (requestCode) {",
                    "      case REQUEST_WRITESETTINGS:",
                    "      if (PermissionUtils.hasSelfPermissions(target.requireActivity(), PERMISSION_WRITESETTINGS) || Settings.System.canWrite(target.requireActivity())) {",
                    "        target.writeSettings();",
                    "      } else {",
                    "        target.writeSettingOnPermissionDenied();",
                    "      }",
                    "      break;",
                    "      default:",
                    "      break;",
                    "    }",
                    "  }",
                    "  private static final class MyFragmentWriteSettingsPermissionRequest implements PermissionRequest {",
                    "    private final WeakReference<MyFragment> weakTarget;",
                    "    private MyFragmentWriteSettingsPermissionRequest(@NonNull MyFragment target) {",
                    "      this.weakTarget = new WeakReference<MyFragment>(target);",
                    "    }",
                    "    @Override",
                    "    public void proceed() {",
                    "      MyFragment target = weakTarget.get();",
                    "      if (target == null) return;",
                    "      Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse(\"package:\" + target.requireActivity().getPackageName()));",
                    "      target.startActivityForResult(intent, REQUEST_WRITESETTINGS);",
                    "    }",
                    "    @Override",
                    "    public void cancel() {",
                    "      MyFragment target = weakTarget.get();",
                    "      if (target == null) return;",
                    "      target.writeSettingOnPermissionDenied();",
                    "    }",
                    "  }",
                    "}"
            };
        }
    };

    public static final BaseTest SystemAlertWindowFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package test;",
                    "import android.Manifest;",
                    "import androidx.fragment.app.Fragment;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnNeverAskAgain;",
                    "import permissions.dispatcher.OnPermissionDenied;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "@RuntimePermissions",
                    "public class MyFragment extends Fragment {",
                    "    @NeedsPermission(Manifest.permission.SYSTEM_ALERT_WINDOW)",
                    "    void systemAlertWindow() {",
                    "    }",
                    "    @OnShowRationale(Manifest.permission.SYSTEM_ALERT_WINDOW)",
                    "    void systemAlertWindowOnShowRationale(PermissionRequest request) {",
                    "    }",
                    "    @OnPermissionDenied(Manifest.permission.SYSTEM_ALERT_WINDOW)",
                    "    void systemAlertWindowOnPermissionDenied() {",
                    "    }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package test;",
                    "import android.content.Intent;",
                    "import android.net.Uri;",
                    "import android.provider.Settings;",
                    "import androidx.annotation.NonNull;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "  private static final int REQUEST_SYSTEMALERTWINDOW = 0;",
                    "  private static final String[] PERMISSION_SYSTEMALERTWINDOW = new String[] {\"android.permission.SYSTEM_ALERT_WINDOW\"};",
                    "  private MyFragmentPermissionsDispatcher() {",
                    "  }",
                    "  static void systemAlertWindowWithPermissionCheck(@NonNull MyFragment target) {",
                    "    if (PermissionUtils.hasSelfPermissions(target.requireActivity(), PERMISSION_SYSTEMALERTWINDOW) || Settings.canDrawOverlays(target.requireActivity())) {",
                    "      target.systemAlertWindow();",
                    "    } else {",
                    "      if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SYSTEMALERTWINDOW)) {",
                    "        target.systemAlertWindowOnShowRationale(new MyFragmentSystemAlertWindowPermissionRequest(target));",
                    "      } else {",
                    "        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse(\"package:\" + target.requireActivity().getPackageName()));",
                    "        target.startActivityForResult(intent, REQUEST_SYSTEMALERTWINDOW);",
                    "      }",
                    "    }",
                    "  }",
                    "  static void onActivityResult(@NonNull MyFragment target, int requestCode) {",
                    "    switch (requestCode) {",
                    "      case REQUEST_SYSTEMALERTWINDOW:",
                    "      if (PermissionUtils.hasSelfPermissions(target.requireActivity(), PERMISSION_SYSTEMALERTWINDOW) || Settings.canDrawOverlays(target.requireActivity())) {",
                    "        target.systemAlertWindow();",
                    "      } else {",
                    "        target.systemAlertWindowOnPermissionDenied();",
                    "      }",
                    "      break;",
                    "      default:",
                    "      break;",
                    "    }",
                    "  }",
                    "  private static final class MyFragmentSystemAlertWindowPermissionRequest implements PermissionRequest {",
                    "    private final WeakReference<MyFragment> weakTarget;",
                    "    private MyFragmentSystemAlertWindowPermissionRequest(@NonNull MyFragment target) {",
                    "      this.weakTarget = new WeakReference<MyFragment>(target);",
                    "    }",
                    "    @Override",
                    "    public void proceed() {",
                    "      MyFragment target = weakTarget.get();",
                    "      if (target == null) return;",
                    "      Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse(\"package:\" + target.requireActivity().getPackageName()));",
                    "      target.startActivityForResult(intent, REQUEST_SYSTEMALERTWINDOW);",
                    "    }",
                    "    @Override",
                    "    public void cancel() {",
                    "      MyFragment target = weakTarget.get();",
                    "      if (target == null) return;",
                    "      target.systemAlertWindowOnPermissionDenied();",
                    "    }",
                    "  }",
                    "}",
            };
        }
    };

    public static final BaseTest WriteSettingsActivity = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package test;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnNeverAskAgain;",
                    "import permissions.dispatcher.OnPermissionDenied;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "    @NeedsPermission(Manifest.permission.WRITE_SETTINGS)",
                    "    void writeSettings() {",
                    "    }",
                    "    @OnShowRationale(Manifest.permission.WRITE_SETTINGS)",
                    "    void writeSettingsOnShowRationale(PermissionRequest request) {",
                    "    }",
                    "    @OnPermissionDenied(Manifest.permission.WRITE_SETTINGS)",
                    "    void writeSettingsOnPermissionDenied() {",
                    "    }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package test;",
                    "import android.content.Intent;",
                    "import android.net.Uri;",
                    "import android.provider.Settings;",
                    "import androidx.annotation.NonNull;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "  private static final int REQUEST_WRITESETTINGS = 0;",
                    "  private static final String[] PERMISSION_WRITESETTINGS = new String[] {\"android.permission.WRITE_SETTINGS\"};",
                    "  private MyActivityPermissionsDispatcher() {",
                    "  }",
                    "  static void writeSettingsWithPermissionCheck(@NonNull MyActivity target) {",
                    "    if (PermissionUtils.hasSelfPermissions(target, PERMISSION_WRITESETTINGS) || Settings.System.canWrite(target)) {",
                    "      target.writeSettings();",
                    "    } else {",
                    "      if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_WRITESETTINGS)) {",
                    "        target.writeSettingsOnShowRationale(new MyActivityWriteSettingsPermissionRequest(target));",
                    "      } else {",
                    "        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse(\"package:\" + target.getPackageName()));",
                    "        target.startActivityForResult(intent, REQUEST_WRITESETTINGS);",
                    "      }",
                    "    }",
                    "  }",
                    "  static void onActivityResult(@NonNull MyActivity target, int requestCode) {",
                    "    switch (requestCode) {",
                    "      case REQUEST_WRITESETTINGS:",
                    "      if (PermissionUtils.hasSelfPermissions(target, PERMISSION_WRITESETTINGS) || Settings.System.canWrite(target)) {",
                    "        target.writeSettings();",
                    "      } else {",
                    "        target.writeSettingsOnPermissionDenied();",
                    "      }",
                    "      break;",
                    "      default:",
                    "      break;",
                    "    }",
                    "  }",
                    "  private static final class MyActivityWriteSettingsPermissionRequest implements PermissionRequest {",
                    "    private final WeakReference<MyActivity> weakTarget;",
                    "    private MyActivityWriteSettingsPermissionRequest(@NonNull MyActivity target) {",
                    "      this.weakTarget = new WeakReference<MyActivity>(target);",
                    "    }",
                    "    @Override",
                    "    public void proceed() {",
                    "      MyActivity target = weakTarget.get();",
                    "      if (target == null) return;",
                    "      Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse(\"package:\" + target.getPackageName()));",
                    "      target.startActivityForResult(intent, REQUEST_WRITESETTINGS);",
                    "    }",
                    "    @Override",
                    "    public void cancel() {",
                    "      MyActivity target = weakTarget.get();",
                    "      if (target == null) return;",
                    "      target.writeSettingsOnPermissionDenied();",
                    "    }",
                    "  }",
                    "}"
            };
        }
    };

    public static final BaseTest SystemAlertWindowActivity = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package test;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnNeverAskAgain;",
                    "import permissions.dispatcher.OnPermissionDenied;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "    @NeedsPermission(Manifest.permission.SYSTEM_ALERT_WINDOW)",
                    "    void systemAlertWindow() {",
                    "    }",
                    "    @OnShowRationale(Manifest.permission.SYSTEM_ALERT_WINDOW)",
                    "    void systemAlertWindowOnShowRationale(PermissionRequest request) {",
                    "    }",
                    "    @OnPermissionDenied(Manifest.permission.SYSTEM_ALERT_WINDOW)",
                    "    void systemAlertWindowOnPermissionDenied() {",
                    "    }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package test;",
                    "import android.content.Intent;",
                    "import android.net.Uri;",
                    "import android.provider.Settings;",
                    "import androidx.annotation.NonNull;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "  private static final int REQUEST_SYSTEMALERTWINDOW = 0;",
                    "  private static final String[] PERMISSION_SYSTEMALERTWINDOW = new String[] {\"android.permission.SYSTEM_ALERT_WINDOW\"};",
                    "  private MyActivityPermissionsDispatcher() {",
                    "  }",
                    "  static void systemAlertWindowWithPermissionCheck(@NonNull MyActivity target) {",
                    "    if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SYSTEMALERTWINDOW) || Settings.canDrawOverlays(target)) {",
                    "      target.systemAlertWindow();",
                    "    } else {",
                    "      if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SYSTEMALERTWINDOW)) {",
                    "        target.systemAlertWindowOnShowRationale(new MyActivitySystemAlertWindowPermissionRequest(target));",
                    "      } else {",
                    "        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse(\"package:\" + target.getPackageName()));",
                    "        target.startActivityForResult(intent, REQUEST_SYSTEMALERTWINDOW);",
                    "      }",
                    "    }",
                    "  }",
                    "  static void onActivityResult(@NonNull MyActivity target, int requestCode) {",
                    "    switch (requestCode) {",
                    "      case REQUEST_SYSTEMALERTWINDOW:",
                    "      if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SYSTEMALERTWINDOW) || Settings.canDrawOverlays(target)) {",
                    "        target.systemAlertWindow();",
                    "      } else {",
                    "        target.systemAlertWindowOnPermissionDenied();",
                    "      }",
                    "      break;",
                    "      default:",
                    "      break;",
                    "    }",
                    "  }",
                    "  private static final class MyActivitySystemAlertWindowPermissionRequest implements PermissionRequest {",
                    "    private final WeakReference<MyActivity> weakTarget;",
                    "    private MyActivitySystemAlertWindowPermissionRequest(@NonNull MyActivity target) {",
                    "      this.weakTarget = new WeakReference<MyActivity>(target);",
                    "    }",
                    "    @Override",
                    "    public void proceed() {",
                    "      MyActivity target = weakTarget.get();",
                    "      if (target == null) return;",
                    "      Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse(\"package:\" + target.getPackageName()));",
                    "      target.startActivityForResult(intent, REQUEST_SYSTEMALERTWINDOW);",
                    "    }",
                    "    @Override",
                    "    public void cancel() {",
                    "      MyActivity target = weakTarget.get();",
                    "      if (target == null) return;",
                    "      target.systemAlertWindowOnPermissionDenied();",
                    "    }",
                    "  }",
                    "}"
            };
        }
    };

    public static final BaseTest SystemAlertWindowSupportGenericsFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package test;",
                    "import android.Manifest;",
                    "import androidx.fragment.app.Fragment;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnNeverAskAgain;",
                    "import permissions.dispatcher.OnPermissionDenied;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import java.lang.annotation.Annotation;",
                    "import java.util.Map;",
                    "@RuntimePermissions",
                    "public class MyFragment<T, U extends Integer, V extends Map<? extends Annotation, ?>> extends Fragment {",
                    "    @NeedsPermission(Manifest.permission.SYSTEM_ALERT_WINDOW)",
                    "    void systemAlertWindow() {",
                    "    }",
                    "    @OnShowRationale(Manifest.permission.SYSTEM_ALERT_WINDOW)",
                    "    void systemAlertWindowOnShowRationale(PermissionRequest request) {",
                    "    }",
                    "    @OnPermissionDenied(Manifest.permission.SYSTEM_ALERT_WINDOW)",
                    "    void systemAlertWindowOnPermissionDenied() {",
                    "    }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package test;",
                    "import android.content.Intent;",
                    "import android.net.Uri;",
                    "import android.provider.Settings;",
                    "import androidx.annotation.NonNull;",
                    "import java.lang.Integer;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.annotation.Annotation;",
                    "import java.lang.ref.WeakReference;",
                    "import java.util.Map;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "  private static final int REQUEST_SYSTEMALERTWINDOW = 0;",
                    "  private static final String[] PERMISSION_SYSTEMALERTWINDOW = new String[] {\"android.permission.SYSTEM_ALERT_WINDOW\"};",
                    "  private MyFragmentPermissionsDispatcher() {",
                    "  }",
                    "  static <T, U extends Integer, V extends Map<? extends Annotation, ?>> void systemAlertWindowWithPermissionCheck(@NonNull MyFragment<T, U, V> target) {",
                    "    if (PermissionUtils.hasSelfPermissions(target.requireActivity(), PERMISSION_SYSTEMALERTWINDOW) || Settings.canDrawOverlays(target.requireActivity())) {",
                    "      target.systemAlertWindow();",
                    "    } else {",
                    "      if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SYSTEMALERTWINDOW)) {",
                    "        target.systemAlertWindowOnShowRationale(new MyFragmentSystemAlertWindowPermissionRequest(target));",
                    "      } else {",
                    "        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse(\"package:\" + target.requireActivity().getPackageName()));",
                    "        target.startActivityForResult(intent, REQUEST_SYSTEMALERTWINDOW);",
                    "      }",
                    "    }",
                    "  }",
                    "  static <T, U extends Integer, V extends Map<? extends Annotation, ?>> void onActivityResult(@NonNull MyFragment<T, U, V> target, int requestCode) {",
                    "    switch (requestCode) {",
                    "      case REQUEST_SYSTEMALERTWINDOW:",
                    "      if (PermissionUtils.hasSelfPermissions(target.requireActivity(), PERMISSION_SYSTEMALERTWINDOW) || Settings.canDrawOverlays(target.requireActivity())) {",
                    "        target.systemAlertWindow();",
                    "      } else {",
                    "        target.systemAlertWindowOnPermissionDenied();",
                    "      }",
                    "      break;",
                    "      default:",
                    "      break;",
                    "    }",
                    "  }",
                    "  private static final class MyFragmentSystemAlertWindowPermissionRequest<T, U extends Integer, V extends Map<? extends Annotation, ?>> implements PermissionRequest {",
                    "    private final WeakReference<MyFragment<T, U, V>> weakTarget;",
                    "    private MyFragmentSystemAlertWindowPermissionRequest(@NonNull MyFragment<T, U, V> target) {",
                    "      this.weakTarget = new WeakReference<MyFragment<T, U, V>>(target);",
                    "    }",
                    "    @Override",
                    "    public void proceed() {",
                    "      MyFragment<T, U, V> target = weakTarget.get();",
                    "      if (target == null) return;",
                    "      Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse(\"package:\" + target.requireActivity().getPackageName()));",
                    "      target.startActivityForResult(intent, REQUEST_SYSTEMALERTWINDOW);",
                    "    }",
                    "    @Override",
                    "    public void cancel() {",
                    "      MyFragment<T, U, V> target = weakTarget.get();",
                    "      if (target == null) return;",
                    "      target.systemAlertWindowOnPermissionDenied();",
                    "    }",
                    "  }",
                    "}",
            };
        }
    };

    public static final BaseTest SystemAlertWindowGenericsActivity = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package test;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnNeverAskAgain;",
                    "import permissions.dispatcher.OnPermissionDenied;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import java.lang.annotation.Annotation;",
                    "import java.util.Map;",
                    "@RuntimePermissions",
                    "public class MyActivity<T, U extends Integer, V extends Map<? extends Annotation, ?>> extends Activity {",
                    "    @NeedsPermission(Manifest.permission.SYSTEM_ALERT_WINDOW)",
                    "    void systemAlertWindow() {",
                    "    }",
                    "    @OnShowRationale(Manifest.permission.SYSTEM_ALERT_WINDOW)",
                    "    void systemAlertWindowOnShowRationale(PermissionRequest request) {",
                    "    }",
                    "    @OnPermissionDenied(Manifest.permission.SYSTEM_ALERT_WINDOW)",
                    "    void systemAlertWindowOnPermissionDenied() {",
                    "    }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package test;",
                    "import android.content.Intent;",
                    "import android.net.Uri;",
                    "import android.provider.Settings;",
                    "import androidx.annotation.NonNull;",
                    "import java.lang.Integer;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.annotation.Annotation;",
                    "import java.lang.ref.WeakReference;",
                    "import java.util.Map;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "  private static final int REQUEST_SYSTEMALERTWINDOW = 0;",
                    "  private static final String[] PERMISSION_SYSTEMALERTWINDOW = new String[] {\"android.permission.SYSTEM_ALERT_WINDOW\"};",
                    "  private MyActivityPermissionsDispatcher() {",
                    "  }",
                    "  static <T, U extends Integer, V extends Map<? extends Annotation, ?>> void systemAlertWindowWithPermissionCheck(@NonNull MyActivity<T, U, V> target) {",
                    "    if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SYSTEMALERTWINDOW) || Settings.canDrawOverlays(target)) {",
                    "      target.systemAlertWindow();",
                    "    } else {",
                    "      if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SYSTEMALERTWINDOW)) {",
                    "        target.systemAlertWindowOnShowRationale(new MyActivitySystemAlertWindowPermissionRequest(target));",
                    "      } else {",
                    "        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse(\"package:\" + target.getPackageName()));",
                    "        target.startActivityForResult(intent, REQUEST_SYSTEMALERTWINDOW);",
                    "      }",
                    "    }",
                    "  }",
                    "  static <T, U extends Integer, V extends Map<? extends Annotation, ?>> void onActivityResult(@NonNull MyActivity<T, U, V> target, int requestCode) {",
                    "    switch (requestCode) {",
                    "      case REQUEST_SYSTEMALERTWINDOW:",
                    "      if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SYSTEMALERTWINDOW) || Settings.canDrawOverlays(target)) {",
                    "        target.systemAlertWindow();",
                    "      } else {",
                    "        target.systemAlertWindowOnPermissionDenied();",
                    "      }",
                    "      break;",
                    "      default:",
                    "      break;",
                    "    }",
                    "  }",
                    "  private static final class MyActivitySystemAlertWindowPermissionRequest<T, U extends Integer, V extends Map<? extends Annotation, ?>> implements PermissionRequest {",
                    "    private final WeakReference<MyActivity<T, U, V>> weakTarget;",
                    "    private MyActivitySystemAlertWindowPermissionRequest(@NonNull MyActivity<T, U, V> target) {",
                    "      this.weakTarget = new WeakReference<MyActivity<T, U, V>>(target);",
                    "    }",
                    "    @Override",
                    "    public void proceed() {",
                    "      MyActivity<T, U, V> target = weakTarget.get();",
                    "      if (target == null) return;",
                    "      Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse(\"package:\" + target.getPackageName()));",
                    "      target.startActivityForResult(intent, REQUEST_SYSTEMALERTWINDOW);",
                    "    }",
                    "    @Override",
                    "    public void cancel() {",
                    "      MyActivity<T, U, V> target = weakTarget.get();",
                    "      if (target == null) return;",
                    "      target.systemAlertWindowOnPermissionDenied();",
                    "    }",
                    "  }",
                    "}"
            };
        }
    };

    public static final BaseTest SystemAlertWindowMixPermissionCase = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package test;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "    @NeedsPermission({Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.INTERNET})",
                    "    void systemAlertWindow() {",
                    "    }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return null;
        }
    };

    public static final BaseTest WriteSettingsMixPermissionCase = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package test;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "    @NeedsPermission({Manifest.permission.WRITE_SETTINGS, Manifest.permission.INTERNET})",
                    "    void systemAlertWindow() {",
                    "    }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return null;
        }
    };

    public static final BaseTest SystemAlertWindowAndWriteSettingsMixPermissionCase = new BaseTest() {
        @Override
        protected String getName() {
            return "MyActivity";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package test;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "    @NeedsPermission({Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.WRITE_SETTINGS})",
                    "    void systemAlertWindow() {",
                    "    }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return null;
        }
    };

    public static final BaseTest NestedActivity = new BaseTest() {
        @Override
        protected String getName() {
            return "Foo";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "public class Foo {",
                    "  public class Bar {",
                    "    @RuntimePermissions",
                    "    public class MyActivity extends Activity {",
                    "      @NeedsPermission(Manifest.permission.CAMERA)",
                    "      void showCamera() {",
                    "      }",
                    "    }",
                    "  }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import androidx.annotation.NonNull;",
                    "import androidx.core.app.ActivityCompat;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull Foo.Bar.MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull Foo.Bar.MyActivity target, int requestCode, int[] grantResults) {",
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

    public static final BaseTest NestedStaticActivity = new BaseTest() {
        @Override
        protected String getName() {
            return "Foo";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "public class Foo {",
                    "  @RuntimePermissions",
                    "  public static class MyActivity extends Activity {",
                    "    @NeedsPermission(Manifest.permission.CAMERA)",
                    "    void showCamera() {",
                    "    }",
                    "  }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import androidx.annotation.NonNull;",
                    "import androidx.core.app.ActivityCompat;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull Foo.MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull Foo.MyActivity target, int requestCode, int[] grantResults) {",
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

    public static final BaseTest NestedActivityWithDefaultPackage = new BaseTest() {
        @Override
        protected String getName() {
            return "Foo";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "import android.Manifest;",
                    "import android.app.Activity;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "public class Foo {",
                    "  public class Bar {",
                    "    @RuntimePermissions",
                    "    public class MyActivity extends Activity {",
                    "      @NeedsPermission(Manifest.permission.CAMERA)",
                    "      void showCamera() {",
                    "      }",
                    "    }",
                    "  }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "import androidx.annotation.NonNull;",
                    "import androidx.core.app.ActivityCompat;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull Foo.Bar.MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull Foo.Bar.MyActivity target, int requestCode, int[] grantResults) {",
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

    public static final BaseTest NestedFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "Foo";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import androidx.fragment.app.Fragment;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "public class Foo {",
                    "  @RuntimePermissions",
                    "  public class MyFragment extends Fragment {",
                    "     @NeedsPermission(Manifest.permission.CAMERA)",
                    "     void showCamera() {",
                    "     }",
                    "  }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import androidx.annotation.NonNull;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private  MyFragmentPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull Foo.MyFragment target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.requireActivity(), PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull Foo.MyFragment target, int requestCode, int[] grantResults) {",
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

    public static final BaseTest NestedStaticFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "Foo";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import androidx.fragment.app.Fragment;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "public class Foo {",
                    "  @RuntimePermissions",
                    "  public static class MyFragment extends Fragment {",
                    "     @NeedsPermission(Manifest.permission.CAMERA)",
                    "     void showCamera() {",
                    "     }",
                    "  }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[]{
                    "package tests;",
                    "import androidx.annotation.NonNull;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private  MyFragmentPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull Foo.MyFragment target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.requireActivity(), PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull Foo.MyFragment target, int requestCode, int[] grantResults) {",
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

    public static final BaseTest needsPermissionMethodOverload = new BaseTest() {
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
                    "   public void showCamera() {",
                    "   }",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   public void showCamera(int foo) {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return EMPTY_SOURCE;
        }
    };

    public static final BaseTest systemAlertWindowWithOnNeverAskAgain = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package test;",
                    "import android.Manifest;",
                    "import androidx.fragment.app.Fragment;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnNeverAskAgain;",
                    "import permissions.dispatcher.OnPermissionDenied;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "@RuntimePermissions",
                    "public class MyFragment extends Fragment {",
                    "    @NeedsPermission(Manifest.permission.SYSTEM_ALERT_WINDOW)",
                    "    void systemAlertWindow() {",
                    "    }",
                    "    @OnShowRationale(Manifest.permission.SYSTEM_ALERT_WINDOW)",
                    "    void systemAlertWindowOnShowRationale(PermissionRequest request) {",
                    "    }",
                    "    @OnNeverAskAgain(Manifest.permission.SYSTEM_ALERT_WINDOW)",
                    "    void systemAlertWindowOnNeverAskAgain() {",
                    "    }",
                    "    @OnPermissionDenied(Manifest.permission.SYSTEM_ALERT_WINDOW)",
                    "    void systemAlertWindowOnPermissionDenied() {",
                    "    }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return EMPTY_SOURCE;
        }
    };

    public static final BaseTest writeSettingsWithOnNeverAskAgain = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package test;",
                    "import android.Manifest;",
                    "import androidx.fragment.app.Fragment;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnNeverAskAgain;",
                    "import permissions.dispatcher.OnPermissionDenied;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "@RuntimePermissions",
                    "public class MyFragment extends Fragment {",
                    "    @NeedsPermission(Manifest.permission.WRITE_SETTINGS)",
                    "    void writeSettings() {",
                    "    }",
                    "    @OnShowRationale(Manifest.permission.WRITE_SETTINGS)",
                    "    void writeSettingOnShowRationale(PermissionRequest request) {",
                    "    }",
                    "    @OnNeverAskAgain(Manifest.permission.WRITE_SETTINGS)",
                    "    void writeSettingOnNeverAskAgain() {",
                    "    }",
                    "    @OnPermissionDenied(Manifest.permission.WRITE_SETTINGS)",
                    "    void writeSettingOnPermissionDenied() {",
                    "    }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return EMPTY_SOURCE;
        }
    };

    public static final BaseTest OnePermissionWithNoArgumentRationaleActivity = new BaseTest() {
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
            return new String[]{
                    "package tests;",
                    "import androidx.annotation.NonNull;",
                    "import androidx.core.app.ActivityCompat;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale();",
                    "           } else {",
                    "               ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void proceedShowCameraPermissionRequest(@NonNull MyActivity target) {",
                    "       ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyActivity target, int requestCode, int[] grantResults) {",
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

    public static final BaseTest OnePermissionWithNoArgumentRationaleAndDeniedActivity = new BaseTest() {
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
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnShowRationale(Manifest.permission.CAMERA)",
                    "   void cameraRationale() {",
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
                    "import androidx.annotation.NonNull;",
                    "import androidx.core.app.ActivityCompat;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale();",
                    "           } else {",
                    "               ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void proceedShowCameraPermissionRequest(@NonNull MyActivity target) {",
                    "       ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "   }",
                    "   static void cancelShowCameraPermissionRequest(@NonNull MyActivity target) {",
                    "       target.cameraDenied();",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyActivity target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
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

    public static final BaseTest OnePermissionWithParamNoArgumentRationaleAndDeniedActivity = new BaseTest() {
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
                    "@RuntimePermissions",
                    "public class MyActivity extends Activity {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera(String arg) {",
                    "   }",
                    "   @OnShowRationale(Manifest.permission.CAMERA)",
                    "   void cameraRationale() {",
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
                    "import androidx.annotation.NonNull;",
                    "import androidx.core.app.ActivityCompat;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyActivityPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static String showCameraArg;",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull MyActivity target, String arg) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera(arg);",
                    "       } else {",
                    "           showCameraArg = arg;",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale();",
                    "           } else {",
                    "               ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void proceedShowCameraPermissionRequest(@NonNull MyActivity target) {",
                    "       ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "   }",
                    "   static void cancelShowCameraPermissionRequest(@NonNull MyActivity target) {",
                    "       target.cameraDenied();",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyActivity target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera(showCameraArg);",
                    "               } else {",
                    "                   target.cameraDenied();",
                    "               }",
                    "               showCameraArg = null;",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest OnePermissionWithNoArgumentRationaleFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import androidx.fragment.app.Fragment;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "@RuntimePermissions",
                    "public class MyFragment extends Fragment {",
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
            return new String[]{
                    "package tests;",
                    "import androidx.annotation.NonNull;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyFragmentPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull MyFragment target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.requireActivity(), PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale();",
                    "           } else {",
                    "               target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void proceedShowCameraPermissionRequest(@NonNull MyFragment target) {",
                    "       target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyFragment target, int requestCode, int[] grantResults) {",
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

    public static final BaseTest OnePermissionWithNoArgumentRationaleAndDeniedFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import androidx.fragment.app.Fragment;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.OnPermissionDenied;",
                    "@RuntimePermissions",
                    "public class MyFragment extends Fragment {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnShowRationale(Manifest.permission.CAMERA)",
                    "   void cameraRationale() {",
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
                    "import androidx.annotation.NonNull;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyFragmentPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull MyFragment target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.requireActivity(), PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale();",
                    "           } else {",
                    "               target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void proceedShowCameraPermissionRequest(@NonNull MyFragment target) {",
                    "       target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "   }",
                    "   static void cancelShowCameraPermissionRequest(@NonNull MyFragment target) {",
                    "       target.cameraDenied();",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyFragment target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
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
    public static final BaseTest OnePermissionWithParamNoArgumentRationaleAndDeniedFragment = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[]{
                    "package tests;",
                    "import android.Manifest;",
                    "import androidx.fragment.app.Fragment;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.OnPermissionDenied;",
                    "@RuntimePermissions",
                    "public class MyFragment extends Fragment {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera(String arg) {",
                    "   }",
                    "   @OnShowRationale(Manifest.permission.CAMERA)",
                    "   void cameraRationale() {",
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
                    "import androidx.annotation.NonNull;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static String showCameraArg;",
                    "   private MyFragmentPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithPermissionCheck(@NonNull MyFragment target, String arg) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.requireActivity(), PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera(arg);",
                    "       } else {",
                    "           showCameraArg = arg;",
                    "           if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale();",
                    "           } else {",
                    "               target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void proceedShowCameraPermissionRequest(@NonNull MyFragment target) {",
                    "       target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "   }",
                    "   static void cancelShowCameraPermissionRequest(@NonNull MyFragment target) {",
                    "       target.cameraDenied();",
                    "   }",
                    "   static void onRequestPermissionsResult(@NonNull MyFragment target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera(showCameraArg);",
                    "               } else {",
                    "                   target.cameraDenied();",
                    "               }",
                    "               showCameraArg = null;",
                    "               break;",
                    "           default:",
                    "               break;",
                    "       }",
                    "   }",
                    "}"
            };
        }
    };
}
