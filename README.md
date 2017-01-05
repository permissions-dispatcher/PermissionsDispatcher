# PermissionsDispatcher

[![Build Status](https://travis-ci.org/hotchemi/PermissionsDispatcher.svg?branch=master)](https://travis-ci.org/hotchemi/PermissionsDispatcher)

![image](https://raw.githubusercontent.com/hotchemi/PermissionsDispatcher/master/art/logo.png)

PermissionsDispatcher provides a simple annotation-based API to handle runtime permissions in Android Marshmallow.

[Runtime permissions](https://developer.android.com/preview/features/runtime-permissions.html) are great for users, but can be tedious to implement correctly for developers, requiring a lot of boilerplate code.

This library lifts the burden that comes with writing a bunch of check statements whether a permission has been granted or not from you, in order to keep your code clean and safe.

**The library is 100% reflection-free.**

## Usage

Here's a minimum example, in which we register a `MainActivity` which requires `Manifest.permission.CAMERA`.

### 1. Attach annotations

PermissionsDispatcher introduces only a few annotations, keeping its general API concise:

> NOTE: Annotated methods must not be `private`.

|Annotation|Required|Description|
|---|---|---|
|`@RuntimePermissions`|**✓**|Register an `Activity` or `Fragment` to handle permissions|
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
      MainActivityPermissionsDispatcher.showCameraWithCheck(this);
    });
    findViewById(R.id.button_contacts).setOnClickListener(v -> {
      // NOTE: delegate the permission handling to generated method
      MainActivityPermissionsDispatcher.showContactsWithCheck(this);
    });
}

@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    // NOTE: delegate the permission handling to generated method
    MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
}
```

Check out the [sample](https://github.com/hotchemi/PermissionsDispatcher/tree/master/sample) and [generated class](https://github.com/hotchemi/PermissionsDispatcher/blob/master/art/MainActivityPermissionsDispatcher.java) for more details.

## Getting Special Permissions

PermissionsDispatcher takes care of special permissions `Manifest.permission.SYSTEM_ALERT_WINDOW` and `Manifest.permission.WRITE_SETTINGS`.

The following sample is to grant `SYSTEM_ALERT_WINDOW`.

### 0. Prepare AndroidManifest

Add following line to `AndroidManifest.xml`
 
`<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />`

### 1. Attach annotations

It's the same as other permissions

```java
@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    @NeedsPermission(Manifest.permission.SYSTEM_ALERT_WINDOW)
    void systemAlertWindow() {
        // ...
    }

    @OnShowRationale(Manifest.permission.SYSTEM_ALERT_WINDOW)
    void systemAlertWindowOnShowRationale(final PermissionRequest request) {
        // ...
    }

    @OnPermissionDenied(Manifest.permission.SYSTEM_ALERT_WINDOW)
    void systemAlertWindowOnPermissionDenied() {
        // ...
    }

    @OnNeverAskAgain(Manifest.permission.SYSTEM_ALERT_WINDOW)
    void systemAlertWindowOnNeverAskAgain() {
        // ...
    }
}
```

### 2. Delegate to generated class

Unlike other permissions, special permissions require to call the delegation method at `onActivityResult`

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    findViewById(R.id.button_system_alert_window).setOnClickListener(v -> {
      // NOTE: delegate the permission handling to generated method
      MainActivityPermissionsDispatcher.systemAlertWindowWithCheck(this);
    });
}

@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    MainActivityPermissionsDispatcher.onActivityResult(this, requestCode);
}
```

That's it!

## maxSdkVersion

[\<uses-permission\>](https://developer.android.com/guide/topics/manifest/uses-permission-element.html) has an attribute call `maxSdkVersion`. PermissionsDispatcher support the feature as well.

The following sample is for declaring `Manifest.permisison.WRITE_EXTERNAL_STORAGE` up to API level 18.

### 0. AndroidManifest

Declare the permission with `maxSdkVersion` attribute

```xml
<uses-permission
     android:name="android.permission.WRITE_EXTERNAL_STORAGE"
     android:maxSdkVersion="18" />
```

### 1. Attach annotations with `maxSdkVersion`

```java
@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    @NeedsPermission(value = Manifest.permission.WRITE_EXTERNAL_STORAGE, maxSdkVersion = 18)
    void getStorage() {
        // ...
    }
    
}
```

The rest is the same.

## Note

- PermissionsDispatcher depends on the `support-v4` library by default, in order to be able to use some permission compat classes.
- You can use this library with JDK 1.6 or up, but we test library's behaviour on the JDK 1.8 because it has been becoming the default of Android development.

### Fragment Support

PermissionsDispatcher is supported on **API levels 4 and up**, with which you get support for annotating `android.app.Activity` and `android.support.v4.app.Fragment` sub-classes out of the box.

In case you rely on `android.app.Fragment` in your app, you can use these with PermissionsDispatcher as well!

Simply add a dependency on the `support-v13` library alongside PermissionsDispatcher in your project, and it will enable support for native fragments.

### For AndroidAnnotations users

If you use [AndroidAnnotations](http://androidannotations.org/), you need to add [AndroidAnnotationsPermissionsDispatcherPlugin](https://github.com/AleksanderMielczarek/AndroidAnnotationsPermissionsDispatcherPlugin) to your dependencies so PermissionsDispatcher's looks for AA's subclasses (your project won't compile otherwise).

### For 1.x users

- [Migrating to 2.x](https://github.com/hotchemi/PermissionsDispatcher/wiki/Migrating-to-2.x)

## ProGuard

PermissionsDispatcher bundles ProGuard rules in its aar. No extra settings are required.

## Download


### For Android Gradle Plugin >= 2.2 users

To add it to your project, include the following in your **app module** `build.gradle` file:

`${latest.version}` is [![Download](https://api.bintray.com/packages/hotchemi/maven/permissionsdispatcher/images/download.svg)](https://bintray.com/hotchemi/maven/permissionsdispatcher/_latestVersion)

```groovy
dependencies {
  compile 'com.github.hotchemi:permissionsdispatcher:${latest.version}'
  annotationProcessor 'com.github.hotchemi:permissionsdispatcher-processor:${latest.version}'
}
```

### For Android Gradle Plugin < 2.2 users

To add it to your project, include the following in your **project** `build.gradle` file:

```groovy
buildscript {
  dependencies {
    classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
  }
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

And on your **app module** `build.gradle`:

`${latest.version}` is [![Download](https://api.bintray.com/packages/hotchemi/maven/permissionsdispatcher/images/download.svg)](https://bintray.com/hotchemi/maven/permissionsdispatcher/_latestVersion)

```groovy
apply plugin: 'android-apt'

dependencies {
  compile 'com.github.hotchemi:permissionsdispatcher:${latest.version}'
  apt 'com.github.hotchemi:permissionsdispatcher-processor:${latest.version}'
}
```

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
