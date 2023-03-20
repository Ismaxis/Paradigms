function Const(value) {
    this.value = value;
}
Const.prototype.evaluate = function() {
    return this.value;
}
Const.prototype.toString = function() {
    return this.value.toString();
}
Const.prototype.equals = function(that) {
    return that instanceof Const && this.value === that.value;
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
Variable.prototype.equals = function(that) {
    return that instanceof Variable && this.symbol === that.symbol;
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

function operationFabric(symbol, operation, diffRule) {
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
        const chEval = this.children.map(x => x.evaluate(...values))
        return this.operation(...chEval);
    }
    AbstractOperation.prototype.toString = function() {
        return this.children.join(' ') + " " + symbol;
    }
    AbstractOperation.prototype.diff = function(varName) {
        return diffRule(varName, ...this.children);
    }
    AbstractOperation.prototype.simplify = function() {
        const leftSimplified = this.children[0].simplify();
        const rightSimplified = this.children[1].simplify();
        // Common rules
        if (leftSimplified instanceof Const && rightSimplified instanceof Const) {
            return new Const(this.operation(leftSimplified.value, rightSimplified.value))
        } else {
            return this.simplifySpecificRules(leftSimplified, rightSimplified);
        }
    }
    AbstractOperation.prototype.getFactors = function() {
        return [new this.constructor(...this.children)];
    }

    return AbstractOperation;
}

const Pow = operationFabric("pow",
    (base, power) => Math.pow(base, power),
    (varName, base, power) => new Multiply(
        new Pow(base, power),
        new Add(
            new Divide(
                new Multiply(
                    power,
                    base.diff(varName)
                ),
                base
            ),
            new Multiply(
                new Log(new Const(Math.E), base),
                power.diff(varName)
            )
        )
    ));
Pow.prototype.simplifySpecificRules = function(leftSimplified, rightSimplified) {
    if (leftSimplified instanceof Const && leftSimplified.value === 1 || leftSimplified.value === 0) {
        return new Const(leftSimplified.value);
    } else if (rightSimplified instanceof Const && leftSimplified.value === 0) {
        return new Const(1);
    } else if (rightSimplified instanceof Const && leftSimplified.value === 1) {
        return leftSimplified;
    } else {
        return new Pow(leftSimplified, rightSimplified);
    }
}

const Log = operationFabric("log",
    (base, x) => Math.log(Math.abs(x)) / Math.log(Math.abs(base)),
    (varName, base, x) => {
    const absX = new Abs(x);
    const absBase = new Abs(base);
    return new Divide(
        new Subtract(
            new Multiply(
                new Divide(
                    absX.diff(varName),
                    absX
                ),
                new Log(new Const(Math.E), absBase)
            ),
            new Multiply(
                new Divide(
                    absBase.diff(varName),
                    absBase
                ),
                new Log(new Const(Math.E), absX)
            )
        ),
        new Multiply(
            new Log(new Const(Math.E), absBase),
            new Log(new Const(Math.E), absBase)
        )
    )
});
Log.prototype.simplifySpecificRules = function(leftSimplified, rightSimplified) {
    if (rightSimplified instanceof Const && rightSimplified.value === 1) {
        return new Const(0);
    } else {
        return new Log(leftSimplified, rightSimplified);
    }
}

const Add = operationFabric('+',
    (a, b) => a + b,
    (varName, left, right) => new Add(left.diff(varName), right.diff(varName)));
Add.prototype.simplifySpecificRules = function(leftSimplified, rightSimplified) {
    if (leftSimplified instanceof Const && leftSimplified.value === 0) {
        return rightSimplified;
    } else if (rightSimplified instanceof Const && rightSimplified.value === 0) {
        return leftSimplified;
    } else {
        return new Add(leftSimplified, rightSimplified);
    }
}

const Subtract = operationFabric('-',
    (a, b) => a - b,
    (varName, left, right) => new Subtract(left.diff(varName), right.diff(varName)));
Subtract.prototype.simplifySpecificRules = function(leftSimplified, rightSimplified) {
    if (leftSimplified instanceof Const && leftSimplified.value === 0) {
        return new Negate(rightSimplified).simplify();
    } else if (rightSimplified instanceof Const && rightSimplified.value === 0) {
        return leftSimplified;
    } else {
        return new Subtract(leftSimplified, rightSimplified);
    }
}

const Multiply = operationFabric('*',
    (a, b) => a * b,
    (varName, left, right) => new Add(
        new Multiply(left.diff(varName), right),
        new Multiply(left, right.diff(varName))));
Multiply.prototype.simplifySpecificRules = function(leftSimplified, rightSimplified) {
    if (leftSimplified instanceof Const && leftSimplified.value === 0 ||
        rightSimplified instanceof Const && rightSimplified.value === 0) {
        return new Const(0);
    } else if (leftSimplified instanceof Const && leftSimplified.value === 1) {
        return rightSimplified;
    } else if (rightSimplified instanceof Const && rightSimplified.value === 1) {
        return leftSimplified;
    } else if (leftSimplified instanceof Pow) {
        const powBase = leftSimplified.children[0];
        const rightFactors = rightSimplified.getFactors();
        let power = 0;
        for (let i = 0; i < rightFactors.length; i++) {
            if (rightFactors[i] === powBase) {
                rightFactors.splice(i, 1);
                power++;
                i--;
            }
        }
        const rightTree = buildTreeFromFactors(rightFactors);
        if (power > 0) {
            return new Multiply(
                new Pow(leftSimplified.children[0], new Add(leftSimplified.children[1], new Add(power))), rightTree);
        }
    }
    return new Multiply(leftSimplified, rightSimplified);
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
        return new Const(1);
    }
}

