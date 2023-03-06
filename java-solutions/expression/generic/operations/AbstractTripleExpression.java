package expression.generic.operations;

import expression.generic.actualOperations.ActualOperations;

abstract public class AbstractTripleExpression<T> implements Expression<T>{
    protected final ActualOperations<T> actualOperations;
    public AbstractTripleExpression(ActualOperations<T> actualOperations) {
        this.actualOperations = actualOperations;
    }
    public ActualOperations<T> getActualOperations() {
        return actualOperations;
    }
}
