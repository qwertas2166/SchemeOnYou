package see.schemeonyou.importer;

import see.schemeonyou.command.Command;
import see.schemeonyou.command.CommandMetadata;
import see.schemeonyou.model.CanvasState;
import see.schemeonyou.model.DbColumn;
import see.schemeonyou.model.DbTable;
import see.schemeonyou.model.Diagram;
import see.schemeonyou.model.ForeignKey;
import see.schemeonyou.model.Rect;
import see.schemeonyou.model.SchemeProject;
import see.schemeonyou.model.SequenceMessage;
import see.schemeonyou.model.SequenceMessageType;
import see.schemeonyou.model.SequenceParticipant;
import see.schemeonyou.model.SequenceParticipantType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Undoable whole-diagram DB import replacement. */
public final class DbImportReplaceCommand implements Command {
    private static final CommandMetadata METADATA = CommandMetadata.of(
            "db.import.replace",
            "Import DB metadata and replace diagram",
            "",
            "database", "import", "replace"
    ).confirmable();

    private final SchemeProject project;
    private final String selectedDiagramId;
    private final DbImportResult importResult;
    private final DbImportDiagramReplacer replacer;
    private List<DiagramSnapshot> before;
    private List<DiagramSnapshot> after;
    private DbImportDiagramReplacer.Result result;

    public DbImportReplaceCommand(
            SchemeProject project,
            String selectedDiagramId,
            DbImportResult importResult,
            DbImportDiagramReplacer replacer
    ) {
        this.project = Objects.requireNonNull(project, "project");
        this.selectedDiagramId = selectedDiagramId;
        this.importResult = Objects.requireNonNull(importResult, "importResult");
        this.replacer = Objects.requireNonNull(replacer, "replacer");
    }

    @Override
    public CommandMetadata metadata() {
        return METADATA;
    }

    @Override
    public void execute() {
        if (before == null) {
            before = snapshotProject();
        }
        if (after == null) {
            result = replacer.replace(project, selectedDiagramId, importResult);
            after = snapshotProject();
        } else {
            restoreProject(after);
        }
        project.touch();
    }

    @Override
    public void undo() {
        if (before == null) return;
        restoreProject(before);
        project.touch();
    }

    public DbImportDiagramReplacer.Result result() {
        if (result == null) throw new IllegalStateException("Import replace command has not been executed");
        return result;
    }

    private List<DiagramSnapshot> snapshotProject() {
        return project.getDiagrams().stream().map(DiagramSnapshot::copyOf).toList();
    }

    private void restoreProject(List<DiagramSnapshot> snapshots) {
        project.getDiagrams().clear();
        snapshots.stream().map(DiagramSnapshot::restore).forEach(project.getDiagrams()::add);
    }

    private record DiagramSnapshot(
            String id,
            String name,
            see.schemeonyou.model.DiagramType type,
            double viewportX,
            double viewportY,
            double zoom,
            List<TableSnapshot> tables,
            List<ForeignKeySnapshot> foreignKeys,
            List<ParticipantSnapshot> participants,
            List<MessageSnapshot> messages,
            List<BoundsSnapshot> bounds
    ) {
        static DiagramSnapshot copyOf(Diagram diagram) {
            CanvasState canvas = diagram.getCanvasState();
            return new DiagramSnapshot(
                    diagram.getId(),
                    diagram.getName(),
                    diagram.getType(),
                    canvas.getViewportX(),
                    canvas.getViewportY(),
                    canvas.getZoom(),
                    diagram.getTables().stream().map(TableSnapshot::copyOf).toList(),
                    diagram.getForeignKeys().stream().map(ForeignKeySnapshot::copyOf).toList(),
                    diagram.getParticipants().stream().map(ParticipantSnapshot::copyOf).toList(),
                    diagram.getMessages().stream().map(MessageSnapshot::copyOf).toList(),
                    canvas.getBoundsByElementId().entrySet().stream()
                            .map(entry -> new BoundsSnapshot(entry.getKey(), entry.getValue()))
                            .toList()
            );
        }

        Diagram restore() {
            Diagram diagram = new Diagram(id, name, type);
            CanvasState canvas = diagram.getCanvasState();
            canvas.setViewportX(viewportX);
            canvas.setViewportY(viewportY);
            canvas.setZoom(zoom);
            bounds.forEach(bound -> canvas.getBoundsByElementId().put(bound.elementId(), bound.rect()));
            tables.stream().map(TableSnapshot::restore).forEach(diagram.getTables()::add);
            foreignKeys.stream().map(ForeignKeySnapshot::restore).forEach(diagram.getForeignKeys()::add);
            participants.stream().map(ParticipantSnapshot::restore).forEach(diagram.getParticipants()::add);
            messages.stream().map(MessageSnapshot::restore).forEach(diagram.getMessages()::add);
            return diagram;
        }
    }

    private record TableSnapshot(String id, String name, List<ColumnSnapshot> columns) {
        static TableSnapshot copyOf(DbTable table) {
            return new TableSnapshot(table.getId(), table.getName(), table.getColumns().stream().map(ColumnSnapshot::copyOf).toList());
        }
        DbTable restore() {
            DbTable table = new DbTable(id, name);
            columns.stream().map(ColumnSnapshot::restore).forEach(table.getColumns()::add);
            return table;
        }
    }

    private record ColumnSnapshot(String id, String name, String type, boolean primaryKey, boolean unique, boolean nullable) {
        static ColumnSnapshot copyOf(DbColumn column) {
            return new ColumnSnapshot(column.getId(), column.getName(), column.getType(), column.isPrimaryKey(), column.isUnique(), column.isNullable());
        }
        DbColumn restore() {
            DbColumn column = new DbColumn(id, name, type);
            column.setPrimaryKey(primaryKey);
            column.setUnique(unique);
            column.setNullable(nullable);
            return column;
        }
    }

    private record ForeignKeySnapshot(String id, String sourceTableId, String sourceColumnId, String targetTableId, String targetColumnId) {
        static ForeignKeySnapshot copyOf(ForeignKey foreignKey) {
            return new ForeignKeySnapshot(foreignKey.getId(), foreignKey.getSourceTableId(), foreignKey.getSourceColumnId(), foreignKey.getTargetTableId(), foreignKey.getTargetColumnId());
        }
        ForeignKey restore() {
            return new ForeignKey(id, sourceTableId, sourceColumnId, targetTableId, targetColumnId);
        }
    }

    private record ParticipantSnapshot(String id, String name, SequenceParticipantType type) {
        static ParticipantSnapshot copyOf(SequenceParticipant participant) {
            return new ParticipantSnapshot(participant.getId(), participant.getName(), participant.getType());
        }
        SequenceParticipant restore() {
            return new SequenceParticipant(id, name, type);
        }
    }

    private record MessageSnapshot(String id, String fromParticipantId, String toParticipantId, String label, SequenceMessageType type, int order, boolean activation) {
        static MessageSnapshot copyOf(SequenceMessage message) {
            return new MessageSnapshot(message.getId(), message.getFromParticipantId(), message.getToParticipantId(), message.getLabel(), message.getType(), message.getOrder(), message.isActivation());
        }
        SequenceMessage restore() {
            SequenceMessage message = new SequenceMessage(id, fromParticipantId, toParticipantId, label, type, order);
            message.setActivation(activation);
            return message;
        }
    }

    private record BoundsSnapshot(String elementId, Rect rect) { }
}
