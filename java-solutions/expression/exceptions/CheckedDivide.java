package expression.exceptions;

import expression.Divide;
import expression.ExpressionToString;

public class CheckedDivide extends Divide {
    public CheckedDivide(ExpressionToString left, ExpressionToString right) {
        super(left, right);
    }

    @Override
    protected int calc(int left, int right) {
        if (right == 0) {
            throw new DivisionByZeroException("'" + left + " / " + right + "'");
        }
        if (left == Integer.MIN_VALUE && right == -1) {
            throw new IntOverflowException(symbol, left, right);
        }
        return super.calc(left, right);
    }
}
