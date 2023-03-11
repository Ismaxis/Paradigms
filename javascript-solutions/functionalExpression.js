const cnst = value => () => value;

const varToValueIndex = {
    'x': 0,
    'y': 1,
    'z': 2,
}
const variable = name => (...values) => values[varToValueIndex[name]];

const binaryOperation = (f, left, right) => (x,y,z) => f(left(x,y,z), right(x,y,z));

const add = (left, right) => binaryOperation((a, b) => a + b, left, right);
const subtract = (left, right) => binaryOperation((a, b) => a - b, left, right);
const multiply = (left, right) => binaryOperation((a, b) => a * b, left, right);
const divide = (left, right) => binaryOperation((a, b) => a / b, left, right);

const unaryOperation = (f, child) => (x,y,z) => f(child(x,y,z));

const negate = child => unaryOperation(a => -a, child);

const variables = {
    'x': variable('x'),
    'y': variable('y'),
    'z': variable('z'),
}

const operations = {
    '+': add,
    '-': subtract,
    '*': multiply,
    '/': divide,
    "negate": negate,
}

const parse = str => {
    let stack = [];
    let tokens = str.split(' ').filter(token => token.length > 0);
    tokens.reduce((stack, token) => {
        if (isConst(token)) {
            stack.push(cnst(parseInt(token)));
        } else if (token in variables) {
            stack.push(variables[token]);
        } else if (token in operations) {
            const op = operations[token];
            const args = stack.splice(-op.length);
            stack.push(op(...args));
        }
        return stack
    }, stack)
    return stack.pop();
}

const isConst = str => !isNaN(str);

const main = () => {
    const myPrintln = typeof(println) === 'undefined' ? str => console.log(str) : println;
    const eqRPN = "x x * 2 x * - 1 +"
    myPrintln("f(x) = x^2 - 2x + 1 | (" + eqRPN + ")");
    const quadraticEquation = parse(eqRPN);
    const printForI = i => myPrintln("f(" + i + ") = " + quadraticEquation(i, 0, 0));
    [...Array(11).keys()].forEach(printForI)
}

main();