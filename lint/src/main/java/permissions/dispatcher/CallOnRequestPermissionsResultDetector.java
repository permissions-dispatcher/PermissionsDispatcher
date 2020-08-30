package permissions.dispatcher;

import androidx.annotation.Nullable;

import com.android.tools.lint.client.api.UElementHandler;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.intellij.psi.PsiElement;

import org.jetbrains.uast.UAnnotation;
import org.jetbrains.uast.UBlockExpression;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.UExpression;
import org.jetbrains.uast.UMethod;
import org.jetbrains.uast.UQualifiedReferenceExpression;
import org.jetbrains.uast.kotlin.KotlinUFunctionCallExpression;
import org.jetbrains.uast.kotlin.KotlinUImplicitReturnExpression;
import org.jetbrains.uast.kotlin.KotlinUQualifiedReferenceExpression;
import org.jetbrains.uast.visitor.AbstractUastVisitor;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public final class CallOnRequestPermissionsResultDetector extends Detector implements Detector.UastScanner {

    static final Issue ISSUE = Issue.create("NeedOnRequestPermissionsResult",
            "Call the \"onRequestPermissionsResult\" method of the generated PermissionsDispatcher class in the respective method of your Activity or Fragment",
            "You are required to inform the generated PermissionsDispatcher class about the results of a permission request. In your class annotated with @RuntimePermissions, override the \"onRequestPermissionsResult\" method and call through to the generated PermissionsDispatcher method with the same name.",
            Category.CORRECTNESS,
            5,
            Severity.ERROR,
            new Implementation(CallOnRequestPermissionsResultDetector.class,
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
                node.accept(new OnRequestPermissionsResultChecker(context, node));
            }
        };
    }

    private static class OnRequestPermissionsResultChecker extends AbstractUastVisitor {

        private final JavaContext context;

        private final String className;

        private final boolean isKotlin;

        private boolean hasRuntimePermissionAnnotation;


        private OnRequestPermissionsResultChecker(JavaContext context, UClass klass) {
            this.context = context;
            this.className = klass.getName();
            isKotlin = context.getPsiFile() != null && "kotlin".equals(context.getPsiFile().getLanguage().getID());
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
            if (hasRuntimePermissionAnnotation && !isGeneratedMethodCalled(node, className, isKotlin)) {
                context.report(ISSUE, context.getLocation(node), "Generated onRequestPermissionsResult method not called");
            }
            return true;
        }

        private static boolean assertMethodName(@Nullable String name) {
            return "onRequestPermissionsResult".equals(name);
        }

        private static boolean isGeneratedMethodCalled(UMethod method, String className, boolean isKotlin) {
            UExpression methodBody = method.getUastBody();
            if (methodBody == null) {
                return false;
            }
            if (methodBody instanceof UBlockExpression) {
                UBlockExpression methodBodyExpression = (UBlockExpression) methodBody;
                List<UExpression> expressions = methodBodyExpression.getExpressions();
                for (UExpression expression : expressions) {
                    if (isKotlin && expression instanceof KotlinUFunctionCallExpression) {
                        KotlinUFunctionCallExpression functionalExpression = (KotlinUFunctionCallExpression) expression;
                        return assertMethodName(functionalExpression.getMethodName());
                    } else if (isKotlin && expression instanceof KotlinUImplicitReturnExpression) {
                        KotlinUImplicitReturnExpression returnExpression =
                                (KotlinUImplicitReturnExpression) expression;
                        UExpression uExpression = returnExpression.returnExpression;
                        if (uExpression instanceof KotlinUFunctionCallExpression) {
                            KotlinUFunctionCallExpression uFunctionCallExpression =
                                    (KotlinUFunctionCallExpression) uExpression;
                            return assertMethodName(uFunctionCallExpression.getMethodName());
                        }
                    }

                    if (!(expression instanceof UQualifiedReferenceExpression)) {
                        continue;
                    }

                    UQualifiedReferenceExpression referenceExpression = (UQualifiedReferenceExpression) expression;
                    UExpression receiverExpression = referenceExpression.getReceiver();
                    PsiElement receiverPsi = receiverExpression.getPsi();
                    if (receiverPsi == null) {
                        continue; // can this case be happened?
                    }
                    String receiverName = receiverPsi.getText();
                    if ("super".equals(receiverName)) {
                        // skip super method call
                        continue;
                    }

                    if (isKotlin && referenceExpression instanceof KotlinUQualifiedReferenceExpression) {
                        return assertMethodName(referenceExpression.getResolvedName());
                    } else {
                        String targetClassName = className + "PermissionsDispatcher";
                        if (targetClassName.equals(receiverName) && assertMethodName(referenceExpression.getResolvedName())) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }
}
