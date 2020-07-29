package permissions.dispatcher.test

import org.junit.Assert.*
import org.junit.Test
import permissions.dispatcher.ktx.LocationPermission
import permissions.dispatcher.ktx.filterByApiLevel

class LocationPermissionTest {
    @Test
    fun `BACKGRGROUND should be excluded when sdkVer is less than 29`() {
        val permissions = LocationPermission.values()
            .filterByApiLevel(sdkVer = LocationPermission.BACKGROUND.apiLevel - 1)
        assertEquals(2, permissions.size)
        assertFalse(permissions.contains(LocationPermission.BACKGROUND.permission))
    }

    @Test
    fun `BACKGRGROUND should be included when sdkVer equals to 29`() {
        val permissions = LocationPermission.values()
            .filterByApiLevel(sdkVer = LocationPermission.BACKGROUND.apiLevel)
        assertEquals(3, permissions.size)
        assertTrue(permissions.contains(LocationPermission.BACKGROUND.permission))
    }

    @Test
    fun `BACKGRGROUND should be included when sdkVer is more than 29`() {
        val permissions = LocationPermission.values()
            .filterByApiLevel(sdkVer = LocationPermission.BACKGROUND.apiLevel + 1)
        assertEquals(3, permissions.size)
        assertTrue(permissions.contains(LocationPermission.BACKGROUND.permission))
    }
}
