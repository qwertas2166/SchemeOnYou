package see.schemeonyou.export;

import org.junit.jupiter.api.Test;
import org.xml.sax.InputSource;
import see.schemeonyou.model.*;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class SvgExporterTest {
    @Test
    void exportEscapesXmlTextAndNormalizesInvalidControls() throws Exception {
        Diagram diagram = new Diagram("diagram-1", "Database", DiagramType.DATABASE);
        DbTable table = new DbTable("table-1", "Users & <Admins> \"team\" 'core'" + (char) 0x01);
        table.getColumns().add(new DbColumn("column-1", "email\nline\rcell\tTabbed", "varchar & text"));
        diagram.getTables().add(table);
        diagram.getCanvasState().getBoundsByElementId().put("table-1", new Rect(10, 20, 220, 120));

        String svg = new SvgExporter().export(diagram);

        assertTrue(svg.contains("Users &amp; &lt;Admins&gt; &quot;team&quot; &apos;core&apos; "));
        assertTrue(svg.contains("<path class=\"divider\" d=\"M 11.0 58.0 L 229.0 58.0\"/>"));
        assertTrue(svg.contains("email\nline\rcell\tTabbed: varchar &amp; text"));
        assertFalse(svg.contains(String.valueOf((char) 0x01)));
        assertDoesNotThrow(() -> DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new InputSource(new StringReader(svg))));
    }

    @Test
    void exportShowsCompositeConstraintLabels() throws Exception {
        Diagram diagram = new Diagram("diagram-1", "Database", DiagramType.DATABASE);
        DbTable table = new DbTable("table-1", "Join");
        table.getColumns().add(new DbColumn("left_id", "left_id", "uuid"));
        table.getColumns().add(new DbColumn("right_id", "right_id", "uuid"));
        table.getConstraints().add(new DbTableConstraint("join_pk", DbTableConstraintType.COMPOSITE_PRIMARY_KEY, java.util.List.of("left_id", "right_id")));
        diagram.getTables().add(table);
        diagram.getCanvasState().getBoundsByElementId().put("table-1", new Rect(10, 20, 220, 140));

        String svg = new SvgExporter().export(diagram);

        assertTrue(svg.contains("PK(left_id, right_id)"));
        DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(svg)));
    }

    @Test
    void exportEscapesSequenceParticipantAndMessageLabels() throws Exception {
        Diagram diagram = new Diagram("diagram-1", "Sequence", DiagramType.SEQUENCE);
        diagram.getParticipants().add(new SequenceParticipant("p1", "Client <A>"));
        diagram.getParticipants().add(new SequenceParticipant("p2", "API & DB"));
        diagram.getCanvasState().getBoundsByElementId().put("p1", new Rect(10, 20, 140, 48));
        diagram.getCanvasState().getBoundsByElementId().put("p2", new Rect(200, 20, 140, 48));
        diagram.getMessages().add(new SequenceMessage("m1", "p1", "p2", "call \"x\" & return 'y'"));

        String svg = new SvgExporter().export(diagram);

        assertTrue(svg.contains("Client &lt;A&gt;"));
        assertTrue(svg.contains("API &amp; DB"));
        assertTrue(svg.contains("call &quot;x&quot; &amp; return &apos;y&apos;"));
        DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(svg)));
    }
}
