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
    }
}
```
