package permissions.dispatcher.processor.exceptions;

public class WrongClassException extends RuntimeException {

    public WrongClassException(String detailMessage) {
        super(detailMessage);
    }

}
