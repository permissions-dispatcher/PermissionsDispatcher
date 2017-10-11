package permissions.dispatcher.detectors;

import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiReferenceExpression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PsiCallNeedsPermissionDetector extends Detector implements Detector.JavaPsiScanner {

    public static final Issue ISSUE =
            IssueFactory.createCallNeedsPermissionIssue(PsiCallNeedsPermissionDetector.class);

    static List<String> generatedClassNames = new ArrayList<String>();

    static List<String> methods = Collections.emptyList();

    @Override
    public List<Class<? extends PsiElement>> getApplicablePsiTypes() {
        return Collections.<Class<? extends PsiElement>>singletonList(PsiClass.class);
    }

    public PsiCallNeedsPermissionDetector() {
        // No-op
    }

    @Override
    public JavaElementVisitor createPsiVisitor(final JavaContext context) {
        if (context.getPhase() == 1) {
            // find out which class has RuntimePermissions
            return new AnnotationChecker(context);
        }
        return null;
    }

    @Override
    public List<String> getApplicableMethodNames() {
        return methods;
    }

    @Override
    public void visitMethod(JavaContext context, JavaElementVisitor visitor,
                            PsiMethodCallExpression node, PsiMethod method) {
        if (methods.contains(method.getName())) {
            context.report(ISSUE, node, context.getLocation(node),
                    "Trying to access permission-protected method directly");
        }
    }

    private static class AnnotationChecker extends JavaElementVisitor {

        private final JavaContext context;

        private final Set<String> matchingAnnotationTypeNames;

        private AnnotationChecker(JavaContext context) {
            this.context = context;

            matchingAnnotationTypeNames = new HashSet<String>();
            matchingAnnotationTypeNames.add("RuntimePermissions");
            matchingAnnotationTypeNames.add("permissions.dispatcher.RuntimePermissions");
        }

        @Override
        public void visitReferenceExpression(PsiReferenceExpression expression) {
            skipGeneratedFiles(context);
            super.visitReferenceExpression(expression);
        }

        @Override
        public void visitAnnotation(PsiAnnotation annotation) {
            if (!context.isEnabled(ISSUE)) {
                super.visitAnnotation(annotation);
                return;
            }

            String type = annotation.getQualifiedName();
            if (!matchingAnnotationTypeNames.contains(type)) {
                super.visitAnnotation(annotation);
                return;
            }

            PsiClass[] classes = context.getJavaFile().getClasses();
            if (classes.length > 0 && classes[0].getName() != null) {
                generatedClassNames.add(classes[0].getName() + "PermissionsDispatcher");
                // let's check method call!
                context.requestRepeat(new PsiCallNeedsPermissionDetector(),
                        EnumSet.of(Scope.ALL_JAVA_FILES));
            }
            super.visitAnnotation(annotation);
        }

        private static void skipGeneratedFiles(JavaContext context) {
            PsiClass[] classes = context.getJavaFile().getClasses();
            if (classes.length > 0 && classes[0].getName() != null) {
                String qualifiedName = classes[0].getName();
                if (qualifiedName.contains("PermissionsDispatcher")) {
                    // skip generated files
                    return;
                }
            }
        }
    }
}
