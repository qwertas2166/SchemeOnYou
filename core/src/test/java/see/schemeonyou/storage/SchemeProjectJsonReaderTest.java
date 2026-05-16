package see.schemeonyou.storage;

import org.junit.jupiter.api.Test;
import see.schemeonyou.model.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SchemeProjectJsonReaderTest {
    @Test
    void readsWriterFormatAndSupportsRoundTrip() throws Exception {
        SchemeProject project = new SchemeProject("project-1", "Demo");
        Diagram database = new Diagram("diagram-1", "Database", DiagramType.DATABASE);
        DbTable users = new DbTable("table-1", "users");
        DbColumn id = new DbColumn("column-1", "id", "uuid");
        id.setPrimaryKey(true);
        id.setNullable(false);
        users.getColumns().add(id);
        database.getTables().add(users);
        database.getCanvasState().setViewportX(10);
        database.getCanvasState().setViewportY(20);
        database.getCanvasState().setZoom(1.5);
        database.getCanvasState().getBoundsByElementId().put("table-1", new Rect(1, 2, 300, 120));
        project.getDiagrams().add(database);

        Diagram sequence = new Diagram("diagram-2", "Sequence", DiagramType.SEQUENCE);
        sequence.getParticipants().add(new SequenceParticipant("participant-1", "Client"));
        sequence.getParticipants().add(new SequenceParticipant("participant-2", "API"));
        SequenceMessage message = new SequenceMessage("message-1", "participant-1", "participant-2", "request");
        message.setActivation(true);
        sequence.getMessages().add(message);
        project.getDiagrams().add(sequence);

        SchemeProjectJsonWriter writer = new SchemeProjectJsonWriter();
        SchemeProject loaded = new SchemeProjectJsonReader().read(writer.write(project));

        assertEquals(writer.write(project), writer.write(loaded));
        assertEquals("Demo", loaded.getName());
        assertEquals(2, loaded.getDiagrams().size());
        assertEquals("users", loaded.getDiagrams().getFirst().getTables().getFirst().getName());
        assertTrue(loaded.getDiagrams().get(1).getMessages().getFirst().isActivation());
    }

    @Test
    void rejectsUnsupportedStorageVersion() {
        IOException error = assertThrows(IOException.class, () -> new SchemeProjectJsonReader().read("""
                {"version": 999, "id": "p", "name": "Demo", "createdAt": "2026-01-01T00:00:00Z", "updatedAt": "2026-01-01T00:00:00Z", "diagrams": []}
                """));

        assertTrue(error.getMessage().contains("Unsupported project storage version"));
    }

    @Test
    void reportsInvalidDiagramTypeAsFormatError() {
        IOException error = assertThrows(IOException.class, () -> new SchemeProjectJsonReader().read("""
                {"version": 1, "id": "p", "name": "Demo", "createdAt": "2026-01-01T00:00:00Z", "updatedAt": "2026-01-01T00:00:00Z", "diagrams": [
                  {"id":"d","name":"Broken","type":"FLOW","canvas":{"viewportX":0,"viewportY":0,"zoom":1,"bounds":{}},"tables":[],"foreignKeys":[],"participants":[],"messages":[]}
                ]}
                """));

        assertTrue(error.getMessage().contains("Invalid diagram type"));
    }

    @Test
    void reportsInvalidUnicodeEscapeAsFormatError() {
        IOException error = assertThrows(IOException.class, () -> new SchemeProjectJsonReader().read("""
                {"version": 1, "id": "p", "name": "Broken\\u00XZ", "createdAt": "2026-01-01T00:00:00Z", "updatedAt": "2026-01-01T00:00:00Z", "diagrams": []}
                """));

        assertTrue(error.getMessage().contains("Invalid unicode escape"));
    }

    @Test
    void reportsRawControlCharactersInStringsAsFormatError() {
        IOException error = assertThrows(IOException.class, () -> new SchemeProjectJsonReader().read("""
                {"version": 1, "id": "p", "name": "Broken
                name", "createdAt": "2026-01-01T00:00:00Z", "updatedAt": "2026-01-01T00:00:00Z", "diagrams": []}
                """));

        assertTrue(error.getMessage().contains("Invalid control character"));
    }

    @Test
    void reportsNonFiniteNumbersAsFormatError() {
        IOException error = assertThrows(IOException.class, () -> new SchemeProjectJsonReader().read("""
                {"version": 1, "id": "p", "name": "Broken", "createdAt": "2026-01-01T00:00:00Z", "updatedAt": "2026-01-01T00:00:00Z", "diagrams": [
                  {"id":"d","name":"Broken","type":"DATABASE","canvas":{"viewportX":0,"viewportY":0,"zoom":1e9999,"bounds":{}},"tables":[],"foreignKeys":[],"participants":[],"messages":[]}
                ]}
                """));

        assertTrue(error.getMessage().contains("Invalid number"));
    }

    @Test
    void reportsLeadingZeroNumbersAsFormatError() {
        IOException error = assertThrows(IOException.class, () -> new SchemeProjectJsonReader().read("""
                {"version": 01, "id": "p", "name": "Broken", "createdAt": "2026-01-01T00:00:00Z", "updatedAt": "2026-01-01T00:00:00Z", "diagrams": []}
                """));

        assertTrue(error.getMessage().contains("Invalid number"));
    }


    @Test
    void rejectsCanvasZoomOutsideSupportedRange() {
        IOException error = assertThrows(IOException.class, () -> new SchemeProjectJsonReader().read("""
                {"version": 1, "id": "p", "name": "Broken", "createdAt": "2026-01-01T00:00:00Z", "updatedAt": "2026-01-01T00:00:00Z", "diagrams": [
                  {"id":"d","name":"Broken","type":"DATABASE","canvas":{"viewportX":0,"viewportY":0,"zoom":9,"bounds":{}},"tables":[],"foreignKeys":[],"participants":[],"messages":[]}
                ]}
                """));

        assertTrue(error.getMessage().contains("Expected number in range 0.25..3.0 at canvas.zoom"));
    }

    @Test
    void rejectsNonPositiveCanvasBoundsSize() {
        IOException error = assertThrows(IOException.class, () -> new SchemeProjectJsonReader().read("""
                {"version": 1, "id": "p", "name": "Broken", "createdAt": "2026-01-01T00:00:00Z", "updatedAt": "2026-01-01T00:00:00Z", "diagrams": [
                  {"id":"d","name":"Broken","type":"DATABASE","canvas":{"viewportX":0,"viewportY":0,"zoom":1,"bounds":{"t":{"x":0,"y":0,"width":0,"height":120}}},"tables":[],"foreignKeys":[],"participants":[],"messages":[]}
                ]}
                """));

        assertTrue(error.getMessage().contains("Expected positive number at rect.width"));
    }

    @Test
    void rejectsIntegerFieldsOutsideIntRange() {
        IOException error = assertThrows(IOException.class, () -> new SchemeProjectJsonReader().read("""
                {"version": 2147483648, "id": "p", "name": "Broken", "createdAt": "2026-01-01T00:00:00Z", "updatedAt": "2026-01-01T00:00:00Z", "diagrams": []}
                """));

        assertTrue(error.getMessage().contains("Expected integer at version"));
    }

    @Test
    void rejectsNonJsonWhitespaceOutsideStrings() {
        IOException error = assertThrows(IOException.class, () -> new SchemeProjectJsonReader().read("{\f\"version\": 1, \"id\": \"p\", \"name\": \"Broken\", \"createdAt\": \"2026-01-01T00:00:00Z\", \"updatedAt\": \"2026-01-01T00:00:00Z\", \"diagrams\": []}"));

        assertTrue(error.getMessage().contains("Expected"));
    }

    @Test
    void rejectsDuplicateObjectKeys() {
        IOException error = assertThrows(IOException.class, () -> new SchemeProjectJsonReader().read("""
                {"version": 1, "id": "p", "name": "Demo", "name": "Shadow", "createdAt": "2026-01-01T00:00:00Z", "updatedAt": "2026-01-01T00:00:00Z", "diagrams": []}
                """));

        assertTrue(error.getMessage().contains("Duplicate object key"));
    }

    @Test
    void rejectsDuplicateDiagramIds() {
        IOException error = assertThrows(IOException.class, () -> new SchemeProjectJsonReader().read("""
                {"version": 1, "id": "p", "name": "Demo", "createdAt": "2026-01-01T00:00:00Z", "updatedAt": "2026-01-01T00:00:00Z", "diagrams": [
                  {"id":"d","name":"One","type":"DATABASE","canvas":{"viewportX":0,"viewportY":0,"zoom":1,"bounds":{}},"tables":[],"foreignKeys":[],"participants":[],"messages":[]},
                  {"id":"d","name":"Two","type":"DATABASE","canvas":{"viewportX":0,"viewportY":0,"zoom":1,"bounds":{}},"tables":[],"foreignKeys":[],"participants":[],"messages":[]}
                ]}
                """));

        assertTrue(error.getMessage().contains("Duplicate id at diagram.id"));
    }

    @Test
    void rejectsDuplicateElementIdsInsideDiagram() {
        IOException error = assertThrows(IOException.class, () -> new SchemeProjectJsonReader().read("""
                {"version": 1, "id": "p", "name": "Demo", "createdAt": "2026-01-01T00:00:00Z", "updatedAt": "2026-01-01T00:00:00Z", "diagrams": [
                  {"id":"d","name":"Database","type":"DATABASE","canvas":{"viewportX":0,"viewportY":0,"zoom":1,"bounds":{}},"tables":[
                    {"id":"t","name":"users","columns":[]},
                    {"id":"t","name":"orders","columns":[]}
                  ],"foreignKeys":[],"participants":[],"messages":[]}
                ]}
                """));

        assertTrue(error.getMessage().contains("Duplicate id at table.id"));
    }

    @Test
    void rejectsDuplicateColumnIdsInsideTable() {
        IOException error = assertThrows(IOException.class, () -> new SchemeProjectJsonReader().read("""
                {"version": 1, "id": "p", "name": "Demo", "createdAt": "2026-01-01T00:00:00Z", "updatedAt": "2026-01-01T00:00:00Z", "diagrams": [
                  {"id":"d","name":"Database","type":"DATABASE","canvas":{"viewportX":0,"viewportY":0,"zoom":1,"bounds":{}},"tables":[
                    {"id":"t","name":"users","columns":[
                      {"id":"c","name":"id","type":"uuid","primaryKey":true,"unique":true,"nullable":false},
                      {"id":"c","name":"email","type":"text","primaryKey":false,"unique":true,"nullable":false}
                    ]}
                  ],"foreignKeys":[],"participants":[],"messages":[]}
                ]}
                """));

        assertTrue(error.getMessage().contains("Duplicate id at column.id"));
    }
}
