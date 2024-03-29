function Const(value) {
    this.value = value;
}

Const.prototype.evaluate = function() {
    return this.value;
};
Const.prototype.toString = function() {
    return this.value.toString();
};
Const.prototype.prefix = Const.prototype.toString;
Const.prototype.postfix = Const.prototype.toString;
Const.prototype.diff = () => ZERO;
Const.prototype.simplify = function() {
    return this;
};

const MINUS_ONE = new Const(-1);
const ZERO = new Const(0);
const ONE = new Const(1);
const TWO = new Const(2);

const isConstantValue = (element, value) => element instanceof Const && element.value === value;
const isZero = element => isConstantValue(element, 0);
const isOne = element => isConstantValue(element, 1);

const variableSymbolsToIndex = {'x': 0, 'y': 1, 'z': 2};

function Variable(symbol) {
    this.symbol = symbol;
    this.index = variableSymbolsToIndex[symbol];
}

Variable.prototype.evaluate = function(...values) {
    return values[this.index];
};
Variable.prototype.toString = function() {
    return this.symbol.toString();
};
Variable.prototype.prefix = Variable.prototype.toString;
Variable.prototype.postfix = Variable.prototype.toString;
Variable.prototype.diff = function(varName) {
    return this.symbol === varName ? ONE : ZERO;
};
Variable.prototype.simplify = function() {
    return this;
};

function AbstractOperation(...operands) {
    this.operands = operands;
    this.evaluateOperands = operands;
}
AbstractOperation.prototype.evaluate = function(...values) {
    return this.operation(...this.evaluateOperands.map(x => x.evaluate(...values)));
};
AbstractOperation.prototype.toString = function() {
    return this.operands.join(' ') + ' ' + this.symbol;
};
AbstractOperation.prototype.prefix = function() {
    return '(' + this.symbol + ' ' + this.operands.map(op => op.prefix()).join(' ') + ')';
};
AbstractOperation.prototype.postfix = function() {
    return '(' + this.operands.map(op => op.postfix()).join(' ') + ' ' + this.symbol + ')';
};
AbstractOperation.prototype.diff = function(varName) {
    const operands = this.operands;
    if (this.diffCoefficients === undefined) {
        this.diffCoefficients = this.getDiffCoefficients(...this.operands);
    }
    const coefs = this.diffCoefficients;
    const res = operands.reduce((sum, curOperand, i) =>
            new Add(new Multiply(coefs.coefficients[i], curOperand.diff(varName)), sum), ZERO);
    return "commonMultiplier" in coefs ? new Multiply(coefs.commonMultiplier, res) :
        "commonDivisor" in coefs ? new Divide(res, coefs.commonDivisor) : res;
};
AbstractOperation.prototype.simplify = function() {
    const operandsSimplified = this.operands.map(operand => operand.simplify());
    return operandsSimplified.every(operand => operand instanceof Const)
        ? new Const(this.operation(...operandsSimplified.map(operand => operand.value)))
        : this.simplifySpecificRules !== undefined
            ? this.simplifySpecificRules(...operandsSimplified)
            : new this.constructor(...operandsSimplified);
};

function createOperation(symbol, operation, diffCoefficients, simplifySpecificRules) {
    function Operation(...operands) {
        AbstractOperation.call(this, ...operands);
    }

    Operation.prototype = Object.create(AbstractOperation.prototype);
    Operation.argsCount = operation.length;
    Operation.isValidArgsCount = count => count === operation.length;
    Operation.prototype.constructor = Operation;
    Operation.prototype.symbol = symbol;
    Operation.prototype.getDiffCoefficients = diffCoefficients;
    Operation.prototype.operation = operation;
    Operation.prototype.simplifySpecificRules = simplifySpecificRules;
    return Operation;
}

const Add = createOperation('+',
    (a, b) => a + b,
    () => ({"coefficients": [ONE, ONE]}),
    (leftSimplified, rightSimplified) =>
        isZero(leftSimplified) ? rightSimplified :
        isZero(rightSimplified) ? leftSimplified :
        new Add(leftSimplified, rightSimplified)
);

const Subtract = createOperation('-',
    (a, b) => a - b,
    () => ({"coefficients": [ONE, MINUS_ONE]}),
    (leftSimplified, rightSimplified) =>
        isZero(leftSimplified) ? new Negate(rightSimplified).simplify() :
            isZero(rightSimplified) ? leftSimplified :
                new Subtract(leftSimplified, rightSimplified)
);

