## Migrating to PermissionsDispatcher 2.x

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
	// ...
}
```

#### After
```java
@NeedsPermission({ CAMERA, WRITE_EXTERNAL_STORAGE })
void setupCamera() {
	// ...
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
	// ...
}
```

#### After
```java
@OnPermissionDenied({ CAMERA, WRITE_EXTERNAL_STORAGE })
void cameraDenied() {
	// ...
}
```

### That's it!

You're good to go and have successfully migrated to PermissionsDispatcher 2.x! We are striving to continue improving this library, so that the hassle of runtime permission handling becomes as convenient as possible for you.