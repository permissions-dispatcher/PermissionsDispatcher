package permissions.dispatcher;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NoCorrespondingNeedsPermissionDetector extends Detector
        implements Detector.JavaPsiScanner {

    public static final Issue ISSUE = Issue.create("NoCorrespondingNeedsPermission",
            "The method annotated with @OnShowRationale has no corresponding @NeedsPermission method, and will therefore be ignored by PermissionsDispatcher",
            "The @OnShowRationale method with a certain signature is internally connected to another method annotated with @NeedsPermission and the same annotation value. Please ensure that there is a @NeedsPermission method with matching annotation values for this method.",
            Category.CORRECTNESS,
            4,
            Severity.ERROR,
            new Implementation(NoCorrespondingNeedsPermissionDetector.class,
                    EnumSet.of(Scope.JAVA_FILE)));

    static final Set<String> NEEDS_PERMISSION_NAME = new HashSet<String>() {{
        add("NeedsPermission");
        add("permissions.dispatcher.NeedsPermission");
    }};

    static final Set<String> ON_SHOW_RATIONALE_NAME = new HashSet<String>() {{
        add("OnShowRationale");
        add("permissions.dispatcher.OnShowRationale");
    }};

    public NoCorrespondingNeedsPermissionDetector() {
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
