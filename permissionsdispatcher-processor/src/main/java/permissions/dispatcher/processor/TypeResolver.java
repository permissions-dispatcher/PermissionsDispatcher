package permissions.dispatcher.processor;

interface TypeResolver {

    boolean isSubTypeOf(String subTypeClass, String superTypeClass);

}
