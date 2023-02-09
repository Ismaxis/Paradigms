package expression;

public interface ExpressionToString extends TripleExpression {
    void toString(StringBuilder sb);
    void toMiniString(StringBuilder sb, boolean needToShielded);
}
