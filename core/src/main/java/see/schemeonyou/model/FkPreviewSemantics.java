package see.schemeonyou.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class FkPreviewSemantics {
    private FkPreviewSemantics() {}

    public static DbRelationMeaning relationMeaning(Diagram diagram, FkPreview preview) {
        return DbRelationSemantics.derive(diagram, new ForeignKey(
                "__fk_preview__",
                preview.getSourceTableId(),
                preview.getSourceColumnId(),
                preview.getTargetTableId(),
                preview.getTargetColumnId()
        ));
    }

    public static String relationMeaningLabel(Diagram diagram, FkPreview preview) {
        return switch (relationMeaning(diagram, preview)) {
            case MANY_TO_ONE -> "many-to-one (many source rows may reference one target row)";
            case ONE_TO_ONE -> "one-to-one (source and target columns are individually unique)";
            case AMBIGUOUS_TARGET -> "ambiguous/unknown target uniqueness";
        };
    }

    public static List<String> errors(Diagram diagram, FkPreview preview) {
        Optional<DbTable> source = diagram.findTable(preview.getSourceTableId());
        Optional<DbTable> target = diagram.findTable(preview.getTargetTableId());
        if (source.isEmpty() || target.isEmpty()) return List.of("source or target table is missing");
        Optional<DbColumn> sourceColumn = source.get().findColumn(preview.getSourceColumnId());
        Optional<DbColumn> targetColumn = target.get().findColumn(preview.getTargetColumnId());
        if (sourceColumn.isEmpty() || targetColumn.isEmpty()) return List.of("source or target column is missing");
        if (preview.getSourceTableId().equals(preview.getTargetTableId()) && preview.getSourceColumnId().equals(preview.getTargetColumnId())) {
            return List.of("source and target are the same column");
        }
        return List.of();
    }

    public static List<String> warnings(Diagram diagram, FkPreview preview) {
        if (!errors(diagram, preview).isEmpty()) return List.of();
        DbTable source = diagram.findTable(preview.getSourceTableId()).orElseThrow();
        DbTable target = diagram.findTable(preview.getTargetTableId()).orElseThrow();
        DbColumn sourceColumn = source.findColumn(preview.getSourceColumnId()).orElseThrow();
        DbColumn targetColumn = target.findColumn(preview.getTargetColumnId()).orElseThrow();
        ArrayList<String> warnings = new ArrayList<>();
        if (!sourceColumn.getType().equalsIgnoreCase(targetColumn.getType())) {
            warnings.add("source type " + sourceColumn.getType() + " differs from target type " + targetColumn.getType());
        }
        if (!DbRelationSemantics.isIndividuallyUnique(target, targetColumn)) {
            warnings.add("target column is not PK or unique; relation meaning may be ambiguous");
        }
        return warnings;
    }
}
