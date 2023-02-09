package expression;

public class Divide extends BinaryOperation {
    protected static final String symbol = "/";
    private static final int prior = 2;
    private static final BinaryOperationProperties props =
            new BinaryOperationProperties(false, false);

    public Divide(ExpressionToString left, ExpressionToString right) {
        super(left, right, symbol, prior, props);
    }

    @Override
    protected int calc(int left, int right) {
        return left / right;
    }

    @Override
    protected double calc(double left, double right) {
        return left / right;
    }
}
