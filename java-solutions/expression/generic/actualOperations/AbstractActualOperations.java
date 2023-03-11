package expression.generic.actualOperations;

public abstract class AbstractActualOperations<T extends Number & Comparable<T>> implements ActualOperations<T> {
    @Override
    public boolean isStartOfConst(char ch) {
        return Character.isDigit(ch);
    }
}
