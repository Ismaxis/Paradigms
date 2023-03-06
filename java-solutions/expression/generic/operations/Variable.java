package expression.generic.operations;

import expression.generic.actualOperations.ActualOperations;

public class Variable<T> extends AbstractTripleExpression<T> {
    private final String name;

    public Variable(ActualOperations<T> actualOperations, String name) {
        super(actualOperations);
        this.name = name;
    }

    @Override
    public void toString(StringBuilder sb) {
        sb.append(toString());
    }

    @Override
    public void toMiniString(StringBuilder sb, boolean needToShielded) {
        sb.append(toString());
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String toMiniString() {
        return toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Variable var) {
            return name.equals(var.name);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return switch (name) {
            case "x" -> x;
            case "y" -> y;
            case "z" -> z;
            default -> throw new RuntimeException("Only x, y and z supported in 3 vars mode");
        };
    }
}
