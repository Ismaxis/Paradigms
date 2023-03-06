package expression.generic.operations;

public class Subtract<T> extends BinaryOperation<T> {
    protected static final String symbol = "-";
    private static final int prior = 1;
    private static final BinaryOperationProperties props =
            new BinaryOperationProperties(false, true);

    public Subtract(Expression<T> left, Expression<T> right) {
        super(left, right, symbol, prior, props);
    }

    @Override
    protected T calc(T left, T right) {
        return actualOperations.subtract(left, right);
    }
}
