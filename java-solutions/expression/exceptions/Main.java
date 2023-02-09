package expression.exceptions;

import expression.TripleExpression;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        TripleParser parser = new ExpressionParser();
        while (true) {
            System.out.println("Enter expression: ");
            try {
                TripleExpression parsed = parser.parse(scn.nextLine());
                System.out.println("Parsed");
                System.out.println("Full: " + parsed.toString());
                System.out.println("Mini: " + parsed.toMiniString());

                System.out.println("Enter variables to evaluate");
                System.out.print("x: ");
                int x = scn.nextInt();
                System.out.print("y: ");
                int y = scn.nextInt();
                System.out.print("z: ");
                int z = scn.nextInt();
                scn.nextLine();

                System.out.println(parsed.evaluate(x, y, z));
            } catch (ParserException e) {
                System.out.println("Parser exception: " + e.toString());
            } catch (ExpressionEvalException e) {
                System.out.println("Expression eval exception: " + e.toString());
            } catch (InputMismatchException e) {
                System.out.println(e.toString());
            } catch (Exception e) {
                System.out.println(e.toString());
                return;
            }
        }
    }
}
