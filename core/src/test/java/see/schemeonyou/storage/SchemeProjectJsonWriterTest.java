package see.schemeonyou.storage;

import org.junit.jupiter.api.Test;
import see.schemeonyou.model.*;

import static org.junit.jupiter.api.Assertions.*;

class SchemeProjectJsonWriterTest {
    @Test
    void writeIsDeterministicAndSortsCanvasBoundsByElementId() {
        SchemeProject project = new SchemeProject("project-1", "Demo");
        Diagram diagram = new Diagram("diagram-1", "Database", DiagramType.DATABASE);
        DbTable users = new DbTable("table-1", "users");
        users.getColumns().add(new DbColumn("column-1", "id", "uuid"));
        diagram.getTables().add(users);
        diagram.getCanvasState().getBoundsByElementId().put("z", new Rect(2, 2, 10, 10));
        diagram.getCanvasState().getBoundsByElementId().put("a", new Rect(1, 1, 20, 20));
        project.getDiagrams().add(diagram);

        SchemeProjectJsonWriter writer = new SchemeProjectJsonWriter();
        String first = writer.write(project);
        String second = writer.write(project);

        assertEquals(first, second);
        assertTrue(first.indexOf("\"a\"") < first.indexOf("\"z\""));
        assertTrue(first.contains("\"version\": 1"));
    }

    @Test
    void writeEscapesJsonStringValues() {
        SchemeProject project = new SchemeProject("project-1", "Demo \"quoted\"");
        project.getDiagrams().add(new Diagram("diagram-1", "Line\nBreak", DiagramType.SEQUENCE));

        String json = new SchemeProjectJsonWriter().write(project);

        assertTrue(json.contains("Demo \\\"quoted\\\""));
        assertTrue(json.contains("Line\\nBreak"));
    }

    @Test
    void writeEscapesAllJsonControlCharactersAndRoundTrips() throws Exception {
        String special = "quote\" slash\\ newline\n carriage\r tab\t back\b form\f zero" + (char) 0x01;
        SchemeProject project = new SchemeProject("project-1", special);
        Diagram database = new Diagram("diagram-1", special, DiagramType.DATABASE);
        DbTable table = new DbTable("table-1", special);
        table.getColumns().add(new DbColumn("column-1", special, special));
        database.getTables().add(table);
        project.getDiagrams().add(database);

        Diagram sequence = new Diagram("diagram-2", "Sequence", DiagramType.SEQUENCE);
        sequence.getParticipants().add(new SequenceParticipant("participant-1", special));
        sequence.getParticipants().add(new SequenceParticipant("participant-2", "Target"));
        sequence.getMessages().add(new SequenceMessage("message-1", "participant-1", "participant-2", special));
        project.getDiagrams().add(sequence);

        SchemeProjectJsonWriter writer = new SchemeProjectJsonWriter();
        String json = writer.write(project);
        SchemeProject loaded = new SchemeProjectJsonReader().read(json);

        assertFalse(json.contains("carriage\r"));
        assertFalse(json.contains("tab\t"));
        assertTrue(json.contains("carriage\\r"));
        assertTrue(json.contains("tab\\t"));
        assertTrue(json.contains("back\\b"));
        assertTrue(json.contains("form\\f"));
        assertTrue(json.contains("zero\\u0001"));
        assertEquals(special, loaded.getName());
        assertEquals(special, loaded.getDiagrams().getFirst().getTables().getFirst().getColumns().getFirst().getType());
        assertEquals(special, loaded.getDiagrams().get(1).getMessages().getFirst().getLabel());
        assertEquals(json, writer.write(loaded));
    }
}
