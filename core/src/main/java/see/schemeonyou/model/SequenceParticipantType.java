package see.schemeonyou.model;

import java.io.IOException;
import java.util.Locale;

public enum SequenceParticipantType {
    ACTOR,
    SERVICE,
    DATABASE,
    EXTERNAL_SYSTEM;

    public String storageName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public static SequenceParticipantType fromStorageName(String value, String field) throws IOException {
        try {
            return SequenceParticipantType.valueOf(value.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new IOException("Invalid sequence participant type at " + field + ": " + value, e);
        }
    }
}
