package see.schemeonyou.ui.canvas;

import see.schemeonyou.model.Diagram;
import see.schemeonyou.model.DiagramType;
import see.schemeonyou.model.DbTable;
import see.schemeonyou.model.ForeignKey;
import see.schemeonyou.model.Rect;
import see.schemeonyou.model.SequenceMessage;
import see.schemeonyou.model.SequenceParticipant;

import java.util.Optional;

/**
 * Headless canvas hit-testing for click/drag selection.
 * JavaFX views provide canvas coordinates; this presenter resolves model elements
 * without depending on Canvas, Stage, or DISPLAY.
 */
public class CanvasHitTestPresenter {
    private static final double EDGE_HIT_TOLERANCE = 10.0;
    private static final double SEQUENCE_MESSAGE_START_Y = 150.0;
    private static final double SEQUENCE_MESSAGE_STEP_Y = 72.0;

    public Optional<Hit> hitAt(Diagram diagram, double canvasX, double canvasY) {
        double x = modelX(diagram, canvasX);
        double y = modelY(diagram, canvasY);
        return hitAtModel(diagram, x, y);
    }

    public Optional<Hit> hitAtModel(Diagram diagram, double x, double y) {
        if (diagram == null) return Optional.empty();
        if (diagram.getType() == DiagramType.DATABASE) {
            Optional<DbTable> tableHit = diagram.getTables().stream()
                    .filter(table -> contains(diagram, table.getId(), x, y))
                    .findFirst();
            if (tableHit.isPresent()) return tableHit.map(table -> new TableHit(table.getId(), table.getName()));
            return hitForeignKeyAtModel(diagram, x, y).map(fk -> new ForeignKeyHit(fk.getId()));
        }
        if (diagram.getType() == DiagramType.SEQUENCE) {
            Optional<SequenceMessage> messageHit = hitSequenceMessageAtModel(diagram, x, y);
            if (messageHit.isPresent()) return messageHit.map(message -> new MessageHit(message.getId(), message.getLabel(), message.getFromParticipantId()));
            return diagram.getParticipants().stream()
                    .filter(participant -> contains(diagram, participant.getId(), x, y))
                    .findFirst()
                    .map(participant -> new ParticipantHit(participant.getId(), participant.getName()));
        }
        return Optional.empty();
    }

    public Optional<String> draggableElementAtModel(Diagram diagram, double x, double y) {
        if (diagram == null) return Optional.empty();
        if (diagram.getType() == DiagramType.DATABASE) {
            return diagram.getTables().stream()
                    .filter(table -> contains(diagram, table.getId(), x, y))
                    .map(DbTable::getId)
                    .findFirst();
        }
        if (diagram.getType() == DiagramType.SEQUENCE) {
            return diagram.getParticipants().stream()
                    .filter(participant -> contains(diagram, participant.getId(), x, y))
                    .map(SequenceParticipant::getId)
                    .findFirst();
        }
        return Optional.empty();
    }

    public Optional<ForeignKey> hitForeignKeyAtModel(Diagram diagram, double x, double y) {
        if (diagram == null) return Optional.empty();
        return diagram.getForeignKeys().stream()
                .filter(fk -> {
                    Rect source = diagram.getCanvasState().getBoundsByElementId().get(fk.getSourceTableId());
                    Rect target = diagram.getCanvasState().getBoundsByElementId().get(fk.getTargetTableId());
                    return source != null && target != null
                            && distanceToSegment(x, y, source.center().x(), source.center().y(), target.center().x(), target.center().y()) <= EDGE_HIT_TOLERANCE;
                })
                .findFirst();
    }

    public Optional<SequenceMessage> hitSequenceMessageAtModel(Diagram diagram, double x, double y) {
        if (diagram == null) return Optional.empty();
        for (int i = 0; i < diagram.getMessages().size(); i++) {
            SequenceMessage message = diagram.getMessages().get(i);
            Rect from = diagram.getCanvasState().getBoundsByElementId().get(message.getFromParticipantId());
            Rect to = diagram.getCanvasState().getBoundsByElementId().get(message.getToParticipantId());
            if (from == null || to == null) continue;
            double messageY = sequenceMessageY(i);
            if (distanceToSegment(x, y, from.center().x(), messageY, to.center().x(), messageY) <= EDGE_HIT_TOLERANCE) {
                return Optional.of(message);
            }
        }
        return Optional.empty();
    }

    public double modelX(Diagram diagram, double canvasX) {
        return canvasX / diagram.getCanvasState().getZoom() + diagram.getCanvasState().getViewportX();
    }

    public double modelY(Diagram diagram, double canvasY) {
        return canvasY / diagram.getCanvasState().getZoom() + diagram.getCanvasState().getViewportY();
    }

    static double sequenceMessageY(int index) {
        return SEQUENCE_MESSAGE_START_Y + index * SEQUENCE_MESSAGE_STEP_Y;
    }

    static double distanceToSegment(double px, double py, double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double lengthSquared = dx * dx + dy * dy;
        if (lengthSquared == 0) return Math.hypot(px - x1, py - y1);
        double t = Math.max(0.0, Math.min(1.0, ((px - x1) * dx + (py - y1) * dy) / lengthSquared));
        double closestX = x1 + t * dx;
        double closestY = y1 + t * dy;
        return Math.hypot(px - closestX, py - closestY);
    }

    private boolean contains(Diagram diagram, String elementId, double x, double y) {
        Rect rect = diagram.getCanvasState().getBoundsByElementId().get(elementId);
        return rect != null && x >= rect.x() && x <= rect.x() + rect.width() && y >= rect.y() && y <= rect.y() + rect.height();
    }

    public sealed interface Hit permits TableHit, ForeignKeyHit, ParticipantHit, MessageHit {
        String elementId();
    }

    public record TableHit(String elementId, String name) implements Hit {}
    public record ForeignKeyHit(String elementId) implements Hit {}
    public record ParticipantHit(String elementId, String name) implements Hit {}
    public record MessageHit(String elementId, String label, String fromParticipantId) implements Hit {}
}
