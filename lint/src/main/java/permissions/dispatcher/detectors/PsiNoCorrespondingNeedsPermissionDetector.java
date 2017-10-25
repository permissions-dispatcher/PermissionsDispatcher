package permissions.dispatcher.detectors;

import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PsiNoCorrespondingNeedsPermissionDetector extends Detector
        implements Detector.JavaPsiScanner {

    public static final Issue ISSUE =
            IssueFactory.createNoCorrespondingNeedsPermissionIssue(
                    PsiNoCorrespondingNeedsPermissionDetector.class);

    static final Set<String> NEEDS_PERMISSION_NAME = new HashSet<String>() {{
        add("NeedsPermission");
        add("permissions.dispatcher.NeedsPermission");
    }};

    static final Set<String> ON_SHOW_RATIONALE_NAME = new HashSet<String>() {{
        add("OnShowRationale");
        add("permissions.dispatcher.OnShowRationale");
    }};

    public PsiNoCorrespondingNeedsPermissionDetector() {
        // No-op
    }

    @Override
    public JavaElementVisitor createPsiVisitor(JavaContext context) {
        return new AnnotationChecker(context);
    }

    @Override
    public List<Class<? extends PsiElement>> getApplicablePsiTypes() {
        return Collections.<Class<? extends PsiElement>>singletonList(PsiAnnotation.class);
    }

    static class AnnotationChecker extends JavaElementVisitor {

        private final JavaContext context;

        private final Set<PsiAnnotation> needsPermissionAnnotations;

        private final Set<PsiAnnotation> onShowRationaleAnnotations;

        private AnnotationChecker(JavaContext context) {
            this.context = context;

            needsPermissionAnnotations = new HashSet<PsiAnnotation>();
            onShowRationaleAnnotations = new HashSet<PsiAnnotation>();
        }

        @Override
        public void visitAnnotation(PsiAnnotation annotation) {
            if (!context.isEnabled(ISSUE)) {
                super.visitAnnotation(annotation);
                return;
            }

            // Let's store NeedsPermission and OnShowRationale
            String type = annotation.getQualifiedName();
            if (NEEDS_PERMISSION_NAME.contains(type)) {
                needsPermissionAnnotations.add(annotation);
            } else if (ON_SHOW_RATIONALE_NAME.contains(type)) {
                onShowRationaleAnnotations.add(annotation);
            }

            if (onShowRationaleAnnotations.size() <= 0) {
                super.visitAnnotation(annotation);
                return;
            }
            // If there is OnShowRationale, find corresponding NeedsPermission
            boolean found = false;
            for (PsiAnnotation onShowRationaleAnnotation : onShowRationaleAnnotations) {
                for (PsiAnnotation needsPermissionAnnotation : needsPermissionAnnotations) {
                    if (hasSameNodes(onShowRationaleAnnotation.getReferences(),
                            needsPermissionAnnotation.getReferences())) {
                        found = true;
                    }
                }

                if (!found) {
                    context.report(ISSUE, context.getLocation(onShowRationaleAnnotation),
                            "Useless @OnShowRationale declaration");
                }
            }
            super.visitAnnotation(annotation);
        }

        private boolean hasSameNodes(PsiReference[] first, PsiReference[] second) {
            if (first.length != second.length) {
                return false;
            }

            for (int i = 0, size = first.length; i < size; i++) {
                if (!first[i].toString().equals(second[i].toString())) {
                    return false;
                }
            }
            return true;
        }
    }
}
