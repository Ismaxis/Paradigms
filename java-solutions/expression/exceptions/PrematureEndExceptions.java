package expression.exceptions;

public class PrematureEndExceptions extends ParserException {
    public PrematureEndExceptions(int pos, String expected) {
        super(pos, expected, "END");
    }
}
