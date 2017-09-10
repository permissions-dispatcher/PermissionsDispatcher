package permissions.dispatcher;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public final class PermissionsDispatcherIssueRegistryTest {
    @Test
    public void issues() {
        assertThat(new PermissionsDispatcherIssueRegistry().getIssues()).containsAllOf(
                CallNeedsPermissionDetector.ISSUE,
                CallOnRequestPermissionsResultDetector.ISSUE,
                NoCorrespondingNeedsPermissionDetector.ISSUE);
    }
}