const Multiply = createOperation('*',
    (a, b) => a * b,
    (left, right) => ({"coefficients": [right, left]}),
    (leftSimplified, rightSimplified) =>
        isZero(leftSimplified) || isZero(rightSimplified) ? ZERO :
            isOne(leftSimplified) ? rightSimplified :
                isOne(rightSimplified) ? leftSimplified :
                    new Multiply(leftSimplified, rightSimplified)

);

const Divide = createOperation('/',
    (a, b) => a / b,
    (left, right) => ({"commonDivisor": right, "coefficients": [ONE, new Divide(new Negate(left), right)]}),
    (leftSimplified, rightSimplified) =>
        isZero(leftSimplified) ? ZERO :
            isOne(rightSimplified) ? leftSimplified :
                new Divide(leftSimplified, rightSimplified)
);

const createComplexSumOperation = function(argsCount, symbol, operation, diffCoefficients, simplifySpecificRules) {
    function ComplexOperation (...operands) {
        AbstractOperation.call(this, ...operands);
    }
    ComplexOperation.argsCount = argsCount;
    ComplexOperation.prototype = Object.create(
        createOperation(symbol, operation, diffCoefficients, simplifySpecificRules).prototype);
    ComplexOperation.prototype.constructor = ComplexOperation;
    return ComplexOperation;
};

const sum = operandValues => operandValues.reduce((sum, curVal) => sum + curVal, 0);
const square = operandValues => operandValues.map(x => x * x);
const sumsq = (...operandValues) => sum(square(operandValues));
const createSumSqN = argsCount => createComplexSumOperation(
    argsCount,
    'sumsq' + argsCount,
    sumsq,
    (...operands) => ({commonMultiplier: TWO, coefficients: operands})
);

const [Sumsq2, Sumsq3, Sumsq4, Sumsq5] = [2, 3, 4, 5].map(createSumSqN);

const createDistanceN = argsCount => createComplexSumOperation(
    argsCount,
    'distance' + argsCount,
    (...operandValues) => Math.sqrt(sumsq(...operandValues)),
    function (...operands) {
        return {"commonDivisor": this, "coefficients": operands};
    }
);

const [Distance2, Distance3, Distance4, Distance5] = [2, 3, 4, 5].map(createDistanceN);

const exp = operandValues => operandValues.map(Math.exp);
const sumexp = (...operandValues) => sum(exp(operandValues));
const expDiffCoeffs = operands => operands.map(x => new Sumexp(x));
const Sumexp = createComplexSumOperation(
    Infinity,
    'sumexp',
    sumexp,
    (...operands) => ({"coefficients": expDiffCoeffs(operands)})
);
Sumexp.isValidArgsCount = () => count => count >= 0;

const LSE = createComplexSumOperation(
    Infinity,
    'lse',
    (...operandValues) => Math.log(sumexp(...operandValues)),
    (...operands) => ({"commonDivisor": new Sumexp(...operands), "coefficients": expDiffCoeffs(operands)})
);
LSE.isValidArgsCount = () => count => count > 0;

const Negate = createOperation('negate',
    a => -a,
    () => ({"coefficients": [MINUS_ONE]})
);

const literals = {'x': new Variable('x'), 'y': new Variable('y'), 'z': new Variable('z'),
    '0': ZERO, '1': ONE, '2': TWO, '-1': MINUS_ONE, };
const operations = {
    '+': Add, '-': Subtract, '*': Multiply, '/': Divide, "negate": Negate,
    "sumsq2": Sumsq2, "sumsq3": Sumsq3, "sumsq4": Sumsq4, "sumsq5": Sumsq5,
    "distance2": Distance2, "distance3": Distance3, "distance4": Distance4, "distance5": Distance5,
    "sumexp": Sumexp, "lse": LSE,
};

const parse = str =>
    str.trim()
        .split(/\s+/)
        .reduce(
            (stack, token) => {
                if (token in operations) {
                    const op = operations[token];
                    stack.push(new op(...stack.splice(-op.argsCount)));
                } else if (token in literals) {
                    stack.push(literals[token]);
                } else {
                    stack.push(new Const(parseFloat(token)));
                }
                return stack
            },
            []
        )
        .pop();

function ParserException(index, message) {
    this.message = index + ": " + message;
}

ParserException.prototype = Object.create(Error.prototype);
ParserException.prototype.name = "ParserException";
ParserException.prototype.constructor = ParserException;

