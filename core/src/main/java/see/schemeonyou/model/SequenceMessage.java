package see.schemeonyou.model;

import java.util.Objects;

public class SequenceMessage {
    private final String id;
    private final String fromParticipantId;
    private final String toParticipantId;
    private String label;
    private boolean activation;

    public SequenceMessage(String id, String fromParticipantId, String toParticipantId, String label) {
        this.id = Objects.requireNonNull(id);
        this.fromParticipantId = Objects.requireNonNull(fromParticipantId);
        this.toParticipantId = Objects.requireNonNull(toParticipantId);
        this.label = Objects.requireNonNull(label);
    }
    public String getId() { return id; }
    public String getFromParticipantId() { return fromParticipantId; }
    public String getToParticipantId() { return toParticipantId; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = Objects.requireNonNull(label); }
    public boolean isActivation() { return activation; }
    public void setActivation(boolean activation) { this.activation = activation; }
}
