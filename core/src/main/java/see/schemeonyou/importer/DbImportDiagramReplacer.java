package see.schemeonyou.importer;

import see.schemeonyou.model.DbColumn;
import see.schemeonyou.model.DbTable;
import see.schemeonyou.model.DbTableConstraint;
import see.schemeonyou.model.DbTableConstraintType;
import see.schemeonyou.model.Diagram;
import see.schemeonyou.model.DiagramType;
import see.schemeonyou.model.ForeignKey;
import see.schemeonyou.model.SchemeProject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Applies a DB metadata import by replacing one whole DB diagram.
 *
 * <p>MVP policy: no merge and no imported-subset tracking. The UI must guard this
 * service with a destructive warning because all current DB diagram content is cleared.</p>
 */
public class DbImportDiagramReplacer {
    private static final String DEFAULT_IMPORTED_DIAGRAM_NAME = "Imported database";

    public Result replace(SchemeProject project, String selectedDiagramId, DbImportResult importResult) {
        Objects.requireNonNull(project, "project");
        Objects.requireNonNull(importResult, "importResult");

        Diagram diagram = resolveTargetDiagram(project, selectedDiagramId);
        replaceDiagramContents(diagram, importResult);
        project.touch();
        return new Result(diagram.getId(), diagram.getName(), importResult.tables().size(), importResult.foreignKeys().size());
    }

    private Diagram resolveTargetDiagram(SchemeProject project, String selectedDiagramId) {
        Optional<Diagram> selectedDbDiagram = selectedDiagramId == null || selectedDiagramId.isBlank()
                ? Optional.empty()
                : project.findDiagram(selectedDiagramId).filter(diagram -> diagram.getType() == DiagramType.DATABASE);

        return selectedDbDiagram
                .or(() -> project.getDiagrams().stream().filter(diagram -> diagram.getType() == DiagramType.DATABASE).findFirst())
                .orElseGet(() -> {
                    Diagram created = new Diagram(uniqueDiagramId(project), DEFAULT_IMPORTED_DIAGRAM_NAME, DiagramType.DATABASE);
                    project.getDiagrams().add(created);
                    return created;
                });
    }

    private void replaceDiagramContents(Diagram diagram, DbImportResult importResult) {
        diagram.getTables().clear();
        diagram.getForeignKeys().clear();
        diagram.getParticipants().clear();
        diagram.getMessages().clear();
        diagram.getCanvasState().getBoundsByElementId().clear();
        diagram.getCanvasState().actualSize();

        Map<String, DbTable> tablesByName = new LinkedHashMap<>();
        Map<String, Map<String, DbColumn>> columnsByTableAndName = new LinkedHashMap<>();
        UniqueIdAllocator tableIds = UniqueIdAllocator.forValues("table", importResult.tables().stream()
                .map(DbImportTable::name)
                .toList());
        UniqueIdAllocator columnIds = UniqueIdAllocator.forValues("column", importResult.tables().stream()
                .flatMap(table -> table.columns().stream().map(column -> columnIdentity(table.name(), column.name())))
                .toList());
        UniqueIdAllocator foreignKeyIds = UniqueIdAllocator.forValues("fk", importResult.foreignKeys().stream()
                .map(DbImportDiagramReplacer::foreignKeyIdentity)
                .toList());
        UniqueIdAllocator constraintIds = UniqueIdAllocator.forValues("constraint", importResult.tables().stream()
                .filter(table -> table.primaryKeyColumnNames().size() > 1)
                .map(table -> compositePrimaryKeyIdentity(table.name(), table.primaryKeyColumnNames()))
                .toList());

        for (DbImportTable importedTable : importResult.tables()) {
            DbTable table = new DbTable(tableIds.next(importedTable.name()), importedTable.name());
            tablesByName.put(importedTable.name(), table);
            diagram.getTables().add(table);

            Map<String, DbColumn> columnsByName = new LinkedHashMap<>();
            columnsByTableAndName.put(importedTable.name(), columnsByName);
            for (DbImportColumn importedColumn : importedTable.columns()) {
                DbColumn column = new DbColumn(
                        columnIds.next(columnIdentity(importedTable.name(), importedColumn.name())),
                        importedColumn.name(),
                        importedColumn.typeName()
                );
                column.setNullable(importedColumn.nullable());
                column.setPrimaryKey(importedColumn.primaryKey());
                table.getColumns().add(column);
                columnsByName.put(importedColumn.name(), column);
            }

            if (importedTable.primaryKeyColumnNames().size() > 1) {
                List<String> primaryKeyColumnIds = importedTable.primaryKeyColumnNames().stream()
                        .map(columnsByName::get)
                        .filter(Objects::nonNull)
                        .map(DbColumn::getId)
                        .toList();
                if (primaryKeyColumnIds.size() > 1) {
                    table.getConstraints().add(new DbTableConstraint(
                            constraintIds.next(compositePrimaryKeyIdentity(importedTable.name(), importedTable.primaryKeyColumnNames())),
                            DbTableConstraintType.COMPOSITE_PRIMARY_KEY,
                            primaryKeyColumnIds
                    ));
                }
            }
        }

        for (DbImportForeignKey importedForeignKey : importResult.foreignKeys()) {
            DbTable sourceTable = tablesByName.get(importedForeignKey.sourceTable());
            DbTable targetTable = tablesByName.get(importedForeignKey.targetTable());
            DbColumn sourceColumn = column(columnsByTableAndName, importedForeignKey.sourceTable(), importedForeignKey.sourceColumn());
            DbColumn targetColumn = column(columnsByTableAndName, importedForeignKey.targetTable(), importedForeignKey.targetColumn());
            if (sourceTable == null || targetTable == null || sourceColumn == null || targetColumn == null) {
                continue;
            }
            diagram.getForeignKeys().add(new ForeignKey(
                    foreignKeyIds.next(foreignKeyIdentity(importedForeignKey)),
                    sourceTable.getId(),
                    sourceColumn.getId(),
                    targetTable.getId(),
                    targetColumn.getId()
            ));
        }
    }

