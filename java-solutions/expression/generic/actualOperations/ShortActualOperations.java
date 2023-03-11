package expression.generic.actualOperations;

public class ShortActualOperations extends AbstractActualOperations<Short> {
    @Override
    public Short add(Short left, Short right) {
        return (short) (left + right);
    }
    @Override
    public Short subtract(Short left, Short right) {
        return (short) (left - right);
    }
    @Override
    public Short multiply(Short left, Short right) {
        return (short) (left * right);
    }
    @Override
    public Short divide(Short left, Short right) {
        return (short) (left / right);
    }
    @Override
    public Short mod(Short left, Short right) {
        return (short) (left % right);
    }

    @Override
    public Short negate(Short val) {
        return (short) -val;
    }
    @Override
    public Short abs(Short val) {
        return (short) Math.abs(val);
    }

    @Override
    public Short toConst(String str) {
        return Short.parseShort(str);
    }
    // :NOTE: обычно ты никогда не хочешь приводишь больший тип к меньшему, с большой вероятностью ты получишь не то, что ожидаешь
    @Override
    public Short toConst(int val) {
        return (short) val;
    }

    @Override
    public String getOperationTypeName() {
        return "SHORT";
    }
}
