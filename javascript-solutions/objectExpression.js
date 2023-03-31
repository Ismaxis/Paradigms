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
Const.prototype.diff = function() {
    return ZERO;
};
Const.prototype.simplify = function() {
    return this;
};

const MINUS_ONE = new Const(-1);
const ZERO = new Const(0);
const ONE = new Const(1);
const TWO = new Const(2);

function isConstantValue(element, value) {
    return element instanceof Const && element.value === value;
}
const isZero = element => isConstantValue(element, 0);
function isOne(element) {
    return isConstantValue(element, 1);
}

const variableSymbolsToIndex = { 'x': 0, 'y': 1, 'z': 2 };
function Variable(symbol) {
    this.symbol = symbol;
}
Variable.prototype.evaluate = function(...values) {
    return values[variableSymbolsToIndex[this.symbol]];
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
    this.getOperands = () => operands;
    this.getEvaluateOperands = this.getOperands;
}
AbstractOperation.prototype.evaluate = function(...values) {
    return this.operation(...this.getEvaluateOperands().map(x => x.evaluate(...values)));
};
AbstractOperation.prototype.toString = function() {
    return this.getOperands().join(' ') + ' ' + this.symbol;
};
AbstractOperation.prototype.prefix = function() {
    return '(' + this.symbol + ' ' + this.getOperands().map(op => op.prefix()).join(' ') + ')';
};
AbstractOperation.prototype.postfix = function() {
    return '(' + this.getOperands().map(op => op.postfix()).join(' ') + ' ' + this.symbol + ')';
};
AbstractOperation.prototype.diff = function(varName) {
    const operands = this.getOperands();
    const coefficients = this.getDiffCoefficients(...operands);
    return operands.reduce((sum, curOperand, i) =>
        new Add(new Multiply(coefficients[i], curOperand.diff(varName)), sum), ZERO);
};
AbstractOperation.prototype.simplify = function() {
    const operandsSimplified = this.getOperands().map(operand => operand.simplify());
    for (const operandSimplified of operandsSimplified) {
        if (!(operandSimplified instanceof Const)) {
            if (this.simplifySpecificRules !== undefined) {
                return this.simplifySpecificRules(...operandsSimplified);
            } else {
                return new this.constructor(...operandsSimplified);
            }
        }
    }
    return new Const(this.operation(...operandsSimplified.map(operand => operand.value)))
};

function createOperation(symbol, operation, diffCoefficients, simplifySpecificRules) {
    function Operation (...operands) {
        AbstractOperation.call(this, ...operands);
    }
    Operation.getArgsCount = function() {
        return operation.length;
    };
    Operation.prototype = Object.create(AbstractOperation.prototype);
    Operation.prototype.constructor = Operation;
    Operation.prototype.symbol = symbol;
    Operation.prototype.operation = operation;
    Operation.prototype.getDiffCoefficients = diffCoefficients;
    Operation.prototype.simplifySpecificRules = simplifySpecificRules;
    return Operation;
}

const Add = createOperation('+',
    (a, b) => a + b,
    () => [ONE, ONE],
    (leftSimplified, rightSimplified) =>
        isZero(leftSimplified) ? rightSimplified :
        isZero(rightSimplified) ? leftSimplified :
        new Add(leftSimplified, rightSimplified)
);

const Subtract = createOperation('-',
    (a, b) => a - b,
    () => [ONE, MINUS_ONE],
    (leftSimplified, rightSimplified) => {
        if (isZero(leftSimplified)) {
            return new Negate(rightSimplified).simplify();
        } else if (isZero(rightSimplified)) {
            return leftSimplified;
        } else {
            return new Subtract(leftSimplified, rightSimplified);
        }
    }
);

const Multiply = createOperation('*',
    (a, b) => a * b,
    (left, right) => [right, left],
    (leftSimplified, rightSimplified) => {
        if (isZero(leftSimplified) || isZero(rightSimplified)) {
            return ZERO;
        } else if (isOne(leftSimplified)) {
            return rightSimplified;
        } else if (isOne(rightSimplified)) {
            return leftSimplified;
        } else {
            return new Multiply(leftSimplified, rightSimplified);
        }
    }
);

const Divide = createOperation('/',
    (a, b) => a / b,
    (left, right) => [new Divide(ONE, right), new Divide(new Negate(left), new Multiply(right, right))],
    (leftSimplified, rightSimplified) => {
        if (isZero(leftSimplified)) {
            return ZERO;
        } else if (isOne(rightSimplified)) {
            return leftSimplified;
        } else {
            return new Divide(leftSimplified, rightSimplified);
        }
    }
);

function AbstractComplexSumOperation(evaluateOperandsFunc, ...operands) {
    AbstractOperation.call(this, ...operands);
    this._evaluateOperands = evaluateOperandsFunc(operands);
    this.getEvaluateOperands = () => this._evaluateOperands;
}
AbstractComplexSumOperation.prototype = Object.create(AbstractOperation.prototype);

const createComplexSumOperation = function(argsCount, symbol, evalOperands, operation, diffCoefficients, simplifySpecificRules) {
    function ComplexOperation (...operands) {
        AbstractComplexSumOperation.call(this, evalOperands, ...operands);
    }
    ComplexOperation.getArgsCount = function() {
        return argsCount;
    };
    ComplexOperation.prototype = Object.create(
        createOperation(symbol, operation, diffCoefficients, simplifySpecificRules).prototype);
    ComplexOperation.prototype.sum = (...operandValues) => operandValues.reduce((sum, curVal) => sum + curVal);
    ComplexOperation.prototype.constructor = ComplexOperation;
    return ComplexOperation;
};

