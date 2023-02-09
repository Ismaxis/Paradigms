package expression.exceptions;


public class CloseBracketsException extends ParserException {
    public CloseBracketsException(int pos, char found) {
        super(pos, ")", found);
    }
}
