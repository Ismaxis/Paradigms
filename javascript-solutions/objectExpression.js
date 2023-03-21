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
Const.prototype.getFactors = function() {
    return [this];
}

const ZERO = new Const(0);
const ONE = new Const(1);

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
    return new Const(this.symbol === varName ? 1 : 0);
}
Variable.prototype.simplify = function() {
    return this;
}
Variable.prototype.getFactors = function() {
    return [this];
}

function AbstractOperation(...operands) {
    this.getOperands = function() {
        return operands;
    }
}
AbstractOperation.prototype.getOperand = function(i) {
    return this.getOperands()[i];
}
AbstractOperation.prototype.operation = function(...values) {
    return this.operation(...values);
}
AbstractOperation.prototype.evaluate = function(...values) {
    return this.operation(...this.getOperands().map(x => x.evaluate(...values)));
}
AbstractOperation.prototype.toString = function() {
    return this.getOperands().join(' ') + " " + this.symbol;
}
AbstractOperation.prototype.diff = function(varName) {
    return this.diffRule(varName, ...this.getOperands());
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
AbstractOperation.prototype.getFactors = function() {
    return [new this.constructor(...this.getOperands())];
}

function operationFactory(symbol, operation, diffRule, simplifySpecificRules) {
    function Operation (...operands) {
        AbstractOperation.call(this, ...operands);
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
function getFactorsRecur(arr, elem) {
    if (elem instanceof Multiply) {
        arr.push(...elem.getFactors());
    } else if (elem instanceof Divide) {
        arr.push(...elem.getOperand(0).getFactors());
        const denominatorStraighten = elem.getOperand(1).getFactors();
        for (const element of denominatorStraighten) {
            arr.push(new Divide(ONE, element));
        }
    } else {
        arr.push(elem);
    }
}
Multiply.prototype.getFactors = function() {
    const arr = [];
    getFactorsRecur(arr, this.getOperand(0));
    getFactorsRecur(arr, this.getOperand(1));
    return arr;
}

function buildTreeFromFactors(factors) {
    if (factors.length > 1) {
        let curMul = new Multiply(factors[0], factors[1]);
        for (let i = 2; i < factors.length; i++) {
            curMul = new Multiply(curMul, factors[i]);
        }
        return curMul;
    } else if (factors.length === 1) {
        return factors[0]
    } else {
        return ONE;
    }
}

const Divide = operationFactory('/',
    (a, b) => a / b,
    (varName, left, right) => new Divide(
        new Subtract(
            new Multiply(left.diff(varName), right),
            new Multiply(left, right.diff(varName))),
        new Multiply(right, right)),
    function(leftSimplified, rightSimplified) {
        if (isZero(leftSimplified)) {
            return ZERO;
        } else if (isOne(rightSimplified)) {
            return leftSimplified;
        } else {
            const leftFactors = leftSimplified.getFactors();
            const rightFactors = rightSimplified.getFactors();
            for (let i = 0; i < leftFactors.length; i++) {
                const leftFactor = leftFactors[i];
                for (let j = 0; j < rightFactors.length; j++) {
                    const rightFactor = rightFactors[j];
                    if (leftFactor === rightFactor) {
                        leftFactors.splice(i, 1);
                        rightFactors.splice(j, 1);
                        i--;
                        break;
                    }
                }
            }
            const leftTree = buildTreeFromFactors(leftFactors);
            const rightTree = buildTreeFromFactors(rightFactors);
            return new Divide(leftTree, rightTree);
        }
    }
);

const createSumSqN = function(argsCount) {
    const SumSqN = operationFactory( 'sumsq' + argsCount.toString(),
        (...operandValues) => operandValues.map(x => x * x).reduce((sum, curVal) => sum + curVal),
        (varName, ...operands) => {
            if(operands.length > 1) {
                let curAdd = new Add(new Square(operands[0]).diff(varName), new Square(operands[1]).diff(varName));
                for (let i = 2; i < operands.length; i++) {
                    curAdd = new Add(curAdd, new Square(operands[i]).diff(varName));
                }
                return curAdd;
            } else {
                return operands[0].diff(varName);
            }
        }
    )
    SumSqN.getArgsCount = function() {
        return argsCount;
    }
    return SumSqN;
}

const Sumsq2 = createSumSqN(2);
const Sumsq3 = createSumSqN(3);
const Sumsq4 = createSumSqN(4);
const Sumsq5 = createSumSqN(5);

const createDistanceN = function(argsCount) {
    const SumSqN = operationFactory( 'distance' + argsCount.toString(),
        (...operandValues) => Math.sqrt(operandValues.map(x => x * x).reduce((sum, curVal) => sum + curVal)),
        (varName, ...operands) => {
            if(operands.length > 1) {
                let curAdd = new Add(new Square(operands[0]), new Square(operands[1]));
                for (let i = 2; i < operands.length; i++) {
                    curAdd = new Add(curAdd, new Square(operands[i]));
                }
                return new Sqrt(curAdd).diff(varName);
            } else {
                return operands[0].diff(varName);
            }
        }
    )
    SumSqN.getArgsCount = function() {
        return argsCount;
    }
    return SumSqN;
}

const Distance2 = createDistanceN(2);
const Distance3 = createDistanceN(3);
const Distance4 = createDistanceN(4);
const Distance5 = createDistanceN(5);

const TWO = new Const(2);
const Square = operationFactory("",
    a => a * a,
    (varName, operand) => new Multiply(new Multiply(TWO, operand), operand.diff(varName))
)

const Sqrt = operationFactory("",
    a => Math.sqrt(a),
    (varName, operand) => new Divide(operand.diff(varName), new Multiply(TWO, new Sqrt(operand)))
)

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
const expDif = exp.diff('y');
console.log(exp.toString());
console.log(expDif.toString());
console.log(exp.evaluate(2,2,2));
console.log(expDif.evaluate(2,2,2));
// console.log(expSimp.toString());
