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
    this.operands = operands; // TODO: temp
    this.getOperands = function() {
        return operands;
    }
    this.getEvaluateOperands = this.getOperands;
}
AbstractOperation.prototype.operation = function(...values) {
    return this.operation(...values);
}
AbstractOperation.prototype.evaluate = function(...values) {
    const op = this.getEvaluateOperands(); // TODO: temp
    return this.operation(...op.map(x => x.evaluate(...values)));
}
AbstractOperation.prototype.toString = function() {
    return this.getOperands().join(' ') + " " + this.symbol;
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

function operationFactory(symbol, operation, diffRule, simplifySpecificRules) {
    function Operation (...operands) {
        AbstractOperation.call(this, ...operands);
        this.symbol = symbol; // TODO: temp
    }
    Operation.getArgsCount = function() {
        return operation.length;
    }
    Operation.prototype = Object.create(AbstractOperation.prototype);
    Operation.prototype.constructor = Operation;
    Operation.prototype.symbol = symbol;
    Operation.prototype.operation = operation;
    Operation.prototype.diffRule = diffRule;
    Operation.prototype.simplifySpecificRules = simplifySpecificRules;
    return Operation;
}

const Add = operationFactory('+',
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

const Subtract = operationFactory('-',
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

const Multiply = operationFactory('*',
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

const Divide = operationFactory('/',
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

const createSumSqN = function(argsCount) {
    function SumSqN(...operands) {
        AbstractOperation.call(this, ...operands);
        this._evaluateOperands = operands.map(x => new Square(x));
        this.getEvaluateOperands = () => this._evaluateOperands;
    }
    SumSqN.getArgsCount = function() {
        return argsCount;
    }
    SumSqN.prototype = Object.create(operationFactory( 'sumsq' + argsCount.toString(),
        (...operandValues) => operandValues.reduce((sum, curVal) => sum + curVal),
        (varName, ...operands) => {
            if(operands.length > 1) {
                let curAdd = new Add(operands[0].diff(varName), operands[1].diff(varName));
                for (let i = 2; i < operands.length; i++) {
                    curAdd = new Add(curAdd, operands[i].diff(varName));
                }
                return curAdd;
            } else {
                return operands[0].diff(varName);
            }
        }
    ).prototype);

    SumSqN.prototype.constructor = SumSqN;
    return SumSqN;
}

const Sumsq2 = createSumSqN(2);
const Sumsq3 = createSumSqN(3);
const Sumsq4 = createSumSqN(4);
const Sumsq5 = createSumSqN(5);

const createDistanceN = function(argsCount) {
    function DistanceN(...operands) {
        AbstractOperation.call(this, ...operands);
        this._evaluateOperand = operands.map(x => new Square(x));
        this.getEvaluateOperands = () => this._evaluateOperand;
    }
    DistanceN.getArgsCount = function() {
        return argsCount;
    }
    DistanceN.prototype = Object.create(operationFactory( 'distance' + argsCount.toString(),
        (...operandValues) => Math.sqrt(operandValues.reduce((sum, curVal) => sum + curVal)),
        (varName, ...operands) => {
            let diffSum;
            if (operands.length > 1) {
                diffSum = new Add(operands[0].diff(varName).getOperands()[1], operands[1].diff(varName).getOperands()[1]);
                for (let i = 2; i < operands.length; i++) {
                    diffSum = new Add(diffSum, operands[i].diff(varName).getOperands()[1]);
                }
            } else {
                diffSum = operands[0].diff(varName).getOperands()[1];
            }
            return new Divide(diffSum, new DistanceN(...operands.map(x => x.getOperands()[0])));
        }
    ).prototype);

    DistanceN.prototype.constructor = DistanceN;
    return DistanceN;
}

const Distance2 = createDistanceN(2);
const Distance3 = createDistanceN(3);
const Distance4 = createDistanceN(4);
const Distance5 = createDistanceN(5);


const Square = operationFactory("square",
    a => a * a,
    (varName, operand) => new Multiply(TWO, new Multiply(operand, operand.diff(varName)))
);

const Negate = operationFactory('negate',
    a => -a,
    (varName, operand) => new Negate(operand.diff(varName)));

const literals = { 'x': new Variable('x'), 'y': new Variable('y'), 'z': new Variable('z'), }
const operations = { '+': Add, '-': Subtract, '*': Multiply, '/': Divide, "negate": Negate,
                    "sumsq2": Sumsq2, "sumsq3": Sumsq3, "sumsq4": Sumsq4, "sumsq5": Sumsq5,
                    "distance2": Distance2, "distance3": Distance3, "distance4": Distance4, "distance5": Distance5, }

//:NOTE: reduce usage
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
