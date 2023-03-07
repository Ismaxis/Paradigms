package expression.generic.operations;

public class Square<T> extends UnaryOperation<T> {
    protected final static String symbol = "square";

    public Square(Expression<T> child) {
        super(child, symbol);
    }

    @Override
    protected T calc(T value) {
        return actualOperations.multiply(value, value);
    }
}
