package see.schemeonyou.ui.presenter;

import org.junit.jupiter.api.Test;
import see.schemeonyou.model.DbColumn;
import see.schemeonyou.model.DbTable;
import see.schemeonyou.model.Diagram;
import see.schemeonyou.model.DiagramType;
import see.schemeonyou.model.SequenceMessage;
import see.schemeonyou.model.SequenceParticipant;
import see.schemeonyou.ui.presenter.InspectorPresenter.EditField;
import see.schemeonyou.ui.presenter.InspectorPresenter.Field;
import see.schemeonyou.ui.presenter.InspectorPresenter.FieldKind;
import see.schemeonyou.ui.presenter.InspectorPresenter.InspectorModel;
import see.schemeonyou.ui.presenter.InspectorPresenter.Selection;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InspectorPresenterTest {
    private final InspectorPresenter presenter = new InspectorPresenter();

    @Test
    void databaseTableAndColumnInspectorModelIsHeadless() {
        Diagram diagram = databaseDiagram();

        InspectorModel model = presenter.inspect(diagram, Selection.of("users", "email", null, null), Optional.empty());

        assertEquals("Table", model.sections().getFirst().title());
        assertField(model, FieldKind.TEXT, EditField.TABLE_NAME, "Name", "Users", "", false);
        assertField(model, FieldKind.GROUP, null, "Selected column: Users.email", "", "", false);
        assertField(model, FieldKind.TEXT, EditField.COLUMN_NAME, "Name", "email", "email", true);
        assertField(model, FieldKind.TEXT, EditField.COLUMN_TYPE, "Type", "varchar", "email", true);
        assertField(model, FieldKind.CHECKBOX, EditField.COLUMN_NULLABLE, "Nullable", "true", "email", true);
        assertField(model, FieldKind.CHECKBOX, EditField.COLUMN_PRIMARY_KEY, "Primary key", "false", "email", true);
        assertField(model, FieldKind.CHECKBOX, EditField.COLUMN_UNIQUE, "Unique", "true", "email", true);
    }

    @Test
    void sequenceParticipantAndMessageModelsExposeEditableAndReadonlyFields() {
        Diagram diagram = sequenceDiagram();

        InspectorModel participant = presenter.inspect(diagram, Selection.of(null, null, "api", null), Optional.empty());
        assertEquals("Sequence participant", participant.sections().getFirst().title());
        assertField(participant, FieldKind.TEXT, EditField.SEQUENCE_PARTICIPANT_NAME, "Name", "API", "", false);
        assertField(participant, FieldKind.ACTION_HINT, null, "Delete", "Delete / Space D", "", false);

        InspectorModel message = presenter.inspect(diagram, Selection.of(null, null, "web", "m1"), Optional.empty());
        assertEquals("Sequence message", message.sections().getFirst().title());
        assertField(message, FieldKind.TEXT, EditField.SEQUENCE_MESSAGE_LABEL, "Label", "GET /orders", "", false);
        assertField(message, FieldKind.CHECKBOX, EditField.SEQUENCE_MESSAGE_ACTIVATION, "Activation", "true", "", false);
        assertField(message, FieldKind.READ_ONLY, null, "From", "Web", "", false);
        assertField(message, FieldKind.READ_ONLY, null, "To", "API", "", false);
    }

    @Test
    void editDecisionsIgnoreBlankAndUnchangedValues() {
        assertTrue(presenter.textEdit(EditField.TABLE_NAME, "Users", " Users ").isEmpty());
        assertTrue(presenter.textEdit(EditField.TABLE_NAME, "Users", "   ").isEmpty());

        var textDecision = presenter.textEdit(EditField.TABLE_NAME, "Users", "Accounts").orElseThrow();
        assertEquals(EditField.TABLE_NAME, textDecision.field());
        assertEquals("Accounts", textDecision.value());

        assertTrue(presenter.booleanEdit(EditField.COLUMN_UNIQUE, true, true).isEmpty());
        var boolDecision = presenter.booleanEdit(EditField.COLUMN_UNIQUE, false, true).orElseThrow();
        assertEquals(EditField.COLUMN_UNIQUE, boolDecision.field());
        assertTrue(boolDecision.booleanValue());
    }

    @Test
    void fkPreviewIsRepresentedWithoutJavaFxControls() {
        Diagram diagram = databaseDiagram();
        var preview = new InspectorPresenter.FkPreviewModel("Orders.user_id", "Users.id", true, "No blocking issues");

        InspectorModel model = presenter.inspect(diagram, Selection.of("users", null, null, null), Optional.of(preview));

        var fkSection = model.sections().get(1);
        assertEquals("FK preview", fkSection.title());
        assertTrue(fkSection.fields().stream().anyMatch(field -> field.kind() == FieldKind.CHECKBOX
                && field.editField() == EditField.FK_PREVIEW_KEEP_TARGET_PINNED
                && field.booleanValue()));
        assertTrue(fkSection.fields().stream().anyMatch(field -> field.kind() == FieldKind.ACTION_HINT
                && field.value().contains("Enter Create")));
    }

    private void assertField(InspectorModel model, FieldKind kind, EditField editField, String label, String value, String subjectId, boolean selected) {
        assertTrue(model.sections().stream()
                .flatMap(section -> section.fields().stream())
                .anyMatch(field -> matches(field, kind, editField, label, value, subjectId, selected)),
                () -> "Missing field " + kind + " " + editField + " " + label + "=" + value + " subject=" + subjectId + " selected=" + selected);
    }

    private boolean matches(Field field, FieldKind kind, EditField editField, String label, String value, String subjectId, boolean selected) {
        return field.kind() == kind
                && field.editField() == editField
                && field.label().equals(label)
                && field.value().equals(value)
                && field.subjectId().equals(subjectId)
                && field.selected() == selected;
    }

    private Diagram databaseDiagram() {
        Diagram diagram = new Diagram("db", "DB", DiagramType.DATABASE);
        DbTable users = new DbTable("users", "Users");
        DbColumn id = new DbColumn("id", "id", "uuid");
        id.setPrimaryKey(true);
        id.setNullable(false);
        DbColumn email = new DbColumn("email", "email", "varchar");
        email.setUnique(true);
        users.getColumns().add(id);
        users.getColumns().add(email);
        diagram.getTables().add(users);
        return diagram;
    }

    private Diagram sequenceDiagram() {
        Diagram diagram = new Diagram("seq", "Sequence", DiagramType.SEQUENCE);
        diagram.getParticipants().add(new SequenceParticipant("web", "Web"));
        diagram.getParticipants().add(new SequenceParticipant("api", "API"));
        SequenceMessage message = new SequenceMessage("m1", "web", "api", "GET /orders");
        message.setActivation(true);
        diagram.getMessages().add(message);
        return diagram;
    }
}
