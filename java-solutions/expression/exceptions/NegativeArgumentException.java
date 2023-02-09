package expression.exceptions;

public class NegativeArgumentException extends ExpressionEvalException {
    public NegativeArgumentException(String operation, Number value) {
        super("Argument should greater than zero: '" + operation + "(" + value + ")'");
    }
}
