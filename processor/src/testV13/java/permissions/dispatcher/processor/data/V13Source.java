package permissions.dispatcher.processor.data;

import permissions.dispatcher.processor.base.BaseTest;

import static permissions.dispatcher.processor.data.Source.EMPTY_SOURCE;

public final class V13Source {

    private V13Source() {
    }

    public static final BaseTest NativeFragmentWithoutNeeds = new BaseTest() {
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

    public static final BaseTest OnePermissionNativeFragment = new BaseTest() {
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
                    "import permissions.dispatcher.Needs;",
                    "@RuntimePermissions",
                    "public class MyFragment extends Fragment {",
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
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "import permissions.dispatcher.v13.PermissionUtilsV13;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyFragment target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.getActivity(), PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           PermissionUtilsV13.getInstance().requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
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

    public static final BaseTest TwoPermissionsNativeFragment = new BaseTest() {
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
                    "import permissions.dispatcher.Needs;",
                    "@RuntimePermissions",
                    "public class MyFragment extends Fragment {",
                    "   @Needs(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @Needs(Manifest.permission.READ_CONTACTS)",
                    "   void accessContacts() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[] {
                    "package tests;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "import permissions.dispatcher.v13.PermissionUtilsV13;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static final int REQUEST_ACCESSCONTACTS = 1;",
                    "   private static final String[] PERMISSION_ACCESSCONTACTS = new String[] {\"android.permission.READ_CONTACTS\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyFragment target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.getActivity(), PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           PermissionUtilsV13.getInstance().requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void accessContactsWithCheck(MyFragment target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.getActivity(), PERMISSION_ACCESSCONTACTS)) {",
                    "           target.accessContacts();",
                    "       } else {",
                    "           PermissionUtilsV13.getInstance().requestPermissions(target, PERMISSION_ACCESSCONTACTS, REQUEST_ACCESSCONTACTS);",
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

    public static final BaseTest OnePermissionWithRationaleNativeFragment = new BaseTest() {
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
                    "import permissions.dispatcher.Needs;",
                    "import permissions.dispatcher.OnRationale;",
                    "@RuntimePermissions",
                    "public class MyFragment extends Fragment {",
                    "   @Needs(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnRationale(Manifest.permission.CAMERA)",
                    "   void cameraRationale() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[] {
                    "package tests;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "import permissions.dispatcher.v13.PermissionUtilsV13;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyFragment target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.getActivity(), PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else if (PermissionUtilsV13.getInstance().shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "           target.cameraRationale();",
                    "       } else {",
                    "           PermissionUtilsV13.getInstance().requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
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

    public static final BaseTest OnePermissionWithDeniedNativeFragment = new BaseTest() {
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
                    "import permissions.dispatcher.Needs;",
                    "import permissions.dispatcher.OnDenied;",
                    "@RuntimePermissions",
                    "public class MyFragment extends Fragment {",
                    "   @Needs(Manifest.permission.CAMERA)",
                    "   void showCamera() {",
                    "   }",
                    "   @OnDenied(Manifest.permission.CAMERA)",
                    "   void onCameraDenied() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[] {
                    "package tests;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "import permissions.dispatcher.v13.PermissionUtilsV13;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyFragment target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.getActivity(), PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           PermissionUtilsV13.getInstance().requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
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
}
