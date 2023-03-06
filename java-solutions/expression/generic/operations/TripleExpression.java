package expression.generic.operations;

public interface TripleExpression<T> {
    T evaluate(T x, T y, T z);
}
