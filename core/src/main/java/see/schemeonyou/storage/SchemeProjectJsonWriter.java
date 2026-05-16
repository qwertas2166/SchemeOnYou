package see.schemeonyou.storage;

import see.di.See;
import see.schemeonyou.model.*;

import java.util.Map;

@See
public class SchemeProjectJsonWriter {
    public String write(SchemeProject project) {
        Json out = new Json();
        out.line("{"); out.indent();
        out.field("version", SchemeProject.STORAGE_VERSION, true);
        out.field("id", project.getId(), true);
        out.field("name", project.getName(), true);
        out.field("createdAt", project.getCreatedAt().toString(), true);
        out.field("updatedAt", project.getUpdatedAt().toString(), true);
        out.line("\"diagrams\": ["); out.indent();
        for (int i = 0; i < project.getDiagrams().size(); i++) {
            writeDiagram(out, project.getDiagrams().get(i));
            out.line(i + 1 == project.getDiagrams().size() ? "" : ",");
        }
        out.dedent(); out.line("]");
        out.dedent(); out.line("}");
        return out.toString();
    }

    private void writeDiagram(Json out, Diagram d) {
        out.line("{"); out.indent();
        out.field("id", d.getId(), true);
        out.field("name", d.getName(), true);
        out.field("type", d.getType().name(), true);
        writeCanvas(out, d.getCanvasState()); out.line(",");
        out.line("\"tables\": ["); out.indent();
        for (int i = 0; i < d.getTables().size(); i++) { writeTable(out, d.getTables().get(i)); out.line(i + 1 == d.getTables().size() ? "" : ","); }
        out.dedent(); out.line("],");
        out.line("\"foreignKeys\": ["); out.indent();
        for (int i = 0; i < d.getForeignKeys().size(); i++) { writeForeignKey(out, d.getForeignKeys().get(i)); out.line(i + 1 == d.getForeignKeys().size() ? "" : ","); }
        out.dedent(); out.line("],");
        out.line("\"participants\": ["); out.indent();
        for (int i = 0; i < d.getParticipants().size(); i++) { writeParticipant(out, d.getParticipants().get(i)); out.line(i + 1 == d.getParticipants().size() ? "" : ","); }
        out.dedent(); out.line("],");
        out.line("\"messages\": ["); out.indent();
        for (int i = 0; i < d.getMessages().size(); i++) { writeMessage(out, d.getMessages().get(i)); out.line(i + 1 == d.getMessages().size() ? "" : ","); }
        out.dedent(); out.line("]");
        out.dedent(); out.line("}");
    }

    private void writeCanvas(Json out, CanvasState canvas) {
        out.line("\"canvas\": {"); out.indent();
        out.field("viewportX", canvas.getViewportX(), true);
        out.field("viewportY", canvas.getViewportY(), true);
        out.field("zoom", canvas.getZoom(), true);
        out.line("\"bounds\": {"); out.indent();
        var entries = canvas.getBoundsByElementId().entrySet().stream().sorted(Map.Entry.comparingByKey()).toList();
        for (int i = 0; i < entries.size(); i++) {
            var e = entries.get(i); Rect r = e.getValue();
            out.rawField(e.getKey(), "{\"x\":" + r.x() + ",\"y\":" + r.y() + ",\"width\":" + r.width() + ",\"height\":" + r.height() + "}", i + 1 < entries.size());
        }
        out.dedent(); out.line("}");
        out.dedent(); out.line("}");
    }

    private void writeTable(Json out, DbTable t) {
        out.line("{"); out.indent();
        out.field("id", t.getId(), true);
        out.field("name", t.getName(), true);
        out.line("\"columns\": ["); out.indent();
        for (int i = 0; i < t.getColumns().size(); i++) { writeColumn(out, t.getColumns().get(i)); out.line(i + 1 == t.getColumns().size() ? "" : ","); }
        out.dedent(); out.line("]");
        out.dedent(); out.line("}");
    }

    private void writeColumn(Json out, DbColumn c) {
        out.line("{\"id\":" + Json.q(c.getId()) + ",\"name\":" + Json.q(c.getName()) + ",\"type\":" + Json.q(c.getType()) + ",\"primaryKey\":" + c.isPrimaryKey() + ",\"unique\":" + c.isUnique() + ",\"nullable\":" + c.isNullable() + "}");
    }

    private void writeForeignKey(Json out, ForeignKey fk) {
        out.line("{\"id\":" + Json.q(fk.getId()) + ",\"sourceTableId\":" + Json.q(fk.getSourceTableId()) + ",\"sourceColumnId\":" + Json.q(fk.getSourceColumnId()) + ",\"targetTableId\":" + Json.q(fk.getTargetTableId()) + ",\"targetColumnId\":" + Json.q(fk.getTargetColumnId()) + "}");
    }

    private void writeParticipant(Json out, SequenceParticipant p) {
        out.line("{\"id\":" + Json.q(p.getId()) + ",\"name\":" + Json.q(p.getName()) + "}");
    }

    private void writeMessage(Json out, SequenceMessage m) {
        out.line("{\"id\":" + Json.q(m.getId()) + ",\"from\":" + Json.q(m.getFromParticipantId()) + ",\"to\":" + Json.q(m.getToParticipantId()) + ",\"label\":" + Json.q(m.getLabel()) + ",\"activation\":" + m.isActivation() + "}");
    }

    static class Json {
        private final StringBuilder sb = new StringBuilder();
        private int indent;
        void indent() { indent++; }
        void dedent() { indent--; }
        void line(String text) { sb.append("  ".repeat(Math.max(0, indent))).append(text).append('\n'); }
        void field(String name, String value, boolean comma) { rawField(name, q(value), comma); }
        void field(String name, int value, boolean comma) { rawField(name, Integer.toString(value), comma); }
        void field(String name, double value, boolean comma) { rawField(name, Double.toString(value), comma); }
        void rawField(String name, String value, boolean comma) { line(q(name) + ": " + value + (comma ? "," : "")); }
        static String q(String s) {
            StringBuilder escaped = new StringBuilder(s.length() + 2);
            escaped.append('"');
            for (int i = 0; i < s.length(); i++) {
                char ch = s.charAt(i);
                switch (ch) {
                    case '\"' -> escaped.append("\\\"");
                    case '\\' -> escaped.append("\\\\");
                    case '\b' -> escaped.append("\\b");
                    case '\f' -> escaped.append("\\f");
                    case '\n' -> escaped.append("\\n");
                    case '\r' -> escaped.append("\\r");
                    case '\t' -> escaped.append("\\t");
                    default -> {
                        if (ch < 0x20) {
                            escaped.append(String.format("\\u%04x", (int) ch));
                        } else {
                            escaped.append(ch);
                        }
                    }
                }
            }
            escaped.append('"');
            return escaped.toString();
        }
        public String toString() { return sb.toString(); }
    }
}
