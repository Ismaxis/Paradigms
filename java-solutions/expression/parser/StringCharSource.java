package expression.parser;

public class StringCharSource implements CharSource {
    private final String string;
    private int pos;
    private int mark;

    public StringCharSource(String string) {
        this.string = string;
    }

    @Override
    public boolean hasNext() {
        return pos < string.length();
    }

    @Override
    public char next() {
        return string.charAt(pos++);
    }

    @Override
    public IllegalArgumentException error(String message) {
        return new IllegalArgumentException(pos + ": " + message);
    }

    @Override
    public int getPos() {
        return pos;
    }

    @Override
    public void mark() {
        mark = pos;
    }

    @Override
    public void reset() {
        pos = mark;
    }

    @Override
    public String getString() {
        return string;
    }
}
