package permissions.dispatcher;

import org.junit.Test;
import permissions.dispatcher.uast.UastCallNeedsPermissionDetector;
import permissions.dispatcher.uast.UastCallOnRequestPermissionsResultDetector;
import permissions.dispatcher.uast.UastNoCorrespondingNeedsPermissionDetector;

import static com.google.common.truth.Truth.assertThat;

public final class PermissionsDispatcherIssueRegistryTest {
    @Test
    public void issues() {
        assertThat(new PermissionsDispatcherIssueRegistry().getIssues()).containsAllOf(
                UastCallNeedsPermissionDetector.ISSUE,
                UastCallOnRequestPermissionsResultDetector.ISSUE,
                UastNoCorrespondingNeedsPermissionDetector.ISSUE);
    }
}
