## Usage with Kotlin

Here's a minimum example, in which you register a `MainActivity` which requires `Manifest.permission.CAMERA`.

### 0. Preparation

Add the following line to `AndroidManifest.xml`:
 
`<uses-permission android:name="android.permission.CAMERA" />`

### 1. Attach annotations

> NOTE: Annotated methods must not be `private`.

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
    fun showRationaleForCamera() {
        AlertDialog.Builder(this)
            .setMessage(R.string.permission_camera_rationale)
            .setPositiveButton(R.string.button_allow, {dialog, button -> /* TODO */ })
            .setNegativeButton(R.string.button_deny, {dialog, button -> /* TODO */ })
            .show()
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
        // delegate the permission handling to generated function
        showCameraWithPermissionCheck()
    }
}

@OnShowRationale(Manifest.permission.CAMERA)
fun showRationaleForCamera() {
    AlertDialog.Builder(this)
       .setMessage(R.string.permission_camera_rationale)
       .setPositiveButton(R.string.button_allow, {dialog, button -> proceedShowCameraPermissionRequest() })
       .setNegativeButton(R.string.button_deny, {dialog, button -> cancelShowCameraPermissionRequest() })
       .show()
}

override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    // delegate the permission handling to generated function
    onRequestPermissionsResult(requestCode, grantResults)
}
```

Check out the [sample](https://github.com/hotchemi/PermissionsDispatcher/tree/master/sample-kotlin) for more details.