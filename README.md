# PermissionsDispatcher [![Build Status](https://travis-ci.org/permissions-dispatcher/PermissionsDispatcher.svg?branch=master)](https://travis-ci.org/permissions-dispatcher/PermissionsDispatcher)

![image](https://raw.githubusercontent.com/hotchemi/PermissionsDispatcher/master/doc/logo.png)

- [**Fully Kotlin support**](https://github.com/hotchemi/PermissionsDispatcher/blob/master/doc/kotlin_support.md)
- [**Special Permissions support**](https://github.com/hotchemi/PermissionsDispatcher/blob/master/doc/special_permissions.md)
- **Xiaomi support**
- **100% reflection-free**

PermissionsDispatcher provides a simple annotation-based API to handle runtime permissions.

This library lifts the burden that comes with writing a bunch of check statements whether a permission has been granted or not from you, in order to keep your code clean and safe.

## Usage

- If you're using Kotlin check [**Kotlin ver**](https://github.com/hotchemi/PermissionsDispatcher/blob/master/doc/kotlin_support.md) first of all.

Here's a minimum example, in which you register a `MainActivity` which requires `Manifest.permission.CAMERA`.

### 0. Prepare AndroidManifest

Add the following line to `AndroidManifest.xml`:
 
`<uses-permission android:name="android.permission.CAMERA" />`

### 1. Attach annotations

PermissionsDispatcher introduces only a few annotations, keeping its general API concise:

> NOTE: Annotated methods must not be `private`.

|Annotation|Required|Description|
|---|---|---|
|`@RuntimePermissions`|**✓**|Register an `Activity` or `Fragment`(we support both) to handle permissions|
|`@NeedsPermission`|**✓**|Annotate a method which performs the action that requires one or more permissions|
|`@OnShowRationale`||Annotate a method which explains why the permission/s is/are needed. It passes in a `PermissionRequest` object which can be used to continue or abort the current permission request upon user input|
|`@OnPermissionDenied`||Annotate a method which is invoked if the user doesn't grant the permissions|
|`@OnNeverAskAgain`||Annotate a method which is invoked if the user chose to have the device "never ask again" about a permission|

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
    void showRationaleForCamera(final PermissionRequest request) {
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

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void showNeverAskForCamera() {
        Toast.makeText(this, R.string.permission_camera_neverask, Toast.LENGTH_SHORT).show();
    }
}
```

### 2. Delegate to generated class

Upon compilation, PermissionsDispatcher generates a class for `MainActivityPermissionsDispatcher`([Activity Name] + PermissionsDispatcher), which you can use to safely access these permission-protected methods.

The only step you have to do is delegating the work to this helper class:

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    findViewById(R.id.button_camera).setOnClickListener(v -> {
      // NOTE: delegate the permission handling to generated method
      MainActivityPermissionsDispatcher.showCameraWithPermissionCheck(this);
    });
}

@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    // NOTE: delegate the permission handling to generated method
    MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
}
```

Check out the [sample](https://github.com/hotchemi/PermissionsDispatcher/tree/master/sample) for more details.

## Other features

- [Getting Special Permissions](https://github.com/hotchemi/PermissionsDispatcher/blob/master/doc/special_permissions.md)
- [maxSdkVersion](https://github.com/hotchemi/PermissionsDispatcher/blob/master/doc/maxsdkversion.md)
- Xiaomi
  - Since Xiaomi manipulates something around runtime permission mechanism Google's recommended way [doesn't work well](https://github.com/hotchemi/PermissionsDispatcher/issues/187). But don't worry, PermissionsDispatcher supports it! Check related [PR](https://github.com/hotchemi/PermissionsDispatcher/issues/187) for more detail.
- [IntelliJ plugin](https://github.com/shiraji/permissions-dispatcher-plugin)
- AndroidAnnotations plugin
  - If you use [AndroidAnnotations](http://androidannotations.org/), you need to add [AndroidAnnotationsPermissionsDispatcherPlugin](https://github.com/AleksanderMielczarek/AndroidAnnotationsPermissionsDispatcherPlugin).

### Known issues

If you're in trouble check known issues [list](https://github.com/hotchemi/PermissionsDispatcher/blob/master/doc/known_issues.md) before filing an issue.

### Users

Thankfully we've got hundreds of [users](https://github.com/hotchemi/PermissionsDispatcher/blob/master/doc/users.md) around the world!

## Download

To add PermissionsDispatcher to your project, include the following in your **app module** `build.gradle` file:

`${latest.version}` is [![Download](https://api.bintray.com/packages/hotchemi/maven/permissionsdispatcher/images/download.svg)](https://bintray.com/hotchemi/maven/permissionsdispatcher/_latestVersion)

```groovy
dependencies {
  compile("com.github.hotchemi:permissionsdispatcher:${latest.version}") {
      // if you don't use android.app.Fragment you can exclude support for them
      exclude module: "support-v13"
  }
  annotationProcessor "com.github.hotchemi:permissionsdispatcher-processor:${latest.version}"
}
```

Snapshots of the development version are available in [JFrog's snapshots repository](https://oss.jfrog.org/oss-snapshot-local/). 
Add the repo below to download `SNAPSHOT` releases.

```groovy
repositories {
  jcenter()
  maven { url 'http://oss.jfrog.org/artifactory/oss-snapshot-local/' }
}
```

### Misc

- If you include [Jitpack.io](https://jitpack.io/) dependencies in your project, it is important to review the order of the repositories available to your app module
  - Because of the library's artifact ID, Jitpack might be tempted to resolve the dependency on its own, which could lead to an error during Gradle's configuration time
- If you're going to bump up the major version number we recommend to refer to [migration guide](https://github.com/hotchemi/PermissionsDispatcher/blob/master/doc/migration_guide.md) 

## Licence

```
Copyright 2016 Shintaro Katafuchi, Marcel Schnelle, Yoshinori Isogai

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
