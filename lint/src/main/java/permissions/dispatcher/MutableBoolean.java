package permissions.dispatcher;

final class MutableBoolean {

    private boolean value;

    MutableBoolean(boolean value) {
        this.value = value;
    }

    boolean isValue() {
        return value;
    }

    void setValue(boolean value) {
        this.value = value;
    }
}
