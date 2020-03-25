## permissionsdispatcher-ktx

**Note: the status of the module is now alpha and we're looking forward to your feedback!**

permissionsdispatcher-ktx aims to let developers cope with runtime permissions handling in declarative way without using annotation processing([kapt](https://kotlinlang.org/docs/reference/kapt.html)).

Let's see a minimum example, in which you register a `MainActivity` which requires `Manifest.permission.CAMERA`.

### 0. Declare a permission in AndroidManifest.xml

Add the following line to `AndroidManifest.xml`:
 
`<uses-permission android:name="android.permission.CAMERA" />`

### 1. Define a method with `withPermissionsCheck`

The library provides `withPermissionsCheck`, which you can delegate exact runtime
 permission handling and register several callback methods to be called in an appropriate situation.
 
```kotlin
/**
 * @param permissions the permissions [requiresPermission] requires.
 * @param onShowRationale the method explains why the permissions are required.
 * @param onPermissionDenied the method invoked if the user doesn't grant the permissions.
 * @param onNeverAskAgain the method invoked if the user does not deny the permissions with
 * "never ask again" option.
 * @param requiresPermission the action requires [permissions].
 */
fun FragmentActivity/*(or Fragment)*/.withPermissionsCheck(
    vararg permissions: String,
    onShowRationale: ShowRationaleFunc? = null,
    onPermissionDenied: Func? = null,
    onNeverAskAgain: Func? = null,
    requiresPermission: Func)
```

Here you just define `showCamera` with the provided method and that's it! Don't have to take care
 about `onRequestPermissionsResult` and so on.

```kotlin
class MainActivity: AppCompatActivity {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById(R.id.button_camera).setOnClickListener {
            showCamera()
        }
    }

    private fun showCamera() = withPermissionsCheck(Manifest.permission.CAMERA,
        onShowRationale = ::onCameraShowRationale,
        onPermissionDenied = ::onCameraDenied,
        onNeverAskAgain = ::onCameraNeverAskAgain) {
        // do something here
    }

    private fun onCameraDenied() {
        Toast.makeText(requireContext(), R.string.permission_camera_denied, Toast.LENGTH_SHORT).show()
    }

    private fun onCameraShowRationale(request: PermissionRequest) {
        request.proceed()
    }

    private fun onCameraNeverAskAgain() {
        Toast.makeText(requireContext(), R.string.permission_camera_never_ask_again, Toast.LENGTH_SHORT).show()
    }
}
```

Check out the [sample](https://github.com/hotchemi/PermissionsDispatcher/tree/master/ktx-sample) for more details.
 
#### Special Permissions

The library also provides `withWriteSettingsPermissionCheck` and
 `withSystemAlertWindowPermissionCheck` to support `WRITE_SETTINGS` and `SYSTEM_ALERT_WINDOW` that
 requires exceptional handling.

```kotlin
/**
 * Wraps [requiresPermission] in the dedicated runtime permission check for
 * [Manifest.permission.WRITE_SETTINGS] with the given arguments.
 *
 * @param onShowRationale the method explains why the permissions are required.
 * @param onPermissionDenied the method invoked if the user doesn't grant the permissions.
 * @param requiresPermission the action requires [permissions].
 */
fun FragmentActivity/*(or Fragment)*/.withWriteSettingsPermissionCheck(
    onShowRationale: ShowRationaleFunc? = null,
    onPermissionDenied: Func? = null,
    requiresPermission: Func)

/**
 * Wraps [requiresPermission] in the dedicated runtime permission check for
 * [Manifest.permission.SYSTEM_ALERT_WINDOW] with the given arguments.
 *
 * @param onShowRationale the method explains why the permissions are required.
 * @param onPermissionDenied the method invoked if the user doesn't grant the permissions.
 * @param requiresPermission the action requires [permissions].
 */
fun FragmentActivity/*(or Fragment)*/.withSystemAlertWindowPermissionCheck(
    onShowRationale: ShowRationaleFunc? = null,
    onPermissionDenied: Func? = null,
    requiresPermission: Func)
```

## Installation

`${latest.version}` is [![Download](https://api.bintray.com/packages/hotchemi/org.permissionsdispatcher/permissionsdispatcher-ktx/images/download.svg) ](https://bintray.com/hotchemi/org.permissionsdispatcher/permissionsdispatcher-ktx/_latestVersion)

```groovy
dependencies {
  implementation "org.permissionsdispatcher:permissionsdispatcher-ktx:${latest.version}"
}
```
