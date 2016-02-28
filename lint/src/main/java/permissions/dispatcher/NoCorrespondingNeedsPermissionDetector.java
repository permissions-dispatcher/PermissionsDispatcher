package permissions.dispatcher;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.ast.Annotation;
import lombok.ast.AstVisitor;
import lombok.ast.CompilationUnit;
import lombok.ast.ForwardingAstVisitor;
import lombok.ast.Node;

public class NoCorrespondingNeedsPermissionDetector extends Detector implements Detector.JavaScanner {

    public static final Issue ISSUE = Issue.create("NoCorrespondingNeedsPermission",
            "The method annotated with @OnShowRationale has no corresponding @NeedsPermission method, and will therefore be ignored by PermissionsDispatcher",
            "The @OnShowRationale method with a certain signature is internally connected to another method annotated with @NeedsPermission and the same annotation value. Please ensure that there is a @NeedsPermission method with matching annotation values for this method.",
            Category.CORRECTNESS,
            4,
            Severity.ERROR,
            new Implementation(NoCorrespondingNeedsPermissionDetector.class, EnumSet.of(Scope.JAVA_FILE)));

    static final Set<String> NEEDS_PERMISSION_NAME = new HashSet<String>() {{
        add("NeedsPermission");
        add("permissions.dispatcher.NeedsPermission");
    }};

    static final Set<String> ON_SHOW_RATIONALE_NAME = new HashSet<String>() {{
        add("OnShowRationale");
        add("permissions.dispatcher.OnShowRationale");
    }};

    @Override
    public AstVisitor createJavaVisitor(JavaContext context) {
        return new AnnotationChecker(context);
    }

    static class AnnotationChecker extends ForwardingAstVisitor {
        private final JavaContext context;
        private Set<Annotation> needsPermissionAnnotations;
        private Set<Annotation> onShowRationaleAnnotations;

        private AnnotationChecker(JavaContext context) {
            this.context = context;

            needsPermissionAnnotations = new HashSet<>();
            onShowRationaleAnnotations = new HashSet<>();
        }

        @Override
        public boolean visitAnnotation(Annotation node) {
            if (!context.isEnabled(ISSUE)) {
                return super.visitAnnotation(node);
            }

            // Let's store NeedsPermission and OnShowRationale
            String type = node.astAnnotationTypeReference().getTypeName();
            if (NEEDS_PERMISSION_NAME.contains(type)) {
                needsPermissionAnnotations.add(node);
            } else if (ON_SHOW_RATIONALE_NAME.contains(type)) {
                onShowRationaleAnnotations.add(node);
            }
            return super.visitAnnotation(node);
        }

        @Override
        public void afterVisitCompilationUnit(CompilationUnit node) {
            if (onShowRationaleAnnotations.size() <= 0) {
                super.afterVisitCompilationUnit(node);
                return;
            }

            // If there is OnShowRationale, find corresponding NeedsPermission
            boolean found = false;
            for (Annotation onShowRationaleAnnotation : onShowRationaleAnnotations) {
                for (Annotation needsPermissionAnnotation : needsPermissionAnnotations) {
                    if (hasSameNodes(onShowRationaleAnnotation.getValueValues(), needsPermissionAnnotation.getValueValues())) {
                        found = true;
                    }
                }

                if (!found) {
                    context.report(ISSUE, context.getLocation(onShowRationaleAnnotation), "Useless @OnShowRationale declaration");
                }
            }

            super.afterVisitCompilationUnit(node);
        }

        private boolean hasSameNodes(List<Node> first, List<Node> second) {
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
