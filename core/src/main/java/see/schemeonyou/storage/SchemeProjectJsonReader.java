package see.schemeonyou.storage;

import see.di.See;
import see.schemeonyou.model.*;

import java.io.IOException;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@See
public class SchemeProjectJsonReader {
    public SchemeProject read(String json) throws IOException {
        Object root = new Parser(json).parse();
        Map<String, Object> object = object(root, "project");
        int version = integer(object.get("version"), "version");
        if (version != SchemeProject.STORAGE_VERSION) {
            throw new IOException("Unsupported project storage version: " + version);
        }

        SchemeProject project = new SchemeProject(
                string(object.get("id"), "id"),
                string(object.get("name"), "name"),
                instant(object.get("createdAt"), "createdAt"),
                instant(object.get("updatedAt"), "updatedAt")
        );
        Set<String> diagramIds = new HashSet<>();
        for (Object diagramValue : array(object.get("diagrams"), "diagrams")) {
            Diagram diagram = readDiagram(object(diagramValue, "diagram"));
            requireUniqueId(diagramIds, diagram.getId(), "diagram.id");
            project.getDiagrams().add(diagram);
        }
        return project;
    }

    private Diagram readDiagram(Map<String, Object> object) throws IOException {
        Diagram diagram = new Diagram(
                string(object.get("id"), "diagram.id"),
                string(object.get("name"), "diagram.name"),
                diagramType(object.get("type"), "diagram.type")
        );
        readCanvas(object(object.get("canvas"), "diagram.canvas"), diagram.getCanvasState());
        Set<String> tableIds = new HashSet<>();
        for (Object tableValue : array(object.get("tables"), "diagram.tables")) {
            DbTable table = readTable(object(tableValue, "table"));
            requireUniqueId(tableIds, table.getId(), "table.id");
            diagram.getTables().add(table);
        }
        Set<String> foreignKeyIds = new HashSet<>();
        for (Object fkValue : array(object.get("foreignKeys"), "diagram.foreignKeys")) {
            Map<String, Object> fk = object(fkValue, "foreignKey");
            ForeignKey foreignKey = new ForeignKey(
                    string(fk.get("id"), "foreignKey.id"),
                    string(fk.get("sourceTableId"), "foreignKey.sourceTableId"),
                    string(fk.get("sourceColumnId"), "foreignKey.sourceColumnId"),
                    string(fk.get("targetTableId"), "foreignKey.targetTableId"),
                    string(fk.get("targetColumnId"), "foreignKey.targetColumnId")
            );
            requireUniqueId(foreignKeyIds, foreignKey.getId(), "foreignKey.id");
            diagram.getForeignKeys().add(foreignKey);
        }
        Set<String> participantIds = new HashSet<>();
        for (Object participantValue : array(object.get("participants"), "diagram.participants")) {
            Map<String, Object> p = object(participantValue, "participant");
            SequenceParticipant participant = new SequenceParticipant(
                    string(p.get("id"), "participant.id"),
                    string(p.get("name"), "participant.name")
            );
            requireUniqueId(participantIds, participant.getId(), "participant.id");
            diagram.getParticipants().add(participant);
        }
        Set<String> messageIds = new HashSet<>();
        for (Object messageValue : array(object.get("messages"), "diagram.messages")) {
            Map<String, Object> m = object(messageValue, "message");
            SequenceMessage message = new SequenceMessage(
                    string(m.get("id"), "message.id"),
                    string(m.get("from"), "message.from"),
                    string(m.get("to"), "message.to"),
                    string(m.get("label"), "message.label")
            );
            message.setActivation(bool(m.get("activation"), "message.activation"));
            requireUniqueId(messageIds, message.getId(), "message.id");
            diagram.getMessages().add(message);
        }
        return diagram;
    }

