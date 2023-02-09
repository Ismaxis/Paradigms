package expression.exceptions;

import expression.ExpressionToString;
import expression.Multiply;

public class CheckedMultiply extends Multiply {
    public CheckedMultiply(ExpressionToString left, ExpressionToString right) {
        super(left, right);
    }

    private static final int HALF_OF_INTS_BITS = 15;

    @Override
    protected int calc(int left, int right) {
        int r = left * right;
        // :NOTE: почему именно 15? Немного не хватает комментариев и константы
        // :ANS: При умножении двух n битных чисел получается 2n значное.
        // Если 'побитовое или' сдвинутое на 15 вправо равно 0 => оба числа не более чем 15 битные,
        // и в таком случае можно не проверять переполнение.
        // Модуль нужен, чтобы у отрицательных чисел (в дополнении до 2) старший бит был 0

        // Это лишь оптимизация, для того чтобы не делать дорогое деление
        // для чисел, которые заведомо не дадут переполнение
        if (((abs(left) | abs(right)) >>> HALF_OF_INTS_BITS != 0)) {
            if (((right != 0) && (r / right != left)) ||
                    (left == Integer.MIN_VALUE && right == -1)) {
                throw new IntOverflowException(symbol, left, right);
            }
        }
        return r;
    }

    private static int abs(int value) {
        return (value < 0) ? -value : value;
    }
}
