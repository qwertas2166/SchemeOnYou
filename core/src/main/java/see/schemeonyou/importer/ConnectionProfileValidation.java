package see.schemeonyou.importer;

import java.util.ArrayList;
import java.util.List;

public final class ConnectionProfileValidation {
    private ConnectionProfileValidation() {}

    public static List<String> validate(ConnectionProfileDraft draft) {
        List<String> errors = new ArrayList<>();
        if (draft == null) {
            errors.add("Profile is required");
            return errors;
        }
        required(errors, "Display name", draft.displayName());
        required(errors, "Host", draft.host());
        required(errors, "Database", draft.database());
        required(errors, "User", draft.user());
        if (draft.port() < 1 || draft.port() > 65_535) errors.add("Port must be between 1 and 65535");
        String driver = trim(draft.driverKind());
        if (driver.isEmpty()) errors.add("Driver kind is required");
        else if (!"postgresql".equals(driver)) errors.add("Only postgresql driver is supported");
        return errors;
    }

    public static void requireValid(ConnectionProfileDraft draft) {
        List<String> errors = validate(draft);
        if (!errors.isEmpty()) throw new IllegalArgumentException(String.join("; ", errors));
    }

    static String trim(String value) {
        return value == null ? "" : value.trim();
    }

    private static void required(List<String> errors, String name, String value) {
        if (trim(value).isEmpty()) errors.add(name + " is required");
    }
}
