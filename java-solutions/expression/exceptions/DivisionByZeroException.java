package expression.exceptions;

public class DivisionByZeroException extends ExpressionEvalException {
    public DivisionByZeroException(String message) {
        super(message);
    }
}
