package see.schemeonyou.command;

import see.schemeonyou.model.DbTable;
import see.schemeonyou.model.Diagram;

public class AddTableCommand implements Command {
    private final Diagram diagram;
    private final DbTable table;
    public AddTableCommand(Diagram diagram, DbTable table) { this.diagram = diagram; this.table = table; }
    public CommandMetadata metadata() { return CommandMetadata.of("db.table.add", "Add table", "Space A T", "table", "database"); }
    public void execute() { diagram.getTables().add(table); }
    public void undo() { diagram.getTables().remove(table); }
}
