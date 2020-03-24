package permissions.dispatcher.test

import android.content.pm.ActivityInfo
import androidx.fragment.app.FragmentActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.annotation.Config
import permissions.dispatcher.ktx.PermissionsRequestFragment

// TODO: cover more scenarios
@RunWith(AndroidJUnit4::class)
class PermissionRequestFragmentTest {
    private lateinit var fragment: PermissionsRequestFragment

    @Before
    fun setUp() {
        fragment = PermissionsRequestFragment.newInstance()
        Robolectric.setupActivity(FragmentActivity::class.java)
            .supportFragmentManager.beginTransaction()
            .add(fragment, PermissionsRequestFragment.tag)
            .commitAllowingStateLoss()
    }

    @Test
    fun `fragment does not have view and retainInstance == true`() {
        assertNull(fragment.view)
        assertTrue(fragment.retainInstance)
    }

    @Test
    fun `requestedOrientation is portrait`() {
        assertEquals(
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT,
            fragment.requireActivity().requestedOrientation
        )

        fragment.onDestroy()

        assertEquals(
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED,
            fragment.requireActivity().requestedOrientation
        )
    }

    @Test
    @Config(qualifiers = "land")
    fun `requestedOrientation is landscape`() {
        assertEquals(
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE,
            fragment.requireActivity().requestedOrientation
        )

        fragment.onDestroy()

        assertEquals(
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED,
            fragment.requireActivity().requestedOrientation
        )
    }
}