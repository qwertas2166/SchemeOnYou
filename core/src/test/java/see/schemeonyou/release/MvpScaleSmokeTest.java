package see.schemeonyou.release;

import org.junit.jupiter.api.Test;
import see.schemeonyou.export.SvgExporter;
import see.schemeonyou.layout.DeterministicLayoutEngine;
import see.schemeonyou.model.DbColumn;
import see.schemeonyou.model.DbTable;
import see.schemeonyou.model.Diagram;
import see.schemeonyou.model.DiagramType;
import see.schemeonyou.model.ForeignKey;
import see.schemeonyou.model.SchemeProject;
import see.schemeonyou.storage.SchemeProjectJsonReader;
import see.schemeonyou.storage.SchemeProjectJsonWriter;
import see.schemeonyou.validation.DiagramValidator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MvpScaleSmokeTest {
    private static final int TABLE_COUNT = 200;
    private static final int FOREIGN_KEY_COUNT = 500;

    @Test
    void databaseDiagramMvpScaleRoundTripsValidatesAndExportsWithoutJavaFx() throws Exception {
        SchemeProject project = new SchemeProject("project-scale", "MVP Scale Smoke");
        Diagram diagram = buildDatabaseDiagram(TABLE_COUNT, FOREIGN_KEY_COUNT);
        project.getDiagrams().add(diagram);

        new DeterministicLayoutEngine().layout(diagram);
        assertEquals(TABLE_COUNT, diagram.getCanvasState().getBoundsByElementId().size());
        assertFalse(new DiagramValidator().validate(diagram).hasErrors());

        SchemeProjectJsonWriter writer = new SchemeProjectJsonWriter();
        String json = writer.write(project);
        SchemeProject loaded = new SchemeProjectJsonReader().read(json);
        Diagram loadedDiagram = loaded.getDiagrams().getFirst();

        assertEquals(TABLE_COUNT, loadedDiagram.getTables().size());
        assertEquals(FOREIGN_KEY_COUNT, loadedDiagram.getForeignKeys().size());
        assertEquals(json, writer.write(loaded));

        String svg = new SvgExporter().export(loadedDiagram);
        assertTrue(svg.startsWith("<svg"));
        assertTrue(svg.contains("table_199"));
        assertEquals(FOREIGN_KEY_COUNT, count(svg, "<path class=\"line\""));
    }

    private static int count(String text, String needle) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(needle, index)) >= 0) {
            count++;
            index += needle.length();
        }
        return count;
    }

    private static Diagram buildDatabaseDiagram(int tableCount, int foreignKeyCount) {
        Diagram diagram = new Diagram("diagram-scale", "Database scale", DiagramType.DATABASE);
        for (int i = 0; i < tableCount; i++) {
            DbTable table = new DbTable(tableId(i), "table_" + i);
            DbColumn id = new DbColumn(columnId(i, "id"), "id", "bigint");
            id.setPrimaryKey(true);
            id.setNullable(false);
            table.getColumns().add(id);
            table.getColumns().add(new DbColumn(columnId(i, "name"), "name", "varchar"));
            table.getColumns().add(new DbColumn(columnId(i, "status"), "status", "varchar"));
            diagram.getTables().add(table);
        }
        for (int i = 0; i < foreignKeyCount; i++) {
            int source = i % tableCount;
            int target = (i * 37 + 11) % tableCount;
            if (target == source) {
                target = (target + 1) % tableCount;
            }
            diagram.getForeignKeys().add(new ForeignKey(
                    "fk_" + i,
                    tableId(source),
                    columnId(source, "id"),
                    tableId(target),
                    columnId(target, "id")
            ));
        }
        return diagram;
    }

    private static String tableId(int index) {
        return "table_" + index;
    }

    private static String columnId(int tableIndex, String name) {
        return "table_" + tableIndex + "_" + name;
    }
}
