package see.schemeonyou.command;

import see.di.See;
import see.schemeonyou.model.*;

import java.util.List;

@See
public class CreateJoinTableCommandFactory {
    public Command create(Diagram diagram, DbTable left, DbColumn leftPk, DbTable right, DbColumn rightPk, DbTable join, DbColumn leftFk, DbColumn rightFk, ForeignKey leftRelation, ForeignKey rightRelation) {
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
}
