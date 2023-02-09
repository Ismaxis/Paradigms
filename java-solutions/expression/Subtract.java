package expression;

public class Subtract extends BinaryOperation {
    protected static final String symbol = "-";
    private static final int prior = 1;
    private static final BinaryOperationProperties props =
            new BinaryOperationProperties(false, true);

    public Subtract(ExpressionToString left, ExpressionToString right) {
        super(left, right, symbol, prior, props);
    }

    @Override
    protected int calc(int left, int right) {
        return left - right;
    }

    @Override
    protected double calc(double left, double right) {
        return left - right;
    }
}
