package see.schemeonyou.ui.canvas;

import org.junit.jupiter.api.Test;
import see.schemeonyou.command.DeletePreview;
import see.schemeonyou.command.UndoRedoStack;
import see.schemeonyou.model.DbColumn;
import see.schemeonyou.model.DbTable;
import see.schemeonyou.model.Diagram;
import see.schemeonyou.model.DiagramType;
import see.schemeonyou.model.ForeignKey;
import see.schemeonyou.model.SelectionDepth;
import see.schemeonyou.model.SequenceMessage;
import see.schemeonyou.model.SequenceParticipant;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeleteSelectedControllerTest {
    @Test
    void deletesSelectedColumnWithAffectedForeignKeyAsUndoableCommand() {
        Fixture fixture = fixture();
        CanvasShell canvas = new CanvasShell();
        canvas.selectionDepth(SelectionDepth.COLUMN);
        canvas.selectedElementId("orders.user_id");
        UndoRedoStack undo = new UndoRedoStack();
        DeleteSelectedController controller = new DeleteSelectedController();

        DeletePreview preview = controller.requestDelete(fixture.diagram, canvas).orElseThrow();
        assertEquals("db.column.delete", preview.commandId());
        assertEquals(1, preview.foreignKeysRemoved());

        controller.confirmDelete(undo, canvas);
        assertTrue(fixture.orders.findColumn("orders.user_id").isEmpty());
        assertTrue(fixture.diagram.getForeignKeys().isEmpty());

        undo.undo();
        assertTrue(fixture.orders.findColumn("orders.user_id").isPresent());
        assertEquals(1, fixture.diagram.getForeignKeys().size());
    }

    @Test
    void deletesSelectedForeignKeyAsUndoableCommand() {
        Fixture fixture = fixture();
        CanvasShell canvas = new CanvasShell();
        canvas.selectionDepth(SelectionDepth.RELATION);
        canvas.selectedElementId("fk.orders.user");
        UndoRedoStack undo = new UndoRedoStack();
        DeleteSelectedController controller = new DeleteSelectedController();

        DeletePreview preview = controller.requestDelete(fixture.diagram, canvas).orElseThrow();
        assertEquals("db.fk.delete", preview.commandId());

        controller.confirmDelete(undo, canvas);
        assertTrue(fixture.diagram.getForeignKeys().isEmpty());

        undo.undo();
        assertEquals("fk.orders.user", fixture.diagram.getForeignKeys().getFirst().getId());
    }

    @Test
    void deletesSelectedSequenceMessageAsUndoableCommand() {
        SequenceFixture fixture = sequenceFixture();
        CanvasShell canvas = new CanvasShell();
        canvas.selectionDepth(SelectionDepth.MESSAGE);
        canvas.selectedElementId("m1");
        UndoRedoStack undo = new UndoRedoStack();
        DeleteSelectedController controller = new DeleteSelectedController();

        DeletePreview preview = controller.requestDelete(fixture.diagram, canvas).orElseThrow();
        assertEquals("sequence.message.delete", preview.commandId());
        assertEquals(1, preview.sequenceMessagesRemoved());

        controller.confirmDelete(undo, canvas);
        assertEquals(List.of("m2"), fixture.diagram.getMessages().stream().map(SequenceMessage::getId).toList());
        assertTrue(canvas.selectedElementId().isEmpty());

        undo.undo();
        assertEquals(List.of("m1", "m2"), fixture.diagram.getMessages().stream().map(SequenceMessage::getId).toList());
    }

    @Test
    void deletesSelectedSequenceParticipantWithCascadePreviewAsUndoableCommand() {
        SequenceFixture fixture = sequenceFixture();
        CanvasShell canvas = new CanvasShell();
        canvas.selectionDepth(SelectionDepth.PARTICIPANT);
        canvas.selectedElementId("api");
        UndoRedoStack undo = new UndoRedoStack();
        DeleteSelectedController controller = new DeleteSelectedController();

        DeletePreview preview = controller.requestDelete(fixture.diagram, canvas).orElseThrow();
        assertEquals("sequence.participant.delete", preview.commandId());
        assertEquals(1, preview.sequenceParticipantsRemoved());
        assertEquals(2, preview.sequenceMessagesRemoved());
        assertEquals(List.of("m1", "m2"), preview.affectedSequenceMessageIds());

        controller.confirmDelete(undo, canvas);
        assertEquals(List.of("web"), fixture.diagram.getParticipants().stream().map(SequenceParticipant::getId).toList());
        assertTrue(fixture.diagram.getMessages().isEmpty());
        assertTrue(canvas.selectedElementId().isEmpty());

        undo.undo();
        assertEquals(List.of("web", "api"), fixture.diagram.getParticipants().stream().map(SequenceParticipant::getId).toList());
        assertEquals(List.of("m1", "m2"), fixture.diagram.getMessages().stream().map(SequenceMessage::getId).toList());
    }

    private Fixture fixture() {
        Diagram diagram = new Diagram("d", "Database", DiagramType.DATABASE);
        DbTable users = new DbTable("users", "users");
        DbColumn usersId = new DbColumn("users.id", "id", "uuid");
        users.getColumns().add(usersId);
        DbTable orders = new DbTable("orders", "orders");
        DbColumn ordersId = new DbColumn("orders.id", "id", "uuid");
        DbColumn userId = new DbColumn("orders.user_id", "user_id", "uuid");
        orders.getColumns().add(ordersId);
        orders.getColumns().add(userId);
        ForeignKey fk = new ForeignKey("fk.orders.user", orders.getId(), userId.getId(), users.getId(), usersId.getId());
        diagram.getTables().add(users);
        diagram.getTables().add(orders);
        diagram.getForeignKeys().add(fk);
        return new Fixture(diagram, orders);
    }

    private SequenceFixture sequenceFixture() {
        Diagram diagram = new Diagram("seq", "Checkout", DiagramType.SEQUENCE);
        SequenceParticipant web = new SequenceParticipant("web", "Web");
        SequenceParticipant api = new SequenceParticipant("api", "API");
        diagram.getParticipants().add(web);
        diagram.getParticipants().add(api);
        diagram.getMessages().add(new SequenceMessage("m1", web.getId(), api.getId(), "POST /checkout"));
        diagram.getMessages().add(new SequenceMessage("m2", api.getId(), web.getId(), "200 OK"));
        return new SequenceFixture(diagram);
    }

    private record Fixture(Diagram diagram, DbTable orders) { }
    private record SequenceFixture(Diagram diagram) { }
}
