## Installation

NOTE: 4.x only supports [Jetpack](https://developer.android.com/jetpack/). If you still use appcompat 3.x is the way to go.

To add PermissionsDispatcher to your project, include the following in your **app module** `build.gradle` file:

`${latest.version}` is [![Download](https://api.bintray.com/packages/hotchemi/maven/permissionsdispatcher/images/download.svg?version=4.1.0) ](https://bintray.com/hotchemi/maven/permissionsdispatcher/4.1.0/link)

```groovy
dependencies {
  implementation "com.github.hotchemi:permissionsdispatcher:${latest.version}"
  annotationProcessor "com.github.hotchemi:permissionsdispatcher-processor:${latest.version}"
}
```

With Kotlin:

```groovy
apply plugin: 'kotlin-kapt'

dependencies {
  implementation "com.github.hotchemi:permissionsdispatcher:${latest.version}"
  kapt "com.github.hotchemi:permissionsdispatcher-processor:${latest.version}"
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
