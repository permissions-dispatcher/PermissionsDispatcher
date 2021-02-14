## permissionsdispatcher-ktx

`permissionsdispatcher-ktx` aims to let developers cope with runtime permissions handling in a declarative style without using annotation processing([kapt](https://kotlinlang.org/docs/reference/kapt.html)).

Let's see a minimum example, in which you register a `MainActivity` which requires `Manifest.permission.CAMERA`.

### 0. Declare a permission in AndroidManifest.xml

Add the following line to `AndroidManifest.xml`:
 
`<uses-permission android:name="android.permission.CAMERA" />`

### 1. Define a requester with `constructPermissionsRequest`

The library provides `constructPermissionsRequest` which you can construct a requester object with the given several callback functions to be called in an appropriate situation.
 
```kotlin
/**
 * @param permissions the permissions [requiresPermission] requires.
 * @param onShowRationale the method explains why the permissions are required.
 * @param onPermissionDenied the method invoked if the user doesn't grant the permissions.
 * @param onNeverAskAgain the method invoked if the user does not deny the permissions with
 * "never ask again" option.
 * @param requiresPermission the action requires [permissions].
 */
fun FragmentActivity/*(or Fragment)*/.constructPermissionsRequest(
    vararg permissions: String,
    onShowRationale: ShowRationaleFunc? = null,
    onPermissionDenied: Func? = null,
    onNeverAskAgain: Func? = null,
    requiresPermission: Func): PermissionsRequester
```

Here you just define `showCamera` and basically that's it! With the library you don't need to manually override `onRequestPermissionsResult`.

NOTE: Be sure to construct a requester every time an activity is created to capture the callbacks appropriately.

```kotlin
class MainActivity: AppCompatActivity {
    // constructPermissionsRequest must be invoked every time an activity is created 
    private val showCamera = constructPermissionsRequest(Manifest.permission.CAMERA,
        onShowRationale = ::onCameraShowRationale,
        onPermissionDenied = ::onCameraDenied,
        onNeverAskAgain = ::onCameraNeverAskAgain) {
		    // do something here
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById(R.id.button_camera).setOnClickListener {
            showCamera.launch()
        }
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

#### Location Permissions

Since the location permissions have been one of the most sensitive permission group to deal with, we provide a dedicated method `constructLocationPermissionRequest`.
With the method you don't have to think of which API version you can ask [ACCESS_BACKGROUND_LOCATION](https://developer.android.com/about/versions/10/privacy/changes#app-access-device-location)(see the [issue](https://github.com/permissions-dispatcher/PermissionsDispatcher/issues/646) for more detail).

```kotlin
/**
 * @param onShowRationale the method explains why the permissions are required.
 * @param onPermissionDenied the method invoked if the user doesn't grant the permissions.
 * @param requiresPermission the action requires [permissions].
 */
fun FragmentActivity/*(or Fragment)*/.constructLocationPermissionRequest(
    vararg permissions: LocationPermission,
    onShowRationale: ShowRationaleFun? = null,
    onPermissionDenied: Fun? = null,
    onNeverAskAgain: Fun? = null,
    requiresPermission: Fun
): PermissionsRequester
```
 
#### Special Permissions

The library also provides `constructWriteSettingsPermissionRequest` and
 `constructSystemAlertWindowPermissionRequest` to support `WRITE_SETTINGS` and `SYSTEM_ALERT_WINDOW` that
 requires exceptional handling.

```kotlin
/**
 * @param onShowRationale the method explains why the permissions are required.
 * @param onPermissionDenied the method invoked if the user doesn't grant the permissions.
 * @param requiresPermission the action requires [permissions].
 */
fun FragmentActivity/*(or Fragment)*/.constructWriteSettingsPermissionRequest(
    onShowRationale: ShowRationaleFunc? = null,
    onPermissionDenied: Func? = null,
    requiresPermission: Func): PermissionsRequester

 /**
 * @param onShowRationale the method explains why the permissions are required.
 * @param onPermissionDenied the method invoked if the user doesn't grant the permissions.
 * @param requiresPermission the action requires [permissions].
 */
fun FragmentActivity/*(or Fragment)*/.constructSystemAlertWindowPermissionRequest(
    onShowRationale: ShowRationaleFunc? = null,
    onPermissionDenied: Func? = null,
    requiresPermission: Func): PermissionsRequester
```

## Installation

`${latest.version}` is [![Download](https://maven-badges.herokuapp.com/maven-central/com.github.permissions-dispatcher/permissionsdispatcher-ktx/badge.svg)](https://search.maven.org/search?q=a:permissionsdispatcher-ktx)

```groovy
dependencies {
  implementation "com.github.permissions-dispatcher:permissionsdispatcher-ktx:${latest.version}"
}
```
