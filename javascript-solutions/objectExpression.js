function Const(value) {
    this.value = value;
}
Const.prototype.evaluate = function() {
    return this.value;
}
Const.prototype.toString = function() {
    return this.value.toString();
}
Const.prototype.diff = function() {
    return ZERO;
}
Const.prototype.simplify = function() {
    return this;
}

const ZERO = new Const(0);
const ONE = new Const(1);
const TWO = new Const(2);

function isConstantValue(element, value) {
    return element instanceof Const && element.value === value;
}
function isZero(element) {
    return isConstantValue(element, 0);
}
function isOne(element) {
    return isConstantValue(element, 1);
}

const variableSymbolsToIndex = { 'x': 0, 'y': 1, 'z': 2 };
function Variable(symbol) {
    this.symbol = symbol;
}
Variable.prototype.evaluate = function(...values) {
    return values[variableSymbolsToIndex[this.symbol]];
}
Variable.prototype.toString = function() {
    return this.symbol.toString();
}
Variable.prototype.diff = function(varName) {
    return this.symbol === varName ? ONE : ZERO;
}
Variable.prototype.simplify = function() {
    return this;
}

function AbstractOperation(...operands) {
    this.getOperands = () => operands;
    this.getEvaluateOperands = this.getOperands;
}
AbstractOperation.prototype.evaluate = function(...values) {
    return this.operation(...this.getEvaluateOperands().map(x => x.evaluate(...values)));
}
AbstractOperation.prototype._toString = function(symbol) {
    return this.getOperands().join(' ') + ' ' + symbol;
}
AbstractOperation.prototype.diff = function(varName) {
    return this.diffRule(varName, ...this.getEvaluateOperands());
}
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
}

function createOperation(symbol, operation, diffRule, simplifySpecificRules) {
    function Operation (...operands) {
        AbstractOperation.call(this, ...operands);
    }
    Operation.getArgsCount = function() {
        return operation.length;
    }
    Operation.prototype = Object.create(AbstractOperation.prototype);
    Operation.prototype.constructor = Operation;
    Operation.prototype.toString = function() {
        return this._toString(symbol);
    }
    Operation.prototype.operation = operation;
    Operation.prototype.diffRule = diffRule;
    Operation.prototype.simplifySpecificRules = simplifySpecificRules;
    return Operation;
}

const Add = createOperation('+',
    (a, b) => a + b,
    (varName, left, right) => new Add(left.diff(varName), right.diff(varName)),
    function(leftSimplified, rightSimplified) {
        if (isZero(leftSimplified)) {
            return rightSimplified;
        } else if (isZero(rightSimplified)) {
            return leftSimplified;
        } else {
            return new Add(leftSimplified, rightSimplified);
        }
    }
);

const Subtract = createOperation('-',
    (a, b) => a - b,
    (varName, left, right) => new Subtract(left.diff(varName), right.diff(varName)),
    function(leftSimplified, rightSimplified) {
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
    (varName, left, right) => new Add(
        new Multiply(left.diff(varName), right),
        new Multiply(left, right.diff(varName))),
    function(leftSimplified, rightSimplified) {
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
    (varName, left, right) => new Add(
        new Divide(left.diff(varName), right),
        new Divide(new Negate(new Multiply(left, right.diff(varName))), new Multiply(right, right))
    ),
    function(leftSimplified, rightSimplified) {
        if (isZero(leftSimplified)) {
            return ZERO;
        } else if (isOne(rightSimplified)) {
            return leftSimplified;
        } else {
            return new Divide(leftSimplified, rightSimplified);
        }
    }
);

function AbstractComplexOperation(evaluateOperandsFunc, ...operands) {
    AbstractOperation.call(this, ...operands);
    this._evaluateOperands = evaluateOperandsFunc(operands);
    this.getEvaluateOperands = () => this._evaluateOperands;
}
AbstractComplexOperation.prototype = Object.create(AbstractOperation.prototype);

const createComplexOperation = function(argsCount, symbol, evalOperands, operation, diffRule, simplifySpecificRules) {
    function ComplexOperation (...operands) {
        AbstractComplexOperation.call(this, evalOperands, ...operands);
    }
    ComplexOperation.getArgsCount = function() {
        return argsCount;
    }
    ComplexOperation.prototype = Object.create(
        createOperation(symbol, operation, diffRule, simplifySpecificRules).prototype);
    ComplexOperation.prototype.constructor = ComplexOperation;
    return ComplexOperation;
}

const createSumSqN = function(argsCount) {
    return createComplexOperation(argsCount, 'sumsq' + argsCount.toString(),
        operands => operands.map(x => new Square(x)),
        (...operandValues) => operandValues.reduce((sum, curVal) => sum + curVal),
        (varName, ...operands) =>
            operands.map(x => x.diff(varName)).reduce((addTree, cur) => new Add(addTree, cur)));
}

const Sumsq2 = createSumSqN(2);
const Sumsq3 = createSumSqN(3);
const Sumsq4 = createSumSqN(4);
const Sumsq5 = createSumSqN(5);

const createDistanceN = function(argsCount) {
    const DistanceN = createComplexOperation(argsCount, 'distance' + argsCount.toString(),
        operands => [new (createSumSqN(operands.length))(...operands)],
        (...operandValues) => Math.sqrt(operandValues.reduce((sum, curVal) => sum + curVal)),
        (varName, operand) => new Divide(
            operand.getOperands()
                .map(x => new Multiply(x, x.diff(varName)))
                .reduce((addTree, cur) => new Add(addTree, cur)),
            new DistanceN(...operand.getOperands())
        ))
    return DistanceN;
}

const Distance2 = createDistanceN(2);
const Distance3 = createDistanceN(3);
const Distance4 = createDistanceN(4);
const Distance5 = createDistanceN(5);

const Square = createOperation("square",
    a => a * a,
    (varName, operand) => new Multiply(TWO, new Multiply(operand, operand.diff(varName)))
);

const Negate = createOperation('negate',
    a => -a,
    (varName, operand) => new Negate(operand.diff(varName))
);

const literals = { 'x': new Variable('x'), 'y': new Variable('y'), 'z': new Variable('z'), }
const operations = { '+': Add, '-': Subtract, '*': Multiply, '/': Divide, "negate": Negate,
                    "sumsq2": Sumsq2, "sumsq3": Sumsq3, "sumsq4": Sumsq4, "sumsq5": Sumsq5,
                    "distance2": Distance2, "distance3": Distance3, "distance4": Distance4, "distance5": Distance5, }

const parse = str => str.split(' ').filter(token => token.length > 0).reduce((stack, token) => {
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
const isConst = str => /^-?\d+$/.test(str);

const exp = new Distance2(new Const(2), new Variable('y'));
console.log(exp.toString());
const expDif = exp.diff('y');
console.log(expDif.toString());
const expSimp = expDif.simplify();
console.log(expSimp.toString());
console.log(expSimp.evaluate(2,2,2));
