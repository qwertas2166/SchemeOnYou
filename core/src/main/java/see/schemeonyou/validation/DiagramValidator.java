package see.schemeonyou.validation;

import see.di.See;
import see.schemeonyou.model.*;

@See
public class DiagramValidator {
    public ValidationResult validate(Diagram diagram) {
        ValidationResult result = new ValidationResult();
        if (diagram.getType() == DiagramType.DATABASE) validateDatabase(diagram, result);
        return result;
    }

    private void validateDatabase(Diagram diagram, ValidationResult result) {
        for (ForeignKey fk : diagram.getForeignKeys()) {
            DbTable source = diagram.findTable(fk.getSourceTableId()).orElse(null);
            DbTable target = diagram.findTable(fk.getTargetTableId()).orElse(null);
            if (source == null || target == null) {
                result.error("fk.table.missing", "Foreign key references a missing table", fk.getId());
                continue;
            }
            DbColumn sourceColumn = source.findColumn(fk.getSourceColumnId()).orElse(null);
            DbColumn targetColumn = target.findColumn(fk.getTargetColumnId()).orElse(null);
            if (sourceColumn == null || targetColumn == null) {
                result.error("fk.column.missing", "Foreign key references a missing column", fk.getId());
                continue;
            }
            if (!sourceColumn.getType().equalsIgnoreCase(targetColumn.getType())) {
                result.warn("fk.type.mismatch", "Foreign key column types differ", fk.getId());
            }
            if (!targetColumn.isPrimaryKey() && !targetColumn.isUnique()) {
                result.warn("fk.target.not.unique", "Target column is not PK or unique; relation meaning may be ambiguous", fk.getId());
            }
        }
    }
}
