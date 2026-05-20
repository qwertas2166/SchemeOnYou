package see.schemeonyou.model;

import java.io.IOException;
import java.util.Locale;

public enum SequenceMessageType {
    SYNC,
    ASYNC,
    RETURN,
    SELF_CALL;

    public String storageName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public static SequenceMessageType fromStorageName(String value, String field) throws IOException {
        try {
            return SequenceMessageType.valueOf(value.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new IOException("Invalid sequence message type at " + field + ": " + value, e);
        }
    }
}
