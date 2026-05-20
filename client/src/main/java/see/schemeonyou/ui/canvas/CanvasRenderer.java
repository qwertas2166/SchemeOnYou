package see.schemeonyou.ui.canvas;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import see.schemeonyou.model.*;
import see.schemeonyou.validation.ValidationIssue;
import see.schemeonyou.validation.ValidationResult;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class CanvasRenderer {
    private static final String APP_BG = "#1F232A";
    private static final String PANEL_ELEVATED = "#2B313B";
    private static final String CANVAS_BG = "#303640";
    private static final String CARD_BG = "#252B34";
    private static final String CARD_HEADER = "#303847";
    private static final String TABLE_HEADER_DIVIDER = "#4B5563";
    private static final String TEXT_PRIMARY = "#E6EAF0";
    private static final String TEXT_SECONDARY = "#A8B0BD";
    private static final String SELECTION = "#4C8DFF";
    private static final String FK_COLOR = "#62AEEF";
    private static final String PK_COLOR = "#D7A84A";
    private static final String PIN_COLOR = "#E6B450";
    private static final String WARNING = "#D19A3E";
    private static final String ERROR = "#E06C75";
    private static final String UI_FONT_FAMILY = "Monaspace Krypton";

    public void render(Canvas canvas, Diagram diagram, RelationPin relationPin, FkPreview fkPreview, Selection selection, ValidationResult validation) {
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.setFill(Color.web(CANVAS_BG));
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g.setFont(uiFont(14));

        CanvasState state = diagram.getCanvasState();
        g.save();
        g.translate(-state.getViewportX() * state.getZoom(), -state.getViewportY() * state.getZoom());
        g.scale(state.getZoom(), state.getZoom());
        if (diagram.getType() == DiagramType.DATABASE) {
            drawDatabase(g, diagram, relationPin, fkPreview, selection, validation);
        } else {
            drawSequence(g, diagram, selection);
        }
        g.restore();
        drawViewportStatus(g, canvas, state);
        drawCanvasFocusRing(g, canvas);
    }

    public static Rect diagramBounds(Diagram diagram) {
        return diagram.getCanvasState().getBoundsByElementId().values().stream()
                .reduce(null, (acc, r) -> {
                    if (acc == null) return r;
                    double minX = Math.min(acc.x(), r.x());
                    double minY = Math.min(acc.y(), r.y());
                    double maxX = Math.max(acc.x() + acc.width(), r.x() + r.width());
                    double maxY = Math.max(acc.y() + acc.height(), r.y() + r.height());
                    return new Rect(minX, minY, maxX - minX, maxY - minY);
                });
    }

    private void drawViewportStatus(GraphicsContext g, Canvas canvas, CanvasState state) {
        g.setFill(Color.web(TEXT_SECONDARY));
        g.setFont(uiFont(11));
        g.fillText("zoom " + Math.round(state.getZoom() * 100) + "% · view " + Math.round(state.getViewportX()) + "," + Math.round(state.getViewportY()), 12, canvas.getHeight() - 12);
    }

    private void drawCanvasFocusRing(GraphicsContext g, Canvas canvas) {
        if (!canvas.isFocused()) return;
        g.setStroke(Color.web(SELECTION));
        g.setLineWidth(3);
        g.strokeRect(1.5, 1.5, canvas.getWidth() - 3, canvas.getHeight() - 3);
    }

    private void drawDatabase(GraphicsContext g, Diagram diagram, RelationPin relationPin, FkPreview fkPreview, Selection selection, ValidationResult validation) {
        Set<String> errorElementIds = validation.issues().stream()
                .filter(issue -> issue.severity() == ValidationIssue.Severity.ERROR)
                .map(ValidationIssue::elementId)
                .collect(Collectors.toSet());
        Set<String> warningElementIds = validation.issues().stream()
                .filter(issue -> issue.severity() == ValidationIssue.Severity.WARNING)
                .map(ValidationIssue::elementId)
                .collect(Collectors.toSet());
        Set<String> warningTableIds = diagram.getForeignKeys().stream()
                .filter(fk -> warningElementIds.contains(fk.getId()) || errorElementIds.contains(fk.getId()))
                .flatMap(fk -> java.util.stream.Stream.of(fk.getSourceTableId(), fk.getTargetTableId()))
                .collect(Collectors.toSet());

        for (ForeignKey fk : diagram.getForeignKeys()) {
            Rect a = diagram.getCanvasState().getBoundsByElementId().get(fk.getSourceTableId());
            Rect b = diagram.getCanvasState().getBoundsByElementId().get(fk.getTargetTableId());
            if (a != null && b != null) {
                boolean error = errorElementIds.contains(fk.getId());
                boolean warning = warningElementIds.contains(fk.getId());
                boolean selectedFk = fk.getId().equals(selection.foreignKeyId());
                Color edgeColor = Color.web(selectedFk ? SELECTION : error ? ERROR : warning ? WARNING : TEXT_SECONDARY);
                drawDirectedEdge(g, a.center().x(), a.center().y(), b.center().x(), b.center().y(), edgeColor, selectedFk ? "FK selected" : error || warning ? "FK !" : "FK");
                if (error || warning) {
                    g.setFill(edgeColor);
                    g.fillOval((a.center().x() + b.center().x()) / 2 - 4, (a.center().y() + b.center().y()) / 2 - 4, 8, 8);
                }
            }
        }
        for (DbTable table : diagram.getTables()) {
            Rect r = diagram.getCanvasState().getBoundsByElementId().getOrDefault(table.getId(), new Rect(64,64,220,120));
            boolean selected = table.getId().equals(selection.tableId());
            boolean pinnedTable = relationPin.matchesTable(table.getId());
            boolean columnDepth = selected && selection.columnId() != null;
            boolean hasValidationWarning = warningTableIds.contains(table.getId());
            g.setFill(Color.web(selected ? CARD_HEADER : CARD_BG));
            g.fillRoundRect(r.x(), r.y(), r.width(), r.height(), 16, 16);
            g.setStroke(Color.web(columnDepth ? SELECTION : selected ? PIN_COLOR : hasValidationWarning ? WARNING : FK_COLOR));
            g.setLineWidth(columnDepth || selected ? 2.5 : 1.0);
            g.strokeRoundRect(r.x(), r.y(), r.width(), r.height(), 16, 16);
            g.setLineWidth(1.0);
            g.setFill(Color.web(TEXT_PRIMARY));
            g.fillText((hasValidationWarning ? "⚠ " : "") + table.getName(), r.x() + 12, r.y() + 24);
            g.setStroke(Color.web(TABLE_HEADER_DIVIDER));
            g.setLineWidth(1.0);
            g.strokeLine(r.x() + 1, r.y() + 38, r.x() + r.width() - 1, r.y() + 38);
            if (pinnedTable) drawRelationPinBadge(g, r.x() + r.width() - 54, r.y() + 8, "PIN");
            int row = 0;
            for (DbColumn column : table.getColumns()) {
                double rowY = r.y() + 58 + row * 24;
                boolean pinnedColumn = relationPin.matchesColumn(table.getId(), column.getId());
                if (column.getId().equals(selection.columnId())) {
                    g.setFill(Color.web(PANEL_ELEVATED));
                    g.fillRoundRect(r.x() + 10, rowY - 16, r.width() - 20, 22, 8, 8);
                    g.setStroke(Color.web(SELECTION));
                    g.strokeRoundRect(r.x() + 10, rowY - 16, r.width() - 20, 22, 8, 8);
                    g.setFill(Color.web(TEXT_PRIMARY));
                }
                g.fillText((column.isPrimaryKey() ? "◆ " : "") + column.getName() + ": " + column.getType(), r.x() + 16, rowY);
                if (pinnedColumn) drawRelationPinBadge(g, r.x() + r.width() - 54, rowY - 15, "PIN");
                row++;
            }
            Map<String, String> columnNames = table.getColumns().stream().collect(Collectors.toMap(DbColumn::getId, DbColumn::getName));
            for (DbTableConstraint constraint : table.getConstraints()) {
                double rowY = r.y() + 58 + row * 24;
                g.setFill(Color.web(PK_COLOR));
                g.setFont(uiFont(12));
                g.fillText(constraintLabel(constraint, columnNames), r.x() + 16, rowY);
                g.setFont(uiFont(14));
                row++;
            }
        }
        drawFkPreview(g, diagram, fkPreview);
    }

    private String constraintLabel(DbTableConstraint constraint, Map<String, String> columnNames) {
        String type = constraint.type() == DbTableConstraintType.COMPOSITE_PRIMARY_KEY ? "PK" : "UNQ";
        String columns = constraint.columnIds().stream()
                .map(columnId -> columnNames.getOrDefault(columnId, columnId))
                .collect(Collectors.joining(", "));
        return type + "(" + columns + ")";
    }

    private void drawDirectedEdge(GraphicsContext g, double x1, double y1, double x2, double y2, Color color, String label) {
        drawDirectedEdge(g, x1, y1, x2, y2, color, label, false);
    }

    private void drawDirectedEdge(GraphicsContext g, double x1, double y1, double x2, double y2, Color color, String label, boolean dashed) {
        g.save();
        g.setStroke(color);
        g.setFill(color);
        g.setLineWidth(2.0);
        if (dashed) g.setLineDashes(8, 6);
        g.strokeLine(x1, y1, x2, y2);
        double angle = Math.atan2(y2 - y1, x2 - x1);
        double arrowLength = 12.0;
        double arrowWidth = 7.0;
        double ax = x2 - Math.cos(angle) * 18.0;
        double ay = y2 - Math.sin(angle) * 18.0;
        double leftX = ax - Math.cos(angle) * arrowLength + Math.sin(angle) * arrowWidth;
        double leftY = ay - Math.sin(angle) * arrowLength - Math.cos(angle) * arrowWidth;
        double rightX = ax - Math.cos(angle) * arrowLength - Math.sin(angle) * arrowWidth;
        double rightY = ay - Math.sin(angle) * arrowLength + Math.cos(angle) * arrowWidth;
        g.fillPolygon(new double[]{ax, leftX, rightX}, new double[]{ay, leftY, rightY}, 3);
        double midX = (x1 + x2) / 2.0;
        double midY = (y1 + y2) / 2.0;
        g.setFont(uiFont(10));
        g.fillText(label, midX + 6, midY - 6);
        g.restore();
    }

    private void drawSelfCall(GraphicsContext g, double x, double y, Color color, String label) {
        g.save();
        g.setStroke(color);
        g.setFill(color);
        g.setLineWidth(2.0);
        double width = 64;
        double height = 34;
        g.strokePolyline(new double[]{x, x + width, x + width, x + 12}, new double[]{y, y, y + height, y + height}, 4);
        g.fillPolygon(new double[]{x + 12, x + 24, x + 24}, new double[]{y + height, y + height - 7, y + height + 7}, 3);
        g.setFont(uiFont(10));
        g.fillText(label, x + 10, y - 6);
        g.restore();
    }

    private void drawRelationPinBadge(GraphicsContext g, double x, double y, String text) {
        g.setFill(Color.web(WARNING));
        g.fillRoundRect(x, y, 42, 18, 8, 8);
        g.setStroke(Color.web(PK_COLOR));
        g.strokeRoundRect(x, y, 42, 18, 8, 8);
        g.setFill(Color.web(APP_BG));
        g.setFont(uiFont(10));
        g.fillText(text, x + 10, y + 13);
    }

    private void drawSequence(GraphicsContext g, Diagram diagram, Selection selection) {
        if (diagram.getParticipants().isEmpty()) {
            g.setFill(Color.web(TEXT_SECONDARY));
            g.fillText("Sequence diagram is empty. Use command palette: New/Add participant/message.", 64, 64);
            return;
        }

        for (SequenceParticipant participant : diagram.getParticipants()) {
            Rect r = diagram.getCanvasState().getBoundsByElementId().getOrDefault(participant.getId(), new Rect(64, 64, 140, 48));
            boolean selected = participant.getId().equals(selection.participantId());
            g.setFill(Color.web(selected ? CARD_HEADER : CARD_BG));
            g.fillRoundRect(r.x(), r.y(), r.width(), r.height(), 14, 14);
            g.setStroke(Color.web(selected ? PIN_COLOR : FK_COLOR));
            g.setLineWidth(selected ? 2.5 : 1.5);
            g.strokeRoundRect(r.x(), r.y(), r.width(), r.height(), 14, 14);
            g.setFill(Color.web(TEXT_PRIMARY));
            g.fillText(participant.getName(), r.x() + 12, r.y() + 29);
            g.setStroke(Color.web(TEXT_SECONDARY));
            g.setLineDashes(8, 6);
            g.strokeLine(r.center().x(), r.y() + r.height(), r.center().x(), 760);
            g.setLineDashes();
        }

        int index = 0;
        for (SequenceMessage message : diagram.getMessages()) {
            Rect from = sequenceParticipantBounds(diagram, message.getFromParticipantId()).orElse(null);
            Rect to = sequenceParticipantBounds(diagram, message.getToParticipantId()).orElse(null);
            if (from == null || to == null) continue;
            double y = sequenceMessageY(index);
            double x1 = from.center().x();
            double x2 = to.center().x();
            boolean selected = message.getId().equals(selection.messageId());
            Color lineColor = Color.web(selected ? SELECTION : TEXT_SECONDARY);
            String label = "#" + message.getOrder() + " " + message.getType().storageName() + ": " + message.getLabel();
            if (message.getType() == SequenceMessageType.SELF_CALL || message.getFromParticipantId().equals(message.getToParticipantId())) {
                drawSelfCall(g, x1, y, lineColor, label);
            } else {
                drawDirectedEdge(g, x1, y, x2, y, lineColor, label, message.getType() == SequenceMessageType.RETURN);
            }
            if (selected) {
                g.setStroke(Color.web(SELECTION));
                g.setLineWidth(2.0);
                double highlightWidth = Math.max(48, Math.abs(x2 - x1) - 12);
                g.strokeRoundRect(Math.min(x1, x2) + 6, y - 18, highlightWidth, 30, 8, 8);
            }
            if (message.isActivation()) {
                double activationX = x2 - 6;
                g.setFill(Color.web(PANEL_ELEVATED));
                g.fillRoundRect(activationX, y, 12, 52, 6, 6);
                g.setStroke(Color.web(SELECTION));
                g.strokeRoundRect(activationX, y, 12, 52, 6, 6);
            }
            index++;
        }
    }

    private Optional<Rect> sequenceParticipantBounds(Diagram diagram, String participantId) {
        return diagram.getParticipants().stream()
                .filter(participant -> participant.getId().equals(participantId))
                .findFirst()
                .map(participant -> diagram.getCanvasState().getBoundsByElementId().getOrDefault(participant.getId(), new Rect(64, 64, 140, 48)));
    }

    private void drawFkPreview(GraphicsContext g, Diagram diagram, FkPreview fkPreview) {
        if (fkPreview == null) return;
        Rect source = diagram.getCanvasState().getBoundsByElementId().get(fkPreview.getSourceTableId());
        Rect target = diagram.getCanvasState().getBoundsByElementId().get(fkPreview.getTargetTableId());
        if (source == null || target == null) return;
        g.save();
        g.setStroke(Color.web(WARNING));
        g.setLineWidth(2.5);
        g.setLineDashes(8, 6);
        g.strokeLine(source.center().x(), source.center().y(), target.center().x(), target.center().y());
        g.setLineDashes(0);
        drawPreviewChip(g, source.center().x() - 38, source.center().y() - 24, "SOURCE");
        drawPreviewChip(g, target.center().x() - 38, target.center().y() - 24, "TARGET");
        g.restore();
    }

    private void drawPreviewChip(GraphicsContext g, double x, double y, String text) {
        g.setFill(Color.web(WARNING));
        g.fillRoundRect(x, y, 76, 22, 10, 10);
        g.setStroke(Color.web(PK_COLOR));
        g.strokeRoundRect(x, y, 76, 22, 10, 10);
        g.setFill(Color.web(APP_BG));
        g.setFont(uiFont(10));
        g.fillText(text, x + 14, y + 15);
    }

    private static double sequenceMessageY(int index) {
        return 150 + index * 72.0;
    }

    private Font uiFont(double size) {
        return Font.font(UI_FONT_FAMILY, FontWeight.EXTRA_LIGHT, size);
    }

    public record Selection(
            String tableId,
            String columnId,
            String foreignKeyId,
            String participantId,
            String messageId
    ) {
    }
}
