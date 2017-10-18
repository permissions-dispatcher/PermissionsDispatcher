package permissions.dispatcher.detectors;

import com.android.tools.lint.client.api.UElementHandler;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;

import org.jetbrains.uast.UAnnotation;
import org.jetbrains.uast.UBlockExpression;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.UExpression;
import org.jetbrains.uast.UMethod;
import org.jetbrains.uast.UQualifiedReferenceExpression;
import org.jetbrains.uast.visitor.AbstractUastVisitor;

import java.util.Collections;
import java.util.List;

public final class UastCallOnRequestPermissionsResultDetector extends Detector implements Detector.UastScanner {

    public static final Issue ISSUE =
            IssueFactory.createCallOnRequestPermissionsResultIssue(
                    UastCallOnRequestPermissionsResultDetector.class);

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

        private final String className;

        private boolean hasRuntimePermissionAnnotation;

        private OnRequestPermissionsResultChecker(JavaContext context, UClass klass) {
            this.context = context;
            this.className = klass.getName();
        }

        @Override
        public boolean visitAnnotation(UAnnotation node) {
            if (!"permissions.dispatcher.RuntimePermissions".equals(node.getQualifiedName())) {
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
            if (hasRuntimePermissionAnnotation && !isGeneratedMethodCalled(node, className)) {
                context.report(ISSUE, context.getLocation(node), "Generated onRequestPermissionsResult method not called");
            }
            return true;
        }

        private static boolean isGeneratedMethodCalled(UMethod method, String className) {
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
                    String receiverName = referenceExpression.getReceiver().toString();
                    if ("super".equals(receiverName)) {
                        // skip super method call
                        continue;
                    }
                    String targetClassName = className + "PermissionsDispatcher";
                    if (targetClassName.equals(receiverName) && "onRequestPermissionsResult".equals(referenceExpression.getResolvedName())) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
