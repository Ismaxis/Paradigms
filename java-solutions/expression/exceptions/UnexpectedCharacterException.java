package expression.exceptions;

public class UnexpectedCharacterException extends ParserException {
    public UnexpectedCharacterException(int pos, String expected, char found) {
        super(pos, expected, found);
    }
}
