package see.schemeonyou.layout;

import see.di.See;
import see.schemeonyou.model.*;

@See
public class DeterministicLayoutEngine {
    public void layout(Diagram diagram) {
        if (diagram.getType() == DiagramType.DATABASE) layoutDatabase(diagram, false);
        if (diagram.getType() == DiagramType.SEQUENCE) layoutSequence(diagram, false);
    }

    public void resetLayout(Diagram diagram) {
        diagram.getCanvasState().getBoundsByElementId().clear();
        if (diagram.getType() == DiagramType.DATABASE) layoutDatabase(diagram, true);
        if (diagram.getType() == DiagramType.SEQUENCE) layoutSequence(diagram, true);
    }

    private void layoutDatabase(Diagram diagram, boolean overwrite) {
        int i = 0;
        for (DbTable table : diagram.getTables()) {
            double x = 64 + (i % 3) * 280.0;
            double y = 64 + (i / 3) * 220.0;
            double h = 64 + Math.max(1, table.getColumns().size()) * 28.0;
            putBounds(diagram, table.getId(), new Rect(x, y, 220, h), overwrite);
            i++;
        }
    }
    private void layoutSequence(Diagram diagram, boolean overwrite) {
        int i = 0;
        for (SequenceParticipant p : diagram.getParticipants()) {
            putBounds(diagram, p.getId(), new Rect(80 + i * 180.0, 64, 140, 48), overwrite);
            i++;
        }
        for (int m = 0; m < diagram.getMessages().size(); m++) {
            putBounds(diagram, diagram.getMessages().get(m).getId(), new Rect(80, 150 + m * 72.0, 500, 32), overwrite);
        }
    }

    private void putBounds(Diagram diagram, String elementId, Rect bounds, boolean overwrite) {
        if (overwrite || !diagram.getCanvasState().getBoundsByElementId().containsKey(elementId)) {
            diagram.getCanvasState().getBoundsByElementId().put(elementId, bounds);
        }
    }
}
