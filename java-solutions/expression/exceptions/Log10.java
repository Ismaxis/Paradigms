package expression.exceptions;

import expression.ExpressionToString;
import expression.UnaryOperation;

public class Log10 extends UnaryOperation {
    public static final String symbol = "log10";

    public Log10(ExpressionToString child) {
        super(child, symbol);
    }

    @Override
    protected int calc(int value) {
        if (value <= 0) {
            throw new NegativeArgumentException(symbol, value);
        }

        int log = 0;
        while (value >= 10) {
            value /= 10;
            log++;
        }

        return log;
    }

    @Override
    protected double calc(double value) {
        throw new WrongOperationTypeException("Can't perform CLEAR operation on double value");
    }

}
