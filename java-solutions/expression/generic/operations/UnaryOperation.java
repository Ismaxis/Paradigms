package expression.generic.operations;

abstract public class UnaryOperation<T> extends Operation<T> {
    protected final Expression<T> child;

    protected UnaryOperation(Expression<T> child, String symbol) {
        super(child.getActualOperations(), symbol, -1);
        this.child = child;
    }

    abstract protected T calc(T value);

    @Override
    public T evaluate(T x, T y, T z) {
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
        if (child instanceof Operation<T> operation) {
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
}
