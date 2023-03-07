package expression.generic.operations;

public class Abs<T> extends UnaryOperation<T> {
    protected final static String symdol = "abs";

    public Abs(Expression<T> child) {
        super(child, symdol);
    }

    @Override
    protected T calc(T value) {
        return actualOperations.abs(value);
    }
}
