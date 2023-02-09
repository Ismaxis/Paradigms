package expression.exceptions;

import expression.parser.BaseParser;

public class ParserException extends RuntimeException {
    public ParserException(int pos, String expected, char found) {
        super(pos + ": Expected: '" + expected + "', Found: '" + ((found == BaseParser.END) ? "END" : found) + "'");
    }

    public ParserException(int pos, String expected, String found) {
        super(pos + ": Expected: '" + expected + "', Found: '" + found + "'");
    }

    public ParserException(String message) {
        super(message);
    }
}
