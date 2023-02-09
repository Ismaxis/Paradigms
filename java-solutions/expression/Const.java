package expression;

public class Const implements ExpressionToString {
    private final Number value;

    public Const(int value) {
        this.value = value;
    }

    public Const(double value) {
        this.value = value;
    }

    @Override
    public void toString(StringBuilder sb) {
        sb.append(value);
    }

    @Override
    public void toMiniString(StringBuilder sb, boolean needToShielded) {
        sb.append(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Const con) {
            return value.equals(con.value);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return value.intValue();
    }
}
