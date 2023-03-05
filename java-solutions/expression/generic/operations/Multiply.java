package expression.generic.operations;

public class Multiply<T> extends BinaryOperation<T> {
    protected static final String symbol = "*";
    private static final int prior = 2;
    private static final BinaryOperationProperties props =
            new BinaryOperationProperties(true, true);

    public Multiply(Expression<T> left, Expression<T> right) {
        super(left, right, symbol, prior, props);
    }

    @Override
    protected T calc(T left, T right) {
        return actualOperations.multiply(left, right);
    }
}
