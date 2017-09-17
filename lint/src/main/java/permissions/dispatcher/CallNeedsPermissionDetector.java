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
import org.jetbrains.uast.UCallExpression;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.UFile;
import org.jetbrains.uast.UMethod;
import org.jetbrains.uast.visitor.AbstractUastVisitor;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class CallNeedsPermissionDetector extends Detector implements Detector.UastScanner {

    static final Issue ISSUE = Issue.create("CallNeedsPermission",
            "Call the corresponding \"WithPermissionCheck\" method of the generated PermissionsDispatcher class instead",
            "Directly invoking a method annotated with @NeedsPermission may lead to misleading behaviour on devices running Marshmallow and up. Therefore, it is advised to use the generated PermissionsDispatcher class instead, which provides a \"WithPermissionCheck\" method that safely handles runtime permissions.",
            Category.CORRECTNESS,
            7,
            Severity.ERROR,
            new Implementation(CallNeedsPermissionDetector.class, EnumSet.of(Scope.ALL_JAVA_FILES)));

    static Set<String> annotatedMethods = new HashSet<String>();

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

    private static class AnnotationChecker extends AbstractUastVisitor {

        private final JavaContext context;

        private AnnotationChecker(JavaContext context) {
            this.context = context;
        }

        @Override
        public boolean visitCallExpression(UCallExpression node) {
            if (isGeneratedFiles(context)) {
                return true;
            }
            if (node.getReceiver() == null && annotatedMethods.contains(node.getMethodName())) {
                context.report(ISSUE, node, context.getLocation(node), "Trying to access permission-protected method directly");
            }
            return true;
        }

        @Override
        public boolean visitMethod(UMethod node) {
            if (isGeneratedFiles(context)) {
                return super.visitMethod(node);
            }
            UAnnotation annotation = node.findAnnotation("permissions.dispatcher.NeedsPermission");
            if (annotation == null) {
                return super.visitMethod(node);
            }
            annotatedMethods.add(node.getName());
            return super.visitMethod(node);
        }

        private static boolean isGeneratedFiles(JavaContext context) {
            UFile sourceFile = context.getUastFile();
            if (sourceFile == null) {
                return false;
            }
            List<UClass> classes = sourceFile.getClasses();
            if (!classes.isEmpty()) {
                String qualifiedName = classes.get(0).getName();
                if (qualifiedName != null && qualifiedName.contains("PermissionsDispatcher")) {
                    return true;
                }
            }
            return false;
        }
    }
}
