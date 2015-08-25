package permissions.dispatcher.processor

enum class ClassType(public val activity: String) {
    ACTIVITY("target"),
    FRAGMENT("target.getActivity()");

    companion object {

        fun getClassType(className: String): ClassType? {
            if (className.endsWith("Activity")) {
                return ACTIVITY
            } else if (className.endsWith("Fragment")) {
                return FRAGMENT
            }
            return null
        }
    }

}
