package expression;

import expression.exceptions.CheckedNegate;
import expression.exceptions.Log10;
import expression.exceptions.Pow10;

abstract public class UnaryOperation extends Operation {
    protected final ExpressionToString child;

    protected UnaryOperation(ExpressionToString child, String symbol) {
        super(symbol, -1);
        this.child = child;
    }

    abstract protected int calc(int value);

    abstract protected double calc(double value);

    @Override
    public int evaluate(int x, int y, int z) {
        return calc(child.evaluate(x, y, z));
    }

    @Override
    public void toString(StringBuilder sb) {
        sb.append(symbol).append("(");
        child.toString(sb);
        sb.append(")");
    }

    @Override
    public void toMiniString(StringBuilder sb, boolean needToShielded) {
        boolean shieldNeeded = needChildToBeShielded();
        sb.append(symbol).append(shieldNeeded ? "" : " ");
        child.toMiniString(sb, shieldNeeded);
    }

    private boolean needChildToBeShielded() {
        if (child instanceof Operation operation) {
            return getPriority() < operation.getPriority();
        }
        return false;
    }

    @Override
    public boolean bracketsEqualPriority() {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UnaryOperation that) {
            return this.getClass() == that.getClass() && this.child.equals(that.child);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 67 * (67 * symbol.hashCode() + child.hashCode());
    }

    public static UnaryOperation createInstance(Class<?> cl, ExpressionToString child) {
        if (cl == Negate.class) {
            return new Negate(child);
        } else if (cl == Count.class) {
            return new Count(child);
        } else if (cl == CheckedNegate.class) {
            return new CheckedNegate(child);
        } else if (cl == Pow10.class) {
            return new Pow10(child);
        } else if (cl == Log10.class) {
            return new Log10(child);
        } else {
            throw new RuntimeException(cl.getName() + " cannot be created with UnaryOperation.createInstance");
        }
    }
}
