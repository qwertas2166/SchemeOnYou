package see.schemeonyou.command;

import see.schemeonyou.model.DbColumn;
import see.schemeonyou.model.DbTable;
import see.schemeonyou.model.Diagram;
import see.schemeonyou.model.ForeignKey;
import see.schemeonyou.model.SequenceMessage;
import see.schemeonyou.model.SequenceParticipant;

import java.util.List;

public class DeletePreviewFactory {
    public DeletePreview table(Diagram diagram, DbTable table) {
        List<String> fkIds = diagram.getForeignKeys().stream()
                .filter(fk -> fk.getSourceTableId().equals(table.getId()) || fk.getTargetTableId().equals(table.getId()))
                .map(ForeignKey::getId)
                .toList();
        return new DeletePreview("db.table.delete", "Delete table", table.getId(), table.getName(),
                1, table.getColumns().size(), fkIds.size(), fkIds);
    }

    public DeletePreview column(Diagram diagram, DbTable table, DbColumn column) {
        List<String> fkIds = diagram.getForeignKeys().stream()
                .filter(fk -> (fk.getSourceTableId().equals(table.getId()) && fk.getSourceColumnId().equals(column.getId()))
                        || (fk.getTargetTableId().equals(table.getId()) && fk.getTargetColumnId().equals(column.getId())))
                .map(ForeignKey::getId)
                .toList();
        return new DeletePreview("db.column.delete", "Delete column", column.getId(), table.getName() + "." + column.getName(),
                0, 1, fkIds.size(), fkIds);
    }

    public DeletePreview foreignKey(ForeignKey foreignKey) {
        return new DeletePreview("db.fk.delete", "Delete foreign key", foreignKey.getId(), foreignKey.getId(),
                0, 0, 1, List.of(foreignKey.getId()));
    }

    public DeletePreview sequenceMessage(SequenceMessage message) {
        return new DeletePreview("sequence.message.delete", "Delete sequence message", message.getId(), message.getLabel(),
                0, 0, 0, List.of(), 0, 1, List.of(message.getId()));
    }

    public DeletePreview sequenceParticipant(Diagram diagram, SequenceParticipant participant) {
        List<String> messageIds = diagram.getMessages().stream()
                .filter(message -> message.getFromParticipantId().equals(participant.getId())
                        || message.getToParticipantId().equals(participant.getId()))
                .map(SequenceMessage::getId)
                .toList();
        return new DeletePreview("sequence.participant.delete", "Delete sequence participant", participant.getId(), participant.getName(),
                0, 0, 0, List.of(), 1, messageIds.size(), messageIds);
    }
}
