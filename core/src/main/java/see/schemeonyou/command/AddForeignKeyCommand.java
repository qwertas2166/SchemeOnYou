package see.schemeonyou.command;

import see.schemeonyou.model.Diagram;
import see.schemeonyou.model.ForeignKey;

public class AddForeignKeyCommand implements Command {
    private final Diagram diagram;
    private final ForeignKey foreignKey;
    public AddForeignKeyCommand(Diagram diagram, ForeignKey foreignKey) { this.diagram = diagram; this.foreignKey = foreignKey; }
    public CommandMetadata metadata() { return CommandMetadata.of("db.fk.add", "Add foreign key", "Space A F", "foreign", "relation"); }
    public void execute() { diagram.getForeignKeys().add(foreignKey); }
    public void undo() { diagram.getForeignKeys().remove(foreignKey); }
}