    private void readCanvas(Map<String, Object> object, CanvasState canvas) throws IOException {
        canvas.setViewportX(number(object.get("viewportX"), "canvas.viewportX"));
        canvas.setViewportY(number(object.get("viewportY"), "canvas.viewportY"));
        canvas.setZoom(numberInRange(object.get("zoom"), "canvas.zoom", 0.25, 3.0));
        for (var entry : object(object.get("bounds"), "canvas.bounds").entrySet()) {
            Map<String, Object> rect = object(entry.getValue(), "canvas.bounds." + entry.getKey());
            canvas.getBoundsByElementId().put(entry.getKey(), new Rect(
                    number(rect.get("x"), "rect.x"),
                    number(rect.get("y"), "rect.y"),
                    positiveNumber(rect.get("width"), "rect.width"),
                    positiveNumber(rect.get("height"), "rect.height")
            ));
        }
    }

    private DbTable readTable(Map<String, Object> object) throws IOException {
        DbTable table = new DbTable(string(object.get("id"), "table.id"), string(object.get("name"), "table.name"));
        Set<String> columnIds = new HashSet<>();
        for (Object columnValue : array(object.get("columns"), "table.columns")) {
            Map<String, Object> c = object(columnValue, "column");
            DbColumn column = new DbColumn(
                    string(c.get("id"), "column.id"),
                    string(c.get("name"), "column.name"),
                    string(c.get("type"), "column.type")
            );
            column.setPrimaryKey(bool(c.get("primaryKey"), "column.primaryKey"));
            column.setUnique(bool(c.get("unique"), "column.unique"));
            column.setNullable(bool(c.get("nullable"), "column.nullable"));
            requireUniqueId(columnIds, column.getId(), "column.id");
            table.getColumns().add(column);
        }
        return table;
    }

    private static void requireUniqueId(Set<String> ids, String id, String field) throws IOException {
        if (!ids.add(id)) throw new IOException("Duplicate id at " + field + ": " + id);
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> object(Object value, String field) throws IOException {
        if (value instanceof Map<?, ?> map) return (Map<String, Object>) map;
        throw new IOException("Expected object at " + field);
    }

    @SuppressWarnings("unchecked")
    private static List<Object> array(Object value, String field) throws IOException {
        if (value instanceof List<?> list) return (List<Object>) list;
        throw new IOException("Expected array at " + field);
    }

    private static String string(Object value, String field) throws IOException {
        if (value instanceof String s) return s;
        throw new IOException("Expected string at " + field);
    }

    private static double number(Object value, String field) throws IOException {
        if (value instanceof Number n) return n.doubleValue();
        throw new IOException("Expected number at " + field);
    }

    private static double positiveNumber(Object value, String field) throws IOException {
        double number = number(value, field);
        if (number > 0) return number;
        throw new IOException("Expected positive number at " + field);
    }

    private static double numberInRange(Object value, String field, double min, double max) throws IOException {
        double number = number(value, field);
        if (number >= min && number <= max) return number;
        throw new IOException("Expected number in range " + min + ".." + max + " at " + field);
    }

    private static int integer(Object value, String field) throws IOException {
        if (value instanceof Number n) {
            double number = n.doubleValue();
            if (number == Math.rint(number) && number >= Integer.MIN_VALUE && number <= Integer.MAX_VALUE) {
                return (int) number;
            }
        }
        throw new IOException("Expected integer at " + field);
    }

    private static boolean bool(Object value, String field) throws IOException {
        if (value instanceof Boolean b) return b;
        throw new IOException("Expected boolean at " + field);
    }

    private static Instant instant(Object value, String field) throws IOException {
        try { return Instant.parse(string(value, field)); }
        catch (RuntimeException e) { throw new IOException("Invalid instant at " + field, e); }
    }

    private static DiagramType diagramType(Object value, String field) throws IOException {
        String text = string(value, field);
        try { return DiagramType.valueOf(text); }
        catch (IllegalArgumentException e) { throw new IOException("Invalid diagram type at " + field + ": " + text, e); }
    }
}
