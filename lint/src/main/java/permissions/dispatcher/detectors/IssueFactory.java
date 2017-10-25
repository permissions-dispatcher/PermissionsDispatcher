package permissions.dispatcher.detectors;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;

import java.util.EnumSet;

final class IssueFactory {

    private IssueFactory() {
        throw new AssertionError();
    }

    static Issue createCallNeedsPermissionIssue(Class<? extends Detector> cls) {
        return Issue.create("CallNeedsPermission",
                "Call the corresponding \"WithPermissionCheck\" method of the generated PermissionsDispatcher class instead",
                "Directly invoking a method annotated with @NeedsPermission may lead to misleading behaviour on devices running Marshmallow and up. Therefore, it is advised to use the generated PermissionsDispatcher class instead, which provides a \"WithPermissionCheck\" method that safely handles runtime permissions.",
                Category.CORRECTNESS,
                7,
                Severity.ERROR,
                new Implementation(cls, EnumSet.of(Scope.ALL_JAVA_FILES)));
    }

    static Issue createCallOnRequestPermissionsResultIssue(Class<? extends Detector> cls) {
        return Issue.create("NeedOnRequestPermissionsResult",
                "Call the \"onRequestPermissionsResult\" method of the generated PermissionsDispatcher class in the respective method of your Activity or Fragment",
                "You are required to inform the generated PermissionsDispatcher class about the results of a permission request. In your class annotated with @RuntimePermissions, override the \"onRequestPermissionsResult\" method and call through to the generated PermissionsDispatcher method with the same name.",
                Category.CORRECTNESS,
                5,
                Severity.ERROR,
                new Implementation(cls, EnumSet.of(Scope.JAVA_FILE)));
    }

    static Issue createNoCorrespondingNeedsPermissionIssue(Class<? extends Detector> cls) {
        return Issue.create("NoCorrespondingNeedsPermission",
                "The method annotated with @OnShowRationale has no corresponding @NeedsPermission method, and will therefore be ignored by PermissionsDispatcher",
                "The @OnShowRationale method with a certain signature is internally connected to another method annotated with @NeedsPermission and the same annotation value. Please ensure that there is a @NeedsPermission method with matching annotation values for this method.",
                Category.CORRECTNESS,
                4,
                Severity.ERROR,
                new Implementation(cls, EnumSet.of(Scope.JAVA_FILE)));
    }
}
