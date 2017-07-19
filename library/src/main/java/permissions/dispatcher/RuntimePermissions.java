package permissions.dispatcher;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Register an <code>Activity</code> or <code>Fragment</code> to handle permissions.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface RuntimePermissions {
    /**
     * If returns true generates .kt file at compile time.
     * Otherwise generate .java file even annotated class is Kotlin.
     * <b>NOTE:</b> This is a beta feature.
     *
     * @return generates .kt file or not.
     */
    boolean kotlin() default false;
}