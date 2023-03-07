package expression.generic;

import expression.exceptions.ParserException;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static final String EXIT_PHRASE = "exit";

    public static void main(String[] args) {
        GenericTabulator tabulator = new GenericTabulator();
        Scanner scn = new Scanner(System.in);

        int[][] bounds = new int[3][2];
        Map<Integer, String> dimentionMap = Map.of(0, "x", 1, "y", 2, "z");
        String availableModes = Arrays.toString(GenericTabulator.getAvailableModes());

        while (true) {
            try {
                System.out.println("Enter mode " + availableModes + " (or '" + EXIT_PHRASE + "' to exit): ");
                String mode = scn.nextLine();
                if (mode.equals(EXIT_PHRASE)) {
                    return;
                }

                System.out.println("Enter expression: ");
                String expression = scn.nextLine();

                for (int dimension = 0; dimension < 3; dimension++) {
                    String dimensionStr = dimentionMap.getOrDefault(dimension, String.valueOf(dimension));
                    System.out.println("Enter lower bound for " + dimensionStr + ": ");
                    bounds[dimension][0] = scn.nextInt();
                    System.out.println("Enter upper bound for " + dimensionStr + ": ");
                    bounds[dimension][1] = scn.nextInt();
                }

                Object[][][] res = tabulator.tabulate(mode, expression,
                        bounds[0][0], bounds[0][1],
                        bounds[1][0], bounds[1][1],
                        bounds[2][0], bounds[2][1]
                );
                printResult(res);
            } catch (UnknownTabulatorModeException e) {
                System.out.println(e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("Input mismatch! Try again.");
            } catch (ParserException e) {
                System.out.println("Invalid expression:\n" + e);
            } catch (Exception e) {
                System.out.println("Exit...\nCause: '" + e.getMessage() + "'");
                return;
            }

            if (scn.hasNextLine()) {
                scn.nextLine();
            }
        }
    }

    private static void printResult(Object[][][] res) {
        System.out.println(" === Result ===");
        for (Object[][] matrix : res) {
            for (Object[] row : matrix) {
                for (Object value : row) {
                    System.out.print(value + " ");
                }
                System.out.println();
            }
            System.out.println("---");
        }
    }
}
