package expression.parser;

public interface CharSource {
    boolean hasNext();

    char next();

    IllegalArgumentException error(String message);

    int getPos();

    void mark();

    void reset();

    String getString();
}
