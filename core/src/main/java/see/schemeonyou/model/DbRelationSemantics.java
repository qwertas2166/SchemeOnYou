package see.schemeonyou.model;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public final class DbRelationSemantics {
    private DbRelationSemantics() {}

    public static DbRelationMeaning derive(Diagram diagram, ForeignKey foreignKey) {
        Optional<DbTable> source = diagram.findTable(foreignKey.getSourceTableId());
        Optional<DbTable> target = diagram.findTable(foreignKey.getTargetTableId());
        if (source.isEmpty() || target.isEmpty()) return DbRelationMeaning.AMBIGUOUS_TARGET;
        Optional<DbColumn> sourceColumn = source.get().findColumn(foreignKey.getSourceColumnId());
        Optional<DbColumn> targetColumn = target.get().findColumn(foreignKey.getTargetColumnId());
        if (sourceColumn.isEmpty() || targetColumn.isEmpty()) return DbRelationMeaning.AMBIGUOUS_TARGET;
        if (!isIndividuallyUnique(target.get(), targetColumn.get())) return DbRelationMeaning.AMBIGUOUS_TARGET;
        return isIndividuallyUnique(source.get(), sourceColumn.get())
                ? DbRelationMeaning.ONE_TO_ONE
                : DbRelationMeaning.MANY_TO_ONE;
    }

    public static boolean isManyToManyJoinTable(Diagram diagram, DbTable table) {
        List<ForeignKey> outgoing = diagram.getForeignKeys().stream()
                .filter(foreignKey -> table.getId().equals(foreignKey.getSourceTableId()))
                .toList();
        if (outgoing.size() != 2) return false;
        Set<String> sourceColumnIds = new HashSet<>();
        for (ForeignKey foreignKey : outgoing) {
            Optional<DbTable> target = diagram.findTable(foreignKey.getTargetTableId());
            Optional<DbColumn> targetColumn = target.flatMap(t -> t.findColumn(foreignKey.getTargetColumnId()));
            if (target.isEmpty() || targetColumn.isEmpty() || !isIndividuallyUnique(target.get(), targetColumn.get())) {
                return false;
            }
            sourceColumnIds.add(foreignKey.getSourceColumnId());
        }
        return table.getConstraints().stream()
                .filter(constraint -> constraint.type() == DbTableConstraintType.COMPOSITE_PRIMARY_KEY
                        || constraint.type() == DbTableConstraintType.COMPOSITE_UNIQUE)
                .map(DbTableConstraint::columnIds)
                .anyMatch(columnIds -> columnIds.size() == sourceColumnIds.size()
                        && sourceColumnIds.containsAll(columnIds));
    }

    public static boolean isIndividuallyUnique(DbTable table, DbColumn column) {
        if (column.isPrimaryKey() || column.isUnique()) return true;
        return table.getConstraints().stream()
                .filter(constraint -> constraint.type() == DbTableConstraintType.COMPOSITE_PRIMARY_KEY
                        || constraint.type() == DbTableConstraintType.COMPOSITE_UNIQUE)
                .anyMatch(constraint -> constraint.columnIds().size() == 1
                        && constraint.columnIds().getFirst().equals(column.getId()));
    }
}
