const cnst = value => () => value;
const varToValueIndex = {'x': 0, 'y': 1, 'z': 2 };
const variable = name => (...values) => values[varToValueIndex[name]];
const operation = func => {
    let operation = (...args) => (x, y, z) => func(...args.map(op => op(x, y, z)));
    operation['argsCount'] = func.length;
    return operation;
};
const negate = operation(a => -a);
const add = operation((a, b) => a + b);
const subtract = operation((a, b) => a - b);
const multiply = operation((a, b) => a * b);
const divide = operation((a, b) => a / b);

const madd = operation((a, b, c) => a*b + c);
const floor = operation(a => Math.floor(a));
const ceil = operation(a => Math.ceil(a));

const one = cnst(1);
const two = cnst(2);
const literals = { 'x': variable('x'), 'y': variable('y'), 'z': variable('z'),
    "one": one, "two": two };
const operations = { '+': add, '-': subtract, '*': multiply, '/': divide, "negate": negate,
                    "*+": madd, '_': floor, '^': ceil};
const parse = str =>
    str.split(' ').filter(token => token.length > 0).reduce((stack, token) => {
        if (isConst(token)) {
            stack.push(cnst(parseInt(token)));
        } else if (token in literals) {
            stack.push(literals[token]);
        } else if (token in operations) {
            const op = operations[token];
            stack.push(op(...stack.splice(-op['argsCount'])));
        }
        return stack
    }, []).pop();
const isConst = str => /^-?\d+$/.test(str);

const main = () => {
    const eqRPN = "x x * 2 x * - 1 +";
    const eq = add(subtract(multiply(variable('x'), variable('x')),
            multiply(cnst(2), variable('x'))),cnst(1));
    println("f(x) = x^2 - 2x + 1 | (" + eqRPN + ")");
    const quadraticEquation = parse(eqRPN);
    const printForI = i => println("f(" + i + ") = " + quadraticEquation(i, 0, 0));
    [...Array(11).keys()].forEach(printForI);
};
main();
