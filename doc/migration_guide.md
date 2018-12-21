# Migration guide

- [Migrating to 4.x](#migrating-to-4x)
- [Migrating to 3.x](#migrating-to-3x)
- [Migrating to 2.x](#migrating-to-2x)

## Migrating to 4.x

### Change Maven groupId 

Issue: [#560](https://github.com/permissions-dispatcher/PermissionsDispatcher/issues/560)

```diff
dependencies {
-  implementation "com.github.hotchemi:permissionsdispatcher:${latest.version}"
+  implementation "org.permissionsdispatcher:permissionsdispatcher:${latest.version}"
-  annotationProcessor "com.github.hotchemi:permissionsdispatcher-processor:${latest.version}"
+  annotationProcessor "org.permissionsdispatcher:permissionsdispatcher-processor:${latest.version}"
}
```

### Migrate to AndroidX from AppCompat

Issue: [#488](https://github.com/permissions-dispatcher/PermissionsDispatcher/pull/488)

From 4.x we only support [Jetpack](https://developer.android.com/jetpack/).
Be sure you've gotten AndroidX migration done before upgrading the library version to 4.x.

## Migrating to 3.x

### Method name changing

Issue: [#355](https://github.com/permissions-dispatcher/PermissionsDispatcher/issues/355)

PermissionsDispatcher used to generate `***WithCheck` methods.
Starting with 3.0, the suffix of these methods becomes `***WithPermissionCheck`.

```diff
- MainActivityPermissionsDispatcher.showCameraWithCheck(this);
+ MainActivityPermissionsDispatcher.showCameraWithPermissionCheck(this);
```

The motivation of this change is to make generated code more declarative and easy to figure out what'd be going on under the hood.

This change is especially beneficial in Kotlin, because the receiver of the generated method is a class annotated with `@RuntimePermissions`, instead of a helper class named `***PermissionsDispatcher`.

### Kotlin support

Issue: [#320](https://github.com/permissions-dispatcher/PermissionsDispatcher/issues/320)

Actually it's been possible to use PermissionsDispatcher with Kotlin already, because of its interoperability with Java. But to give you a more concise and idiomatic experience, we added full Kotlin support which is described in [here](kotlin_support.md).

If you're already using PermissionsDispatcher with Kotlin, be aware of the following 2 changes:

#### `***WithPermissionCheck`

```diff
button.setOnClickListener {
-   MainActivityPermissionsDispatcher.showCameraWithCheck(this)
+   showCameraWithPermissionCheck()
}
```

#### `onRequestPermissionsResult`

```diff
override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
-   MainActivityPermissionsDispatcher.onRequestPermissionsResult(requestCode, grantResults)
+   onRequestPermissionsResult(requestCode, grantResults)
}
```

Those methods are defined as extension functions so now you don't have to call `***MainActivityPermissionsDispatcher` class!

## Migrating to 2.x

Since the internals of PermissionsDispatcher 2 have undergone a fundamental refactoring, most notably in the switch of languages to Kotlin for our annotation processor, the exposed APIs to users of the library have been tweaked as well. This guide will help you migrate to the latest version in just a few minutes!

### Update the artifacts

First of all, make sure you're pulling in the correct version of the library:

#### Before
```groovy
dependencies {
    compile "com.github.hotchemi:permissionsdispatcher:1.2.1"
    apt "com.github.hotchemi:permissionsdispatcher-processor:1.2.1"
}
```

#### After
```groovy
dependencies {
    compile "com.github.hotchemi:permissionsdispatcher:2.0.0"
    apt "com.github.hotchemi:permissionsdispatcher-processor:2.0.0"
}
```

*Make sure to change both the library and processor artifacts!*

### @RuntimePermissions

Your `Activity` or `Fragment` classes utilizing PermissionsDispatcher still need to be annotated with `@RuntimePermissions`, so nothing has changed there.

### @NeedsPermission

While version 1.x of the library had two flavors of the `@NeedsPermission` annotation (both the singular and plural form `@NeedsPermissions`), from now on there is only one. Make sure to change your previous usage of `@NeedsPermissions` to the unified one:

#### Before
```java
@NeedsPermissions({ CAMERA, WRITE_EXTERNAL_STORAGE })
void setupCamera() {
}
```

#### After
```java
@NeedsPermission({ CAMERA, WRITE_EXTERNAL_STORAGE })
void setupCamera() {
}
```

### @OnShowRationale

Both `@ShowsRationale` and its plural form `@ShowsRationales` have been removed in version 2. They have been unified in the new annotation `@OnShowRationale`, which is used to display a reasoning to your users about why you need to request permissions for specific actions. The signature of methods annotated with this new annotation require a single parameter of type `PermissionRequest`: When PermissionsDispatcher calls this method on your class, the `PermissionRequest` will provide an interface for you to either `cancel()` or `proceed()` the ongoing runtime permission process. This allows you to defer showing a system dialog, for instance if you would like to display your own explanation in a dialog beforehand. And don't worry: If you forget to follow this signature pattern, PermissionsDispatcher will remind you at compile time! This is one of the most exciting new features of PermissionsDispatcher 2:

#### Before
```java
@ShowsRationale({ CAMERA, WRITE_EXTERNAL_STORAGE })
void showCameraRationale() {
    // Can't really do much here, since the system dialog is shown immediately afterwards...
    Toast.makeText(...).show();
}
```

#### After
```java
@OnShowRationale({ CAMERA, WRITE_EXTERNAL_STORAGE })
void showCameraRationale(final PermissionRequest request) {
    // E.g. show a dialog explaining why you need the permission.
    // Call proceed() or cancel() on the incoming request to continue or abort the current permissions process
    new AlertDialog.Builder(...)
        .setPositiveButton("OK", (dialog, which) -> request.proceed())
        .setNegativeButton("Abort", (dialog, which) -> request.cancel())
        .show();
}
```

### @OnPermissionDenied

The old annotations `@DeniedPermission` and `@DeniedPermissions` have been unified into the new annotation `@OnPermissionDenied`. A method annotated with this annotation will be invoked if the user declines an ongoing permission request. For developers, it's as simple as switching the annotation's name:

#### Before
```java
@DeniedPermission({ CAMERA, WRITE_EXTERNAL_STORAGE })
void cameraDenied() {
}
```

#### After
```java
@OnPermissionDenied({ CAMERA, WRITE_EXTERNAL_STORAGE })
void cameraDenied() {
}
```

### That's it!

You're good to go and have successfully migrated to PermissionsDispatcher 2.x! We are striving to continue improving this library, so that the hassle of runtime permission handling becomes as convenient as possible for you.
