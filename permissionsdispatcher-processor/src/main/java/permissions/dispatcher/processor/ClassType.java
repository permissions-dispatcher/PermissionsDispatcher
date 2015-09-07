package permissions.dispatcher.processor;

enum ClassType {
    ACTIVITY("target"),
    FRAGMENT("target.getActivity()");

    private final String activity;

    ClassType(String activity) {
        this.activity = activity;
    }

    static ClassType getClassType(String className, TypeResolver resolver) {
        if (resolver.isSubTypeOf(className, "android.app.Activity")) {
            return ACTIVITY;
        } else if (resolver.isSubTypeOf(className, "android.support.v4.app.Fragment")) {
            return FRAGMENT;
        }
        return null;
    }

    public String getActivity() {
        return activity;
    }

}
