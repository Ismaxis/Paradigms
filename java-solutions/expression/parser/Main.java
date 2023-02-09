package expression.parser;

import java.util.Scanner;

import expression.TripleExpression;
import expression.exceptions.TripleParser;

public class Main {
    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        TripleParser parser = new ExpressionParser();
        while (scn.hasNextLine()) {
            try {
                TripleExpression parsed = parser.parse(scn.nextLine());
                System.out.println("Full: " + parsed.toString());
                System.out.println("Mini: " + parsed.toMiniString());
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return;
            }
        }
    }
}
