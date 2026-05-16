package see.schemeonyou.command;

import see.schemeonyou.model.Diagram;
import see.schemeonyou.model.ForeignKey;

public class DeleteForeignKeyCommand implements Command {
    private final Diagram diagram;
    private final ForeignKey foreignKey;
    private int index = -1;

    public DeleteForeignKeyCommand(Diagram diagram, ForeignKey foreignKey) {
        this.diagram = diagram;
        this.foreignKey = foreignKey;
    }

    public CommandMetadata metadata() {
        return CommandMetadata.of("db.fk.delete", "Delete foreign key", "Delete", "delete", "foreign key", "relation").confirmable();
    }

    public void execute() {
        index = diagram.getForeignKeys().indexOf(foreignKey);
        diagram.getForeignKeys().remove(foreignKey);
    }

    public void undo() {
        if (index >= 0 && index <= diagram.getForeignKeys().size()) {
            diagram.getForeignKeys().add(index, foreignKey);
        } else {
            diagram.getForeignKeys().add(foreignKey);
        }
    }
}
