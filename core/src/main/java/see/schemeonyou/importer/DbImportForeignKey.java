package see.schemeonyou.importer;

public record DbImportForeignKey(
        String name,
        String sourceTable,
        String sourceColumn,
        String targetTable,
        String targetColumn,
        int keySequence
) { }