function createParserException(name, messageGenerator) {
    function CustomParserException(index, ...args) {
        ParserException.call(this, index, messageGenerator(...args));
    }
    CustomParserException.prototype = Object.create(ParserException.prototype);
    CustomParserException.prototype.name = name;
    CustomParserException.prototype.constructor = CustomParserException;
    return CustomParserException;
}

const PrimitiveExpectedException = createParserException(
    "PrimitiveExpectedException", found => "Primitive expected, '" + found + "' found");

const OpenBracketsExpectedException = createParserException(
    "OpenBracketsExpectedException", found => "'(' expected, '" + found + "' found");

const OperationExpectedException = createParserException(
    "OperationExpectedException", found => "Operation expected, '" + found + "' found");

const OperationAsArgumentException = createParserException(
    "OperationAsArgumentException", found => "Operation: '" + found + "' cannot be an argument");

const PrematureEndException = createParserException(
    "PrematureEndException",
    expected => "Premature end." + (expected ? (" '" + expected + "' expected") : ""));

const WrongArgumentsCountException = createParserException(
    "WrongArgumentsCountException",
    (operation, expected, found) =>
        "Expected arguments for '" + operation + "': " + expected + " found: " + found);

const IndexOutOfBoundException = createParserException(
    "IndexOutOfBoundException",
    (index, str) => "Index '" + index + "' out of bounds for string '" + str + "'");

const isWhitespace = str => /\s/.test(str);

function skipWhitespaces(source) {
    while (source.index < source.str.length && isWhitespace(source.str.charAt(source.index))) {
        source.index++;
    }
}

function parseToken(source) {
    skipWhitespaces(source);
    if (source.str.charAt(source.index) === '(') {
        return '(';
    }
    const startIndex = source.index;
    let curChar = source.str.charAt(source.index);
    while (source.index < source.str.length && curChar !== ')' && curChar !== '(' && !isWhitespace(curChar)) {
        source.index++;
        curChar = source.str.charAt(source.index);
    }
    return source.str.substring(startIndex, source.index);
}

function parsePrimitive(index, token) {
    if (token in literals) {
        return literals[token];
    } else if (isConst(token)) {
        return new Const(parseFloat(token));
    } else {
        throw new PrimitiveExpectedException(index, token);
    }
}
const isConst = str => !isNaN(str) && isFinite(parseFloat(str));

function parseOperand(source, takeOperation) {
    if (!(0 <= source.index && source.index < source.str.length)) {
        throw new IndexOutOfBoundException(source.index, source.index, source.str);
    }
    const firstToken = parseToken(source);
    if (firstToken !== '(') {
        throw new OpenBracketsExpectedException(source.index, firstToken);
    }

    source.index++;
    const parsedTokens = [];
    let isOperationFound = false;
    while (source.index < source.str.length && source.str.charAt(source.index) !== ')') {
        const token = parseToken(source);
        if (token === '(') {
            parsedTokens.push(parseOperand(source, takeOperation));
        } else if (token in operations) {
            if (isOperationFound) {
                throw new OperationAsArgumentException(source.index, token);
            }
            parsedTokens.push(token);
            isOperationFound = true;
        } else {
            parsedTokens.push(parsePrimitive(source.index, token));
        }
        skipWhitespaces(source);
    }
    const operationToken = takeOperation(parsedTokens);
    if (!(operationToken in operations)) {
        throw new OperationExpectedException(source.index, operationToken);
    }
    const operation = operations[operationToken];
    if (source.index >= source.str.length) {
        throw new PrematureEndException(source.index, ")");
    }
    source.index++;
    if (!operation.isValidArgsCount(parsedTokens.length)) {
        throw new WrongArgumentsCountException(source.index,
            operationToken, operation.argsCount, parsedTokens.length);
    }
    return new operation(...parsedTokens);
}

function parseString(str, takeOperation) {
    const source = {str: str, index: 0};
    const firstToken = parseToken(source);
    const res = firstToken === '(' ? parseOperand(source, takeOperation) : parsePrimitive(source.index, firstToken);
    skipWhitespaces(source);
    if (source.index < source.str.length) {
        throw new PrematureEndException(source.index);
    }
    return res;
}

const parsePrefix = str => parseString(str, parsedTokens => parsedTokens.shift());
const parsePostfix = str => parseString(str, parsedTokens => parsedTokens.pop());
