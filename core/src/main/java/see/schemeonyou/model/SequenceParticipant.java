package see.schemeonyou.model;

import java.util.Objects;

public class SequenceParticipant {
    private final String id;
    private String name;

    public SequenceParticipant(String id, String name) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
    }
    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = Objects.requireNonNull(name); }
}
