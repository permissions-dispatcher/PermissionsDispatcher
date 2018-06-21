package permissions.dispatcher;

import com.android.tools.lint.client.api.UElementHandler;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.intellij.psi.PsiType;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.uast.UCallExpression;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.UExpression;
import org.jetbrains.uast.UIdentifier;
import org.jetbrains.uast.UMethod;
import org.jetbrains.uast.UastVisibility;
import org.jetbrains.uast.visitor.AbstractUastVisitor;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class NoDelegateOnResumeDetector extends Detector implements Detector.UastScanner {
    static final Issue ISSUE = Issue.create("NoDelegateOnResumeDetector",
            "Asking permissions at `onResume()` will cause an infinite loop.",
            "Asking permissions in `onResume()` will cause an infinite loop when user selects \"Never ask again\" and denies the permission, since `onResume()` is called again after returning from a permission request check. This problem can be solved by moving this method call to `onStart()`.",
            Category.CORRECTNESS,
            4,
            Severity.ERROR,
            new Implementation(NoDelegateOnResumeDetector.class,
                    EnumSet.of(Scope.JAVA_FILE)));

    @Override
    public List<Class<? extends UElement>> getApplicableUastTypes() {
        return Collections.<Class<? extends UElement>>singletonList(UClass.class);
    }

    @Override
    public UElementHandler createUastHandler(final JavaContext context) {
        return new UElementHandler() {
            @Override
            public void visitClass(UClass node) {
                if (node.findAnnotation("permissions.dispatcher.RuntimePermissions") != null) {
                    node.accept(new Checker(context));
                }
            }
        };
    }

    private class Checker extends AbstractUastVisitor {
        private final JavaContext context;
        private String needPermissionMethodName = null;

        private Checker(JavaContext context) {
            this.context = context;
        }

        @Override
        public boolean visitMethod(UMethod node) {
            super.visitMethod(node);
            if (node.findAnnotation("permissions.dispatcher.NeedsPermission") != null) {
                needPermissionMethodName = node.getName();
            }
            return true;
        }

        @Override
        public void afterVisitClass(UClass node) {
            super.afterVisitClass(node);
            if (needPermissionMethodName != null) {
                node.accept(new OnResumeChecker(context, node, needPermissionMethodName));
            }
        }
    }

    private class OnResumeChecker extends AbstractUastVisitor {
        private final JavaContext context;
        private boolean isKotlin;
        private UClass uClass;
        private String needPermissionMethodName;

        private OnResumeChecker(JavaContext context, UClass uClass, String needPermissionMethodName) {
            this.context = context;
            this.uClass = uClass;
            this.needPermissionMethodName = needPermissionMethodName;
            isKotlin = context.getPsiFile() != null && "kotlin".equals(context.getPsiFile().getLanguage().getID());
        }

        /**
         * @return return true if visiting end (if lint does not visit inside the method), false otherwise
         */
        @Override
        public boolean visitMethod(UMethod node) {
            super.visitMethod(node);
            return !"onResume".equalsIgnoreCase(node.getName())
                    || node.getReturnType() != PsiType.VOID
                    || node.getUastParameters().size() != 0
                    || !isPublicOrProtected(node);
        }

        private boolean isPublicOrProtected(UMethod node) {
            UastVisibility visibility = node.getVisibility();
            return visibility == UastVisibility.PUBLIC || visibility == UastVisibility.PROTECTED;
        }

        @Override
        public boolean visitCallExpression(UCallExpression node) {
            super.visitCallExpression(node);
            UIdentifier methodIdentifier = node.getMethodIdentifier();
            if (methodIdentifier != null && isWithPermissionCheckCalled(node, methodIdentifier)) {
                context.report(ISSUE, context.getLocation(node), "Asking permission inside onResume()");
            }
            return true;
        }

        private boolean isWithPermissionCheckCalled(@NotNull UCallExpression node, @NotNull UIdentifier methodIdentifier) {
            if (!(needPermissionMethodName + "WithPermissionCheck").equalsIgnoreCase(methodIdentifier.getName())) {
                return false;
            }

            UExpression receiver = node.getReceiver();
            if (isKotlin) {
                return receiver == null;
            } else {
                String qualifiedName = uClass.getQualifiedName();
                if (node.getValueArgumentCount() < 1) return false;
                PsiType expressionType = node.getValueArguments().get(0).getExpressionType();
                return receiver != null
                        && qualifiedName != null
                        && expressionType != null
                        && qualifiedName.equalsIgnoreCase(expressionType.getCanonicalText())
                        && (uClass.getName() + "PermissionsDispatcher").equalsIgnoreCase(receiver.asSourceString());
            }
        }
    }
}
