package permissions.dispatcher

import org.junit.Test

import com.google.common.truth.Truth.assertThat

class PermissionsDispatcherIssueRegistryTest {
    @Test
    fun issues() {
        assertThat(PermissionsDispatcherIssueRegistry().issues).containsAllOf(
                CallNeedsPermissionDetector.ISSUE,
                CallOnRequestPermissionsResultDetector.ISSUE,
                NoCorrespondingNeedsPermissionDetector.ISSUE)
    }
}
