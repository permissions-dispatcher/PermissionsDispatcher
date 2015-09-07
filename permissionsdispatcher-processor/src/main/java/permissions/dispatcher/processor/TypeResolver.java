package permissions.dispatcher.processor;

/**
 * Created by marcel on 07.09.15.
 */
interface TypeResolver {

    boolean isSubTypeOf(String subTypeClass, String superTypeClass);
}
