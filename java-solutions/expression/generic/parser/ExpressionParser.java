package expression.generic.parser;

import expression.exceptions.*;
import expression.generic.actualOperations.ActualOperations;
import expression.generic.operations.*;
import expression.parser.BaseParser;
import expression.parser.StringCharSource;

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
//
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
                left = new Add<>(left, parseTerm());
            } else if (take('-')) {
                left = new Subtract<>(left, parseTerm());
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
                left = new Multiply<>(left, parseFactor());
            } else if (take('/')) {
                left = new Divide<>(left, parseFactor());
            } else if (take("mod")) {
              return new Mod<>(left, parseFactor());
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
            if (isStartOfConst(pick()) || Character.isAlphabetic(pick())) {
                return parsePrimitive(true);
            } else {
                return new Negate<>(parseBrackets());
            }
        } else if (take("abs")) {
            return new Abs<>(parseBrackets());
        } else if (take("square")) {
            return new Square<>(parseBrackets());
        } else {
            return parsePrimitive(false);
        }
    }

    private void expectCloseBracket() {
        if (!take(')')) {
            throw new CloseBracketsException(source.getPos(), pick());
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

    private Expression<T> parseBrackets() {
        final Expression<T> expression;
        if (take('(')) {
            expression = parseTopLevel();
            skipWhitespace();
            expectCloseBracket();
        } else {
            expression = parseFactor();
        }
        return expression;
    }

    private Expression<T> parsePrimitive(boolean isNegative) {
        final Expression<T> primitive;
        skipWhitespace();
        if (isStartOfVariable(pick())) {
            final Expression<T> variable = parseVariable();
            primitive = isNegative ? new Negate<T>(variable) : variable;
        } else if (isStartOfConst(pick())) {
            primitive = parseConst(isNegative);
        }  else {
            throw primitiveStartParseError(pick());
        }
        expectEndOfPrimitive();
        return primitive;
    }
    private ParserException primitiveStartParseError(char found) {
        if (found == END) {
            return new PrematureEndExceptions(source.getPos(), "Variable or Const");
        } else {
            return new PrimitiveExpectedException(source.getPos(), found);
        }
    }
    private void expectEndOfPrimitive() {
        if (!isValidEndOfPrimitive(pick())) {
            throw new UnexpectedCharacterException(source.getPos(), "Whitespace or END", pick());
        }
    }

    private Expression<T> parseVariable() {
        StringBuilder sb = new StringBuilder();
        sb.append(take());
        while (isPartOfVariable(pick())) {
            sb.append(take());
        }
        return new Variable<T>(actualOperations, sb.toString());
    }
    private Expression<T> parseConst(boolean isNegative) {
        StringBuilder sb = new StringBuilder(isNegative ? "-" : "");
        while (isPartOfConst(pick())) {
            sb.append(take());
        }

        try {
            return new Const<>(actualOperations, sb.toString());
        } catch (NumberFormatException e) {
            throw new ParseConstException(sb.toString(), actualOperations.getOperationTypeName());
        }
    }

    private static boolean isStartOfVariable(char ch) {
        return ch == 'x' || ch == 'y' || ch == 'z';
    }
    private static boolean isPartOfVariable(char ch) {
        return false;
    }
    private boolean isStartOfConst(char ch) {
        return actualOperations.isStartOfConst(ch);
    }
    private boolean isPartOfConst(char ch) {
        return ch != END && ch != ')' && !Character.isWhitespace(ch);
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
}
