package app.model.enums;

import java.util.Arrays;

public enum AllowedExtensions {
    BAT,
    CMD,
    COM,
    SH;

    public static boolean isScriptFile(String path) {
        return Arrays.stream(AllowedExtensions.values())
                .filter(ex -> path.toLowerCase().contains(".".concat(ex.name().toLowerCase())))
                .count() != 0;
    }
}
