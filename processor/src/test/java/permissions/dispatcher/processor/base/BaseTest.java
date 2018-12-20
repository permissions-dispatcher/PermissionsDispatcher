package permissions.dispatcher.processor.base;

import com.google.testing.compile.JavaFileObjects;

import javax.tools.JavaFileObject;

public abstract class BaseTest {

    final JavaFileObject actual() {
        return JavaFileObjects.forSourceLines(getName(), getActualSource());
    }

    final JavaFileObject expect() {
        return JavaFileObjects.forSourceLines(getName() + "PermissionsDispatcher", getExpectSource());
    }

    protected abstract String getName();
    protected abstract String[] getActualSource();
    protected abstract String[] getExpectSource();
}
