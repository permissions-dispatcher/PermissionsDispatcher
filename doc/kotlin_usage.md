## Usage with Kotlin

Here's a minimum example, in which you register a `MainActivity` which requires `Manifest.permission.CAMERA`.

### 0. Prepare AndroidManifest

Add the following line to `AndroidManifest.xml`:
 
`<uses-permission android:name="android.permission.CAMERA" />`

### 1. Attach annotations

PermissionsDispatcher introduces only a few annotations, keeping its general API concise:

> NOTE: Annotated methods must not be `private`.

|Annotation|Required|Description|
|---|---|---|
|`@RuntimePermissions`|**✓**|Register an `Activity`, `Fragment` or [Controller](https://github.com/bluelinelabs/Conductor) to handle permissions|
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

Check out the [sample](https://github.com/hotchemi/PermissionsDispatcher/tree/master/sample-kotlin) for more details.