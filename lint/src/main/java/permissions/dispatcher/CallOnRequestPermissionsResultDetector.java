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
import java.util.Set;

import lombok.ast.Annotation;
import lombok.ast.AstVisitor;
import lombok.ast.ClassDeclaration;
import lombok.ast.ForwardingAstVisitor;
import lombok.ast.MethodDeclaration;
import lombok.ast.MethodInvocation;
import lombok.ast.VariableReference;

public class CallOnRequestPermissionsResultDetector extends Detector implements Detector.ClassScanner {
    public static final Issue ISSUE = Issue.create("NeedOnRequestPermissionsResult",
            "Call the \"onRequestPermissionsResult\" method of the generated PermissionsDispatcher class in the respective method of your Activity or Fragment",
            "You are required to inform the generated PermissionsDispatcher class about the results of a permission request. In your class annotated with @RuntimePermissions, override the \"onRequestPermissionsResult\" method and call through to the generated PermissionsDispatcher method with the same name.",
            Category.CORRECTNESS,
            5,
            Severity.ERROR,
            new Implementation(CallOnRequestPermissionsResultDetector.class, EnumSet.of(Scope.CLASS_FILE)));

    static final Set<String> RUNTIME_PERMISSIONS_NAME = new HashSet<String>() {{
        add("RuntimePermissions");
        add("permissions.dispatcher.RuntimePermissions");
    }};

    @Override
    public AstVisitor createJavaVisitor(JavaContext context) {
        return new OnRequestPermissionsResultChecker(context);
    }

    private static class OnRequestPermissionsResultChecker extends ForwardingAstVisitor {
        private final JavaContext context;
        private boolean hasRuntimePermissionAnnotation;
        private boolean hasOnRequestPermissionResultCall;
        private String generatedClassName;
        private ClassDeclaration classDeclaration;

        private OnRequestPermissionsResultChecker(JavaContext context) {
            this.context = context;
        }

        @Override
        public boolean visitClassDeclaration(ClassDeclaration node) {
            if (!context.isEnabled(ISSUE)) {
                // stop executing lint for this class
                return true;
            }

            classDeclaration = node;
            generatedClassName = node.astName() + "PermissionsDispatcher";
            return super.visitClassDeclaration(node);
        }

        @Override
        public boolean visitAnnotation(Annotation node) {
            String type = node.astAnnotationTypeReference().getTypeName();
            if (!RUNTIME_PERMISSIONS_NAME.contains(type)) {
                return super.visitAnnotation(node);
            }

            hasRuntimePermissionAnnotation = true;
            return super.visitAnnotation(node);
        }

        @Override
        public boolean visitMethodDeclaration(MethodDeclaration node) {
            if (hasRuntimePermissionAnnotation && "public void onRequestPermissionsResult(int, java.lang.String[], int[])".equals(context.resolve(node).getSignature().trim())) {
                return super.visitMethodDeclaration(node);
            } else {
                // ignore this node
                return true;
            }
        }

        @Override
        public boolean visitMethodInvocation(MethodInvocation node) {
            if (!hasRuntimePermissionAnnotation || hasOnRequestPermissionResultCall) {
                return super.visitMethodInvocation(node);
            }

            if (!"onRequestPermissionsResult".equals(node.astName().astValue())) {
                return super.visitMethodInvocation(node);
            }

            if (node.astOperand() instanceof VariableReference) {
                VariableReference ref = (VariableReference) node.astOperand();
                if (generatedClassName.equals(ref.astIdentifier().astValue())) {
                    hasOnRequestPermissionResultCall = true;
                }
            }

            return super.visitMethodInvocation(node);
        }

        @Override
        public void afterVisitClassDeclaration(ClassDeclaration node) {
            if (hasRuntimePermissionAnnotation && !hasOnRequestPermissionResultCall) {
                context.report(ISSUE, context.getLocation(classDeclaration), "Generated onRequestPermissionsResult method not called");
            }
            super.afterVisitClassDeclaration(node);
        }
    }

}
