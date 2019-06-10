package permissions.dispatcher;

import com.android.tools.lint.client.api.UElementHandler;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.uast.UAnnotation;
import org.jetbrains.uast.UCallExpression;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.UFile;
import org.jetbrains.uast.UMethod;
import org.jetbrains.uast.UParameter;
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

    private static Set<String> annotatedMethods = new HashSet<String>();

    @Override
    public List<Class<? extends UElement>> getApplicableUastTypes() {
        return Collections.<Class<? extends UElement>>singletonList(UClass.class);
    }

    @Override
    public UElementHandler createUastHandler(@NotNull final JavaContext context) {
        return new UElementHandler() {
            @Override
            public void visitClass(@NotNull UClass node) {
                node.accept(new AnnotationChecker(context));
            }
        };
    }

    private static class AnnotationChecker extends AbstractUastVisitor {

        private final JavaContext context;

        private boolean hasRuntimePermissionAnnotation;

        private AnnotationChecker(JavaContext context) {
            this.context = context;
        }

        @Override
        public boolean visitAnnotation(@NotNull UAnnotation node) {
            if (!"permissions.dispatcher.RuntimePermissions".equals(node.getQualifiedName())) {
                return true;
            }
            hasRuntimePermissionAnnotation = true;
            return true;
        }

        @Override
        public boolean visitCallExpression(@NotNull UCallExpression node) {
            if (isGeneratedFiles(context) || !hasRuntimePermissionAnnotation) {
                return true;
            }
            UElement blockExpression = node.getUastParent();
            if (blockExpression == null) {
                return true;
            }
            UElement methodElement = blockExpression.getUastParent();
            if (!(methodElement instanceof UMethod)) {
                return true;
            }
            UMethod uMethod = (UMethod) methodElement;
            String methodIdentifier = methodIdentifier(uMethod);

            if (node.getReceiver() == null && annotatedMethods.contains(methodIdentifier)) {
                context.report(ISSUE, node, context.getLocation(node), "Trying to access permission-protected method directly");
            }
            return true;
        }

        @Override
        public boolean visitMethod(@NotNull UMethod node) {
            if (isGeneratedFiles(context)) {
                return super.visitMethod(node);
            }
            UAnnotation annotation = node.findAnnotation("permissions.dispatcher.NeedsPermission");
            if (annotation == null) {
                return super.visitMethod(node);
            }
            String methodIdentifier = methodIdentifier(node);
            if (methodIdentifier != null) {
                annotatedMethods.add(methodIdentifier);
            }
            return super.visitMethod(node);
        }

        @Nullable
        private static String methodIdentifier(UMethod node) {
            // check parent class name
            UElement parent = node.getUastParent();
            if (!(parent instanceof UClass)) {
                return null;
            }
            UClass uClass = (UClass) parent;
            // check parameters type
            List<UParameter> parameters = node.getUastParameters();
            StringBuilder builder = new StringBuilder();
            for (UParameter parameter : parameters) {
                String typeName = parameter.getType().getPresentableText();
                builder.append(typeName).append(".");
            }
            return uClass.getName() + builder.toString() + node.getName();
        }

        private static boolean isGeneratedFiles(JavaContext context) {
            UFile sourceFile = context.getUastFile();
            if (sourceFile == null) {
                return false;
            }
            List<UClass> classes = sourceFile.getClasses();
            if (classes.isEmpty()) {
                return false;
            }
            String qualifiedName = classes.get(0).getName();
            return qualifiedName != null && qualifiedName.contains("PermissionsDispatcher");
        }
    }
}
