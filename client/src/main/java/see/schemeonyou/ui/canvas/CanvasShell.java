package see.schemeonyou.ui.canvas;

import see.schemeonyou.model.SelectionDepth;

import java.util.Optional;

public class CanvasShell {
    private SelectionDepth selectionDepth = SelectionDepth.DIAGRAM;
    private String selectedElementId;
    private String relationPinTableId;

    public SelectionDepth selectionDepth() { return selectionDepth; }
    public void selectionDepth(SelectionDepth selectionDepth) { this.selectionDepth = selectionDepth; }
    public Optional<String> selectedElementId() { return Optional.ofNullable(selectedElementId); }
    public void selectedElementId(String selectedElementId) { this.selectedElementId = selectedElementId; }
    public Optional<String> relationPinTableId() { return Optional.ofNullable(relationPinTableId); }
    public void pinRelationTarget(String tableId) { this.relationPinTableId = tableId; }
    public void clearRelationPin() { this.relationPinTableId = null; }
}
