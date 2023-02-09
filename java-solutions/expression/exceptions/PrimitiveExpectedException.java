package expression.exceptions;

public class PrimitiveExpectedException extends ParserException {
    public PrimitiveExpectedException(int pos, char found) {
        super(pos, "Variable or Const", found);
    }
}
