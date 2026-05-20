package see.schemeonyou.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Objects;

@Getter
public class SequenceMessage {
    private final String id;
    @Setter
    @NonNull
    private String fromParticipantId;
    @Setter
    @NonNull
    private String toParticipantId;
    @Setter
    @NonNull
    private String label;
    @Setter
    private boolean activation;
    @Setter
    @NonNull
    private SequenceMessageType type;
    @Setter
    private int order;

    public SequenceMessage(String id, String fromParticipantId, String toParticipantId, String label) {
        this(id, fromParticipantId, toParticipantId, label, SequenceMessageType.SYNC, 0);
    }

    public SequenceMessage(String id, String fromParticipantId, String toParticipantId, String label, SequenceMessageType type, int order) {
        this.id = Objects.requireNonNull(id);
        this.fromParticipantId = Objects.requireNonNull(fromParticipantId);
        this.toParticipantId = Objects.requireNonNull(toParticipantId);
        this.label = Objects.requireNonNull(label);
        this.type = Objects.requireNonNull(type);
        this.order = order;
    }
}
