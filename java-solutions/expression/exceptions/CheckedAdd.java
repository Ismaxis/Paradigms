package expression.exceptions;

import expression.Add;
import expression.ExpressionToString;

public class CheckedAdd extends Add {
    public CheckedAdd(ExpressionToString left, ExpressionToString right) {
        super(left, right);
    }

    @Override
    protected int calc(int left, int right) {
        int result = left + right;
        if (((left & right & ~result) | (~left & ~right & result)) < 0) {
            throw new IntOverflowException(symbol, left, right);
        }
        return result;
    }
}
