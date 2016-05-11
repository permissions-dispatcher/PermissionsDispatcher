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
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.PermissionRequest;",
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
                    "           if (PermissionUtilsV13.getInstance().shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(new ShowCameraPermissionRequest(target));",
                    "           } else {",
                    "               PermissionUtilsV13.getInstance().requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
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
                    "            PermissionUtilsV13.getInstance().requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "        }",
                    "        @Override",
                    "        public void cancel() {",
                    "        }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest OnePermissionWithNeverAskAndRationaleNativeFragment = new BaseTest() {
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
                    "import permissions.dispatcher.OnNeverAskAgain;",
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
                    "   @OnNeverAskAgain(Manifest.permission.CAMERA)",
                    "   void onNeverAskForCamera() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[] {
                    "package tests;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.PermissionRequest;",
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
                    "           if (PermissionUtilsV13.getInstance().shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(new ShowCameraPermissionRequest(target));",
                    "           } else {",
                    "               PermissionUtilsV13.getInstance().requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyFragment target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               } else {",
                    "                   if (!PermissionUtilsV13.getInstance().shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
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
                    "            PermissionUtilsV13.getInstance().requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "        }",
                    "        @Override",
                    "        public void cancel() {",
                    "        }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest OnePermissionWithNeverAskRationaleAndDeniedNativeFragment = new BaseTest() {
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
                    "import permissions.dispatcher.OnNeverAskAgain;",
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
            return new String[] {
                    "package tests;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.PermissionRequest;",
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
                    "           if (PermissionUtilsV13.getInstance().shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(new ShowCameraPermissionRequest(target));",
                    "           } else {",
                    "               PermissionUtilsV13.getInstance().requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void onRequestPermissionsResult(MyFragment target, int requestCode, int[] grantResults) {",
                    "       switch (requestCode) {",
                    "           case REQUEST_SHOWCAMERA:",
                    "               if (PermissionUtils.verifyPermissions(grantResults)) {",
                    "                   target.showCamera();",
                    "               } else {",
                    "                   if (!PermissionUtilsV13.getInstance().shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
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
                    "            PermissionUtilsV13.getInstance().requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
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

    public static final BaseTest OnePermissionWithNeverAskNativeFragment = new BaseTest() {
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
                    "                   if (!PermissionUtilsV13.getInstance().shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
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

    public static final BaseTest OnePermissionWithNeverAskAndDeniedNativeFragment = new BaseTest() {
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
                    "                   if (!PermissionUtilsV13.getInstance().shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
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

    public static final BaseTest OnePermissionWithRationaleAndDeniedNativeFragment = new BaseTest() {
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
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.PermissionRequest;",
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
                    "           if (PermissionUtilsV13.getInstance().shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(new ShowCameraPermissionRequest(target));",
                    "           } else {",
                    "               PermissionUtilsV13.getInstance().requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
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
                    "            PermissionUtilsV13.getInstance().requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
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

    public static final BaseTest OnePermissionWithParametersNativeFragment = new BaseTest() {
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
                    "   void showCamera(int[] values) {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[] {
                    "package tests;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.GrantableRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "import permissions.dispatcher.v13.PermissionUtilsV13;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static GrantableRequest PENDING_SHOWCAMERA;",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyFragment target, int[] values) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.getActivity(), PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera(values);",
                    "       } else {",
                    "           PENDING_SHOWCAMERA = new ShowCameraPermissionRequest(target, values);",
                    "           PermissionUtilsV13.getInstance().requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
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
                    "        private final int[] values;",
                    "        private ShowCameraPermissionRequest(MyFragment target, int[] values) {",
                    "            this.weakTarget = new WeakReference<>(target);",
                    "            this.values = values;",
                    "        }",
                    "        @Override",
                    "        public void proceed() {",
                    "            MyFragment target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            PermissionUtilsV13.getInstance().requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "        }",
                    "        @Override",
                    "        public void cancel() {",
                    "        }",
                    "        @Override",
                    "        public void grant() {",
                    "            MyFragment target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            target.showCamera(values);",
                    "        }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest OnePermissionWithParametersAndRationaleNativeFragment = new BaseTest() {
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
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "@RuntimePermissions",
                    "public class MyFragment extends Fragment {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera(int[] values) {",
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
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.GrantableRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "import permissions.dispatcher.v13.PermissionUtilsV13;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static GrantableRequest PENDING_SHOWCAMERA;",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyFragment target, int[] values) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.getActivity(), PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera(values);",
                    "       } else {",
                    "           PENDING_SHOWCAMERA = new ShowCameraPermissionRequest(target, values);",
                    "           if (PermissionUtilsV13.getInstance().shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(PENDING_SHOWCAMERA);",
                    "           } else {",
                    "               PermissionUtilsV13.getInstance().requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
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
                    "        private final int[] values;",
                    "        private ShowCameraPermissionRequest(MyFragment target, int[] values) {",
                    "            this.weakTarget = new WeakReference<>(target);",
                    "            this.values = values;",
                    "        }",
                    "        @Override",
                    "        public void proceed() {",
                    "            MyFragment target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            PermissionUtilsV13.getInstance().requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "        }",
                    "        @Override",
                    "        public void cancel() {",
                    "        }",
                    "        @Override",
                    "        public void grant() {",
                    "            MyFragment target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            target.showCamera(values);",
                    "        }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest OnePermissionWithParametersAndDeniedNativeFragment = new BaseTest() {
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
                    "import java.util.List;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "import permissions.dispatcher.NeedsPermission;",
                    "import permissions.dispatcher.OnPermissionDenied;",
                    "@RuntimePermissions",
                    "public class MyFragment extends Fragment {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera(List<String> values) {",
                    "   }",
                    "   @OnPermissionDenied(Manifest.permission.CAMERA)",
                    "   void cameraDenied() {",
                    "   }",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return new String[] {
                    "package tests;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import java.util.List;",
                    "import permissions.dispatcher.GrantableRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "import permissions.dispatcher.v13.PermissionUtilsV13;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static GrantableRequest PENDING_SHOWCAMERA;",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyFragment target, List<String> values) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.getActivity(), PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera(values);",
                    "       } else {",
                    "           PENDING_SHOWCAMERA = new ShowCameraPermissionRequest(target, values);",
                    "           PermissionUtilsV13.getInstance().requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
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
                    "        private final List<String> values;",
                    "        private ShowCameraPermissionRequest(MyFragment target, List<String> values) {",
                    "            this.weakTarget = new WeakReference<>(target);",
                    "            this.values = values;",
                    "        }",
                    "        @Override",
                    "        public void proceed() {",
                    "            MyFragment target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            PermissionUtilsV13.getInstance().requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
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
                    "            target.showCamera(values);",
                    "        }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest OnePermissionWithParametersRationaleAndDeniedNativeFragment = new BaseTest() {
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
                    "import permissions.dispatcher.OnShowRationale;",
                    "import permissions.dispatcher.OnPermissionDenied;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "@RuntimePermissions",
                    "public class MyFragment extends Fragment {",
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera(int[] values) {",
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
            return new String[] {
                    "package tests;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.GrantableRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "import permissions.dispatcher.v13.PermissionUtilsV13;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static GrantableRequest PENDING_SHOWCAMERA;",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyFragment target, int[] values) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.getActivity(), PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera(values);",
                    "       } else {",
                    "           PENDING_SHOWCAMERA = new ShowCameraPermissionRequest(target, values);",
                    "           if (PermissionUtilsV13.getInstance().shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(PENDING_SHOWCAMERA);",
                    "           } else {",
                    "               PermissionUtilsV13.getInstance().requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
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
                    "        private final int[] values;",
                    "        private ShowCameraPermissionRequest(MyFragment target, int[] values) {",
                    "            this.weakTarget = new WeakReference<>(target);",
                    "            this.values = values;",
                    "        }",
                    "        @Override",
                    "        public void proceed() {",
                    "            MyFragment target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            PermissionUtilsV13.getInstance().requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
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
                    "            target.showCamera(values);",
                    "        }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest TwoPermissionsWithSameSignatureNativeFragment = new BaseTest() {
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
                    "   @NeedsPermission(Manifest.permission.CAMERA)",
                    "   void showCamera2() {",
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
                    "   private static final int REQUEST_SHOWCAMERA2 = 1;",
                    "   private static final String[] PERMISSION_SHOWCAMERA2 = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyFragment target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.getActivity(), PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           PermissionUtilsV13.getInstance().requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void showCamera2WithCheck(MyFragment target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.getActivity(), PERMISSION_SHOWCAMERA2)) {",
                    "           target.showCamera2();",
                    "       } else {",
                    "           PermissionUtilsV13.getInstance().requestPermissions(target, PERMISSION_SHOWCAMERA2, REQUEST_SHOWCAMERA2);",
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

    public static final BaseTest TwoPermissionsWithSameSignatureAndRationaleNativeFragment = new BaseTest() {
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
            return new String[] {
                    "package tests;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "import permissions.dispatcher.v13.PermissionUtilsV13;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static final int REQUEST_SHOWCAMERA2 = 1;",
                    "   private static final String[] PERMISSION_SHOWCAMERA2 = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyFragment target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.getActivity(), PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           if (PermissionUtilsV13.getInstance().shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(new ShowCameraPermissionRequest(target));",
                    "           } else {",
                    "               PermissionUtilsV13.getInstance().requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void showCamera2WithCheck(MyFragment target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.getActivity(), PERMISSION_SHOWCAMERA2)) {",
                    "           target.showCamera2();",
                    "       } else {",
                    "           if (PermissionUtilsV13.getInstance().shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA2)) {",
                    "               target.cameraRationale(new ShowCamera2PermissionRequest(target));",
                    "           } else {",
                    "               PermissionUtilsV13.getInstance().requestPermissions(target, PERMISSION_SHOWCAMERA2, REQUEST_SHOWCAMERA2);",
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
                    "            PermissionUtilsV13.getInstance().requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "        }",
                    "        @Override",
                    "        public void cancel() {",
                    "        }",
                    "   }",
                    "   private static final class ShowCamera2PermissionRequest implements PermissionRequest {",
                    "        private final WeakReference<MyFragment> weakTarget;",
                    "        private ShowCamera2PermissionRequest(MyFragment target) {",
                    "            this.weakTarget = new WeakReference<>(target);",
                    "        }",
                    "        @Override",
                    "        public void proceed() {",
                    "            MyFragment target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            PermissionUtilsV13.getInstance().requestPermissions(target, PERMISSION_SHOWCAMERA2, REQUEST_SHOWCAMERA2);",
                    "        }",
                    "        @Override",
                    "        public void cancel() {",
                    "        }",
                    "   }",
                    "}"
            };
        }
    };

    public static final BaseTest TwoPermissionsWithSameSignatureAndDeniedNativeFragment = new BaseTest() {
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
            return new String[] {
                    "package tests;",
                    "import java.lang.String;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "import permissions.dispatcher.v13.PermissionUtilsV13;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static final int REQUEST_SHOWCAMERA2 = 1;",
                    "   private static final String[] PERMISSION_SHOWCAMERA2 = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyFragment target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.getActivity(), PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           PermissionUtilsV13.getInstance().requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "       }",
                    "   }",
                    "   static void showCamera2WithCheck(MyFragment target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.getActivity(), PERMISSION_SHOWCAMERA2)) {",
                    "           target.showCamera2();",
                    "       } else {",
                    "           PermissionUtilsV13.getInstance().requestPermissions(target, PERMISSION_SHOWCAMERA2, REQUEST_SHOWCAMERA2);",
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

    public static final BaseTest TwoPermissionsWithSameSignatureRationaleAndDeniedNativeFragment = new BaseTest() {
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
            return new String[] {
                    "package tests;",
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.PermissionUtils;",
                    "import permissions.dispatcher.v13.PermissionUtilsV13;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "   private static final int REQUEST_SHOWCAMERA = 0;",
                    "   private static final String[] PERMISSION_SHOWCAMERA = new String[] {\"android.permission.CAMERA\"};",
                    "   private static final int REQUEST_SHOWCAMERA2 = 1;",
                    "   private static final String[] PERMISSION_SHOWCAMERA2 = new String[] {\"android.permission.CAMERA\"};",
                    "   private MyActivityPermissionsDispatcher() {",
                    "   }",
                    "   static void showCameraWithCheck(MyFragment target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.getActivity(), PERMISSION_SHOWCAMERA)) {",
                    "           target.showCamera();",
                    "       } else {",
                    "           if (PermissionUtilsV13.getInstance().shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {",
                    "               target.cameraRationale(new ShowCameraPermissionRequest(target));",
                    "           } else {",
                    "               PermissionUtilsV13.getInstance().requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
                    "           }",
                    "       }",
                    "   }",
                    "   static void showCamera2WithCheck(MyFragment target) {",
                    "       if (PermissionUtils.hasSelfPermissions(target.getActivity(), PERMISSION_SHOWCAMERA2)) {",
                    "           target.showCamera2();",
                    "       } else {",
                    "           if (PermissionUtilsV13.getInstance().shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA2)) {",
                    "               target.cameraRationale(new ShowCamera2PermissionRequest(target));",
                    "           } else {",
                    "               PermissionUtilsV13.getInstance().requestPermissions(target, PERMISSION_SHOWCAMERA2, REQUEST_SHOWCAMERA2);",
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
                    "            PermissionUtilsV13.getInstance().requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);",
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
                    "        private ShowCamera2PermissionRequest(MyFragment target) {",
                    "            this.weakTarget = new WeakReference<>(target);",
                    "        }",
                    "        @Override",
                    "        public void proceed() {",
                    "            MyFragment target = weakTarget.get();",
                    "            if (target == null) return;",
                    "            PermissionUtilsV13.getInstance().requestPermissions(target, PERMISSION_SHOWCAMERA2, REQUEST_SHOWCAMERA2);",
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
                    "import android.app.Fragment;",
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
                    "    void writeSettingsOnShowRationale(PermissionRequest request) {",
                    "    }",
                    "    @OnPermissionDenied(Manifest.permission.WRITE_SETTINGS)",
                    "    void writeSettingsOnPermissionDenied() {",
                    "    }",
                    "    @OnNeverAskAgain(Manifest.permission.WRITE_SETTINGS)",
                    "    void writeSettingsOnNeverAskAgain() {",
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
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.v13.PermissionUtilsV13;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "  private static final int REQUEST_WRITESETTINGS = 8;",
                    "  private static final String[] PERMISSION_WRITESETTINGS = new String[] {\"android.permission.WRITE_SETTINGS\"};",
                    "  private MyFragmentPermissionsDispatcher() {",
                    "  }",
                    "  static void writeSettingsWithCheck(MyFragment target) {",
                    "    if (PermissionUtils.hasSelfPermissions(target.getActivity(), PERMISSION_WRITESETTINGS) || Settings.System.canWrite(target.getActivity())) {",
                    "      target.writeSettings();",
                    "    } else {",
                    "      if (PermissionUtilsV13.getInstance().shouldShowRequestPermissionRationale(target, PERMISSION_WRITESETTINGS)) {",
                    "        target.writeSettingsOnShowRationale(new WriteSettingsPermissionRequest(target));",
                    "      } else {",
                    "        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse(\"package:\" + target.getActivity().getPackageName()));",
                    "        target.getActivity().startActivityForResult(intent, REQUEST_WRITESETTINGS);",
                    "      }",
                    "    }",
                    "  }",
                    "  static void onActivityResult(MyFragment target, int requestCode, int[] grantResults) {",
                    "    switch (requestCode) {",
                    "      case REQUEST_WRITESETTINGS:",
                    "      if (PermissionUtils.hasSelfPermissions(target.getActivity(), PERMISSION_WRITESETTINGS) || Settings.System.canWrite(target.getActivity())) {",
                    "        target.writeSettings();",
                    "      } else {",
                    "        if (!PermissionUtilsV13.getInstance().shouldShowRequestPermissionRationale(target, PERMISSION_WRITESETTINGS)) {",
                    "          target.writeSettingsOnNeverAskAgain();",
                    "        } else {",
                    "          target.writeSettingsOnPermissionDenied();",
                    "        }",
                    "      }",
                    "      break;",
                    "      default:",
                    "      break;",
                    "    }",
                    "  }",
                    "  private static final class WriteSettingsPermissionRequest implements PermissionRequest {",
                    "    private final WeakReference<MyFragment> weakTarget;",
                    "    private WriteSettingsPermissionRequest(MyFragment target) {",
                    "      this.weakTarget = new WeakReference<>(target);",
                    "    }",
                    "    @Override",
                    "    public void proceed() {",
                    "      MyFragment target = weakTarget.get();",
                    "      if (target == null) return;",
                    "      Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse(\"package:\" + target.getActivity().getPackageName()));",
                    "      target.getActivity().startActivityForResult(intent, REQUEST_WRITESETTINGS);",
                    "    }",
                    "    @Override",
                    "    public void cancel() {",
                    "      MyFragment target = weakTarget.get();",
                    "      if (target == null) return;",
                    "      target.writeSettingsOnPermissionDenied();",
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
                    "import android.app.Fragment;",
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
                    "    @OnNeverAskAgain(Manifest.permission.SYSTEM_ALERT_WINDOW)",
                    "    void systemAlertWindowOnNeverAskAgain() {",
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
                    "import java.lang.Override;",
                    "import java.lang.String;",
                    "import java.lang.ref.WeakReference;",
                    "import permissions.dispatcher.PermissionRequest;",
                    "import permissions.dispatcher.v13.PermissionUtilsV13;",
                    "final class MyFragmentPermissionsDispatcher {",
                    "  private static final int REQUEST_SYSTEMALERTWINDOW = 8;",
                    "  private static final String[] PERMISSION_SYSTEMALERTWINDOW = new String[] {\"android.permission.SYSTEM_ALERT_WINDOW\"};",
                    "  private MyFragmentPermissionsDispatcher() {",
                    "  }",
                    "  static void systemAlertWindowWithCheck(MyFragment target) {",
                    "    if (PermissionUtils.hasSelfPermissions(target.getActivity(), PERMISSION_SYSTEMALERTWINDOW) || Settings.canDrawOverlays(target.getActivity())) {",
                    "      target.systemAlertWindow();",
                    "    } else {",
                    "      if (PermissionUtilsV13.getInstance().shouldShowRequestPermissionRationale(target, PERMISSION_SYSTEMALERTWINDOW)) {",
                    "        target.systemAlertWindowOnShowRationale(new SystemAlertWindowPermissionRequest(target));",
                    "      } else {",
                    "        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse(\"package:\" + target.getActivity().getPackageName()));",
                    "        target.getActivity().startActivityForResult(intent, REQUEST_SYSTEMALERTWINDOW);",
                    "      }",
                    "    }",
                    "  }",
                    "  static void onActivityResult(MyFragment target, int requestCode, int[] grantResults) {",
                    "    switch (requestCode) {",
                    "      case REQUEST_SYSTEMALERTWINDOW:",
                    "      if (PermissionUtils.hasSelfPermissions(target.getActivity(), PERMISSION_SYSTEMALERTWINDOW) || Settings.canDrawOverlays(target.getActivity())) {",
                    "        target.systemAlertWindow();",
                    "      } else {",
                    "        if (!PermissionUtilsV13.getInstance().shouldShowRequestPermissionRationale(target, PERMISSION_SYSTEMALERTWINDOW)) {",
                    "          target.systemAlertWindowOnNeverAskAgain();",
                    "        } else {",
                    "          target.systemAlertWindowOnPermissionDenied();",
                    "        }",
                    "      }",
                    "      break;",
                    "      default:",
                    "      break;",
                    "    }",
                    "  }",
                    "  private static final class SystemAlertWindowPermissionRequest implements PermissionRequest {",
                    "    private final WeakReference<MyFragment> weakTarget;",
                    "    private SystemAlertWindowPermissionRequest(MyFragment target) {",
                    "      this.weakTarget = new WeakReference<>(target);",
                    "    }",
                    "    @Override",
                    "    public void proceed() {",
                    "      MyFragment target = weakTarget.get();",
                    "      if (target == null) return;",
                    "      Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse(\"package:\" + target.getActivity().getPackageName()));",
                    "      target.getActivity().startActivityForResult(intent, REQUEST_SYSTEMALERTWINDOW);",
                    "    }",
                    "    @Override",
                    "    public void cancel() {",
                    "      MyFragment target = weakTarget.get();",
                    "      if (target == null) return;",
                    "      target.systemAlertWindowOnPermissionDenied();",
                    "    }",
                    "  }",
                    "}"
            };
        }
    };
}
