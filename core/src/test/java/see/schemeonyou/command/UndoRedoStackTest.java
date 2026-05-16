package see.schemeonyou.command;

import org.junit.jupiter.api.Test;
import see.schemeonyou.model.DbColumn;
import see.schemeonyou.model.DbTable;
import see.schemeonyou.model.Diagram;
import see.schemeonyou.model.DiagramType;

import static org.junit.jupiter.api.Assertions.*;

class UndoRedoStackTest {
    @Test
    void runUndoRedoKeepsDiagramMutationsReversible() {
        Diagram diagram = new Diagram("diagram-1", "Database", DiagramType.DATABASE);
        DbTable table = new DbTable("table-1", "users");
        UndoRedoStack stack = new UndoRedoStack();

        stack.run(new AddTableCommand(diagram, table));

        assertTrue(stack.canUndo());
        assertFalse(stack.canRedo());
        assertEquals(1, diagram.getTables().size());

        CommandMetadata undo = stack.undo().orElseThrow();

        assertEquals("db.table.add", undo.id());
        assertTrue(diagram.getTables().isEmpty());
        assertFalse(stack.canUndo());
        assertTrue(stack.canRedo());

        CommandMetadata redo = stack.redo().orElseThrow();

        assertEquals("db.table.add", redo.id());
        assertEquals(table, diagram.getTables().getFirst());
    }

    @Test
    void newCommandClearsRedoHistory() {
        DbTable table = new DbTable("table-1", "users");
        DbColumn id = new DbColumn("column-1", "id", "uuid");
        DbColumn email = new DbColumn("column-2", "email", "text");
        UndoRedoStack stack = new UndoRedoStack();

        stack.run(new AddColumnCommand(table, id));
        stack.undo();
        assertTrue(stack.canRedo());

        stack.run(new AddColumnCommand(table, email));

        assertFalse(stack.canRedo());
        assertEquals(1, table.getColumns().size());
        assertEquals(email, table.getColumns().getFirst());
    }
}
