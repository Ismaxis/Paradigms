package expression.parser;

import expression.*;
import expression.exceptions.TripleParser;

public class ExpressionParser extends BaseParser implements TripleParser {

    @Override
    public TripleExpression parse(final String expression) {
        source = new StringCharSource(expression);
        take();
        return parseTopLevel();
    }

    private ExpressionToString parseTopLevel() {
        return parseBitOps();
    }

    private ExpressionToString parseBitOps() {
        ExpressionToString left = parseExpression();

        while (true) {
            skipWhitespace();
            if (take("set")) {
                left = new Set(left, parseExpression());
            } else if (take("clear")) {
                left = new Clear(left, parseExpression());
            } else {
                return left;
            }
        }
    }

    private ExpressionToString parseExpression() {
        ExpressionToString left = parseTerm();

        while (true) {
            skipWhitespace();
            if (take('+')) {
                left = new Add(left, parseTerm());
            } else if (take('-')) {
                left = new Subtract(left, parseTerm());
            } else {
                return left;
            }
        }
    }

    private ExpressionToString parseTerm() {
        ExpressionToString left = parseFactor();

        while (true) {
            skipWhitespace();
            if (take('*')) {
                left = new Multiply(left, parseFactor());
            } else if (take('/')) {
                left = new Divide(left, parseFactor());
            } else {
                return left;
            }
        }
    }

    private ExpressionToString parseFactor() {
        skipWhitespace();
        if (take('(')) {
            final ExpressionToString res = parseTopLevel();
            skipWhitespace();
            expect(')');
            return res;
        } else if (take('-')) {
            if (Character.isDigit(pick()) || Character.isAlphabetic(pick())) {
                return parsePrimitive(true);
            } else {
                return parseBrackets(Negate.class);
            }
        } else if (take("count")) {
            return parseBrackets(Count.class);
        } else {
            return parsePrimitive(false);
        }
    }

    private ExpressionToString parseBrackets(Class<?> cl) {
        if (take('(')) {
            final ExpressionToString neg = UnaryOperation.createInstance(cl, parseTopLevel());
            skipWhitespace();
            expect(')');
            return neg;
        } else {
            return UnaryOperation.createInstance(cl, parseFactor());
        }
    }

    private ExpressionToString parsePrimitive(boolean isNegative) {
        skipWhitespace();
        if (Character.isDigit(pick())) {
            return parseConst(isNegative); // :NOTE: переменная начинается с цифры?
        } else if (Character.isAlphabetic(pick())) {
            final ExpressionToString variable = parseVariable();
            return isNegative ? new Negate(variable) : variable;
        } else {
            throw source.error("Variable or constant expexted '" + take() + "' found");
        }
    }

    private ExpressionToString parseVariable() {
        StringBuilder sb = new StringBuilder();
        sb.append(take());
        while (Character.isAlphabetic(pick())) {
            sb.append(take());
        }
        return new Variable(sb.toString());
    }

    private ExpressionToString parseConst(boolean isNegative) {
        StringBuilder sb = new StringBuilder(isNegative ? "-" : "");
        while (Character.isDigit(pick())) {
            sb.append(take());
        }
        return new Const(Integer.parseInt(sb.toString()));
    }
}
