package expression.generic.operations;

public class Add<T> extends BinaryOperation<T> {
    protected static final String symbol = "+";
    private static final int prior = 1;
    private static final BinaryOperationProperties props =
            new BinaryOperationProperties(true, true);

    public Add(Expression<T> left, Expression<T> right) {
        super(left, right, symbol, prior, props);
    }

    @Override
    protected T calc(T left, T right) {
        return actualOperations.add(left, right);
    }
}
