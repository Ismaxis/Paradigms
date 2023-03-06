package expression.generic.operations;

import expression.generic.actualOperations.ActualOperations;

public interface Expression<T> extends TripleExpression<T>, ExpressionToString {
    ActualOperations<T> getActualOperations();
}
