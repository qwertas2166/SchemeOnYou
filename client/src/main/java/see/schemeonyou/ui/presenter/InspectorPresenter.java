package see.schemeonyou.ui.presenter;

import see.schemeonyou.model.DbColumn;
import see.schemeonyou.model.DbTable;
import see.schemeonyou.model.Diagram;
import see.schemeonyou.model.SequenceMessage;
import see.schemeonyou.model.SequenceParticipant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class InspectorPresenter {
    public InspectorModel inspect(Diagram diagram, Selection selection, Optional<FkPreviewModel> fkPreview) {
        Objects.requireNonNull(diagram);
        Objects.requireNonNull(selection);
        Objects.requireNonNull(fkPreview);

        List<Section> sections = new ArrayList<>();
        Optional<SequenceMessage> selectedMessage = selection.messageId()
                .flatMap(id -> diagram.getMessages().stream().filter(message -> message.getId().equals(id)).findFirst());
        Optional<SequenceParticipant> selectedParticipant = selectedMessage.isPresent()
                ? Optional.empty()
                : selection.participantId().flatMap(id -> findParticipant(diagram, id));
        Optional<DbTable> selectedTable = selectedMessage.isEmpty() && selectedParticipant.isEmpty()
                ? selection.tableId().flatMap(diagram::findTable)
                : Optional.empty();

        if (selectedMessage.isPresent()) {
            sections.add(sequenceMessageSection(diagram, selectedMessage.get()));
        } else if (selectedParticipant.isPresent()) {
            sections.add(sequenceParticipantSection(selectedParticipant.get()));
        } else if (selectedTable.isPresent()) {
            sections.add(tableSection(selectedTable.get(), selection.columnId()));
        } else {
            sections.add(new Section("Diagram", List.of(
                    Field.readOnly("Name", diagram.getName())
            )));
        }

        fkPreview.ifPresent(preview -> sections.add(new Section("FK preview", List.of(
                Field.readOnly("Source", preview.sourceLabel()),
                Field.readOnly("Target", preview.targetLabel()),
                Field.readOnly("Creates", "source column references target column"),
                Field.checkbox(EditField.FK_PREVIEW_KEEP_TARGET_PINNED, "Keep target pinned after create", preview.keepTargetPinnedAfterCreate()),
                Field.readOnly("Validation", preview.validationText()),
                Field.actionHint("Actions", "X Swap · Enter Create · Esc Cancel")
        ))));

        return new InspectorModel(sections);
    }

    public Optional<EditDecision> textEdit(EditField field, String original, String submitted) {
        Objects.requireNonNull(field);
        String normalizedOriginal = original == null ? "" : original;
        String value = submitted == null ? "" : submitted.trim();
        if (value.isBlank() || value.equals(normalizedOriginal)) {
            return Optional.empty();
        }
        return Optional.of(new EditDecision(field, value, false));
    }

    public Optional<EditDecision> booleanEdit(EditField field, boolean original, boolean submitted) {
        Objects.requireNonNull(field);
        if (original == submitted) {
            return Optional.empty();
        }
        return Optional.of(new EditDecision(field, Boolean.toString(submitted), submitted));
    }

    private Section sequenceMessageSection(Diagram diagram, SequenceMessage message) {
        return new Section("Sequence message", List.of(
                Field.text(EditField.SEQUENCE_MESSAGE_LABEL, "Label", message.getLabel()),
                Field.checkbox(EditField.SEQUENCE_MESSAGE_ACTIVATION, "Activation", message.isActivation()),
                Field.readOnly("From", participantName(diagram, message.getFromParticipantId())),
                Field.readOnly("To", participantName(diagram, message.getToParticipantId())),
                Field.actionHint("Delete", "Delete / Space D")
        ));
    }

    private Section sequenceParticipantSection(SequenceParticipant participant) {
        return new Section("Sequence participant", List.of(
                Field.text(EditField.SEQUENCE_PARTICIPANT_NAME, "Name", participant.getName()),
                Field.actionHint("Delete", "Delete / Space D")
        ));
    }

    private Section tableSection(DbTable table, Optional<String> selectedColumnId) {
        List<Field> fields = new ArrayList<>();
        fields.add(Field.text(EditField.TABLE_NAME, "Name", table.getName()));
        fields.add(Field.readOnly("Columns", Integer.toString(table.getColumns().size())));
        for (DbColumn column : table.getColumns()) {
            boolean selected = selectedColumnId.filter(column.getId()::equals).isPresent();
            String prefix = selected ? "Selected column: " : "Column: ";
            fields.add(Field.group(prefix + table.getName() + "." + column.getName()));
            fields.add(Field.text(EditField.COLUMN_NAME, "Name", column.getName(), column.getId(), selected));
            fields.add(Field.text(EditField.COLUMN_TYPE, "Type", column.getType(), column.getId(), selected));
            fields.add(Field.checkbox(EditField.COLUMN_NULLABLE, "Nullable", column.isNullable(), column.getId(), selected));
            fields.add(Field.checkbox(EditField.COLUMN_PRIMARY_KEY, "Primary key", column.isPrimaryKey(), column.getId(), selected));
            fields.add(Field.checkbox(EditField.COLUMN_UNIQUE, "Unique", column.isUnique(), column.getId(), selected));
        }
        return new Section("Table", fields);
    }

    private Optional<SequenceParticipant> findParticipant(Diagram diagram, String id) {
        return diagram.getParticipants().stream()
                .filter(participant -> participant.getId().equals(id))
                .findFirst();
    }

    private String participantName(Diagram diagram, String participantId) {
        return findParticipant(diagram, participantId)
                .map(SequenceParticipant::getName)
                .orElse(participantId);
    }

    public record Selection(Optional<String> tableId, Optional<String> columnId, Optional<String> participantId, Optional<String> messageId) {
        public Selection {
            tableId = tableId == null ? Optional.empty() : tableId;
            columnId = columnId == null ? Optional.empty() : columnId;
            participantId = participantId == null ? Optional.empty() : participantId;
            messageId = messageId == null ? Optional.empty() : messageId;
        }

        public static Selection of(String tableId, String columnId, String participantId, String messageId) {
            return new Selection(Optional.ofNullable(tableId), Optional.ofNullable(columnId), Optional.ofNullable(participantId), Optional.ofNullable(messageId));
        }
    }

    public record InspectorModel(List<Section> sections) {
        public InspectorModel {
            sections = List.copyOf(sections);
        }
    }

    public record Section(String title, List<Field> fields) {
        public Section {
            title = Objects.requireNonNull(title);
            fields = List.copyOf(fields);
        }
    }

    public record Field(FieldKind kind, EditField editField, String label, String value, boolean booleanValue, String subjectId, boolean selected) {
        public Field {
            kind = Objects.requireNonNull(kind);
            label = Objects.requireNonNull(label);
            value = value == null ? "" : value;
            subjectId = subjectId == null ? "" : subjectId;
        }

        public static Field text(EditField editField, String label, String value) {
            return text(editField, label, value, "", false);
        }

        public static Field text(EditField editField, String label, String value, String subjectId, boolean selected) {
            return new Field(FieldKind.TEXT, editField, label, value, false, subjectId, selected);
        }

        public static Field checkbox(EditField editField, String label, boolean value) {
            return checkbox(editField, label, value, "", false);
        }

        public static Field checkbox(EditField editField, String label, boolean value, String subjectId, boolean selected) {
            return new Field(FieldKind.CHECKBOX, editField, label, Boolean.toString(value), value, subjectId, selected);
        }

        public static Field readOnly(String label, String value) {
            return new Field(FieldKind.READ_ONLY, null, label, value, false, "", false);
        }

        public static Field group(String label) {
            return new Field(FieldKind.GROUP, null, label, "", false, "", false);
        }

        public static Field actionHint(String label, String value) {
            return new Field(FieldKind.ACTION_HINT, null, label, value, false, "", false);
        }
    }

    public record EditDecision(EditField field, String value, boolean booleanValue) {
        public EditDecision {
            field = Objects.requireNonNull(field);
            value = Objects.requireNonNull(value);
        }
    }

    public record FkPreviewModel(String sourceLabel, String targetLabel, boolean keepTargetPinnedAfterCreate, String validationText) {
        public FkPreviewModel {
            sourceLabel = Objects.requireNonNull(sourceLabel);
            targetLabel = Objects.requireNonNull(targetLabel);
            validationText = Objects.requireNonNull(validationText);
        }
    }

    public enum FieldKind {
        TEXT,
        CHECKBOX,
        READ_ONLY,
        GROUP,
        ACTION_HINT
    }

    public enum EditField {
        TABLE_NAME,
        COLUMN_NAME,
        COLUMN_TYPE,
        COLUMN_NULLABLE,
        COLUMN_PRIMARY_KEY,
        COLUMN_UNIQUE,
        SEQUENCE_PARTICIPANT_NAME,
        SEQUENCE_MESSAGE_LABEL,
        SEQUENCE_MESSAGE_ACTIVATION,
        FK_PREVIEW_KEEP_TARGET_PINNED
    }
}
