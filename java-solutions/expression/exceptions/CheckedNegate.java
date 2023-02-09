package expression.exceptions;

import expression.ExpressionToString;
import expression.Negate;

public class CheckedNegate extends Negate {

    public CheckedNegate(ExpressionToString child) {
        super(child);
    }

    @Override
    protected int calc(int value) {
        if (value == Integer.MIN_VALUE) {
            throw new IntOverflowException(symbol, value);
        }
        return super.calc(value);
    }
}
