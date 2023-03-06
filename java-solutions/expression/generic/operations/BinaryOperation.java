package expression.generic.operations;

import java.util.Objects;

abstract public class BinaryOperation<T> extends Operation<T> {
    protected final Expression<T> left;
    protected final Expression<T> right;
    protected final BinaryOperationProperties props;

    protected BinaryOperation(Expression<T> left, Expression<T> right,
                              String symbol, int priority, BinaryOperationProperties props) {
        super(left.getActualOperations(), symbol, priority);
        this.left = left;
        this.right = right;
        this.props = props;
    }

    protected boolean needRightToShield() {
        if (right instanceof Operation<T> operation) {
            return operation.getPriority() < getPriority() ||
                    operation.getPriority() == getPriority() &&
                            (operation.bracketsEqualPriority() || !this.props.isCommutative);
        }
        return false;
    }

    protected boolean needLeftToShield() {
        if (left instanceof Operation<T> operation) {
            return operation.getPriority() < getPriority();
        }
        return false;
    }

    abstract protected T calc(T left, T right);

    @Override
    public boolean bracketsEqualPriority() {
        return !props.isAssociative;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return calc(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }

    private void appendOp(StringBuilder sb) {
        sb.append(' ');
        sb.append(symbol);
        sb.append(' ');
    }

    @Override
    public void toString(StringBuilder sb) {
        sb.append('(');
        left.toString(sb);
        appendOp(sb);
        right.toString(sb);
        sb.append(')');

    }

    @Override
    public void toMiniString(StringBuilder sb, boolean needToBeShielded) {
        if (needToBeShielded) {
            sb.append('(');
        }
        left.toMiniString(sb, needLeftToShield());
        appendOp(sb);
        right.toMiniString(sb, needRightToShield());
        if (needToBeShielded) {
            sb.append(')');
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BinaryOperation that) {
            boolean leftEq = Objects.equals(left, that.left);
            boolean rightEq = Objects.equals(right, that.right);
            return this.getClass() == that.getClass() && leftEq && rightEq;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int leftHash = (left == null) ? 0 : left.hashCode();
        int rightHash = (right == null) ? 0 : right.hashCode();
        return (((this.getClass().hashCode() * 67) + leftHash) * 67 + rightHash) * 67;
    }
}
