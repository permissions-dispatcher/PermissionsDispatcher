package permissions.dispatcher.processor;

enum ClassType {
    ACTIVITY("target"),
    FRAGMENT("target.getActivity()");

    private final String activity;

    ClassType(String activity) {
        this.activity = activity;
    }

    static ClassType getClassType(String className) {
        if (className.endsWith("Activity")) {
            return ACTIVITY;
        } else if (className.endsWith("Fragment")) {
            return FRAGMENT;
        }
        return null;
    }

    public String getActivity() {
        return activity;
    }
}