## Installation

NOTE: 4.x only supports [Jetpack](https://developer.android.com/jetpack/). If you still use appcompat 3.x is the way to go.

To add PermissionsDispatcher to your project, include the following in your **app module** `build.gradle` file:

`${latest.version}` is [![Download](https://api.bintray.com/packages/hotchemi/org.permissionsdispatcher/permissionsdispatcher/images/download.svg) ](https://bintray.com/hotchemi/org.permissionsdispatcher/permissionsdispatcher/_latestVersion)

```groovy
dependencies {
  implementation "org.permissionsdispatcher:permissionsdispatcher:${latest.version}"
  annotationProcessor "org.permissionsdispatcher:permissionsdispatcher-processor:${latest.version}"
}
```

With Kotlin:

```groovy
apply plugin: 'kotlin-kapt'

dependencies {
  implementation "org.permissionsdispatcher:permissionsdispatcher:${latest.version}"
  kapt "org.permissionsdispatcher:permissionsdispatcher-processor:${latest.version}"
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
