package expression.exceptions;

public class ParseConstException extends ParserException {
    public ParseConstException(String numberStr, String type) {
        super("Can't parse: '" + numberStr + "' to '" + type + "'");
    }
}
