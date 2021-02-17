# PermissionsDispatcher ![CI for pull request](https://github.com/permissions-dispatcher/PermissionsDispatcher/workflows/CI%20for%20pull%20request/badge.svg?branch=master)

- **Fully Kotlin/Java support**
- [**Special permissions support**](https://github.com/hotchemi/PermissionsDispatcher/blob/master/doc/special_permissions.md)
- **100% reflection-free**

PermissionsDispatcher provides a simple annotation-based API to handle runtime permissions.

This library lifts the burden that comes with writing a bunch of check statements whether a permission has been granted or not from you, in order to keep your code clean and safe.

## Usage

- Kotlin: You can pick either of [ktx](https://github.com/permissions-dispatcher/PermissionsDispatcher/tree/master/ktx) or [kapt](https://github.com/permissions-dispatcher/PermissionsDispatcher#0-prepare-androidmanifest).
- Java: [apt](https://github.com/hotchemi/PermissionsDispatcher/blob/master/doc/java_usage.md)

Here's a minimum example, in which you register a `MainActivity` which requires `Manifest.permission.CAMERA`.

### 0. Prepare AndroidManifest

Add the following line to `AndroidManifest.xml`:
 
`<uses-permission android:name="android.permission.CAMERA" />`

### 1. Attach annotations

PermissionsDispatcher introduces only a few annotations, keeping its general API concise:

> NOTE: Annotated methods must not be `private`.

|Annotation|Required|Description|
|---|---|---|
|`@RuntimePermissions`|**✓**|Register an `Activity` or `Fragment` to handle permissions|
|`@NeedsPermission`|**✓**|Annotate a method which performs the action that requires one or more permissions|
|`@OnShowRationale`||Annotate a method which explains why the permissions are needed. It passes in a `PermissionRequest` object which can be used to continue or abort the current permission request upon user input. If you don't specify any argument for the method compiler will generate `process${NeedsPermissionMethodName}ProcessRequest` and `cancel${NeedsPermissionMethodName}ProcessRequest`. You can use those methods in place of `PermissionRequest`(ex: with `DialogFragment`)|
|`@OnPermissionDenied`||Annotate a method which is invoked if the user doesn't grant the permissions|
|`@OnNeverAskAgain`||Annotate a method which is invoked if the user chose to have the device "never ask again" about a permission|

```kotlin
@RuntimePermissions
class MainActivity : AppCompatActivity(), View.OnClickListener {

    @NeedsPermission(Manifest.permission.CAMERA)
    fun showCamera() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.sample_content_fragment, CameraPreviewFragment.newInstance())
                .addToBackStack("camera")
                .commitAllowingStateLoss()
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    fun showRationaleForCamera(request: PermissionRequest) {
        showRationaleDialog(R.string.permission_camera_rationale, request)
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    fun onCameraDenied() {
        Toast.makeText(this, R.string.permission_camera_denied, Toast.LENGTH_SHORT).show()
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    fun onCameraNeverAskAgain() {
        Toast.makeText(this, R.string.permission_camera_never_askagain, Toast.LENGTH_SHORT).show()
    }
}
```

### 2. Delegate to generated functions

Now generated functions become much more concise and intuitive than Java version!

```kotlin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById(R.id.button_camera).setOnClickListener {
            // NOTE: delegate the permission handling to generated function
            showCameraWithPermissionCheck()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // NOTE: delegate the permission handling to generated function
        onRequestPermissionsResult(requestCode, grantResults)
    }
```

Check out the [sample](https://github.com/hotchemi/PermissionsDispatcher/tree/master/sample) for more details.

## Other features/plugins

- [Getting Special Permissions](https://github.com/hotchemi/PermissionsDispatcher/blob/master/doc/special_permissions.md)
- [maxSdkVersion](https://github.com/hotchemi/PermissionsDispatcher/blob/master/doc/maxsdkversion.md)
- [IntelliJ plugin](https://github.com/shiraji/permissions-dispatcher-plugin)
- [AndroidAnnotations plugin](https://github.com/AleksanderMielczarek/AndroidAnnotationsPermissionsDispatcherPlugin)

## Installation

**NOTE:**
  - If you're using jCenter we've moved on to MavenCentral, see [migration guide](https://github.com/hotchemi/PermissionsDispatcher/blob/master/doc/migration_guide.md)
  - 4.x only supports [Jetpack](https://developer.android.com/jetpack/). If you still use appcompat 3.x is the way to go.

To add PermissionsDispatcher to your project, include the following in your **app module** `build.gradle` file:

`${latest.version}` is [![Download](https://maven-badges.herokuapp.com/maven-central/com.github.permissions-dispatcher/permissionsdispatcher/badge.svg)](https://search.maven.org/search?q=g:com.github.permissions-dispatcher)

```groovy
dependencies {
  implementation "com.github.permissions-dispatcher:permissionsdispatcher:${latest.version}"
  annotationProcessor "com.github.permissions-dispatcher:permissionsdispatcher-processor:${latest.version}"
}
```

With Kotlin:

```groovy
apply plugin: 'kotlin-kapt'

dependencies {
  implementation "com.github.permissions-dispatcher:permissionsdispatcher:${latest.version}"
  kapt "com.github.permissions-dispatcher:permissionsdispatcher-processor:${latest.version}"
}
```

## License

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
