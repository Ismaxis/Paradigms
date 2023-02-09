package expression;

public class BinaryOperationProperties {
    public final boolean isCommutative;
    public final boolean isAssociative;

    public BinaryOperationProperties(boolean isCommutative, boolean isAssociative) {
        this.isCommutative = isCommutative;
        this.isAssociative = isAssociative;
    }
}
