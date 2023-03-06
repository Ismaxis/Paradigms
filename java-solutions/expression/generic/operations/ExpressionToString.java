package expression.generic.operations;

public interface ExpressionToString {
    String toString();
    String toMiniString();

    void toString(StringBuilder sb);
    void toMiniString(StringBuilder sb, boolean needToBeShielded);
}
