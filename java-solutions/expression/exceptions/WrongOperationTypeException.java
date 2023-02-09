package expression.exceptions;

public class WrongOperationTypeException extends RuntimeException {
    public WrongOperationTypeException(String message) {
        super(message);
    }
}
