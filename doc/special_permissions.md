## Special Permissions

PermissionsDispatcher takes care of special permissions `Manifest.permission.SYSTEM_ALERT_WINDOW` and `Manifest.permission.WRITE_SETTINGS`.

The following sample is to grant `SYSTEM_ALERT_WINDOW`.

### 0. Prepare AndroidManifest

Add the following line to `AndroidManifest.xml`:
 
`<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />`

### 1. Attach annotations

It's the same as other permissions:

```java
@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    @NeedsPermission(Manifest.permission.SYSTEM_ALERT_WINDOW)
    void systemAlertWindow() {
    }

    @OnShowRationale(Manifest.permission.SYSTEM_ALERT_WINDOW)
    void systemAlertWindowOnShowRationale(final PermissionRequest request) {
    }

    @OnPermissionDenied(Manifest.permission.SYSTEM_ALERT_WINDOW)
    void systemAlertWindowOnPermissionDenied() {
    }

    @OnNeverAskAgain(Manifest.permission.SYSTEM_ALERT_WINDOW)
    void systemAlertWindowOnNeverAskAgain() {
    }
}
```

### 2. Delegate to generated class

Unlike other permissions, special permissions require to call the delegation method at `onActivityResult`:

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    findViewById(R.id.button_system_alert_window).setOnClickListener(v -> {
      // NOTE: delegate the permission handling to generated method
      MainActivityPermissionsDispatcher.systemAlertWindowWithPermissionCheck(this);
    });
}

@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    MainActivityPermissionsDispatcher.onActivityResult(this, requestCode);
}
```
