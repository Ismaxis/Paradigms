package expression.generic.actualOperations;

import java.math.BigInteger;

public class BigIntegerActualOperations extends AbstractActualOperations<BigInteger> {
    @Override
    public BigInteger add(BigInteger left, BigInteger right) {
        return left.add(right);
    }

    @Override
    public BigInteger subtract(BigInteger left, BigInteger right) {
        return left.subtract(right);
    }

    @Override
    public BigInteger multiply(BigInteger left, BigInteger right) {
        return left.multiply(right);
    }

    @Override
    public BigInteger divide(BigInteger left, BigInteger right) {
        return left.divide(right);
    }

    @Override
    public BigInteger negate(BigInteger val) {
        return val.negate();
    }

    @Override
    public BigInteger toConst(String str) {
        return new BigInteger(str);
    }

    @Override
    public BigInteger toConst(int val) {
        return BigInteger.valueOf(val);
    }
}

