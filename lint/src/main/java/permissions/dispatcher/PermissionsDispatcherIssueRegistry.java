package permissions.dispatcher;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Issue;

import java.util.Arrays;
import java.util.List;
import permissions.dispatcher.uast.UastCallNeedsPermissionDetector;
import permissions.dispatcher.uast.UastCallOnRequestPermissionsResultDetector;
import permissions.dispatcher.uast.UastNoCorrespondingNeedsPermissionDetector;

@SuppressWarnings("unused")
public final class PermissionsDispatcherIssueRegistry extends IssueRegistry {
    @Override
    public List<Issue> getIssues() {
        return Arrays.asList(
                UastCallNeedsPermissionDetector.ISSUE,
                UastCallOnRequestPermissionsResultDetector.ISSUE,
                UastNoCorrespondingNeedsPermissionDetector.ISSUE);
    }
}
