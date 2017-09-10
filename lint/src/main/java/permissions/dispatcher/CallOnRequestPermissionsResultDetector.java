package permissions.dispatcher;

import com.android.tools.lint.client.api.UElementHandler;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;

import org.jetbrains.uast.UAnnotation;
import org.jetbrains.uast.UArrayAccessExpression;
import org.jetbrains.uast.UBinaryExpression;
import org.jetbrains.uast.UBinaryExpressionWithType;
import org.jetbrains.uast.UBlockExpression;
import org.jetbrains.uast.UBreakExpression;
import org.jetbrains.uast.UCallExpression;
import org.jetbrains.uast.UCallableReferenceExpression;
import org.jetbrains.uast.UCatchClause;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UClassInitializer;
import org.jetbrains.uast.UClassLiteralExpression;
import org.jetbrains.uast.UContinueExpression;
import org.jetbrains.uast.UDeclarationsExpression;
import org.jetbrains.uast.UDoWhileExpression;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.UEnumConstant;
import org.jetbrains.uast.UExpression;
import org.jetbrains.uast.UExpressionList;
import org.jetbrains.uast.UField;
import org.jetbrains.uast.UFile;
import org.jetbrains.uast.UForEachExpression;
import org.jetbrains.uast.UForExpression;
import org.jetbrains.uast.UIfExpression;
import org.jetbrains.uast.UImportStatement;
import org.jetbrains.uast.ULabeledExpression;
import org.jetbrains.uast.ULambdaExpression;
import org.jetbrains.uast.ULiteralExpression;
import org.jetbrains.uast.ULocalVariable;
import org.jetbrains.uast.UMethod;
import org.jetbrains.uast.UObjectLiteralExpression;
import org.jetbrains.uast.UParameter;
import org.jetbrains.uast.UParenthesizedExpression;
import org.jetbrains.uast.UPolyadicExpression;
import org.jetbrains.uast.UPostfixExpression;
import org.jetbrains.uast.UPrefixExpression;
import org.jetbrains.uast.UQualifiedReferenceExpression;
import org.jetbrains.uast.UReturnExpression;
import org.jetbrains.uast.USimpleNameReferenceExpression;
import org.jetbrains.uast.USuperExpression;
import org.jetbrains.uast.USwitchClauseExpression;
import org.jetbrains.uast.USwitchExpression;
import org.jetbrains.uast.UThisExpression;
import org.jetbrains.uast.UThrowExpression;
import org.jetbrains.uast.UTryExpression;
import org.jetbrains.uast.UTypeReferenceExpression;
import org.jetbrains.uast.UUnaryExpression;
import org.jetbrains.uast.UVariable;
import org.jetbrains.uast.UWhileExpression;
import org.jetbrains.uast.UastUtils;
import org.jetbrains.uast.visitor.AbstractUastVisitor;
import org.jetbrains.uast.visitor.UastVisitor;

import java.util.Arrays;
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

    static final Set<String> RUNTIME_PERMISSIONS_NAME = new HashSet<String>(2) {{
        add("RuntimePermissions");
        add("permissions.dispatcher.RuntimePermissions");
    }};

    @Override
    public List<Class<? extends UElement>> getApplicableUastTypes() {
        return Arrays.asList(UAnnotation.class, UClass.class);
    }

    @Override
    public UElementHandler createUastHandler(final JavaContext context) {
        return new UElementHandler() {
            @Override public void visitClass(UClass node) {
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
                super.visitAnnotation(node);
                return false;
            }
            hasRuntimePermissionAnnotation = true;
            return super.visitAnnotation(node);
        }

        @Override
        public boolean visitMethod(UMethod node) {
            if (!hasRuntimePermissionAnnotation) {
                return super.visitMethod(node);
            }
            if (!"onRequestPermissionsResult".equals(node.getName())) {
                return super.visitMethod(node);
            }
            if (hasRuntimePermissionAnnotation && !checkMethodCall(node, sourceFile)) {
                UExpression body = UastUtils.getUastContext(node).getMethodBody(node);
                String message = body == null ? "" : body.asLogString();
                context.report(ISSUE, context.getLocation(node),
                        message + "Generated onRequestPermissionsResult method not called");
            }
            return super.visitMethod(node);
        }

        @Override
        public void afterVisitMethod(UMethod node) {
            if (!hasRuntimePermissionAnnotation) {
                super.visitMethod(node);
                return;
            }
            if (!"onRequestPermissionsResult".equals(node.getName())) {
                super.visitMethod(node);
                return;
            }
            if (hasRuntimePermissionAnnotation && !checkMethodCall(node, sourceFile)) {
                UExpression body = UastUtils.getUastContext(node).getMethodBody(node);
                String message = body == null ? "" : body.asLogString();
                context.report(ISSUE, context.getLocation(node),
                        message + "Generated onRequestPermissionsResult method not called");
            }
            super.visitMethod(node);
        }
    }

    private static boolean checkMethodCall(UMethod method, UClass uClass) {
        UExpression body = method.getUastBody();
        if (body == null) {
            return false;
        }
        MutableBoolean isMethodCallFound = new MutableBoolean(false);
        String className = uClass.getName();
        body.accept(new MethodCallDetector(isMethodCallFound, className));
        return isMethodCallFound.isValue();
    }

    private static class MethodCallDetector implements UastVisitor {

        private MutableBoolean isMethodCallFound;
        private String className;

        private MethodCallDetector(MutableBoolean isMethodCallFound, String className) {
            this.isMethodCallFound = isMethodCallFound;
            this.className = className;
        }

        @Override
        public boolean visitCallExpression(UCallExpression uCallExpression) {
            PsiMethod resolveMethod = uCallExpression.resolve();
            if (resolveMethod == null) {
                isMethodCallFound.setValue(false);
                return false;
            }
            PsiClass containingClass = resolveMethod.getContainingClass();
            if (containingClass == null) {
                isMethodCallFound.setValue(false);
                return false;
            }
            String targetClassName = className + "PermissionsDispatcher";
            if (targetClassName.equals(containingClass.getName()) && "onRequestPermissionsResult".equals(resolveMethod.getName())) {
                isMethodCallFound.setValue(true);
                return true;
            }
            return false;
        }

        @Override
        public boolean visitElement(UElement uElement) {
            return false;
        }

        @Override
        public boolean visitFile(UFile uFile) {
            return false;
        }

        @Override
        public boolean visitImportStatement(UImportStatement uImportStatement) {
            return false;
        }

        @Override
        public boolean visitClass(UClass uClass) {
            return false;
        }

        @Override
        public boolean visitInitializer(UClassInitializer uClassInitializer) {
            return false;
        }

        @Override
        public boolean visitMethod(UMethod uMethod) {
            return false;
        }

        @Override
        public boolean visitVariable(UVariable uVariable) {
            return false;
        }

        @Override
        public boolean visitParameter(UParameter uParameter) {
            return false;
        }

        @Override
        public boolean visitField(UField uField) {
            return false;
        }

        @Override
        public boolean visitLocalVariable(ULocalVariable uLocalVariable) {
            return false;
        }

        @Override
        public boolean visitEnumConstant(UEnumConstant uEnumConstant) {
            return false;
        }

        @Override
        public boolean visitAnnotation(UAnnotation uAnnotation) {
            return false;
        }

        @Override
        public boolean visitLabeledExpression(ULabeledExpression uLabeledExpression) {
            return false;
        }

        @Override
        public boolean visitDeclarationsExpression(UDeclarationsExpression uDeclarationsExpression) {
            return false;
        }

        @Override
        public boolean visitBlockExpression(UBlockExpression uBlockExpression) {
            return false;
        }

        @Override
        public boolean visitQualifiedReferenceExpression(UQualifiedReferenceExpression uQualifiedReferenceExpression) {
            return false;
        }

        @Override
        public boolean visitSimpleNameReferenceExpression(USimpleNameReferenceExpression uSimpleNameReferenceExpression) {
            return false;
        }

        @Override
        public boolean visitTypeReferenceExpression(UTypeReferenceExpression uTypeReferenceExpression) {
            return false;
        }

        @Override
        public boolean visitBinaryExpression(UBinaryExpression uBinaryExpression) {
            return false;
        }

        @Override
        public boolean visitBinaryExpressionWithType(UBinaryExpressionWithType uBinaryExpressionWithType) {
            return false;
        }

        @Override
        public boolean visitPolyadicExpression(UPolyadicExpression uPolyadicExpression) {
            return false;
        }

        @Override
        public boolean visitParenthesizedExpression(UParenthesizedExpression uParenthesizedExpression) {
            return false;
        }

        @Override
        public boolean visitUnaryExpression(UUnaryExpression uUnaryExpression) {
            return false;
        }

        @Override
        public boolean visitPrefixExpression(UPrefixExpression uPrefixExpression) {
            return false;
        }

        @Override
        public boolean visitPostfixExpression(UPostfixExpression uPostfixExpression) {
            return false;
        }

        @Override
        public boolean visitExpressionList(UExpressionList uExpressionList) {
            return false;
        }

        @Override
        public boolean visitIfExpression(UIfExpression uIfExpression) {
            return false;
        }

        @Override
        public boolean visitSwitchExpression(USwitchExpression uSwitchExpression) {
            return false;
        }

        @Override
        public boolean visitSwitchClauseExpression(USwitchClauseExpression uSwitchClauseExpression) {
            return false;
        }

        @Override
        public boolean visitWhileExpression(UWhileExpression uWhileExpression) {
            return false;
        }

        @Override
        public boolean visitDoWhileExpression(UDoWhileExpression uDoWhileExpression) {
            return false;
        }

        @Override
        public boolean visitForExpression(UForExpression uForExpression) {
            return false;
        }

        @Override
        public boolean visitForEachExpression(UForEachExpression uForEachExpression) {
            return false;
        }

        @Override
        public boolean visitTryExpression(UTryExpression uTryExpression) {
            return false;
        }

        @Override
        public boolean visitCatchClause(UCatchClause uCatchClause) {
            return false;
        }

        @Override
        public boolean visitLiteralExpression(ULiteralExpression uLiteralExpression) {
            return false;
        }

        @Override
        public boolean visitThisExpression(UThisExpression uThisExpression) {
            return false;
        }

        @Override
        public boolean visitSuperExpression(USuperExpression uSuperExpression) {
            return false;
        }

        @Override
        public boolean visitReturnExpression(UReturnExpression uReturnExpression) {
            return false;
        }

        @Override
        public boolean visitBreakExpression(UBreakExpression uBreakExpression) {
            return false;
        }

        @Override
        public boolean visitContinueExpression(UContinueExpression uContinueExpression) {
            return false;
        }

        @Override
        public boolean visitThrowExpression(UThrowExpression uThrowExpression) {
            return false;
        }

        @Override
        public boolean visitArrayAccessExpression(UArrayAccessExpression uArrayAccessExpression) {
            return false;
        }

        @Override
        public boolean visitCallableReferenceExpression(UCallableReferenceExpression uCallableReferenceExpression) {
            return false;
        }

        @Override
        public boolean visitClassLiteralExpression(UClassLiteralExpression uClassLiteralExpression) {
            return false;
        }

        @Override
        public boolean visitLambdaExpression(ULambdaExpression uLambdaExpression) {
            return false;
        }

        @Override
        public boolean visitObjectLiteralExpression(UObjectLiteralExpression uObjectLiteralExpression) {
            return false;
        }

        @Override
        public void afterVisitElement(UElement uElement) {
        }

        @Override
        public void afterVisitFile(UFile uFile) {
        }

        @Override
        public void afterVisitImportStatement(UImportStatement uImportStatement) {
        }

        @Override
        public void afterVisitClass(UClass uClass) {
        }

        @Override
        public void afterVisitInitializer(UClassInitializer uClassInitializer) {
        }

        @Override
        public void afterVisitMethod(UMethod uMethod) {
        }

        @Override
        public void afterVisitVariable(UVariable uVariable) {
        }

        @Override
        public void afterVisitParameter(UParameter uParameter) {
        }

        @Override
        public void afterVisitField(UField uField) {
        }

        @Override
        public void afterVisitLocalVariable(ULocalVariable uLocalVariable) {
        }

        @Override
        public void afterVisitEnumConstant(UEnumConstant uEnumConstant) {
        }

        @Override
        public void afterVisitAnnotation(UAnnotation uAnnotation) {
        }

        @Override
        public void afterVisitLabeledExpression(ULabeledExpression uLabeledExpression) {
        }

        @Override
        public void afterVisitDeclarationsExpression(UDeclarationsExpression uDeclarationsExpression) {
        }

        @Override
        public void afterVisitBlockExpression(UBlockExpression uBlockExpression) {
        }

        @Override
        public void afterVisitQualifiedReferenceExpression(UQualifiedReferenceExpression uQualifiedReferenceExpression) {
        }

        @Override
        public void afterVisitSimpleNameReferenceExpression(USimpleNameReferenceExpression uSimpleNameReferenceExpression) {
        }

        @Override
        public void afterVisitTypeReferenceExpression(UTypeReferenceExpression uTypeReferenceExpression) {
        }

        @Override
        public void afterVisitCallExpression(UCallExpression uCallExpression) {
        }

        @Override
        public void afterVisitBinaryExpression(UBinaryExpression uBinaryExpression) {
        }

        @Override
        public void afterVisitBinaryExpressionWithType(UBinaryExpressionWithType uBinaryExpressionWithType) {
        }

        @Override
        public void afterVisitParenthesizedExpression(UParenthesizedExpression uParenthesizedExpression) {
        }

        @Override
        public void afterVisitUnaryExpression(UUnaryExpression uUnaryExpression) {
        }

        @Override
        public void afterVisitPrefixExpression(UPrefixExpression uPrefixExpression) {
        }

        @Override
        public void afterVisitPostfixExpression(UPostfixExpression uPostfixExpression) {
        }

        @Override
        public void afterVisitExpressionList(UExpressionList uExpressionList) {
        }

        @Override
        public void afterVisitIfExpression(UIfExpression uIfExpression) {
        }

        @Override
        public void afterVisitSwitchExpression(USwitchExpression uSwitchExpression) {
        }

        @Override
        public void afterVisitSwitchClauseExpression(USwitchClauseExpression uSwitchClauseExpression) {
        }

        @Override
        public void afterVisitWhileExpression(UWhileExpression uWhileExpression) {
        }

        @Override
        public void afterVisitDoWhileExpression(UDoWhileExpression uDoWhileExpression) {
        }

        @Override
        public void afterVisitForExpression(UForExpression uForExpression) {
        }

        @Override
        public void afterVisitForEachExpression(UForEachExpression uForEachExpression) {
        }

        @Override
        public void afterVisitTryExpression(UTryExpression uTryExpression) {
        }

        @Override
        public void afterVisitCatchClause(UCatchClause uCatchClause) {
        }

        @Override
        public void afterVisitLiteralExpression(ULiteralExpression uLiteralExpression) {
        }

        @Override
        public void afterVisitThisExpression(UThisExpression uThisExpression) {
        }

        @Override
        public void afterVisitSuperExpression(USuperExpression uSuperExpression) {
        }

        @Override
        public void afterVisitReturnExpression(UReturnExpression uReturnExpression) {
        }

        @Override
        public void afterVisitBreakExpression(UBreakExpression uBreakExpression) {
        }

        @Override
        public void afterVisitContinueExpression(UContinueExpression uContinueExpression) {
        }

        @Override
        public void afterVisitThrowExpression(UThrowExpression uThrowExpression) {
        }

        @Override
        public void afterVisitArrayAccessExpression(UArrayAccessExpression uArrayAccessExpression) {
        }

        @Override
        public void afterVisitCallableReferenceExpression(UCallableReferenceExpression uCallableReferenceExpression) {
        }

        @Override
        public void afterVisitClassLiteralExpression(UClassLiteralExpression uClassLiteralExpression) {
        }

        @Override
        public void afterVisitLambdaExpression(ULambdaExpression uLambdaExpression) {
        }

        @Override
        public void afterVisitObjectLiteralExpression(UObjectLiteralExpression uObjectLiteralExpression) {
        }

        @Override
        public void afterVisitPolyadicExpression(UPolyadicExpression uPolyadicExpression) {
        }
    }
}
