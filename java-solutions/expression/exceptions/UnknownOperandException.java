package expression.exceptions;

public class UnknownOperandException extends ParserException {
    public UnknownOperandException(int pos, String unknownOperand) {
        super(pos + ": Unknown operand found '" + unknownOperand + "'");
    }
}
