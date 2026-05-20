package see.schemeonyou.validation;

import see.di.See;
import see.schemeonyou.model.*;

import java.util.HashSet;
import java.util.Set;

@See
public class DiagramValidator {
    public ValidationResult validate(Diagram diagram) {
        ValidationResult result = new ValidationResult();
        if (diagram.getType() == DiagramType.DATABASE) validateDatabase(diagram, result);
        if (diagram.getType() == DiagramType.SEQUENCE) validateSequence(diagram, result);
        return result;
    }

    private void validateSequence(Diagram diagram, ValidationResult result) {
        Set<String> participantIds = new HashSet<>();
        for (SequenceParticipant participant : diagram.getParticipants()) {
            participantIds.add(participant.getId());
        }
        Set<Integer> orders = new HashSet<>();
        for (SequenceMessage message : diagram.getMessages()) {
            if (!participantIds.contains(message.getFromParticipantId()) || !participantIds.contains(message.getToParticipantId())) {
                result.error("sequence.message.endpoint.missing", "Sequence message references a missing participant", message.getId());
            }
            if (message.getOrder() <= 0 || !orders.add(message.getOrder())) {
                result.error("sequence.message.order.invalid", "Sequence message order must be positive and unique", message.getId());
            }
            boolean selfEndpoint = message.getFromParticipantId().equals(message.getToParticipantId());
            if (message.getType() == SequenceMessageType.SELF_CALL && !selfEndpoint) {
                result.error("sequence.message.self_call.endpoint", "Self-call message must target the same participant", message.getId());
            }
            if (message.getType() != SequenceMessageType.SELF_CALL && selfEndpoint) {
                result.warn("sequence.message.self_endpoint.type", "Message with the same source and target should use self_call type", message.getId());
            }
        }
    }

    private void validateDatabase(Diagram diagram, ValidationResult result) {
        for (DbTable table : diagram.getTables()) {
            validateTableConstraints(table, result);
        }
        for (ForeignKey fk : diagram.getForeignKeys()) {
            DbTable source = diagram.findTable(fk.getSourceTableId()).orElse(null);
            DbTable target = diagram.findTable(fk.getTargetTableId()).orElse(null);
            if (source == null || target == null) {
                result.error("fk.table.missing", "Foreign key references a missing table", fk.getId());
                continue;
            }
            DbColumn sourceColumn = source.findColumn(fk.getSourceColumnId()).orElse(null);
            DbColumn targetColumn = target.findColumn(fk.getTargetColumnId()).orElse(null);
            if (sourceColumn == null || targetColumn == null) {
                result.error("fk.column.missing", "Foreign key references a missing column", fk.getId());
                continue;
            }
            if (!sourceColumn.getType().equalsIgnoreCase(targetColumn.getType())) {
                result.warn("fk.type.mismatch", "Foreign key column types differ", fk.getId());
            }
            if (!DbRelationSemantics.isIndividuallyUnique(target, targetColumn)) {
                result.warn("fk.target.not.unique", "Target column is not PK or unique; relation meaning may be ambiguous", fk.getId());
            }
        }
    }

    private void validateTableConstraints(DbTable table, ValidationResult result) {
        Set<String> columnIds = new HashSet<>();
        for (DbColumn column : table.getColumns()) {
            columnIds.add(column.getId());
        }
        Set<String> constraintIds = new HashSet<>();
        for (DbTableConstraint constraint : table.getConstraints()) {
            if (constraint.columnIds().isEmpty()) {
                result.error("table.constraint.column.count", "Table constraint must reference at least one column", table.getId());
            }
            if (!constraintIds.add(constraint.id())) {
                result.error("table.constraint.id.duplicate", "Table constraint id must be unique within table", table.getId());
            }
            Set<String> seenColumnIds = new HashSet<>();
            for (String columnId : constraint.columnIds()) {
                if (!columnIds.contains(columnId)) {
                    result.error("table.constraint.column.missing", "Table constraint references a missing column", table.getId());
                }
                if (!seenColumnIds.add(columnId)) {
                    result.error("table.constraint.column.duplicate", "Table constraint must not reference the same column twice", table.getId());
                }
            }
        }
    }
}
