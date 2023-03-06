package expression.generic.actualOperations;

import expression.exceptions.IntOverflowException;

public class IntActualOperations extends AbstractActualOperations<Integer>  {
    private final boolean overflowChecksEnabled;
    public IntActualOperations(boolean overflowChecksEnabled) {
        this.overflowChecksEnabled = overflowChecksEnabled;
    }

    @Override
    public Integer add(Integer left, Integer right) {
        if (overflowChecksEnabled &&
                right > 0 && Integer.MAX_VALUE - right < left ||
                right < 0 && Integer.MIN_VALUE - right > left) {
            throw new IntOverflowException("+", left, right);
        }
        return left + right;
    }
    @Override
    public Integer subtract(Integer left, Integer right) {
        if (overflowChecksEnabled &&
                right > 0 && Integer.MIN_VALUE + right > left ||
                right < 0 && Integer.MAX_VALUE + right < left) {
            throw new IntOverflowException("-", left, right);
        }
        return left - right;
    }
    @Override
    public Integer multiply(Integer left, Integer right) {
        int res = left * right;
        if (overflowChecksEnabled &&
                ((right != 0) && (res / right != left)) ||
                (left == Integer.MIN_VALUE && right == -1)) {
            throw new IntOverflowException("*", left, right);
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

    @Override
    public Integer negate(Integer val) {
        if (overflowChecksEnabled && val.equals(Integer.MIN_VALUE)) {
            throw new IntOverflowException("-", val);
        }
        return -val;
    }

    @Override
    public Integer toConst(String str) {
        return Integer.parseInt(str);
    }
    @Override
    public Integer toConst(int val) {
        return val;
    }
    @Override
    public boolean isStartOfConst(char ch) {
        return Character.isDigit(ch);
    }
    @Override
    public boolean isPartOfConst(char ch) {
        return Character.isDigit(ch);
    }
    @Override
    public String getOperationTypeName() {
        return "INT";
    }
}
