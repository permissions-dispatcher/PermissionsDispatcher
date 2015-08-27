package permissions.dispatcher.processor.exceptions;

public class MatchedValueMissingException extends RuntimeException {

    public MatchedValueMissingException(String message) {
        super(message);
    }
}
