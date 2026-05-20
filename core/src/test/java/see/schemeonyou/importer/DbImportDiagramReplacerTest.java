package see.schemeonyou.importer;

import org.junit.jupiter.api.Test;
import see.schemeonyou.command.UndoRedoStack;
import see.schemeonyou.model.DbColumn;
import see.schemeonyou.model.DbTable;
import see.schemeonyou.model.ForeignKey;
import see.schemeonyou.model.Diagram;
import see.schemeonyou.model.DbTableConstraintType;
import see.schemeonyou.model.DiagramType;
import see.schemeonyou.model.SchemeProject;
import see.schemeonyou.model.SequenceParticipant;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DbImportDiagramReplacerTest {
    @Test
    void replacesWholeSelectedDbDiagramAndPreservesOtherDiagrams() {
        SchemeProject project = new SchemeProject("project-1", "Project");
        Diagram db = new Diagram("db-1", "Current DB", DiagramType.DATABASE);
        db.getTables().add(new DbTable("table-old", "old_table"));
        db.getCanvasState().getBoundsByElementId().put("table-old", new see.schemeonyou.model.Rect(1, 2, 3, 4));
        project.getDiagrams().add(db);
        Diagram sequence = new Diagram("seq-1", "Sequence", DiagramType.SEQUENCE);
        sequence.getParticipants().add(new SequenceParticipant("participant-1", "API"));
        project.getDiagrams().add(sequence);

        DbImportDiagramReplacer.Result result = new DbImportDiagramReplacer().replace(project, "db-1", sampleImport());

        assertEquals("db-1", result.diagramId());
        assertEquals(2, result.tableCount());
        assertEquals(1, result.foreignKeyCount());
        assertEquals(List.of("orders", "users"), db.getTables().stream().map(DbTable::getName).toList());
        assertTrue(db.getTables().stream().noneMatch(table -> table.getName().equals("old_table")));
        assertTrue(db.getCanvasState().getBoundsByElementId().isEmpty());
        assertEquals(1, db.getForeignKeys().size());
        assertEquals(1, sequence.getParticipants().size(), "non-target diagrams must be preserved");
    }

    @Test
    void repeatedReplaceDoesNotCreateDuplicates() {
        SchemeProject project = new SchemeProject("project-1", "Project");
        Diagram db = new Diagram("db-1", "Current DB", DiagramType.DATABASE);
        project.getDiagrams().add(db);
        DbImportDiagramReplacer replacer = new DbImportDiagramReplacer();

        replacer.replace(project, "db-1", sampleImport());
        replacer.replace(project, "db-1", sampleImport());

        assertEquals(2, db.getTables().size());
        assertEquals(1, db.getForeignKeys().size());
    }

    @Test
    void createsDbDiagramWhenProjectHasNone() {
        SchemeProject project = new SchemeProject("project-1", "Project");
        project.getDiagrams().add(new Diagram("seq-1", "Sequence", DiagramType.SEQUENCE));

        DbImportDiagramReplacer.Result result = new DbImportDiagramReplacer().replace(project, null, sampleImport());

        Diagram created = project.findDiagram(result.diagramId()).orElseThrow();
        assertEquals(DiagramType.DATABASE, created.getType());
        assertEquals("Imported database", created.getName());
        assertEquals(2, created.getTables().size());
        assertEquals(1, created.getForeignKeys().size());
        assertEquals(2, project.getDiagrams().size());
    }

    @Test
    void undoRedoRestoresReplacedDiagramSnapshot() {
        SchemeProject project = new SchemeProject("project-1", "Project");
        Diagram db = new Diagram("db-1", "Current DB", DiagramType.DATABASE);
        DbTable old = new DbTable("table-old", "old_table");
        db.getTables().add(old);
        db.getCanvasState().setViewportX(10);
        db.getCanvasState().setViewportY(20);
        db.getCanvasState().setZoom(1.5);
        db.getCanvasState().getBoundsByElementId().put("table-old", new see.schemeonyou.model.Rect(1, 2, 3, 4));
        project.getDiagrams().add(db);
        UndoRedoStack undo = new UndoRedoStack();

        DbImportReplaceCommand command = new DbImportReplaceCommand(project, "db-1", sampleImport(), new DbImportDiagramReplacer());
        undo.run(command);

        assertEquals(List.of("orders", "users"), project.findDiagram("db-1").orElseThrow().getTables().stream().map(DbTable::getName).toList());

        undo.undo();
        Diagram restored = project.findDiagram("db-1").orElseThrow();
        assertEquals(List.of("old_table"), restored.getTables().stream().map(DbTable::getName).toList());
        assertEquals(10, restored.getCanvasState().getViewportX());
        assertEquals(20, restored.getCanvasState().getViewportY());
        assertEquals(1.5, restored.getCanvasState().getZoom());
        assertEquals(new see.schemeonyou.model.Rect(1, 2, 3, 4), restored.getCanvasState().getBoundsByElementId().get("table-old"));

        undo.redo();
        Diagram redone = project.findDiagram("db-1").orElseThrow();
        assertEquals(List.of("orders", "users"), redone.getTables().stream().map(DbTable::getName).toList());
        assertTrue(redone.getCanvasState().getBoundsByElementId().isEmpty());
    }

    @Test
    void undoRemovesAutoCreatedDbDiagramAndRedoRestoresIt() {
        SchemeProject project = new SchemeProject("project-1", "Project");
        project.getDiagrams().add(new Diagram("seq-1", "Sequence", DiagramType.SEQUENCE));
        UndoRedoStack undo = new UndoRedoStack();
        DbImportReplaceCommand command = new DbImportReplaceCommand(project, null, sampleImport(), new DbImportDiagramReplacer());

        undo.run(command);
        String createdId = command.result().diagramId();
        assertTrue(project.findDiagram(createdId).isPresent());

        undo.undo();
        assertTrue(project.findDiagram(createdId).isEmpty());
        assertEquals(List.of("seq-1"), project.getDiagrams().stream().map(Diagram::getId).toList());

        undo.redo();
        assertTrue(project.findDiagram(createdId).isPresent());
    }

    @Test
    void resultCanBeSavedAndReopened() throws Exception {
        SchemeProject project = new SchemeProject("project-1", "Project");
        new DbImportDiagramReplacer().replace(project, null, sampleImport());

        Path file = Files.createTempFile("schemeonyou-import-replace", ".json");
        try {
            new see.schemeonyou.storage.SchemeProjectStorage().save(file, project);
            SchemeProject loaded = new see.schemeonyou.storage.SchemeProjectStorage().load(file);

            Diagram loadedDb = loaded.getDiagrams().stream()
                    .filter(diagram -> diagram.getType() == DiagramType.DATABASE)
                    .findFirst()
                    .orElseThrow();
            assertEquals(List.of("orders", "users"), loadedDb.getTables().stream().map(DbTable::getName).toList());
            assertEquals(1, loadedDb.getForeignKeys().size());
        } finally {
            Files.deleteIfExists(file);
        }
    }


    @Test
    void createsCompositePrimaryKeyConstraintAndRoundTripsIt() throws Exception {
        SchemeProject project = new SchemeProject("project-1", "Project");
        DbImportResult compositePkImport = new DbImportResult("public", List.of(
                new DbImportTable("membership", List.of(
                        new DbImportColumn("membership", "group_id", "int8", false, true, 1),
                        new DbImportColumn("membership", "user_id", "int8", false, true, 2),
                        new DbImportColumn("membership", "role", "text", true, false, 3)
                ), List.of("user_id", "group_id"))
        ), List.of());

        new DbImportDiagramReplacer().replace(project, null, compositePkImport);

        DbTable table = project.getDiagrams().getFirst().getTables().getFirst();
        assertEquals(1, table.getConstraints().size());
        assertEquals(DbTableConstraintType.COMPOSITE_PRIMARY_KEY, table.getConstraints().getFirst().type());
        assertEquals(List.of("column-membership-user-id", "column-membership-group-id"), table.getConstraints().getFirst().columnIds());

        Path file = Files.createTempFile("schemeonyou-import-composite-pk", ".json");
        try {
            new see.schemeonyou.storage.SchemeProjectStorage().save(file, project);
            SchemeProject loaded = new see.schemeonyou.storage.SchemeProjectStorage().load(file);
            DbTable loadedTable = loaded.getDiagrams().getFirst().getTables().getFirst();
            assertEquals(DbTableConstraintType.COMPOSITE_PRIMARY_KEY, loadedTable.getConstraints().getFirst().type());
            assertEquals(List.of("column-membership-user-id", "column-membership-group-id"), loadedTable.getConstraints().getFirst().columnIds());
        } finally {
            Files.deleteIfExists(file);
        }
    }

    @Test
    void importGeneratedIdsAreCollisionSafeAndDeterministic() throws Exception {
        DbImportResult collidingImport = new DbImportResult("public", List.of(
                new DbImportTable("user-role", List.of(
                        new DbImportColumn("user-role", "role id", "int8", false, false, 1),
                        new DbImportColumn("user-role", "role_id", "int8", false, false, 2)
                )),
                new DbImportTable("user_role", List.of(
                        new DbImportColumn("user_role", "id", "int8", false, true, 1)
                )),
                new DbImportTable("user", List.of(
                        new DbImportColumn("user", "role-id", "int8", false, false, 1)
                ))
        ), List.of(
                new DbImportForeignKey("fk user role", "user-role", "role id", "user_role", "id", 1),
                new DbImportForeignKey("fk_user_role", "user-role", "role_id", "user_role", "id", 1)
        ));
        SchemeProject first = new SchemeProject("project-1", "Project");
        SchemeProject second = new SchemeProject("project-1", "Project");

        new DbImportDiagramReplacer().replace(first, null, collidingImport);
        new DbImportDiagramReplacer().replace(second, null, collidingImport);

        Diagram firstDb = first.getDiagrams().getFirst();
        Diagram secondDb = second.getDiagrams().getFirst();
        assertEquals(
                secondDb.getTables().stream().map(DbTable::getId).toList(),
                firstDb.getTables().stream().map(DbTable::getId).toList()
        );
        List<String> tableIds = firstDb.getTables().stream().map(DbTable::getId).toList();
        assertUnique(tableIds);
        assertEquals(2, tableIds.stream().filter(id -> id.startsWith("table-user-role-")).count());
        assertTrue(tableIds.contains("table-user"));

        List<String> columnIds = firstDb.getTables().stream()
                .flatMap(table -> table.getColumns().stream())
                .map(DbColumn::getId)
                .toList();
        assertEquals(secondDb.getTables().stream()
                .flatMap(table -> table.getColumns().stream())
                .map(DbColumn::getId)
                .toList(), columnIds);
        assertUnique(columnIds);
        assertEquals(4, columnIds.size());

        List<String> fkIds = firstDb.getForeignKeys().stream().map(ForeignKey::getId).toList();
        assertEquals(secondDb.getForeignKeys().stream().map(ForeignKey::getId).toList(), fkIds);
        assertUnique(fkIds);
        assertTrue(fkIds.stream().allMatch(id -> id.startsWith("fk-fk-user-role-user-role-")));

        Path file = Files.createTempFile("schemeonyou-import-collision", ".json");
        try {
            new see.schemeonyou.storage.SchemeProjectStorage().save(file, first);
            SchemeProject loaded = new see.schemeonyou.storage.SchemeProjectStorage().load(file);
            assertEquals(columnIds, loaded.getDiagrams().getFirst().getTables().stream()
                    .flatMap(table -> table.getColumns().stream())
                    .map(DbColumn::getId)
                    .toList());
        } finally {
            Files.deleteIfExists(file);
        }
    }

    private static void assertUnique(List<String> ids) {
        Set<String> unique = new HashSet<>(ids);
        assertEquals(ids.size(), unique.size(), "ids must be unique: " + ids);
    }

    private static DbImportResult sampleImport() {
        return new DbImportResult("public", List.of(
                new DbImportTable("orders", List.of(
                        new DbImportColumn("orders", "id", "int8", false, true, 1),
                        new DbImportColumn("orders", "user_id", "int8", false, false, 2)
                )),
                new DbImportTable("users", List.of(
                        new DbImportColumn("users", "id", "int8", false, true, 1),
                        new DbImportColumn("users", "email", "text", true, false, 2)
                ))
        ), List.of(
                new DbImportForeignKey("fk_orders_user", "orders", "user_id", "users", "id", 1)
        ));
    }
}
