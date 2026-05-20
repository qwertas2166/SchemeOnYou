package see.schemeonyou.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
public class Diagram {
    private final String id;
    @Setter
    @NonNull
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
    public Optional<DbTable> findTable(String id) { return tables.stream().filter(t -> t.getId().equals(id)).findFirst(); }
}