function createSumSqN(argsCount) {
    return createComplexSumOperation(argsCount, 'sumsq' + argsCount,
        operands => operands.map(x => new Square(x)),
        function(...operandValues) {
            return this.sum(...operandValues);
        },
        (...operands) => operands.map(x => new Multiply(TWO, x)))
}

// :NOTE: const [Sumsq2, Sumsq3] = [2, 3].map(createSumSqN)
const SumsqN = [2, 3, 4, 5].map(createSumSqN);
const [Sumsq2, Sumsq3, Sumsq4, Sumsq5] = SumsqN;
const createDistanceN = function(argsCount) {
    return createComplexSumOperation(argsCount, 'distance' + argsCount.toString(),
        // :NOTE: new type
        operands => [new (SumsqN[operands.length - 2])(...operands)],
        function(...operandValues) {
            return Math.sqrt(this.sum(...operandValues));
        },
        function (...operands) {
            return operands.map(x => new Divide(x, this));
        }
    );
};
const [Distance2, Distance3, Distance4, Distance5] = [2, 3, 4, 5].map(createDistanceN);

const Sumexp = createComplexSumOperation(Infinity, 'sumexp',
    operands => operands.map(x => new Exp(x)),
    function(...operandValues) {
        return  operandValues.length > 0 ? this.sum(...operandValues) : 0;
    },
    function () {
        return this.getEvaluateOperands();
    }
);

const LSE = createComplexSumOperation(Infinity, 'lse',
    operands => [new Sumexp(...operands)],
    function(...operandValues) {
            return operandValues.length > 0 ? Math.log(this.sum(...operandValues)) : 0;
        },
    function (...operands) {
        return operands.map(x => new Divide(new Exp(x), this.getEvaluateOperands()[0]));
    }
);

const Exp = createOperation("exp",
    a => Math.exp(a),
    function() {
        return [this];
    }
);

const Square = createOperation("square",
    a => a * a,
    x => [new Multiply(TWO, x)]
);

const Negate = createOperation('negate',
    a => -a,
    () => [MINUS_ONE]
);

const literals = { 'x': new Variable('x'), 'y': new Variable('y'), 'z': new Variable('z'), };
const operations = { '+': Add, '-': Subtract, '*': Multiply, '/': Divide, "negate": Negate,
                    "sumsq2": Sumsq2, "sumsq3": Sumsq3, "sumsq4": Sumsq4, "sumsq5": Sumsq5,
                    "distance2": Distance2, "distance3": Distance3, "distance4": Distance4, "distance5": Distance5,
                    "sumexp": Sumexp, "lse": LSE, };

// :NOTE: split :FIXED:
const parse = str => str.trim().split(/\s+/).reduce((stack, token) => {
        if (isConst(token)) {
            stack.push(new Const(parseInt(token)));
        } else if (token in literals) {
            stack.push(literals[token]);
        } else if (token in operations) {
            const op = operations[token];
            stack.push(new op(...stack.splice(-op.getArgsCount())));
        }
        return stack
    }, []).pop();

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

const skipWhiteSpaces = source => {
    while(source.index < source.str.length && /\s/.test(source.str.charAt(source.index))) {
        source.index++;
    }
};
const parseToken = source => {
    skipWhiteSpaces(source);
    if (source.str.charAt(source.index) === '(') {
        return '(';
    }
    const startIndex = source.index;
    let curChar = source.str.charAt(source.index);
    while(source.index < source.str.length && curChar !== ')' && curChar !== '(' && !/\s/.test(curChar)) {
        source.index++;
        curChar = source.str.charAt(source.index);
    }
    return source.str.substring(startIndex, source.index);
};
function parsePrimitive(index, token) {
    if (isConst(token)) {
        return new Const(parseInt(token));
    } else if (token in literals) {
        return literals[token];
    } else {
        throw new PrimitiveExpectedException(index, token);
    }
}

function parseOperand(source, takeOperation) {
    if (!(0 <= source.index && source.index < source.str.length)) {
        throw new IndexOutOfBoundException(source.index, source.index, source.str);
    }
    const firstToken = parseToken(source);
    if (firstToken === '(') {
        source.index++;
        const parsedTokens = [];
        let isOperationFound = false;
        while (source.index < source.str.length && source.str.charAt(source.index) !== ')') {
            const token = parseToken(source);
            if (token === '(') {
                parsedTokens.push(parseOperand(source, takeOperation));
            } else if (token in operations) {
                if (!isOperationFound) {
                    parsedTokens.push(token);
                    isOperationFound = true;
                } else {
                    throw new OperationAsArgumentException(source.index, token);
                }
            } else {
                parsedTokens.push(parsePrimitive(source.index, token))
            }
            skipWhiteSpaces(source);
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
        if (parsedTokens.length !== operation.getArgsCount() && operation.getArgsCount() !== Infinity) {
            throw new WrongArgumentsCountException(source.index,
                operationToken, operation.getArgsCount(), parsedTokens.length);
        }
        return new operation(...parsedTokens);
    } else {
        return parsePrimitive(source.index, firstToken);
    }
}
const parsePrefix = str => {
    const source = { str: str, index: 0 };
    const res = parseOperand(source, parsedTokens => parsedTokens.shift());
    // :NOTE: copy-paste
    skipWhiteSpaces(source);
    if (source.index < source.str.length) {
        throw new PrematureEndException(source.index);
    }
    return res;
};

const parsePostfix = str => {
    const source = { str: str, index: 0 };
    const res = parseOperand(source, parsedTokens => parsedTokens.pop());
    skipWhiteSpaces(source);
    if (source.index < source.str.length) {
        throw new PrematureEndException(source.index);
    }
    return res;
};

// :NOTE: 1.1e-1 :FIXED:
const isConst = str => isFinite(str);
