package expression.generic.parser;

import expression.generic.operations.TripleExpression;
import expression.generic.actualOperations.ActualOperations;

public interface TripleParser<T> {
    TripleExpression<T> parse(ActualOperations<T> actualOperations, String expression) throws Exception;
}
