package see.schemeonyou.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SchemeProject {
    public static final int STORAGE_VERSION = 1;

    private String id;
    private String name;
    private Instant createdAt;
    private Instant updatedAt;
    private final List<Diagram> diagrams = new ArrayList<>();

    public SchemeProject(String id, String name) {
        this(id, name, Instant.now());
    }

    private SchemeProject(String id, String name, Instant now) {
        this(id, name, now, now);
    }

    public SchemeProject(String id, String name, Instant createdAt, Instant updatedAt) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);
    }
    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = Objects.requireNonNull(name); touch(); }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public List<Diagram> getDiagrams() { return diagrams; }
    public Optional<Diagram> findDiagram(String diagramId) { return diagrams.stream().filter(d -> d.getId().equals(diagramId)).findFirst(); }
    public void touch() { updatedAt = Instant.now(); }
}
