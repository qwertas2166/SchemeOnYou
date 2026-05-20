package see.schemeonyou.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Objects;

@Getter
public class SequenceParticipant {
    private final String id;
    @Setter
    @NonNull
    private String name;
    @Setter
    @NonNull
    private SequenceParticipantType type;

    public SequenceParticipant(String id, String name) {
        this(id, name, SequenceParticipantType.SERVICE);
    }

    public SequenceParticipant(String id, String name, SequenceParticipantType type) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.type = Objects.requireNonNull(type);
    }
}
