package see.schemeonyou.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public class FkPreview {
    private String sourceTableId;
    private String sourceColumnId;
    private String targetTableId;
    private String targetColumnId;
    @Setter
    private boolean keepTargetPinnedAfterCreate;

    public FkPreview(String sourceTableId, String sourceColumnId, String targetTableId, String targetColumnId) {
        this.sourceTableId = sourceTableId;
        this.sourceColumnId = sourceColumnId;
        this.targetTableId = targetTableId;
        this.targetColumnId = targetColumnId;
    }

    public void setTarget(String targetTableId, String targetColumnId) {
        this.targetTableId = targetTableId;
        this.targetColumnId = targetColumnId;
    }

    public void swapSourceAndTarget() {
        String oldSourceTableId = sourceTableId;
        String oldSourceColumnId = sourceColumnId;
        sourceTableId = targetTableId;
        sourceColumnId = targetColumnId;
        targetTableId = oldSourceTableId;
        targetColumnId = oldSourceColumnId;
    }
}
