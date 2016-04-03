package permissions.dispatcher;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Issue;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class PermissionsDispatcherIssueRegistry extends IssueRegistry {
    @Override
    public List<Issue> getIssues() {
        return Arrays.asList(CallNeedsPermissionDetector.ISSUE,
                NoCorrespondingNeedsPermissionDetector.ISSUE,
                CallOnRequestPermissionsResultDetector.ISSUE);
    }
}
