package permissions.dispatcher.processor;

import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;
import com.squareup.javapoet.JavaFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import permissions.dispatcher.RuntimePermissions;

import static permissions.dispatcher.processor.JavaFileBuilder.createJavaFile;
import static permissions.dispatcher.processor.Utils.getAnnotatedClasses;

@AutoService(Processor.class)
public class PermissionsProcessor extends AbstractProcessor {

    private Filer filer;

    private Messager messager;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return ImmutableSet.of(RuntimePermissions.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        this.filer = env.getFiler();
        this.messager = env.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        List<RuntimePermissionsAnnotatedElement> classes = getAnnotatedClasses(env);
        for (RuntimePermissionsAnnotatedElement clazz : classes) {
            JavaFile javaFile = createJavaFile(clazz);
            try {
                javaFile.writeTo(filer);
            } catch (IOException e) {
                messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
            }
        }
        return true;
    }

}
