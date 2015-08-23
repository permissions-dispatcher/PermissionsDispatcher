package permissions.dispatcher.processor.exceptions;

public class TooManyException extends RuntimeException {

    public TooManyException(String detailMessage) {
        super(detailMessage);
    }

}