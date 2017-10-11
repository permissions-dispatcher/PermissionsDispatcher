package permissions.dispatcher;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Issue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import permissions.dispatcher.detectors.PsiCallNeedsPermissionDetector;
import permissions.dispatcher.detectors.PsiCallOnRequestPermissionsResultDetector;
import permissions.dispatcher.detectors.PsiNoCorrespondingNeedsPermissionDetector;
import permissions.dispatcher.detectors.UastCallNeedsPermissionDetector;
import permissions.dispatcher.detectors.UastCallOnRequestPermissionsResultDetector;
import permissions.dispatcher.detectors.UastNoCorrespondingNeedsPermissionDetector;

public final class PermissionsDispatcherIssueRegistry extends IssueRegistry {

    static final Provider UAST = new UastImplementation();
    static final Provider PSI = new PsiImplementation();

    // Providers are asked to provide Detectors in the order that they appear in this list
    private static final List<Provider> DEFAULT_PROVIDERS = Arrays.asList(UAST, PSI);

    private final List<Provider> providers;

    public PermissionsDispatcherIssueRegistry() {
        this(DEFAULT_PROVIDERS);
    }

    PermissionsDispatcherIssueRegistry(List<Provider> providers) {
        this.providers = providers;
    }

    @Override
    public List<Issue> getIssues() {
        for (Provider provider : providers) {
            if (provider.isAvailable()) {
                log("Running Lint checks with %s", provider.getClass().getName());
                return provider.getIssues();
            }
        }

        log("Warning: No suitable Lint Provider found! Checks disabled.");
        return Collections.emptyList();
    }

    private void log(String message, Object... args) {
        String logStatement = String.format(message, args);
        System.out.println(String.format("[PermissionsDispatcher Lint] %s", logStatement));
    }

    public interface Provider {
        boolean isAvailable();

        List<Issue> getIssues();
    }

    static final class UastImplementation implements Provider {

        private final boolean uastOnClasspath;

        UastImplementation() {
            boolean available;
            try {
                Class.forName("com.android.tools.lint.detector.api.Detector$UastScanner");
                available = true;
            } catch (Throwable ignored) {
                available = false;
            }

            this.uastOnClasspath = available;
        }

        @Override
        public boolean isAvailable() {
            return uastOnClasspath;
        }

        @Override
        public List<Issue> getIssues() {
            return Arrays.asList(
                    UastCallNeedsPermissionDetector.ISSUE,
                    UastCallOnRequestPermissionsResultDetector.ISSUE,
                    UastNoCorrespondingNeedsPermissionDetector.ISSUE);
        }
    }

    static final class PsiImplementation implements Provider {

        @Override
        public boolean isAvailable() {
            return true;
        }

        @Override
        public List<Issue> getIssues() {
            return Arrays.asList(
                    PsiCallNeedsPermissionDetector.ISSUE,
                    PsiCallOnRequestPermissionsResultDetector.ISSUE,
                    PsiNoCorrespondingNeedsPermissionDetector.ISSUE);
        }
    }
}
