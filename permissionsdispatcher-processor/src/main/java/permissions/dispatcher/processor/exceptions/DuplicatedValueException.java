package permissions.dispatcher.processor.exceptions;

public class DuplicatedValueException extends RuntimeException {

    public DuplicatedValueException(String detailMessage) {
        super(detailMessage);
    }

}
