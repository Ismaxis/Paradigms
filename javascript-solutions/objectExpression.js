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

function operationFabric(symbol, operation, diffRule) {
    function AbstractOperation(...children) {
        this.children = children;
    }
    AbstractOperation.getArgsCount = function() {
        return operation.length;
    }
    AbstractOperation.prototype.evaluate = function(...values) {
        return operation(...this.children.map(x => x.evaluate(...values)));
    }
    AbstractOperation.prototype.toString = function() {
        return this.children.join(' ') + " " + symbol;
    }
    AbstractOperation.prototype.diff = function(varName) {
        return diffRule(varName, ...this.children);
    }

    return AbstractOperation;
}

const Add = operationFabric('+',
    (a, b) => a + b,
    (varName, left, right) => new Add(left.diff(varName), right.diff(varName)));

const Subtract = operationFabric('-',
    (a, b) => a - b,
    (varName, left, right) => new Subtract(left.diff(varName), right.diff(varName)));

const Multiply = operationFabric('*',
    (a, b) => a * b,
    (varName, left, right) => new Add(
        new Multiply(left.diff(varName), right),
        new Multiply(left, right.diff(varName))));

const Divide = operationFabric('/',
    (a, b) => a / b,
    (varName, left, right) => new Divide(
        new Subtract(
            new Multiply(left.diff(varName), right),
            new Multiply(left, right.diff(varName))),
        new Multiply(right, right)));

const Negate = operationFabric('negate',
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

const exp = parse('x y z * /');
console.log(exp.evaluate(1, 1, 1))
