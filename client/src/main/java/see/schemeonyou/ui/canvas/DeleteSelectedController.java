package see.schemeonyou.ui.canvas;

import see.schemeonyou.command.Command;
import see.schemeonyou.command.DeleteColumnCommand;
import see.schemeonyou.command.DeleteForeignKeyCommand;
import see.schemeonyou.command.DeletePreview;
import see.schemeonyou.command.DeletePreviewFactory;
import see.schemeonyou.command.DeleteSequenceMessageCommand;
import see.schemeonyou.command.DeleteSequenceParticipantCommand;
import see.schemeonyou.command.DeleteTableCommand;
import see.schemeonyou.command.UndoRedoStack;
import see.schemeonyou.model.DbColumn;
import see.schemeonyou.model.DbTable;
import see.schemeonyou.model.Diagram;
import see.schemeonyou.model.ForeignKey;
import see.schemeonyou.model.SelectionDepth;
import see.schemeonyou.model.SequenceMessage;
import see.schemeonyou.model.SequenceParticipant;

import java.util.Optional;

public class DeleteSelectedController {
    private final DeletePreviewFactory previewFactory = new DeletePreviewFactory();
    private DeletePreview pendingPreview;
    private Command pendingCommand;

    public Optional<DeletePreview> requestDelete(Diagram diagram, CanvasShell canvas) {
        pendingPreview = null;
        pendingCommand = null;
        Optional<String> selectedId = canvas.selectedElementId();
        if (selectedId.isEmpty()) {
            return Optional.empty();
        }

        if (canvas.selectionDepth() == SelectionDepth.TABLE) {
            return diagram.findTable(selectedId.get()).map(table -> {
                pendingCommand = new DeleteTableCommand(diagram, table);
                pendingPreview = previewFactory.table(diagram, table);
                return pendingPreview;
            });
        }
        if (canvas.selectionDepth() == SelectionDepth.COLUMN) {
            return findColumn(diagram, selectedId.get()).map(selection -> {
                pendingCommand = new DeleteColumnCommand(diagram, selection.table(), selection.column());
                pendingPreview = previewFactory.column(diagram, selection.table(), selection.column());
                return pendingPreview;
            });
        }
        if (canvas.selectionDepth() == SelectionDepth.RELATION) {
            return diagram.getForeignKeys().stream()
                    .filter(fk -> fk.getId().equals(selectedId.get()))
                    .findFirst()
                    .map(fk -> {
                        pendingCommand = new DeleteForeignKeyCommand(diagram, fk);
                        pendingPreview = previewFactory.foreignKey(fk);
                        return pendingPreview;
                    });
        }
        if (canvas.selectionDepth() == SelectionDepth.MESSAGE) {
            return findMessage(diagram, selectedId.get()).map(message -> {
                pendingCommand = new DeleteSequenceMessageCommand(diagram, message);
                pendingPreview = previewFactory.sequenceMessage(message);
                return pendingPreview;
            });
        }
        if (canvas.selectionDepth() == SelectionDepth.PARTICIPANT) {
            return findParticipant(diagram, selectedId.get()).map(participant -> {
                pendingCommand = new DeleteSequenceParticipantCommand(diagram, participant);
                pendingPreview = previewFactory.sequenceParticipant(diagram, participant);
                return pendingPreview;
            });
        }
        return Optional.empty();
    }

    public Optional<DeletePreview> pendingPreview() {
        return Optional.ofNullable(pendingPreview);
    }

    public Optional<DeletePreview> cancelDelete() {
        DeletePreview canceled = pendingPreview;
        pendingPreview = null;
        pendingCommand = null;
        return Optional.ofNullable(canceled);
    }

    public Optional<DeletePreview> confirmDelete(UndoRedoStack commands, CanvasShell canvas) {
        if (pendingCommand == null || pendingPreview == null) {
            return Optional.empty();
        }
        DeletePreview executed = pendingPreview;
        commands.run(pendingCommand);
        canvas.selectedElementId(null);
        pendingPreview = null;
        pendingCommand = null;
        return Optional.of(executed);
    }

    private Optional<ColumnSelection> findColumn(Diagram diagram, String columnId) {
        for (DbTable table : diagram.getTables()) {
            Optional<DbColumn> column = table.findColumn(columnId);
            if (column.isPresent()) {
                return Optional.of(new ColumnSelection(table, column.get()));
            }
        }
        return Optional.empty();
    }

    private Optional<SequenceMessage> findMessage(Diagram diagram, String messageId) {
        return diagram.getMessages().stream()
                .filter(message -> message.getId().equals(messageId))
                .findFirst();
    }

    private Optional<SequenceParticipant> findParticipant(Diagram diagram, String participantId) {
        return diagram.getParticipants().stream()
                .filter(participant -> participant.getId().equals(participantId))
                .findFirst();
    }

    private record ColumnSelection(DbTable table, DbColumn column) { }
}
