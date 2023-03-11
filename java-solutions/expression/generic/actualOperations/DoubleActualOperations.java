package expression.generic.actualOperations;

public class DoubleActualOperations extends AbstractActualOperations<Double> {
    @Override
    public Double add(Double left, Double right) {
        return left + right;
    }

    @Override
    public Double subtract(Double left, Double right) {
        return left - right;
    }

    @Override
    public Double multiply(Double left, Double right) {
        return left * right;
    }

    @Override
    public Double divide(Double left, Double right) {
        return left / right;
    }

    @Override
    public Double mod(Double left, Double right) {
        return left % right;
    }

    @Override
    public Double negate(Double val) {
        return -val;
    }

    @Override
    public Double abs(Double val) {
        return Math.abs(val);
    }

    @Override
    public Double toConst(String str) {
        return Double.parseDouble(str);
    }

    @Override
    public Double fromIntToConst(int val) {
        return (double) val;
    }

    @Override
    public String getOperationTypeName() {
        return "DOUBLE";
    }
}
