package expression.generic;

import expression.generic.actualOperations.ActualOperations;
import expression.generic.actualOperations.BigIntegerActualOperations;
import expression.generic.actualOperations.DoubleActualOperations;
import expression.generic.actualOperations.IntActualOperations;
import expression.generic.operations.TripleExpression;
import expression.generic.parser.ExpressionParser;
import expression.generic.parser.TripleParser;

import java.util.Map;

public class GenericTabulator implements Tabulator {
    private static final Map<String, ActualOperations<?>> mapOfActualOperations = Map.of(
            "i", new IntActualOperations(true),
            "d", new DoubleActualOperations(),
            "bi", new BigIntegerActualOperations()
        );
    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        return fillTable(mapOfActualOperations.get(mode), expression, x1, x2, y1, y2, z1, z2);
    }

    private <T> Object[][][] fillTable(ActualOperations<T> actualOperations, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        TripleParser<T> parser = new ExpressionParser<>();
        TripleExpression<T> parsedExpression = parser.parse(actualOperations, expression);
        int rangeX = x2 - x1 + 1;
        int rangeY = y2 - y1 + 1;
        int rangeZ = z2 - z1 + 1;
        Object[][][] res = new Object[rangeX][rangeY][rangeZ];

        for (int i = 0; i < rangeX; i++) {
            for (int j = 0; j < rangeY; j++) {
                for (int k = 0; k < rangeZ; k++) {
                    Object cellValue;
                    try {
                        cellValue = parsedExpression.evaluate(
                            actualOperations.toConst(x1 + i),
                            actualOperations.toConst(y1 + j),
                            actualOperations.toConst(z1 + k)
                        );
                    } catch (Exception e) {
                        cellValue = null;
                    }
                    res[i][j][k] = cellValue;
                }
            }
        }
        return res;
    }
}