    private static DbColumn column(Map<String, Map<String, DbColumn>> columnsByTableAndName, String tableName, String columnName) {
        Map<String, DbColumn> columnsByName = columnsByTableAndName.get(tableName);
        return columnsByName == null ? null : columnsByName.get(columnName);
    }

    private static String columnIdentity(String tableName, String columnName) {
        return tableName + "-" + columnName;
    }

    private static String foreignKeyIdentity(DbImportForeignKey foreignKey) {
        String name = foreignKey.name() == null || foreignKey.name().isBlank() ? "fk" : foreignKey.name();
        return name + "-" + foreignKey.sourceTable() + "-" + foreignKey.sourceColumn()
                + "-" + foreignKey.targetTable() + "-" + foreignKey.targetColumn()
                + "-" + foreignKey.keySequence();
    }

    private static String compositePrimaryKeyIdentity(String tableName, List<String> columnNames) {
        return tableName + "-pk-" + String.join("-", columnNames);
    }

    private static String uniqueDiagramId(SchemeProject project) {
        String base = "diagram-db-import";
        String candidate = base;
        int suffix = 2;
        while (project.findDiagram(candidate).isPresent()) {
            candidate = base + "-" + suffix++;
        }
        return candidate;
    }

    private static String stableId(String prefix, String value) {
        return prefix + "-" + normalizedIdPart(value);
    }

    private static String normalizedIdPart(String value) {
        String normalized = value == null ? "item" : value.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "-");
        normalized = normalized.replaceAll("^-+|-+$", "");
        return normalized.isBlank() ? "item" : normalized;
    }

    private static String shortHash(String value) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(String.valueOf(value).getBytes(StandardCharsets.UTF_8));
            StringBuilder out = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                out.append(String.format(Locale.ROOT, "%02x", digest[i]));
            }
            return out.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is required", e);
        }
    }

    private static final class UniqueIdAllocator {
        private final String prefix;
        private final Map<String, Long> baseCounts;
        private final Map<String, Integer> usedIds = new HashMap<>();
        private final Map<String, Integer> duplicateOrdinalByValue = new HashMap<>();

        private UniqueIdAllocator(String prefix, Map<String, Long> baseCounts) {
            this.prefix = prefix;
            this.baseCounts = baseCounts;
        }

        static UniqueIdAllocator forValues(String prefix, List<String> values) {
            Map<String, Long> counts = new HashMap<>();
            for (String value : values) {
                counts.merge(stableId(prefix, value), 1L, Long::sum);
            }
            return new UniqueIdAllocator(prefix, counts);
        }

        String next(String value) {
            String base = stableId(prefix, value);
            String candidate = baseCounts.getOrDefault(base, 0L) <= 1
                    ? base
                    : base + "-" + shortHash(valueWithOrdinal(value));
            int suffix = 2;
            String unique = candidate;
            while (usedIds.containsKey(unique)) {
                unique = candidate + "-" + suffix++;
            }
            usedIds.put(unique, 1);
            return unique;
        }

        private String valueWithOrdinal(String value) {
            int ordinal = duplicateOrdinalByValue.merge(String.valueOf(value), 1, Integer::sum);
            return ordinal == 1 ? String.valueOf(value) : value + "#" + ordinal;
        }
    }

    public record Result(String diagramId, String diagramName, int tableCount, int foreignKeyCount) { }
}
