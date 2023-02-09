package expression;

import expression.parser.ExpressionParser;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0) {
            TripleExpression ex1 = new Add(new Multiply(new Variable("x"), new Variable("x")),
                    new Add(new Multiply(new Const(-2), new Variable("x")), new Const(1)));
            System.out.println(ex1.evaluate(Integer.parseInt(args[0]),0,0));
        }
        TripleExpression ex2 = new Divide(new Add(new Variable("x"), new Variable("y")), new Variable("z"));
        System.out.println(ex2.toMiniString());
        int x = 10;
        int y = 1230;
        int z = 2;
        System.out.println((x + y) / z);
        System.out.println(ex2.evaluate(x, y, z));
        System.out.println(new ExpressionParser().parse(ex2.toMiniString()).evaluate(x, y, z));
    }
}
