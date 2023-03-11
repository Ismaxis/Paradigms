package expression.generic.actualOperations;

public interface ActualOperations<T> {
    T add(T left, T right);
    T subtract(T left, T right);
    T multiply(T left, T right);
    T divide(T left, T right);
    T mod(T left, T right);

    T negate(T val);
    T abs(T val);

    T toConst(String str);
    T toConst(int val);
    // :NOTE: две одинаковых имплементации - в AbstractActualOperations и DoubleActualOperations
    boolean isStartOfConst(char ch);
    // :NOTE: не используется
    boolean isPartOfConst(char ch);
    String getOperationTypeName();
}
