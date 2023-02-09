package expression;

public class Variable implements ExpressionToString {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public void toString(StringBuilder sb) {
        sb.append(name);
    }

    @Override
    public void toMiniString(StringBuilder sb, boolean needToShielded) {
        sb.append(name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Variable) {
            Variable var = ((Variable) obj);
            return name.equals(var.name);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public int evaluate(int x, int y, int z) {
        if (name.equals("x")) {
            return x;
        } else if (name.equals("y")) {
            return y;
        } else if (name.equals("z")) {
            return z;
        } else {
            throw new RuntimeException("Only x, y and z supported in 3 vars mode");
        }
    }
}
