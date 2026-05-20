package see.schemeonyou.importer;

public record DbImportColumn(
        String tableName,
        String name,
        String typeName,
        boolean nullable,
        boolean primaryKey,
        int ordinalPosition
) { }
