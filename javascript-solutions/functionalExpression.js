const cnst = value => () => value;
const varToValueIndex = {'x': 0, 'y': 1, 'z': 2 }
const variable = name => (...values) => values[varToValueIndex[name]];
const operation = func => {
    let operation = (...args) => (x, y, z) => func(...args.map(op => op(x, y, z)));
    operation['argsCount'] = func.length;
    return operation;
}
const negate = operation(a => -a);
const add = operation((a, b) => a + b);
const subtract = operation((a, b) => a - b);
const multiply = operation((a, b) => a * b);
const divide = operation((a, b) => a / b);

const variables = { 'x': variable('x'), 'y': variable('y'), 'z': variable('z') }
const operations = { '+': add, '-': subtract, '*': multiply, '/': divide, "negate": negate }
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
            stack.push(op(...stack.splice(-op['argsCount'])));
        }
        return stack
    }, stack)
    return stack.pop();
}
const isConst = str => !isNaN(str);
const main = () => {
    const myPrintln = typeof(println) === 'undefined' ? str => console.log(str) : println;
    const eqRPN = "x x * 2 x * - 1 +";
    const eq = add(subtract(multiply(variable('x'), variable('x')),
            multiply(cnst(2), variable('x'))),cnst(1));
    myPrintln("f(x) = x^2 - 2x + 1 | (" + eqRPN + ")");
    const quadraticEquation = parse(eqRPN);
    const printForI = i => myPrintln("f(" + i + ") = " + quadraticEquation(i, 0, 0));
    [...Array(11).keys()].forEach(printForI);
}
main();
