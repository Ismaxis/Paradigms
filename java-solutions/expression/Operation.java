package expression;

abstract public class Operation implements ExpressionToString {
    protected final String symbol;
    protected final int priority;

    protected Operation(String symbol, int priority) {
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
