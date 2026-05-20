package see.schemeonyou.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FkPreviewSemanticsTest {
    @Test
    void singleColumnTableLevelUniqueConstraintDoesNotWarnTargetNotUnique() {
        Diagram diagram = baseDiagram();
        DbTable target = diagram.findTable("customers").orElseThrow();
        target.getConstraints().add(new DbTableConstraint("customers_unique", DbTableConstraintType.COMPOSITE_UNIQUE, List.of("id")));

        List<String> warnings = FkPreviewSemantics.warnings(diagram, preview());

        assertFalse(warnings.contains("target column is not PK or unique; relation meaning may be ambiguous"));
        assertEquals(DbRelationMeaning.MANY_TO_ONE, FkPreviewSemantics.relationMeaning(diagram, preview()));
        assertTrue(FkPreviewSemantics.relationMeaningLabel(diagram, preview()).startsWith("many-to-one"));
    }

    @Test
    void multiColumnCompositeConstraintKeepsAmbiguousTargetWarning() {
        Diagram diagram = baseDiagram();
        DbTable target = diagram.findTable("customers").orElseThrow();
        target.getColumns().add(new DbColumn("tenant_id", "tenant_id", "bigint"));
        target.getConstraints().add(new DbTableConstraint("customers_pk", DbTableConstraintType.COMPOSITE_PRIMARY_KEY, List.of("tenant_id", "id")));

        List<String> warnings = FkPreviewSemantics.warnings(diagram, preview());

        assertTrue(warnings.contains("target column is not PK or unique; relation meaning may be ambiguous"));
        assertEquals(DbRelationMeaning.AMBIGUOUS_TARGET, FkPreviewSemantics.relationMeaning(diagram, preview()));
        assertTrue(FkPreviewSemantics.relationMeaningLabel(diagram, preview()).startsWith("ambiguous/unknown"));
    }

    @Test
    void uniqueSourceAndTargetPreviewDerivesOneToOne() {
        Diagram diagram = baseDiagram();
        DbTable source = diagram.findTable("orders").orElseThrow();
        source.findColumn("customer_id").orElseThrow().setUnique(true);
        DbTable target = diagram.findTable("customers").orElseThrow();
        target.findColumn("id").orElseThrow().setPrimaryKey(true);

        assertEquals(DbRelationMeaning.ONE_TO_ONE, FkPreviewSemantics.relationMeaning(diagram, preview()));
        assertTrue(FkPreviewSemantics.relationMeaningLabel(diagram, preview()).startsWith("one-to-one"));
    }

    private static Diagram baseDiagram() {
        Diagram diagram = new Diagram("db", "DB", DiagramType.DATABASE);
        DbTable source = new DbTable("orders", "orders");
        source.getColumns().add(new DbColumn("customer_id", "customer_id", "bigint"));
        DbTable target = new DbTable("customers", "customers");
        target.getColumns().add(new DbColumn("id", "id", "bigint"));
        diagram.getTables().add(source);
        diagram.getTables().add(target);
        return diagram;
    }

    private static FkPreview preview() {
        return new FkPreview("orders", "customer_id", "customers", "id");
    }
}