const Divide = operationFabric('/',
    (a, b) => a / b,
    (varName, left, right) => new Divide(
        new Subtract(
            new Multiply(left.diff(varName), right),
            new Multiply(left, right.diff(varName))),
        new Multiply(right, right)));
Divide.prototype.simplifySpecificRules = function(leftSimplified, rightSimplified) {
    if (leftSimplified instanceof Const && leftSimplified.value === 0) {
        return new Const(0);
    // } else if (rightSimplified instanceof Const && rightSimplified.value === 0) {
    //     return new Const(leftSimplified > 0 ? 1 : -1 * Infinity);
    } else if (rightSimplified instanceof Const && rightSimplified.value === 1) {
        return leftSimplified;
    } else {
        const leftFactors = leftSimplified.getFactors();
        const rightFactors = rightSimplified.getFactors();
        // reduce common multipliers
        for (let i = 0; i < leftFactors.length; i++) {
            const leftFactor = leftFactors[i];
            for (let j = 0; j < rightFactors.length; j++) {
                const rightFactor = rightFactors[j];
                if (leftFactor === rightFactor) {//} || leftFactor.equals(rightFactor)) {
                    leftFactors.splice(i, 1);
                    rightFactors.splice(j, 1);
                    i--;
                    break;
                }
            }
        }
        // build multiply tree back
        const leftTree = buildTreeFromFactors(leftFactors);
        const rightTree = buildTreeFromFactors(rightFactors);

        return new Divide(leftTree, rightTree);
    }
}

const Negate = operationFabric("negate",
    a => -a,
    (varName, child) => new Negate(child.diff(varName)));
Negate.prototype.simplify = function() {
    const childSimplified = this.children[0].simplify();
    if (childSimplified instanceof Const) {
        return new Const(this.operation(childSimplified.value))
    } else {
        return new Negate(childSimplified);
    }
}

const Abs = operationFabric("abs",
    a => Math.abs(a),
    (varName, child) => new Multiply(
        new Sgn(child),
        child.diff(varName)
    )
)
Abs.prototype.simplify = function() {
    const childSimplified = this.children[0].simplify();
    if (childSimplified instanceof Const) {
        return new Const(this.operation(childSimplified.value))
    } else {
        return new Abs(childSimplified);
    }
}

const Sgn = operationFabric("sgn",
    a => a === 0 ? 0 : (a > 0 ? 1 : -1),
    (varName, child) => new Const(0)
)
Sgn.prototype.simplify = function() {
    const childSimplified = this.children[0].simplify();
    if (childSimplified instanceof Const) {
        return new Const(this.operation(childSimplified.value))
    } else {
        return new Sgn(childSimplified);
    }
}

const literals = { 'x': new Variable('x'), 'y': new Variable('y'), 'z': new Variable('z'), }
const operations = { '+': Add, '-': Subtract, '*': Multiply, '/': Divide, "negate": Negate,
                     "pow": Pow, "log": Log, }
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

const exp = new Pow(new Variable('x'), new Const(3)).diff('x');
const expSimp = exp.simplify();
console.log(exp.toString());
console.log(expSimp.toString());
console.log(expSimp.evaluate(0,0,0))
