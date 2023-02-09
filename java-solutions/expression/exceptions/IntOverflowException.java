package expression.exceptions;

public class IntOverflowException extends ExpressionEvalException {
    public IntOverflowException(String operation, Number left, Number right) {
        super("INT Overflow: '" + left + " " + operation + " " + right + "'");
    }

    public IntOverflowException(String operation, Number value) {
        super("INT Overflow: '" + operation + "(" + value + ")'");
    }
}
