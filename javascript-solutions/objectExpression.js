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
    return new Const(0);
}
Const.prototype.simplify = function() {
    return this;
}
Const.prototype.getFactors = function() {
    return [this];
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

function isConstantValue(element, value) {
    return element instanceof Const && element.value === value;
}
function isZero(element) {
    return isConstantValue(element, 0);
}
function isOne(element) {
    return isConstantValue(element, 1);
}

function operationFactory(symbol, operation, diffRule) {
    function AbstractOperation(...children) {
        this.children = children;
        this.symbol = symbol; // TODO: temp
    }
    AbstractOperation.getArgsCount = function() {
        return operation.length;
    }
    AbstractOperation.prototype.operation = function(...values) {
        return operation(...values);
    }
    AbstractOperation.prototype.evaluate = function(...values) {
        return this.operation(...this.children.map(x => x.evaluate(...values)));
    }
    AbstractOperation.prototype.toString = function() {
        return this.children.join(' ') + " " + symbol;
    }
    AbstractOperation.prototype.diff = function(varName) {
        return diffRule(varName, ...this.children);
    }
    AbstractOperation.prototype.simplify = function() {
        const childrenSimplified = this.children.map(child => child.simplify());
        for (const childSimplified of childrenSimplified) {
            if (!(childSimplified instanceof Const)) {
                if (this.simplifySpecificRules !== undefined) {
                    return this.simplifySpecificRules(...childrenSimplified);
                } else {
                    return new this.constructor(...childrenSimplified);
                }
            }
        }
        return new Const(this.operation(...childrenSimplified.map(child => child.value)))
    }

    AbstractOperation.prototype.getFactors = function() {
        return [new this.constructor(...this.children)];
    }

    return AbstractOperation;
}

const Add = operationFactory('+',
    (a, b) => a + b,
    (varName, left, right) => new Add(left.diff(varName), right.diff(varName)));
Add.prototype.simplifySpecificRules = function(leftSimplified, rightSimplified) {
    if (isZero(leftSimplified)) {
        return rightSimplified;
    } else if (isZero(rightSimplified)) {
        return leftSimplified;
    } else {
        return new Add(leftSimplified, rightSimplified);
    }
}

const Subtract = operationFactory('-',
    (a, b) => a - b,
    (varName, left, right) => new Subtract(left.diff(varName), right.diff(varName)));
Subtract.prototype.simplifySpecificRules = function(leftSimplified, rightSimplified) {
    if (isZero(leftSimplified)) {
        return new Negate(rightSimplified).simplify();
    } else if (isZero(rightSimplified)) {
        return leftSimplified;
    } else {
        return new Subtract(leftSimplified, rightSimplified);
    }
}

const Multiply = operationFactory('*',
    (a, b) => a * b,
    (varName, left, right) => new Add(
        new Multiply(left.diff(varName), right),
        new Multiply(left, right.diff(varName))));

Multiply.prototype.simplifySpecificRules = function(leftSimplified, rightSimplified) {
    if (isZero(leftSimplified) || isZero(rightSimplified)) {
        return new Const(0);
    } else if (isOne(leftSimplified)) {
        return rightSimplified;
    } else if (isOne(rightSimplified)) {
        return leftSimplified;
    } else {
        return new Multiply(leftSimplified, rightSimplified);
    }
}
Multiply.prototype.getFactorsImpl = function(arr, elem) {
    if (elem instanceof Multiply) {
        arr.push(...elem.getFactors());
    } else if (elem instanceof Divide) {
        arr.push(...elem.children[0].getFactors());
        const denominatorStraighten = elem.children[1].getFactors();
        for (const element of denominatorStraighten) {
            arr.push(new Divide(new Const(1), element));
        }
    } else {
        arr.push(elem);
    }
}
Multiply.prototype.getFactors = function() {
    const arr = [];
    this.getFactorsImpl(arr, this.children[0]);
    this.getFactorsImpl(arr, this.children[1]);
    return arr;
}

const Divide = operationFactory('/',
    (a, b) => a / b,
    (varName, left, right) => new Divide(
        new Subtract(
            new Multiply(left.diff(varName), right),
            new Multiply(left, right.diff(varName))),
        new Multiply(right, right)));
Divide.prototype.buildTreeFromFactors = function(factors) {
    if (factors.length > 1) {
        let curMul = new Multiply(factors[0], factors[1]);
        for (let i = 2; i < factors.length; i++) {
            curMul = new Multiply(curMul, factors[i]);
        }
        return curMul;
    } else if (factors.length === 1) {
        return factors[0]
    } else {
        return new Const(1);
    }
}
Divide.prototype.simplifySpecificRules = function(leftSimplified, rightSimplified) {
    if (isZero(leftSimplified)) {
        return new Const(0);
    } else if (isOne(rightSimplified)) {
        return leftSimplified;
    } else {
        const leftFactors = leftSimplified.getFactors();
        const rightFactors = rightSimplified.getFactors();
        // reduce common multipliers
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
        // build multiply tree back
        const leftTree = this.buildTreeFromFactors(leftFactors);
        const rightTree = this.buildTreeFromFactors(rightFactors);

        return new Divide(leftTree, rightTree);
    }
}

const Negate = operationFactory('negate',
    a => -a,
    (varName, child) =>new Negate(child.diff(varName)));

const literals = { 'x': new Variable('x'), 'y': new Variable('y'), 'z': new Variable('z'), }
const operations = { '+': Add, '-': Subtract, '*': Multiply, '/': Divide, "negate": Negate, }
const parse = str => {
    let stack = [];
    let tokens = str.split(' ').filter(token => token.length > 0);
    tokens.reduce((stack, token) => {
        if (isConst(token)) {
            stack.push(new Const(parseInt(token)));
        } else if (token in literals) {
            stack.push(literals[token]);
        } else if (token in operations) {
            const op = operations[token];
            stack.push(new op(...stack.splice(-op.getArgsCount())));
        }
        return stack
    }, stack)
    return stack.pop();
}
const isConst = str => /^-?\d+$/.test(str);

const exp =new Divide(new Variable('x'), new Multiply(new Variable('y'), new Variable('z'))).diff('x');
// const exp = parse("x 2 x * -");
const expSimp = exp.simplify();
console.log(exp.toString());
console.log(expSimp.toString());
