package permissions.dispatcher;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.JavaRecursiveElementVisitor;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiCallExpression;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiCodeBlock;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiExpressionStatement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiStatement;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CallOnRequestPermissionsResultDetector extends Detector
        implements Detector.JavaPsiScanner {

    public static final Issue ISSUE = Issue.create("NeedOnRequestPermissionsResult",
            "Call the \"onRequestPermissionsResult\" method of the generated PermissionsDispatcher class in the respective method of your Activity or Fragment",
            "You are required to inform the generated PermissionsDispatcher class about the results of a permission request. In your class annotated with @RuntimePermissions, override the \"onRequestPermissionsResult\" method and call through to the generated PermissionsDispatcher method with the same name.",
            Category.CORRECTNESS,
            5,
            Severity.ERROR,
            new Implementation(CallOnRequestPermissionsResultDetector.class,
                    EnumSet.of(Scope.JAVA_FILE)));

    static final Set<String> RUNTIME_PERMISSIONS_NAME = new HashSet<String>() {{
        add("RuntimePermissions");
        add("permissions.dispatcher.RuntimePermissions");
    }};

    public CallOnRequestPermissionsResultDetector() {
        // No-op
    }

    @Override
    public List<Class<? extends PsiElement>> getApplicablePsiTypes() {
        return Arrays.asList(PsiAnnotation.class, PsiClass.class);
    }

    @Override
    public JavaElementVisitor createPsiVisitor(final JavaContext context) {
        return new JavaElementVisitor() {
            @Override
            public void visitClass(PsiClass node) {
                node.accept(new OnRequestPermissionsResultChecker(context, node));
            }
        };
    }

    private static class OnRequestPermissionsResultChecker extends JavaRecursiveElementVisitor {

        private final JavaContext context;

        private boolean hasRuntimePermissionAnnotation;

        private PsiClass psiClass;

        private OnRequestPermissionsResultChecker(JavaContext context, PsiClass psiClass) {
            this.context = context;
            this.psiClass = psiClass;
        }

        @Override
        public void visitAnnotation(PsiAnnotation annotation) {
            String type = annotation.getQualifiedName();
            if (!RUNTIME_PERMISSIONS_NAME.contains(type)) {
                super.visitAnnotation(annotation);
                return;
            }

            hasRuntimePermissionAnnotation = true;
            super.visitAnnotation(annotation);
        }

        @Override
        public void visitMethod(PsiMethod method) {
            if (!hasRuntimePermissionAnnotation) {
                super.visitMethod(method);
                return;
            }

            if (!"onRequestPermissionsResult".equals(method.getName())) {
                super.visitMethod(method);
                return;
            }

            if (hasRuntimePermissionAnnotation && !checkMethodCall(method, psiClass)) {
                PsiCodeBlock codeBlock = method.getBody();
                context.report(ISSUE, context.getLocation(method),
                        codeBlock.getText()
                                + "Generated onRequestPermissionsResult method not called");
            }

            super.visitMethod(method);

        }
    }

    private static boolean checkMethodCall(PsiMethod method, PsiClass psiClass) {
        PsiCodeBlock codeBlock = method.getBody();
        if (codeBlock == null) {
            return false;
        }

        PsiStatement[] statements = codeBlock.getStatements();
        for (PsiStatement statement : statements) {
            if (!(statement instanceof PsiExpressionStatement)) {
                continue;
            }
            PsiExpression expression = ((PsiExpressionStatement) statement)
                    .getExpression();
            if (!(expression instanceof PsiCallExpression)) {
                continue;
            }

            PsiCallExpression callExpression = (PsiCallExpression) expression;
            String targetClassName = psiClass.getName() + "PermissionsDispatcher";
            PsiMethod resolveMethod = callExpression.resolveMethod();
            if (resolveMethod == null) {
                continue;
            }

            PsiClass containingClass = resolveMethod.getContainingClass();
            if (containingClass == null) {
                continue;
            }

            if (targetClassName.equals(containingClass.getName())
                    && "onRequestPermissionsResult".equals(resolveMethod
                            .getName())) {
                return true;
            }
        }
        return false;
    }

}
