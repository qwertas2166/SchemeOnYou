package see.schemeonyou.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Diagram {
    private final String id;
    private String name;
    private final DiagramType type;
    private final CanvasState canvasState = new CanvasState();
    private final List<DbTable> tables = new ArrayList<>();
    private final List<ForeignKey> foreignKeys = new ArrayList<>();
    private final List<SequenceParticipant> participants = new ArrayList<>();
    private final List<SequenceMessage> messages = new ArrayList<>();

    public Diagram(String id, String name, DiagramType type) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.type = Objects.requireNonNull(type);
    }
    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = Objects.requireNonNull(name); }
    public DiagramType getType() { return type; }
    public CanvasState getCanvasState() { return canvasState; }
    public List<DbTable> getTables() { return tables; }
    public List<ForeignKey> getForeignKeys() { return foreignKeys; }
    public List<SequenceParticipant> getParticipants() { return participants; }
    public List<SequenceMessage> getMessages() { return messages; }
    public Optional<DbTable> findTable(String id) { return tables.stream().filter(t -> t.getId().equals(id)).findFirst(); }
}
