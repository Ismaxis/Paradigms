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

function AbstractOperation(...children) {
    this.children = children;
}
AbstractOperation.prototype.evaluate = function(...values) {
    return this.operation(...this.children.map(x => x.evaluate(...values)));
}
AbstractOperation.prototype.toString = function() {
    return this.children.map(x => x.toString()).reduce((acc, str) => acc + " " + str) + " " + this.symbol;
}

function Add(left, right) {
    AbstractOperation.call(this, left, right);
}
Add.prototype = Object.create(AbstractOperation.prototype);
Add.prototype.operation = function(a, b) {
    return a + b;
}
Add.prototype.symbol = '+';
Add.prototype.diff = function(varName) {
    return new Add(this.children[0].diff(varName), this.children[1].diff(varName));
}

function Subtract(left, right) {
    AbstractOperation.call(this, left, right);
}
Subtract.prototype = Object.create(AbstractOperation.prototype);
Subtract.prototype.operation = function(a, b) {
    return a - b;
}
Subtract.prototype.symbol = '-';
Subtract.prototype.diff = function(varName) {
    return new Subtract(this.children[0].diff(varName), this.children[1].diff(varName));
}

function Multiply(left, right) {
    AbstractOperation.call(this, left, right);
}
Multiply.prototype = Object.create(AbstractOperation.prototype);
Multiply.prototype.operation = function(a, b) {
    return a * b;
}
Multiply.prototype.symbol = '*';
Multiply.prototype.diff = function(varName) {
    return new Add(
        new Multiply(this.children[0].diff(varName), this.children[1]),
        new Multiply(this.children[0], this.children[1].diff(varName)));
}

function Divide(left, right) {
    AbstractOperation.call(this, left, right);
}
Divide.prototype = Object.create(AbstractOperation.prototype);
Divide.prototype.operation = function(a, b) {
    return a / b;
}
Divide.prototype.symbol = '/';
Divide.prototype.diff = function(varName) {
    return new Divide(
        new Subtract(
        new Multiply(this.children[0].diff(varName), this.children[1]),
        new Multiply(this.children[0], this.children[1].diff(varName))),
        new Multiply(this.children[1], this.children[1]));
}

function Negate(child) {
    AbstractOperation.call(this, child);
}
Negate.prototype = Object.create(AbstractOperation.prototype);
Negate.prototype.operation = function(a) {
    return -a;
}
Negate.prototype.symbol = 'negate';
Negate.prototype.diff = function(varName) {
    return new Negate(this.children[0].diff(varName));
}

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
            stack.push(new op(...stack.splice(-op.length)));
        }
        return stack
    }, stack)
    return stack.pop();
}
const isConst = str => /^-?\d+$/.test(str);

const exp = parse("x x 2 - * x * 1 +");
console.log(exp.evaluate(5, 0, 0))
