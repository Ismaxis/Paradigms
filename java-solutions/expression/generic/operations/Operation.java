package expression.generic.operations;

import expression.generic.actualOperations.ActualOperations;

abstract public class Operation<T> extends AbstractTripleExpression<T> {
    protected final String symbol;
    protected final int priority;

    protected Operation(ActualOperations<T> actualOperations, String symbol, int priority) {
        super(actualOperations);
        this.symbol = symbol;
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    abstract public boolean bracketsEqualPriority();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toString(sb);
        return sb.toString();
    }

    @Override
    public String toMiniString() {
        StringBuilder sb = new StringBuilder();
        toMiniString(sb, false);
        return sb.toString();
    }
}
