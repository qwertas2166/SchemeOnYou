package see.schemeonyou.command;

import see.schemeonyou.model.DbColumn;
import see.schemeonyou.model.DbTable;

public class AddColumnCommand implements Command {
    private final DbTable table;
    private final DbColumn column;
    public AddColumnCommand(DbTable table, DbColumn column) { this.table = table; this.column = column; }
    public CommandMetadata metadata() { return CommandMetadata.of("db.column.add", "Add column", "Space A C", "column", "field"); }
    public void execute() { table.getColumns().add(column); }
    public void undo() { table.getColumns().remove(column); }
}
