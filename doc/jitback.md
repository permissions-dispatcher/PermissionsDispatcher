### For Jitpack users

If you include [Jitpack.io](https://jitpack.io/) dependencies in your project, it is important to review the order of the repositories available to your app module. Because of the library's artifact ID, Jitpack might be tempted to resolve the dependency on its own, which could lead to an error during Gradle's configuration time: