package permissions.dispatcher;

import com.android.tools.lint.client.api.UElementHandler;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.intellij.psi.PsiMethod;

import org.jetbrains.uast.UAnnotation;
import org.jetbrains.uast.UCallExpression;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.UFile;
import org.jetbrains.uast.UQualifiedReferenceExpression;
import org.jetbrains.uast.USimpleNameReferenceExpression;
import org.jetbrains.uast.visitor.AbstractUastVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class CallNeedsPermissionDetector extends Detector implements Detector.UastScanner {

    static final Issue ISSUE = Issue.create("CallNeedsPermission",
            "Call the corresponding \"withCheck\" method of the generated PermissionsDispatcher class instead",
            "Directly invoking a method annotated with @NeedsPermission may lead to misleading behaviour on devices running Marshmallow and up. Therefore, it is advised to use the generated PermissionsDispatcher class instead, which provides a \"withCheck\" method that safely handles runtime permissions.",
            Category.CORRECTNESS,
            7,
            Severity.ERROR,
            new Implementation(CallNeedsPermissionDetector.class,
                    EnumSet.of(Scope.ALL_JAVA_FILES)));

    static List<String> generatedClassNames = new ArrayList<String>();

    static List<String> methods = Collections.emptyList();

    @Override
    public List<Class<? extends UElement>> getApplicableUastTypes() {
        return Collections.<Class<? extends UElement>>singletonList(UClass.class);
    }

    @Override
    public UElementHandler createUastHandler(final JavaContext context) {
        return new UElementHandler() {
            @Override public void visitClass(UClass node) {
                node.accept(new AnnotationChecker(context));
            }
        };
    }

    @Override
    public List<String> getApplicableMethodNames() {
        return methods;
    }

    @Override
    public void visitMethod(JavaContext context, UCallExpression node, PsiMethod method) {
        if (methods.contains(method.getName())) {
            context.report(ISSUE, node, context.getLocation(node), "Trying to access permission-protected method directly");
        }
    }

    private static class AnnotationChecker extends AbstractUastVisitor {

        private final JavaContext context;

        private final Set<String> matchingAnnotationTypeNames;

        private AnnotationChecker(JavaContext context) {
            this.context = context;
            matchingAnnotationTypeNames = new HashSet<String>(2);
            matchingAnnotationTypeNames.add("RuntimePermissions");
            matchingAnnotationTypeNames.add("permissions.dispatcher.RuntimePermissions");
        }

        @Override
        public boolean visitQualifiedReferenceExpression(UQualifiedReferenceExpression node) {
            return !isGeneratedFiles(context) && super.visitQualifiedReferenceExpression(node);
        }

        @Override
        public boolean visitSimpleNameReferenceExpression(USimpleNameReferenceExpression node) {
            return !isGeneratedFiles(context) && super.visitSimpleNameReferenceExpression(node);
        }

        @Override
        public boolean visitAnnotation(UAnnotation annotation) {
            if (!context.isEnabled(ISSUE)) {
                return true;
            }
            String type = annotation.getQualifiedName();
            if (!matchingAnnotationTypeNames.contains(type)) {
                return true;
            }
            UFile file = context.getUastFile();
            if (file == null) {
                return true;
            }
            List<UClass> classes = file.getClasses();
            if (!classes.isEmpty() && classes.get(0).getName() != null) {
                generatedClassNames.add(classes.get(0).getName() + "PermissionsDispatcher");
                // let's check method call!
                context.requestRepeat(new CallNeedsPermissionDetector(), EnumSet.of(Scope.ALL_JAVA_FILES));
            }
            return true;
        }

        private static boolean isGeneratedFiles(JavaContext context) {
            UFile sourceFile = context.getUastFile();
            if (sourceFile == null) {
                return false;
            }
            List<UClass> classes = sourceFile.getClasses();
            if (!classes.isEmpty() && classes.get(0).getName() != null) {
                String qualifiedName = classes.get(0).getName();
                if (qualifiedName != null && qualifiedName.contains("PermissionsDispatcher")) {
                    return true;
                }
            }
            return false;
        }
    }
}
