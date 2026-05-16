package see.schemeonyou.model;

import java.util.Objects;

public class ForeignKey {
    private final String id;
    private final String sourceTableId;
    private final String sourceColumnId;
    private final String targetTableId;
    private final String targetColumnId;

    public ForeignKey(String id, String sourceTableId, String sourceColumnId, String targetTableId, String targetColumnId) {
        this.id = Objects.requireNonNull(id);
        this.sourceTableId = Objects.requireNonNull(sourceTableId);
        this.sourceColumnId = Objects.requireNonNull(sourceColumnId);
        this.targetTableId = Objects.requireNonNull(targetTableId);
        this.targetColumnId = Objects.requireNonNull(targetColumnId);
    }

    public String getId() { return id; }
    public String getSourceTableId() { return sourceTableId; }
    public String getSourceColumnId() { return sourceColumnId; }
    public String getTargetTableId() { return targetTableId; }
    public String getTargetColumnId() { return targetColumnId; }
}
