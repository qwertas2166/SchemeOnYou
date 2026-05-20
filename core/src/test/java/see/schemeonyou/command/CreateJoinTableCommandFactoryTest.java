package see.schemeonyou.command;

import org.junit.jupiter.api.Test;
import see.schemeonyou.model.*;

import static org.junit.jupiter.api.Assertions.*;

class CreateJoinTableCommandFactoryTest {
    @Test
    void createAddsCompositePrimaryKeyConstraintToJoinTable() {
        Diagram diagram = new Diagram("diagram-1", "Database", DiagramType.DATABASE);
        DbTable left = new DbTable("left", "users");
        DbColumn leftPk = new DbColumn("left-id", "id", "uuid");
        leftPk.setPrimaryKey(true);
        left.getColumns().add(leftPk);
        DbTable right = new DbTable("right", "roles");
        DbColumn rightPk = new DbColumn("right-id", "id", "uuid");
        rightPk.setPrimaryKey(true);
        right.getColumns().add(rightPk);
        diagram.getTables().add(left);
        diagram.getTables().add(right);

        DbTable join = new DbTable("join", "users_roles");
        DbColumn leftFk = new DbColumn("join-user-id", "user_id", "uuid");
        DbColumn rightFk = new DbColumn("join-role-id", "role_id", "uuid");
        leftFk.setNullable(false);
        rightFk.setNullable(false);
        leftFk.setPrimaryKey(true);
        rightFk.setPrimaryKey(true);
        ForeignKey leftRelation = new ForeignKey("fk-user", join.getId(), leftFk.getId(), left.getId(), leftPk.getId());
        ForeignKey rightRelation = new ForeignKey("fk-role", join.getId(), rightFk.getId(), right.getId(), rightPk.getId());

        Command command = new CreateJoinTableCommandFactory().create(diagram, left, leftPk, right, rightPk, join, leftFk, rightFk, leftRelation, rightRelation);
        command.execute();

        assertEquals(join, diagram.getTables().get(2));
        assertEquals(1, join.getConstraints().size());
        DbTableConstraint constraint = join.getConstraints().getFirst();
        assertEquals(DbTableConstraintType.COMPOSITE_PRIMARY_KEY, constraint.type());
        assertEquals(java.util.List.of(leftFk.getId(), rightFk.getId()), constraint.columnIds());
    }
}
