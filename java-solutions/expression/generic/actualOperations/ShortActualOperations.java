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
    // :FIXED: метод переименован 'toConst(int)' -> 'fromIntToConst(int)', что лучше отражает суть происходящего.
    /*
       :ANS:
        В метод GenericTabulator.tabulate(...) нам подают границы в int
        и значения мы счиатем в точках с int координатами.
        Данный метод занимался тем, что переводил значение из int (так их дал пользователь) в нужный тип.
        К тому же в ТЗ написано: "s – вычисления в short без проверки на переполнение",
        поэтому в данном случае переполнение является ожидаемым поведением.
    */
    @Override
    public Short fromIntToConst(int val) {
        return (short) val;
    }

    @Override
    public String getOperationTypeName() {
        return "SHORT";
    }
}
