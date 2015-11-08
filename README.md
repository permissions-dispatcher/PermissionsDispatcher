# PermissionsDispatcher

[![Build Status](https://travis-ci.org/hotchemi/PermissionsDispatcher.svg)](https://travis-ci.org/hotchemi/PermissionsDispatcher)
[ ![Download](https://api.bintray.com/packages/hotchemi/maven/permissionsdispatcher/images/download.svg) ](https://bintray.com/hotchemi/maven/permissionsdispatcher/_latestVersion)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-PermissionsDispatcher-green.svg?style=flat)](https://android-arsenal.com/details/1/2316)

PermissionsDispatcher provides a simple annotation-based API to handle runtime permissions in Android Marshmallow.
[Runtime permissions](https://developer.android.com/preview/features/runtime-permissions.html) are great for users, but can be tedious to implement correctly for developers, requiring a lot of boilerplate code. This library lifts the burden that comes with writing a bunch of check statements whether a permission has been granted or not from you, in order to keep your code clean and safe. The library is 100% reflection-free.

## Download

This library is only compatible with Gradle-based builds. To add it to your project, include the following in your `build.gradle` files:

```groovy
buildscript {
  dependencies {
    classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
  }
}

apply plugin: 'android-apt'

dependencies {
  compile 'com.github.hotchemi:permissionsdispatcher:2.0.0'
  apt 'com.github.hotchemi:permissionsdispatcher-processor:2.0.0'
}
```

PermissionsDispatcher depends on the `support-v4` library by default, in order to be able to use `ActivityCompat` for permission requests.

## Usage

Here's a minimum example, in which we register a `MainActivity` which requires `Manifest.permission.CAMERA`.

### 1. Attach annotations

PermissionsDispatcher introduces only a few annotations, keeping its general API concise:

|Annotation|Required|Description|
|---|---|---|
|`@RuntimePermissions`|**✓**|Register an `Activity` or `Fragment` to handle permissions|
|`@NeedsPermission`|**✓**|Annotate a method which performs the action that requires one or more permissions|
|`@OnShowRationale`||Annotate a method which explains why the permission/s is/are needed. It passes in a `PermissionRequest` object which can be used to continue or abort the current permission request upon user input|
|`@OnPermissionDenied`||Annotate a method which is invoked if the user doesn't grant the permissions|

> NOTE: Annotated methods must not be `private`.

```java
@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    @NeedsPermission(Manifest.permission.CAMERA)
    void showCamera() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.sample_content_fragment, CameraPreviewFragment.newInstance())
                .addToBackStack("camera")
                .commitAllowingStateLoss();
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    void showRationaleForCamera(PermissionRequest request) {
		new AlertDialog.Builder(this)
			.setMessage(R.string.permission_camera_rationale)
			.setPositiveButton(R.string.button_allow, (dialog, button) -> request.proceed())
			.setNegativeButton(R.string.button_deny, (dialog, button) -> request.cancel())
			.show();
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void showDeniedForCamera() {
        Toast.makeText(this, R.string.permission_camera_denied, Toast.LENGTH_SHORT).show();
}
```

### 2. Delegate to generated class

Upon compilation, PermissionsDispatcher generates a class for `MainActivity` (suffixed with `PermissionsDispatcher`), which you can use to safely access these permission-protected methods. The only step you have to do is delegating the work to this helper class:

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Button cameraButton = (Button) findViewById(R.id.button_camera);
    cameraButton.setOnClickListener(this);
    Button contactsButton = (Button) findViewById(R.id.button_contacts);
    contactsButton.setOnClickListener(this);
}

@Override
public void onClick(View v) {
    switch (v.getId()) {
        case R.id.button_camera:
            // NOTE: delegate the permission handling to generated method
            MainActivityPermissionsDispatcher.showCameraWithCheck(this);
            break;
        case R.id.button_contacts:
            // NOTE: delegate the permission handling to generated method
            MainActivityPermissionsDispatcher.showContactsWithCheck(this);
            break;
    }
}

@Override
public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    // NOTE: delegate the permission handling to generated method
    MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
}
```

Check out the [sample](https://github.com/hotchemi/PermissionsDispatcher/tree/master/sample) and [generated class](https://gist.github.com/aurae/7821f3a2da098b949f7b) for more details.

## Support

PermissionsDispatcher is supported on **API levels 4 and up**, with which you get support for annotating `android.app.Activity` and `android.support.v4.app.Fragment` sub-classes out of the box. In case you rely on `android.app.Fragment` in your app, you can use these with PermissionsDispatcher as well! Simply add a dependency on the `support-v13` library alongside PermissionsDispatcher in your project, and it will enable support for native fragments.

## License

```
Copyright 2015 Shintaro Katafuchi, Marcel Schnelle

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
