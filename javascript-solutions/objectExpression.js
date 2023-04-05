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
}

AbstractOperation.prototype.evaluate = function(...values) {
    return this.operation(...this.operands.map(x => x.evaluate(...values)));
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
    return this.diffRule(varName, ...this.operands);
};
AbstractOperation.prototype.simplify = function() {
    const operandsSimplified = this.operands.map(operand => operand.simplify());
    if (operandsSimplified.every(operand => operand instanceof Const)) {
        return new Const(this.operation(...operandsSimplified.map(operand => operand.value)));
    } else if (this.simplifySpecificRules !== undefined) {
        return this.simplifySpecificRules(...operandsSimplified);
    } else {
        return new this.constructor(...operandsSimplified);
    }
};

function createOperation(symbol, operation, diffRule, simplifySpecificRules) {
    function Operation(...operands) {
        AbstractOperation.call(this, ...operands);
    }

    Operation.prototype = Object.create(AbstractOperation.prototype);
    Operation.argsCount = operation.length;
    Operation.isValidArgsCount = count => count === operation.length;
    Operation.prototype.constructor = Operation;
    Operation.prototype.symbol = symbol;
    Operation.prototype.diffRule = diffRule;
    Operation.prototype.operation = operation;
    Operation.prototype.simplifySpecificRules = simplifySpecificRules;
    return Operation;
}

const Add = createOperation('+',
    (a, b) => a + b,
    (varName, left, right) => new Add(left.diff(varName), right.diff(varName)),
    (leftSimplified, rightSimplified) =>
        isZero(leftSimplified) ? rightSimplified :
            isZero(rightSimplified) ? leftSimplified :
                new Add(leftSimplified, rightSimplified)
);

const Subtract = createOperation('-',
    (a, b) => a - b,
    (varName, left, right) => new Subtract(left.diff(varName), right.diff(varName)),
    (leftSimplified, rightSimplified) =>
        isZero(leftSimplified) ? new Negate(rightSimplified).simplify() :
            isZero(rightSimplified) ? leftSimplified :
                new Subtract(leftSimplified, rightSimplified)
);

const Multiply = createOperation('*',
    (a, b) => a * b,
    (varName, left, right) => new Add(
        new Multiply(left.diff(varName), right),
        new Multiply(left, right.diff(varName))),
    (leftSimplified, rightSimplified) =>
        isZero(leftSimplified) || isZero(rightSimplified) ? ZERO :
            isOne(leftSimplified) ? rightSimplified :
                isOne(rightSimplified) ? leftSimplified :
                    new Multiply(leftSimplified, rightSimplified)
);

const Divide = createOperation('/',
    (a, b) => a / b,
    (varName, left, right) => new Add(
        new Divide(left.diff(varName), right),
        new Divide(new Negate(new Multiply(left, right.diff(varName))), new Multiply(right, right))
    ),
    (leftSimplified, rightSimplified) =>
        isZero(leftSimplified) ? ZERO :
            isOne(rightSimplified) ? leftSimplified :
                new Divide(leftSimplified, rightSimplified)
);

const diffOfSum = (varName, operands) => operands.map(operand => new Multiply(operand, operand.diff(varName)))
    .reduce((sum, cur) => new Add(sum, cur))

const createSumSqN = argsCount => {
    const SumSqN = createOperation('sumsq' + argsCount,
        (...operandValues) => operandValues.reduce((sum, curVal) => sum + curVal * curVal, 0),
        (varName, ...operands) => new Multiply(TWO, diffOfSum(varName, operands))
    );
    SumSqN.argsCount = argsCount;
    return SumSqN;
}

const SumsqN = [2, 3, 4, 5].map(createSumSqN);
const [Sumsq2, Sumsq3, Sumsq4, Sumsq5] = SumsqN;

const createDistanceN = argsCount => {
    const distanceN = createOperation('distance' + argsCount,
        (...operandValues) => Math.sqrt(operandValues.reduce((sum, curVal) => sum + curVal * curVal, 0)),
        function (varName, ...operands) {
            return new Divide(diffOfSum(varName, operands), this);
        }
    );
    distanceN.argsCount = argsCount;
    return distanceN;
}

const [Distance2, Distance3, Distance4, Distance5] = [2, 3, 4, 5].map(createDistanceN);

const Sumexp = createOperation('sumexp',
    (...operandValues) => operandValues.reduce((sum, cur) => sum + Math.exp(cur), 0),
    (varName, ...operands) => operands.reduce((sum, operand) => new Add(sum, new Multiply(new Exp(operand), operand.diff(varName))), ZERO)
);
Sumexp.isValidArgsCount = () => count => count >= 0;

const LSE = createOperation('lse',
    (...operandValues) => Math.log(operandValues.reduce((sum, cur) => sum + Math.exp(cur), 0)),
    function(varName, ...operands) {
        const sumInLog = new Sumexp(...operands);
        return new Divide(sumInLog.diff(varName), sumInLog);
    }
);
LSE.isValidArgsCount = () => count => count > 0;

const Exp = createOperation("exp",
    a => Math.exp(a),
    function(varName, operand)  {
        return new Multiply(operand.diff(varName), this);
    }
);

const Negate = createOperation('negate',
    a => -a,
    (varName, operand) => new Negate(operand.diff(varName))
);
const literals = {'x': new Variable('x'), 'y': new Variable('y'), 'z': new Variable('z'),
                  '0': ZERO, '1': ONE, '2': TWO, '-1': MINUS_ONE, };
const operations = {
    '+': Add, '-': Subtract, '*': Multiply, '/': Divide, "negate": Negate,
    "sumsq2": Sumsq2, "sumsq3": Sumsq3, "sumsq4": Sumsq4, "sumsq5": Sumsq5,
    "distance2": Distance2, "distance3": Distance3, "distance4": Distance4, "distance5": Distance5,
    "sumexp": Sumexp, "lse": LSE,
};

const parse = str => str.trim().split(/\s+/).reduce((stack, token) => {
    if (token in operations) {
        const op = operations[token];
        stack.push(new op(...stack.splice(-op.argsCount)));
    } else if (token in literals) {
        stack.push(literals[token]);
    } else {
        stack.push(new Const(parseFloat(token)));
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

const isWhitespace = str => /\s/.test(str)

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
        return new Const(parseInt(token));
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
