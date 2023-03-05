package expression.generic.operations;

import expression.generic.actualOperations.ActualOperations;

public class Const<T> extends AbstractTripleExpression<T> {
    private final T value;
    private final String stringValue;

    public Const(ActualOperations<T> actualOperations, String value) {
        super(actualOperations);
        this.value = actualOperations.toConst(value);
        this.stringValue = value;
    }

    @Override
    public void toString(StringBuilder sb) {
        sb.append(value);
    }

    @Override
    public void toMiniString(StringBuilder sb, boolean needToShielded) {
        sb.append(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public String toMiniString() {
        return stringValue;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Const con) {
            return value.equals(con.value);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return value;
    }
}
