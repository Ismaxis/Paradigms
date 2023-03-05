package expression.generic;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        GenericTabulator tabulator = new GenericTabulator();
        String expression = "1.11 + x + y + z";
        int[][] bounds = {
                {-1, 1},
                {-1, 1},
                {-1, 1}
        };

        try {
            Object[][][] res = tabulator.tabulate("d", expression,
                    bounds[0][0], bounds[0][1],
                    bounds[1][0], bounds[1][1],
                    bounds[2][0], bounds[2][1]
            );

            System.out.println(Arrays.deepToString(res));


        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
