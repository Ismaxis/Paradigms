package expression.generic.actualOperations;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.IntOverflowException;

public class IntActualOperations extends AbstractActualOperations<Integer>  {
    private final boolean overflowChecksEnabled;
    public IntActualOperations(boolean overflowChecksEnabled) {
        this.overflowChecksEnabled = overflowChecksEnabled;
    }

    @Override
    public Integer add(Integer left, Integer right) {
        if (overflowChecksEnabled) {
            if ((right > 0 && Integer.MAX_VALUE - right < left) ||
                    (right < 0 && Integer.MIN_VALUE - right > left)) {
                throw new IntOverflowException("+", left, right);
            }
        }
        return left + right;
    }

    @Override
    public Integer subtract(Integer left, Integer right) {
        if (overflowChecksEnabled) {
            if ((right > 0 && Integer.MIN_VALUE + right > left) ||
                    (right < 0 && Integer.MAX_VALUE + right < left)) {
            throw new IntOverflowException("-", left, right);
            }
        }
        return left - right;
    }

    @Override
    public Integer multiply(Integer left, Integer right) {
        int res = left * right;
        if (overflowChecksEnabled) {
            if ((right != 0 && res / right != left) ||
                (left == Integer.MIN_VALUE && right == -1)) {
                throw new IntOverflowException("*", left, right);
            }
        }
        return res;
    }

    @Override
    public Integer divide(Integer left, Integer right) {
        if (overflowChecksEnabled) {
            if (left.equals(Integer.MIN_VALUE) && right.equals(-1)) {
                throw new IntOverflowException("/", left, right);
            }
        }
        return left / right;
    }

    // :NOTE: здесь ничего проверить не нужно? :FIXED:
    // :ANS: вообще, когда right == 0, '%' сам кидает ArithmeticException,
    //       но проверка лишней не будет.
    @Override
    public Integer mod(Integer left, Integer right) {
        if (right == 0) {
            throw new DivisionByZeroException("Division by zero: " + left + " % " + right);
        }
        return left % right;
    }

    @Override
    public Integer negate(Integer val) {
        if (overflowChecksEnabled && val.equals(Integer.MIN_VALUE)) {
            throw new IntOverflowException("-", val);
        }
        return -val;
    }

    @Override
    public Integer abs(Integer val) {
        if (overflowChecksEnabled) {
            if (val == Integer.MIN_VALUE) {
                throw new IntOverflowException("abs", val);
            }
        }
        return Math.abs(val);
    }

    @Override
    public Integer toConst(String str) {
        return Integer.parseInt(str);
    }

    @Override
    public Integer fromIntToConst(int val) {
        return val;
    }

    @Override
    public String getOperationTypeName() {
        return "INT";
    }
}
