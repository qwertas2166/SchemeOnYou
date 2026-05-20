package see.schemeonyou.importer;

import java.util.List;

public record DbImportTable(
        String name,
        List<DbImportColumn> columns,
        List<String> primaryKeyColumnNames
) {
    public DbImportTable(String name, List<DbImportColumn> columns) {
        this(name, columns, columns.stream()
                .filter(DbImportColumn::primaryKey)
                .map(DbImportColumn::name)
                .toList());
    }

    public DbImportTable {
        columns = List.copyOf(columns);
        primaryKeyColumnNames = List.copyOf(primaryKeyColumnNames);
    }
}
