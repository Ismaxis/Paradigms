package expression;

import java.util.Objects;

abstract public class BinaryOperation extends Operation {
    protected final ExpressionToString left;
    protected final ExpressionToString right;
    protected final BinaryOperationProperties props;

    protected BinaryOperation(ExpressionToString left, ExpressionToString right,
                              String symbol, int priority, BinaryOperationProperties props) {
        super(symbol, priority);
        this.left = left;
        this.right = right;
        this.props = props;
    }

    protected boolean needRightToShield() {
        if (right instanceof Operation operation) {
            return operation.getPriority() < getPriority() ||
                    operation.getPriority() == getPriority() &&
                            (operation.bracketsEqualPriority() || !this.props.isCommutative);
        }
        return false;
    }

    protected boolean needLeftToShield() {
        if (left instanceof Operation operation) {
            return operation.getPriority() < getPriority();
        }
        return false;
    }

    abstract protected int calc(int left, int right);

    abstract protected double calc(double left, double right);

    @Override
    public boolean bracketsEqualPriority() {
        return !props.isAssociative;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return calc(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }

    private void appendOp(StringBuilder sb) {
        sb.append(' ');
        sb.append(symbol);
        sb.append(' ');
    }

    public void toString(StringBuilder sb) {
        sb.append('(');
        left.toString(sb);
        appendOp(sb);
        right.toString(sb);
        sb.append(')');

    }

    @Override
    public void toMiniString(StringBuilder sb, boolean needToShielded) {
        if (needToShielded) {
            sb.append('(');
        }
        left.toMiniString(sb, needLeftToShield());
        appendOp(sb);
        right.toMiniString(sb, needRightToShield());
        if (needToShielded) {
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
