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
import org.jetbrains.uast.UBlockExpression;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.UExpression;
import org.jetbrains.uast.UMethod;
import org.jetbrains.uast.UQualifiedReferenceExpression;
import org.jetbrains.uast.visitor.AbstractUastVisitor;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class CallOnRequestPermissionsResultDetector extends Detector implements Detector.UastScanner {

    static final Issue ISSUE = Issue.create("NeedOnRequestPermissionsResult",
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

    @Override
    public List<Class<? extends UElement>> getApplicableUastTypes() {
        return Collections.<Class<? extends UElement>>singletonList(UClass.class);
    }

    @Override
    public UElementHandler createUastHandler(final JavaContext context) {
        return new UElementHandler() {
            @Override
            public void visitClass(UClass node) {
                node.accept(new OnRequestPermissionsResultChecker(context, node));
            }
        };
    }

    private static class OnRequestPermissionsResultChecker extends AbstractUastVisitor {

        private final JavaContext context;

        private final UClass sourceFile;

        private boolean hasRuntimePermissionAnnotation;

        private OnRequestPermissionsResultChecker(JavaContext context, UClass sourceFile) {
            this.context = context;
            this.sourceFile = sourceFile;
        }

        @Override
        public boolean visitAnnotation(UAnnotation node) {
            String type = node.getQualifiedName();
            if (!RUNTIME_PERMISSIONS_NAME.contains(type)) {
                return true;
            }
            hasRuntimePermissionAnnotation = true;
            return true;
        }

        @Override
        public boolean visitMethod(UMethod node) {
            if (!hasRuntimePermissionAnnotation) {
                return true;
            }
            if (!"onRequestPermissionsResult".equals(node.getName())) {
                return true;
            }
            if (hasRuntimePermissionAnnotation && !checkMethodCall(node, sourceFile)) {
                context.report(ISSUE, context.getLocation(node), "Generated onRequestPermissionsResult method not called");
            }
            return true;
        }

        private static boolean checkMethodCall(UMethod method, UClass klass) {
            UExpression methodBody = method.getUastBody();
            if (methodBody == null) {
                return false;
            }
            if (methodBody instanceof UBlockExpression) {
                UBlockExpression methodBodyExpression = (UBlockExpression) methodBody;
                List<UExpression> expressions = methodBodyExpression.getExpressions();
                for (UExpression expression : expressions) {
                    if (!(expression instanceof UQualifiedReferenceExpression)) {
                        continue;
                    }
                    UQualifiedReferenceExpression referenceExpression = (UQualifiedReferenceExpression) expression;
                    String targetClass = klass.getName() + "PermissionsDispatcher";
                    String targetMethod = "onRequestPermissionsResult(requestCode, permissions, grantResults)";
                    if (targetClass.equals(referenceExpression.getReceiver().toString())
                            && targetMethod.equals( referenceExpression.getSelector().toString())) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}