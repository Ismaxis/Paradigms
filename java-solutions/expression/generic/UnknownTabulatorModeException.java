package expression.generic;

import java.util.Arrays;

public class UnknownTabulatorModeException extends RuntimeException {
    public UnknownTabulatorModeException (String givenMode, String[] avalibleModes) {
        super("Unknown expression mode: '" + givenMode + "'\n" +
                "Avalible modes: " + Arrays.toString(avalibleModes));
    }
}
