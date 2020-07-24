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
    FINE(Manifest.permission.ACCESS_FINE_LOCATION, Build.VERSION_CODES.BASE),
    COARSE(Manifest.permission.ACCESS_COARSE_LOCATION, Build.VERSION_CODES.BASE),

    @SuppressLint("InlinedApi")
    BACKGROUND(Manifest.permission.ACCESS_BACKGROUND_LOCATION, Build.VERSION_CODES.Q)
}

internal fun Array<out LocationPermission>.filterByApiLevel(sdkVer: Int = Build.VERSION.SDK_INT) =
    filter { it.apiLevel <= sdkVer }.map { it.permission }.toTypedArray()
