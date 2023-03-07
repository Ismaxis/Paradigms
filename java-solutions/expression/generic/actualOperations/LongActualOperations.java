package expression.generic.actualOperations;

public class LongActualOperations extends AbstractActualOperations<Long> {
    @Override
    public Long add(Long left, Long right) {
        return left + right;
    }
    @Override
    public Long subtract(Long left, Long right) {
        return left - right;
    }
    @Override
    public Long multiply(Long left, Long right) {
        return left * right;
    }
    @Override
    public Long divide(Long left, Long right) {
        return left / right;
    }
    @Override
    public Long mod(Long left, Long right) {
        return left % right;
    }

    @Override
    public Long negate(Long val) {
        return -val;
    }
    @Override
    public Long abs(Long val) {
        return Math.abs(val);
    }

    @Override
    public Long toConst(String str) {
        return Long.parseLong(str);
    }
    @Override
    public Long toConst(int val) {
        return (long) val;
    }

    @Override
    public String getOperationTypeName() {
        return "LONG";
    }
}
