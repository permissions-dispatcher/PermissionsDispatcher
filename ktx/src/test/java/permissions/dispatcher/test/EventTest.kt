package permissions.dispatcher.test

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNull
import org.junit.Test
import permissions.dispatcher.ktx.Event

class EventTest {
    @Test
    fun `getContentIfNotHandled returns null from the second access`() {
        val testInstance = Event(1)
        assertEquals(1, testInstance.getContentIfNotHandled())
        assertNull(testInstance.getContentIfNotHandled())
        assertNull(testInstance.getContentIfNotHandled())
    }
}
