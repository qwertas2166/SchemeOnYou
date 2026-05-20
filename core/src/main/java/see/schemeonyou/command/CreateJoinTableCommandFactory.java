package see.schemeonyou.command;

import see.di.See;
import see.schemeonyou.model.*;

import java.util.List;

@See
public class CreateJoinTableCommandFactory {
    public Command create(Diagram diagram, DbTable left, DbColumn leftPk, DbTable right, DbColumn rightPk, DbTable join, DbColumn leftFk, DbColumn rightFk, ForeignKey leftRelation, ForeignKey rightRelation) {
        ensureCompositePrimaryKey(join, leftFk, rightFk);
        return new CompoundCommand(
                CommandMetadata.of("db.joinTable.add", "Add join table", "Space A J", "join", "many-to-many"),
                List.of(
                        new AddTableCommand(diagram, join),
                        new AddColumnCommand(join, leftFk),
                        new AddColumnCommand(join, rightFk),
                        new AddForeignKeyCommand(diagram, leftRelation),
                        new AddForeignKeyCommand(diagram, rightRelation)
                )
        );
    }

    private void ensureCompositePrimaryKey(DbTable join, DbColumn leftFk, DbColumn rightFk) {
        boolean exists = join.getConstraints().stream().anyMatch(constraint ->
                constraint.type() == DbTableConstraintType.COMPOSITE_PRIMARY_KEY
                        && constraint.columnIds().equals(List.of(leftFk.getId(), rightFk.getId())));
        if (!exists) {
            join.getConstraints().add(new DbTableConstraint(
                    join.getId() + "_composite_pk",
                    DbTableConstraintType.COMPOSITE_PRIMARY_KEY,
                    List.of(leftFk.getId(), rightFk.getId())
            ));
        }
    }
}
