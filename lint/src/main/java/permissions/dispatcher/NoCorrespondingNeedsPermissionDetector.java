package permissions.dispatcher;

import com.android.tools.lint.client.api.UElementHandler;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;

import org.jetbrains.uast.UAnnotation;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.UNamedExpression;
import org.jetbrains.uast.visitor.AbstractUastVisitor;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class NoCorrespondingNeedsPermissionDetector extends Detector implements Detector.UastScanner {

    static final Issue ISSUE = Issue.create("NoCorrespondingNeedsPermission",
            "The method annotated with @OnShowRationale has no corresponding @NeedsPermission method, and will therefore be ignored by PermissionsDispatcher",
            "The @OnShowRationale method with a certain signature is internally connected to another method annotated with @NeedsPermission and the same annotation value. Please ensure that there is a @NeedsPermission method with matching annotation values for this method.",
            Category.CORRECTNESS,
            4,
            Severity.ERROR,
            new Implementation(NoCorrespondingNeedsPermissionDetector.class,
                    EnumSet.of(Scope.JAVA_FILE)));

    @Override
    public List<Class<? extends UElement>> getApplicableUastTypes() {
        return Collections.<Class<? extends UElement>>singletonList(UClass.class);
    }

    @Override
    public UElementHandler createUastHandler(final JavaContext context) {
        return new UElementHandler() {
            @Override public void visitClass(UClass node) {
                node.accept(new AnnotationChecker(context));
            }
        };
    }

    private static class AnnotationChecker extends AbstractUastVisitor {

        private final JavaContext context;

        private final Set<UAnnotation> needsPermissionAnnotations;

        private final Set<UAnnotation> onShowRationaleAnnotations;

        private AnnotationChecker(JavaContext context) {
            this.context = context;
            needsPermissionAnnotations = new HashSet<UAnnotation>();
            onShowRationaleAnnotations = new HashSet<UAnnotation>();
        }

        @Override
        public boolean visitAnnotation(UAnnotation node) {
            if (!context.isEnabled(ISSUE)) {
                return true;
            }
            // Let's store NeedsPermission and OnShowRationale
            String type = node.getQualifiedName();
            if ("permissions.dispatcher.NeedsPermission".equals(type)) {
                needsPermissionAnnotations.add(node);
            } else if ("permissions.dispatcher.OnShowRationale".equals(type)) {
                onShowRationaleAnnotations.add(node);
            }
            if (onShowRationaleAnnotations.isEmpty()) {
                return true;
            }
            // If there is OnShowRationale, find corresponding NeedsPermission
            for (UAnnotation onShowRationaleAnnotation : onShowRationaleAnnotations) {
                boolean found = false;
                for (UAnnotation needsPermissionAnnotation : needsPermissionAnnotations) {
                    if (hasSameNodes(onShowRationaleAnnotation.getAttributeValues(), needsPermissionAnnotation.getAttributeValues())) {
                        found = true;
                    }
                }
                if (!found) {
                    context.report(ISSUE, context.getLocation(onShowRationaleAnnotation), "Useless @OnShowRationale declaration");
                }
            }
            return true;
        }

        private static boolean hasSameNodes(List<UNamedExpression> first, List<UNamedExpression> second) {
            if (first.size() != second.size()) {
                return false;
            }
            for (int i = 0, size = first.size(); i < size; i++) {
                if (!first.get(i).toString().equals(second.get(i).toString())) {
                    return false;
                }
            }
            return true;
        }
    }
}
