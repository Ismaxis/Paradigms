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
    public Double negate(Double val) {
        return -val;
    }

    @Override
    public Double toConst(String str) {
        return Double.parseDouble(str);
    }
    @Override
    public Double toConst(int val) {
        return (double) val;
    }
    @Override
    public boolean isStartOfConst(char ch) {
        return Character.isDigit(ch);
    }
    @Override
    public boolean isPartOfConst(char ch) {
        return Character.isDigit(ch) || ch == '.';
    }
    @Override
    public String getOperationTypeName() {
        return "DOUBLE";
    }
}
