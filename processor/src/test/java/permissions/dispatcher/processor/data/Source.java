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
            return new String[] {
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
            return new String[] {
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
            return new String[] {
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
            return new String[] {
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
            return new String[] {
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

    public static final BaseTest DeniedWithNonVoidReturnType = new BaseTest() {
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

    public static final BaseTest PermissionWithParameters = new BaseTest() {
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
            return new String[] {
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

    public static final BaseTest RationaleWithoutParameters = new BaseTest() {
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
            return new String[] {
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

    public static final BaseTest PermissionWithThrows = new BaseTest() {
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
            return new String[] {
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
            return new String[] {
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

    public static final BaseTest PrivatePermission = new BaseTest() {
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
            return new String[] {
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
            return new String[] {
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

    public static final BaseTest WrongAnnotatedClass = new BaseTest() {
        @Override
        protected String getName() {
            return "MyService";
        }

        @Override
        protected String[] getActualSource() {
            return new String[] {
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

    public static final BaseTest DuplicatedPermission = new BaseTest() {
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
            return new String[] {
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
            return new String[] {
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

    public static final BaseTest OnePermissionActivity = new BaseTest() {
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
            return new String[] {
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
            return new String[] {
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
            return new String[] {
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

    public static final BaseTest TwoPermissionsActivity = new BaseTest() {
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
            return new String[] {
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
            return new String[] {
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
            return new String[] {
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
            return new String[] {
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
            return new String[] {
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

    public static final BaseTest OnePermissionWithRationaleActivity = new BaseTest() {
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
            return new String[] {
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
                    "       } else if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.cameraRationale(new ShowCameraPermissionRequest(target));",
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
            return new String[] {
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
            return new String[] {
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
                    "       } else if (PermissionUtils.shouldShowRequestPermissionRationale(activity, PERMISSION_SHOWCAMERA)) {",
                    "           target.cameraRationale(new ShowCameraPermissionRequest(target));",
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

    public static final BaseTest TwoPermissionsWithOneRationaleActivity = new BaseTest() {
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
            return new String[] {
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
                    "       } else if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.cameraRationale(new ShowCameraPermissionRequest(target));",
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
            return new String[] {
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
            return new String[] {
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
                    "       } else if (PermissionUtils.shouldShowRequestPermissionRationale(activity, PERMISSION_SHOWCAMERA)) {",
                    "           target.cameraRationale(new ShowCameraPermissionRequest(target));",
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
            return new String[] {
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
            return new String[] {
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
                    "       } else if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.cameraRationale(new ShowCameraPermissionRequest(target));",
                    "       } else {",
                    "           ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void accessContactsWithCheck(MyActivity target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target, PERMISSION_ACCESSCONTACTS)) {",
                    "           target.accessContacts();",
                    "       } else if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_ACCESSCONTACTS)) {",
                    "           target.contactsRationale(new AccessContactsPermissionRequest(target));",
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
            return new String[] {
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
            return new String[] {
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
                    "       } else if (PermissionUtils.shouldShowRequestPermissionRationale(activity, PERMISSION_SHOWCAMERA)) {",
                    "           target.cameraRationale(new ShowCameraPermissionRequest(target));",
                    "       } else {",
                    "           target.requestPermissions(PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void accessContactsWithCheck(MyFragment target) {",
                    "       Activity activity = target.getActivity();",
                    "       if (PermissionUtils.hasSelfPermissions(activity, PERMISSION_ACCESSCONTACTS)) {",
                    "           target.accessContacts();",
                    "       } else if (PermissionUtils.shouldShowRequestPermissionRationale(activity, PERMISSION_ACCESSCONTACTS)) {",
                    "           target.contactsRationale(new AccessContactsPermissionRequest(target));",
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
            return new String[] {
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
            return new String[] {
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
            return new String[] {
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
            return new String[] {
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
            return new String[] {
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
            return new String[] {
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
            return new String[] {
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
            return new String[] {
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
            return new String[] {
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
            return new String[] {
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
            return new String[] {
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
            return new String[] {
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
            return new String[] {
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
            return new String[] {
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
                    "       } else if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.cameraRationale(new ShowCameraPermissionRequest(target));",
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
            return new String[] {
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
            return new String[] {
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
                    "       } else if (PermissionUtils.shouldShowRequestPermissionRationale(activity, PERMISSION_SHOWCAMERA)) {",
                    "           target.cameraRationale(new ShowCameraPermissionRequest(target));",
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
}
