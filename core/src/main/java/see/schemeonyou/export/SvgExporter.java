package see.schemeonyou.export;

import see.di.See;
import see.schemeonyou.model.*;

import java.util.Map;
import java.util.stream.Collectors;

@See
public class SvgExporter {
    public String export(Diagram diagram) {
        StringBuilder svg = new StringBuilder("<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"1200\" height=\"800\" viewBox=\"0 0 1200 800\">\n");
        svg.append("<defs><marker id=\"arrow\" viewBox=\"0 0 10 10\" refX=\"8\" refY=\"5\" markerWidth=\"7\" markerHeight=\"7\" orient=\"auto-start-reverse\"><path d=\"M 0 0 L 10 5 L 0 10 z\" fill=\"#8fa3bf\"/></marker></defs>\n");
        svg.append("<style>text{font:14px sans-serif}.card{fill:#151922;stroke:#6aa5ff;stroke-width:1.5}.head{fill:#1f2a3d}.line{stroke:#8fa3bf;stroke-width:1.5;fill:none}.lifeline{stroke-dasharray:8 6}.activation{fill:#263752;stroke:#60a5fa;stroke-width:1}.pk{fill:#ffd166}.divider{stroke:#4b5563;stroke-width:1}</style>\n");
        if (diagram.getType() == DiagramType.DATABASE) exportDatabase(diagram, svg); else exportSequence(diagram, svg);
        svg.append("</svg>\n");
        return svg.toString();
    }

    private void exportDatabase(Diagram diagram, StringBuilder svg) {
        for (ForeignKey fk : diagram.getForeignKeys()) {
            Rect a = diagram.getCanvasState().getBoundsByElementId().get(fk.getSourceTableId());
            Rect b = diagram.getCanvasState().getBoundsByElementId().get(fk.getTargetTableId());
            if (a != null && b != null) {
                svg.append("<path class=\"line\" d=\"M ").append(a.center().x()).append(' ').append(a.center().y())
                        .append(" L ").append(b.center().x()).append(' ').append(b.center().y()).append("\"/>\n");
            }
        }
        for (DbTable t : diagram.getTables()) {
            Rect r = diagram.getCanvasState().getBoundsByElementId().getOrDefault(t.getId(), new Rect(0, 0, 220, 120));
            svg.append("<rect class=\"card\" x=\"").append(r.x()).append("\" y=\"").append(r.y()).append("\" width=\"").append(r.width()).append("\" height=\"").append(r.height()).append("\" rx=\"10\"/>\n");
            svg.append("<rect class=\"head\" x=\"").append(r.x()).append("\" y=\"").append(r.y()).append("\" width=\"").append(r.width()).append("\" height=\"36\" rx=\"10\"/>\n");
            svg.append(text(r.x() + 12, r.y() + 24, t.getName()));
            svg.append("<path class=\"divider\" d=\"M ").append(r.x() + 1).append(' ').append(r.y() + 38)
                    .append(" L ").append(r.x() + r.width() - 1).append(' ').append(r.y() + 38).append("\"/>\n");
            int row = 0;
            for (DbColumn c : t.getColumns()) {
                svg.append(text(r.x() + 16, r.y() + 60 + row * 24, (c.isPrimaryKey() ? "◆ " : "") + c.getName() + ": " + c.getType()));
                row++;
            }
            Map<String, String> columnNames = t.getColumns().stream().collect(Collectors.toMap(DbColumn::getId, DbColumn::getName));
            for (DbTableConstraint constraint : t.getConstraints()) {
                svg.append(text(r.x() + 16, r.y() + 60 + row * 24, constraintLabel(constraint, columnNames)));
                row++;
            }
        }
    }

    private String constraintLabel(DbTableConstraint constraint, Map<String, String> columnNames) {
        String type = constraint.type() == DbTableConstraintType.COMPOSITE_PRIMARY_KEY ? "PK" : "UNQ";
        String columns = constraint.columnIds().stream()
                .map(columnId -> columnNames.getOrDefault(columnId, columnId))
                .collect(Collectors.joining(", "));
        return type + "(" + columns + ")";
    }

    private void exportSequence(Diagram diagram, StringBuilder svg) {
        for (SequenceParticipant p : diagram.getParticipants()) {
            Rect r = diagram.getCanvasState().getBoundsByElementId().getOrDefault(p.getId(), new Rect(0, 0, 140, 48));
            svg.append("<rect class=\"card\" x=\"").append(r.x()).append("\" y=\"").append(r.y()).append("\" width=\"").append(r.width()).append("\" height=\"").append(r.height()).append("\" rx=\"8\"/>\n");
            svg.append(text(r.x() + 12, r.y() + 30, p.getName() + " [" + p.getType().storageName() + "]"));
            svg.append("<path class=\"line lifeline\" d=\"M ").append(r.center().x()).append(' ').append(r.y() + r.height()).append(" L ").append(r.center().x()).append(" 760\"/>\n");
        }
        int i = 0;
        for (SequenceMessage m : diagram.getMessages()) {
            Rect from = participantBounds(diagram, m.getFromParticipantId());
            Rect to = participantBounds(diagram, m.getToParticipantId());
            if (from == null || to == null) continue;
            double y = 150 + i * 72.0;
            if (m.getType() == SequenceMessageType.SELF_CALL) {
                double x = from.center().x();
                svg.append("<path class=\"line\" marker-end=\"url(#arrow)\" d=\"M ")
                        .append(x).append(' ').append(y)
                        .append(" C ").append(x + 80).append(' ').append(y).append(' ')
                        .append(x + 80).append(' ').append(y + 48).append(' ')
                        .append(x).append(' ').append(y + 48)
                        .append("\"/>\n");
            } else {
                svg.append("<path class=\"line\" marker-end=\"url(#arrow)\" d=\"M ")
                        .append(from.center().x()).append(' ').append(y)
                        .append(" L ").append(to.center().x()).append(' ').append(y)
                        .append("\"/>\n");
            }
            svg.append(text(Math.min(from.center().x(), to.center().x()) + 12, y - 8, "#" + m.getOrder() + " " + m.getType().storageName() + ": " + m.getLabel()));
            if (m.isActivation()) {
                svg.append("<rect class=\"activation\" x=\"").append(to.center().x() - 6).append("\" y=\"").append(y)
                        .append("\" width=\"12\" height=\"52\" rx=\"6\"/>\n");
            }
            i++;
        }
    }

    private Rect participantBounds(Diagram diagram, String participantId) {
        return diagram.getParticipants().stream()
                .filter(p -> p.getId().equals(participantId))
                .findFirst()
                .map(p -> diagram.getCanvasState().getBoundsByElementId().getOrDefault(p.getId(), new Rect(0, 0, 140, 48)))
                .orElse(null);
    }

    private String text(double x, double y, String text) {
        return "<text fill=\"#e8eefc\" x=\"" + x + "\" y=\"" + y + "\">" + escape(text) + "</text>\n";
    }

    private String escape(String s) {
        StringBuilder escaped = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '&') escaped.append("&amp;");
            else if (ch == '<') escaped.append("&lt;");
            else if (ch == '>') escaped.append("&gt;");
            else if (ch == '\"') escaped.append("&quot;");
            else if (ch == '\'') escaped.append("&apos;");
            else if (isValidXmlTextChar(ch)) escaped.append(ch);
            else escaped.append(' ');
        }
        return escaped.toString();
    }

    private boolean isValidXmlTextChar(char ch) {
        return ch == 0x9 || ch == 0xA || ch == 0xD || ch >= 0x20;
    }
}
