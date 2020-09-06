package permissions.dispatcher.ktx

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build

/**
 * An enum which represents location permission types.
 *
 * **See:** [Android Developers](https://developer.android.com/training/location/permissions)
 */
enum class LocationPermission(internal val permission: String, internal val apiLevel: Int) {
    FINE(Manifest.permission.ACCESS_FINE_LOCATION, 1),
    COARSE(Manifest.permission.ACCESS_COARSE_LOCATION, 1),

    @SuppressLint("InlinedApi")
    BACKGROUND(Manifest.permission.ACCESS_BACKGROUND_LOCATION, 29)
}

internal fun Array<out LocationPermission>.filterByApiLevel(sdkVer: Int = Build.VERSION.SDK_INT) =
    asSequence().filter { it.apiLevel <= sdkVer }.map { it.permission }.toList().toTypedArray()
