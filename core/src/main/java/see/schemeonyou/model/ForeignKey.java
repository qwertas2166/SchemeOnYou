package see.schemeonyou.model;

import lombok.Getter;

import java.util.Objects;

@Getter
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
}
