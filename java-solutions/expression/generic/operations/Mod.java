package expression.generic.operations;

public class Mod<T> extends BinaryOperation<T> {
    protected static final String symbol = "mod";

    private static final int prior = 2;
    private static final BinaryOperationProperties props =
            new BinaryOperationProperties(false, false);

    public Mod(Expression<T> left, Expression<T> right) {
        super(left, right, symbol, prior, props);
    }

    @Override
    protected T calc(T left, T right) {
        return actualOperations.mod(left, right);
    }
}
