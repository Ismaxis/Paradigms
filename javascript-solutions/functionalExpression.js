const cnst = val => (x,y,z) => val;

const variable = name => (x,y,z) => {
    if (name === "x") {
        return x;
    } else if (name === "y") {
        return y;
    } else if (name === "z") {
        return z;
    }

    return "NO SUCH VAR NAME"
}

const binaryFunc = (f, left, right) => (x,y,z) => f(left(x,y,z), right(x,y,z));

const add = (left, right) => binaryFunc((a, b) => a + b, left, right);
const subtract = (left, right) => binaryFunc((a, b) => a - b, left, right);
const multiply = (left, right) => binaryFunc((a, b) => a * b, left, right);
const divide = (left, right) => binaryFunc((a, b) => a / b, left, right);

const unaryFunc = (f, child) => (x,y,z) => f(child(x,y,z));

const negate = child => unaryFunc(a => -a, child);

const parse = str => {
    let tokens = tokenize(str)
    let stack = [];
    for (let token of tokens) {
        if (isVar(token)) {
            stack.push(variable(token));
        } else if (isConst(token)) {
            stack.push(cnst(parseInt(token)));
        } else if (token === "negate") {
            stack.push(negate(stack.pop()))
        } else {
            const right = stack.pop();
            const left = stack.pop();
            let op;
            if (token === '+') {
                op = add(left, right);
            } else if (token === '-') {
                op = subtract(left, right);
            } else if (token === '*') {
                op = multiply(left, right);
            } else if (token === '/') {
                op = divide(left, right);
            }
            stack.push(op);
        }
    }
    return stack.pop();
}

const tokenize = str => {
    const tokens = []
    for (let i = 0; i < str.length; i++) {
        if (!isWhitespace(str.charAt(i))) {
            const start = i;
            i++;
            while (!isWhitespace(str.charAt(i))) {
                i++;
            }
            tokens.push(str.substring(start, i));
        }
    }

    return tokens;
}

const isWhitespace = str => {
    return str.trim() === '';
}

const isVar = str => {
    return str === "x" || str === "y" || str === "z";
}

const isConst = str => {
    return !isNaN(str);
}

const main = () => { // TODO test program
    println("f(x) = x^2 - 2x + 1");
    const quadraticEquation = parse("x x * 2 x * - 1 +");
    for (let i = 0; i <= 10; i++) {
        println("f(" + i + ") = " + quadraticEquation(i, 0, 0))
    }
}

main();