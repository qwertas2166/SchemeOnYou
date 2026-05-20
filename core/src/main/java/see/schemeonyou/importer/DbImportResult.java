package see.schemeonyou.importer;

import java.util.List;

public record DbImportResult(
        String schemaOrCatalog,
        List<DbImportTable> tables,
        List<DbImportForeignKey> foreignKeys,
        List<String> importWarnings
) {
    public DbImportResult(String schemaOrCatalog, List<DbImportTable> tables, List<DbImportForeignKey> foreignKeys) {
        this(schemaOrCatalog, tables, foreignKeys, List.of());
    }

    public DbImportResult {
        tables = List.copyOf(tables);
        foreignKeys = List.copyOf(foreignKeys);
        importWarnings = List.copyOf(importWarnings);
    }
}
