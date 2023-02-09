package expression.exceptions;

import expression.ExpressionToString;
import expression.Subtract;

public class CheckedSubtract extends Subtract {
    public CheckedSubtract(ExpressionToString left, ExpressionToString right) {
        super(left, right);
    }

    @Override
    protected int calc(int left, int right) {
        int result = left - right;
        if (((left & ~right & ~result) | (~left & right & result)) < 0) {
            throw new IntOverflowException(symbol, left, right);
        }
        return result;
    }
}
