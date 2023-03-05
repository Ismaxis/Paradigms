package expression.generic.parser;

import expression.exceptions.*;
import expression.generic.actualOperations.ActualOperations;
import expression.generic.operations.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ExpressionParser<T> extends BaseParser implements TripleParser<T> {
    ActualOperations<T> actualOperations;

    @Override
    public Expression<T> parse(final ActualOperations<T> actualOperations, final String expression) {
        this.actualOperations = actualOperations;
        source = new StringCharSource(expression);
        take();
        var res = parseTopLevel();
        expectEnd();
        return res;
    }

    private void expectEnd() {
        if (!take(END)) {
            throw new UnexpectedCharacterException(source.getPos(), "END", pick());
        }
    }

    private Expression<T> parseTopLevel() {
        return parseExpression();
    }

//    private Expression<T> parseBitOps() {
//        Expression<T> left = parseExpression();
//
//        while (true) {
//            skipWhitespace();
//            if (take(Set.symbol)) {
//                expectEndOfComplexOperand(Set.symbol);
//                left = new Set(left, parseExpression());
//            } else if (take(Clear.symbol)) {
//                expectEndOfComplexOperand(Clear.symbol);
//                left = new Clear(left, parseExpression());
//            } else {
//                return left;
//            }
//        }
//    }

    private Expression<T> parseExpression() {
        Expression<T> left = parseTerm();

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

    private Expression<T> parseTerm() {
        Expression<T> left = parseFactor();

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

    private Expression<T> parseFactor() {
        skipWhitespace();
        if (take('(')) {
            final Expression<T> res = parseTopLevel();
            skipWhitespace();
            expectCloseBracket();
            return res;
        } else if (take('-')) {
            if (Character.isDigit(pick()) || Character.isAlphabetic(pick())) {
                return parsePrimitive(true);
            } else {
                try {
                    return parseBrackets(Negate.class.getConstructor(Expression.class));
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            return parsePrimitive(false);
        }
    }

    private void expectEndOfComplexOperand(String expected) {
        if (take(END)) {
            throw primitiveStartParseError(END);
        }
        if (!isValidEndOfComplexOperand(pick())) {
            throw new UnknownOperandException(source.getPos(), expected + pick());
        }
    }

    private void expectCloseBracket() {
        if (!take(')')) {
            throw new CloseBracketsException(source.getPos(), pick());
        }
    }

    private Expression<T> parseBrackets(Constructor<? extends Expression> cl) {
        final Expression<T> child;
        if (take('(')) {
            child = parseTopLevel();
            skipWhitespace();
            expectCloseBracket();
        } else {
            child = parseFactor();
        }

        try {
            return cl.newInstance(child);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Expression<T> parsePrimitive(boolean isNegative) {
        Expression<T> primitive;
        skipWhitespace();
        if (Character.isDigit(pick())) {
            primitive = parseConst(isNegative);
        } else if (isVariableStart(pick())) {
            final Expression<T> variable = parseVariable();
            primitive = isNegative ? new Negate<T>(variable) : variable;
        } else {
            throw primitiveStartParseError(pick());
        }
        expectEndOfPrimitive();
        return primitive;
    }

    private void expectEndOfPrimitive() {
        if (!isValidEndOfPrimitive(pick())) {
            throw primitiveEndParseError(pick());
        }
    }

    private ParserException primitiveStartParseError(char found) {
        if (found == END) {
            return new PrematureEndExceptions(source.getPos(), "Variable or Const");
        } else {
            return new PrimitiveExpectedException(source.getPos(), found);
        }
    }

    private UnexpectedCharacterException primitiveEndParseError(char found) {
        return new UnexpectedCharacterException(source.getPos(), "Whitespace or END", found);
    }

    private Expression<T> parseVariable() {
        StringBuilder sb = new StringBuilder();
        sb.append(take());
        while (isVariablePart(pick())) {
            sb.append(take());
        }
        return new Variable<T>(actualOperations, sb.toString());
    }

    private static boolean isVariableStart(char ch) {
        return ch == 'x' || ch == 'y' || ch == 'z';
    }

    private static boolean isVariablePart(char ch) {
        return false;
    }

    private static boolean isValidEndOfComplexOperand(char ch) {
        return Character.isWhitespace(ch) || (ch == '(') || (ch == '-');
    }

    private static boolean isValidEndOfPrimitive(char ch) {
        return Character.isWhitespace(ch) || isOperationSymbol(ch) || (ch == END) || (ch == ')');
    }

    private static boolean isOperationSymbol(char ch) {
        return (ch == '+') || (ch == '-') || (ch == '*') || (ch == '/');
    }

    private Expression<T> parseConst(boolean isNegative) {
        StringBuilder sb = new StringBuilder(isNegative ? "-" : "");
        while (Character.isDigit(pick()) || pick() == '.') {
            sb.append(take());
        }

        Const<T> aConst;
        try {
            aConst = new Const<T>(actualOperations, sb.toString());
        } catch (NumberFormatException e) {
            throw new ParseConstException(sb.toString(), "INT");
        }

        return aConst;
    }
}
