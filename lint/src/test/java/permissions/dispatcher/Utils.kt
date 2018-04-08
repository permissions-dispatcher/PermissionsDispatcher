package permissions.dispatcher

internal object Utils {

    val runtimePermission = """
                 |package permissions.dispatcher;
                 |
                 |import java.lang.annotation.ElementType;
                 |import java.lang.annotation.Retention;
                 |import java.lang.annotation.RetentionPolicy;
                 |import java.lang.annotation.Target;
                 |
                 |@Target(ElementType.TYPE)
                 |@Retention(RetentionPolicy.CLASS)
                 |public @interface RuntimePermissions {
                 |}
                """.trimMargin()

    val onNeedsPermission = """
                |package permissions.dispatcher;
                |
                |import java.lang.annotation.ElementType;
                |import java.lang.annotation.Retention;
                |import java.lang.annotation.RetentionPolicy;
                |import java.lang.annotation.Target;
                |
                |@Target(ElementType.METHOD)
                |@Retention(RetentionPolicy.CLASS)
                |public @interface NeedsPermission {
                |  String[] value();
                |  int maxSdkVersion() default 0;
                |}
                """.trimMargin()

    val onRationaleAnnotation = """
                |package permissions.dispatcher;
                |
                |import java.lang.annotation.ElementType;
                |import java.lang.annotation.Retention;
                |import java.lang.annotation.RetentionPolicy;
                |import java.lang.annotation.Target;
                |
                |@Target(ElementType.METHOD)
                |@Retention(RetentionPolicy.CLASS)
                |public @interface OnShowRationale {
                |  String[] value();
                |}
                """.trimMargin()
}
