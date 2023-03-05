package expression.generic.operations;

public class Negate<T> extends UnaryOperation<T> {
    protected final static String symbol = "-";

    public Negate(Expression<T> child) {
        super(child, symbol);
    }

    @Override
    protected T calc(T value) {
        return actualOperations.negate(value);
    }
}
