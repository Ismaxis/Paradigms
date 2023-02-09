package expression.parser;

public class BaseParser { // :NOTE: почему он не абсрактный?
    public static final char END = 0; // :NOTE: почему не паблик
    protected CharSource source;
    private char ch;

    protected char take() {
        final char result = ch;
        ch = source.hasNext() ? source.next() : END;
        return result;
    }

    protected boolean take(char expected) {
        if (ch == expected) {
            take();
            return true;
        } else {
            return false;
        }
    }

    protected boolean take(String s) {
        source.mark();
        char markedCh = ch;

        for (int i = 0; i < s.length(); i++) {
            if (!take(s.charAt(i))) {
                source.reset();
                ch = markedCh;
                return false;
            }
        }

        return true;
    }

    protected char pick() {
        return ch;
    }

    protected void expect(char expected) {
        if (!take(expected)) {
            throw source.error(String.format("Expected: '%s', Found: '%s'", displayIfEnd(expected), displayIfEnd(take())));
        }
    }

    private Object displayIfEnd(char ch) {
        return (ch == END) ? "END" : ch;
    }

    protected void skipWhitespace() {
        while (Character.isWhitespace(pick())) {
            take();
        }
    }
}
