package permissions.dispatcher;

import com.android.tools.lint.detector.api.Issue;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public final class PermissionsDispatcherIssueRegistryTest {

    private static final PermissionsDispatcherIssueRegistry.Provider UNAVAILABLE_PROVIDER =
            new PermissionsDispatcherIssueRegistry.Provider() {
                @Override
                public boolean isAvailable() {
                    return false;
                }

                @Override
                public List<Issue> getIssues() {
                    throw new RuntimeException("didn't expect to be called");
                }
            };

    private static final PermissionsDispatcherIssueRegistry.Provider THROW_EXCEPTION_PROVIDER =
            new PermissionsDispatcherIssueRegistry.Provider() {
                @Override
                public boolean isAvailable() {
                    throw new RuntimeException("didn't expect to be called");
                }

                @Override
                public List<Issue> getIssues() {
                    throw new RuntimeException("didn't expect to be called");
                }
            };

    @Test
    public void preferUastByDefaultIfAvailable() {
        PermissionsDispatcherIssueRegistry registry = new PermissionsDispatcherIssueRegistry();

        assertThat(registry.getIssues()).containsAllIn(
                PermissionsDispatcherIssueRegistry.UAST.getIssues());
    }

    @Test
    public void fallbackIfFirstProviderNotAvailable() {
        PermissionsDispatcherIssueRegistry registry = new PermissionsDispatcherIssueRegistry(
                Arrays.asList(
                        UNAVAILABLE_PROVIDER,
                        PermissionsDispatcherIssueRegistry.PSI));

        assertThat(registry.getIssues()).containsAllIn(
                PermissionsDispatcherIssueRegistry.PSI.getIssues());
    }

    @Test
    public void dontProceedToSecondProviderIfFirstIsAvailable() {

        PermissionsDispatcherIssueRegistry registry = new PermissionsDispatcherIssueRegistry(
                Arrays.asList(
                        PermissionsDispatcherIssueRegistry.PSI,
                        THROW_EXCEPTION_PROVIDER));

        assertThat(registry.getIssues()).containsAllIn(
                PermissionsDispatcherIssueRegistry.PSI.getIssues());
    }

    @Test
    public void emptyIssueListIfNoProviderIsAvailableUnexpectedly() {
        PermissionsDispatcherIssueRegistry registry =
                new PermissionsDispatcherIssueRegistry(
                        Collections.singletonList(UNAVAILABLE_PROVIDER));

        assertThat(registry.getIssues()).isEmpty();
    }
}
