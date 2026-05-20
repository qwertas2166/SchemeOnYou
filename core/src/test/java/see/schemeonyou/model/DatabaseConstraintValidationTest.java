package see.schemeonyou.model;

import org.junit.jupiter.api.Test;
import see.schemeonyou.validation.DiagramValidator;
import see.schemeonyou.validation.ValidationIssue;
import see.schemeonyou.validation.ValidationResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseConstraintValidationTest {
    @Test
    void compositeConstraintDoesNotMakeOneColumnFkTargetUnique() {
        Diagram diagram = new Diagram("db", "DB", DiagramType.DATABASE);
        DbTable source = new DbTable("orders", "orders");
        source.getColumns().add(new DbColumn("customer_id", "customer_id", "bigint"));
        DbTable target = new DbTable("customers", "customers");
        target.getColumns().add(new DbColumn("tenant_id", "tenant_id", "bigint"));
        target.getColumns().add(new DbColumn("customer_id", "customer_id", "bigint"));
        target.getConstraints().add(new DbTableConstraint(
                "customers_pk",
                DbTableConstraintType.COMPOSITE_PRIMARY_KEY,
                List.of("tenant_id", "customer_id")
        ));
        diagram.getTables().add(source);
        diagram.getTables().add(target);
        diagram.getForeignKeys().add(new ForeignKey("fk_customer", "orders", "customer_id", "customers", "customer_id"));

        ValidationResult result = new DiagramValidator().validate(diagram);

        assertFalse(result.hasErrors());
        assertIssue(result, "fk.target.not.unique", "fk_customer");
    }

    @Test
    void derivesOneToOneAndManyToOneFromIndividualUniquenessOnly() {
        Diagram diagram = new Diagram("db", "DB", DiagramType.DATABASE);
        DbTable source = new DbTable("orders", "orders");
        DbColumn uniqueSourceColumn = new DbColumn("external_id", "external_id", "uuid");
        uniqueSourceColumn.setUnique(true);
        source.getColumns().add(uniqueSourceColumn);
        DbColumn manySourceColumn = new DbColumn("customer_id", "customer_id", "uuid");
        source.getColumns().add(manySourceColumn);
        DbTable target = new DbTable("customers", "customers");
        DbColumn targetPk = new DbColumn("id", "id", "uuid");
        targetPk.setPrimaryKey(true);
        target.getColumns().add(targetPk);
        diagram.getTables().add(source);
        diagram.getTables().add(target);

        assertEquals(DbRelationMeaning.ONE_TO_ONE, DbRelationSemantics.derive(diagram,
                new ForeignKey("fk_unique", "orders", "external_id", "customers", "id")));
        assertEquals(DbRelationMeaning.MANY_TO_ONE, DbRelationSemantics.derive(diagram,
                new ForeignKey("fk_many", "orders", "customer_id", "customers", "id")));
    }

    @Test
    void detectsManyToManyJoinTableFromTwoFksAndCompositeConstraint() {
        Diagram diagram = new Diagram("db", "DB", DiagramType.DATABASE);
        DbTable users = tableWithPk("users");
        DbTable roles = tableWithPk("roles");
        DbTable join = new DbTable("users_roles", "users_roles");
        join.getColumns().add(new DbColumn("user_id", "user_id", "uuid"));
        join.getColumns().add(new DbColumn("role_id", "role_id", "uuid"));
        join.getConstraints().add(new DbTableConstraint("users_roles_pk", DbTableConstraintType.COMPOSITE_PRIMARY_KEY, List.of("user_id", "role_id")));
        diagram.getTables().add(users);
        diagram.getTables().add(roles);
        diagram.getTables().add(join);
        diagram.getForeignKeys().add(new ForeignKey("fk_user", "users_roles", "user_id", "users", "id"));
        diagram.getForeignKeys().add(new ForeignKey("fk_role", "users_roles", "role_id", "roles", "id"));

        assertTrue(DbRelationSemantics.isManyToManyJoinTable(diagram, join));
    }

    private static DbTable tableWithPk(String id) {
        DbTable table = new DbTable(id, id);
        DbColumn pk = new DbColumn("id", "id", "uuid");
        pk.setPrimaryKey(true);
        table.getColumns().add(pk);
        return table;
    }

    private static void assertIssue(ValidationResult result, String code, String elementId) {
        assertTrue(result.issues().stream().anyMatch(issue -> code.equals(issue.code()) && elementId.equals(issue.elementId())),
                () -> "Expected issue " + code + " for " + elementId + ", got " + result.issues().stream().map(ValidationIssue::code).toList());
    }
}
