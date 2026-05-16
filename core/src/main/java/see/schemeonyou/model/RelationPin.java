package see.schemeonyou.model;

import java.util.Optional;

public class RelationPin {
    private String targetTableId;
    private String targetColumnId;

    public Optional<String> targetTableId() { return Optional.ofNullable(targetTableId); }
    public Optional<String> targetColumnId() { return Optional.ofNullable(targetColumnId); }
    public boolean isPinned() { return targetTableId != null; }
    public boolean isColumnPinned() { return targetTableId != null && targetColumnId != null; }
    public boolean matchesTable(String tableId) { return isPinned() && targetTableId.equals(tableId); }
    public boolean matchesColumn(String tableId, String columnId) {
        return isColumnPinned() && targetTableId.equals(tableId) && targetColumnId.equals(columnId);
    }
    public void pin(String targetTableId) { pin(targetTableId, null); }
    public void pin(String targetTableId, String targetColumnId) {
        this.targetTableId = targetTableId;
        this.targetColumnId = targetColumnId;
    }
    public void clear() {
        targetTableId = null;
        targetColumnId = null;
    }
}
