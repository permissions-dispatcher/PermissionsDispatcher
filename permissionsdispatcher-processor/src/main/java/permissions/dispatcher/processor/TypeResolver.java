package permissions.dispatcher.processor;

interface TypeResolver {

    boolean isSubTypeOf(String subType, String superType);

}
