package see.schemeonyou.command;

import see.schemeonyou.model.DbTable;
import see.schemeonyou.model.Diagram;
import see.schemeonyou.model.ForeignKey;

import java.util.ArrayList;
import java.util.List;

public class DeleteTableCommand implements Command {
    private final Diagram diagram;
    private final DbTable table;
    private final List<ForeignKey> removedForeignKeys = new ArrayList<>();
    private int tableIndex = -1;

    public DeleteTableCommand(Diagram diagram, DbTable table) { this.diagram = diagram; this.table = table; }
    public CommandMetadata metadata() { return CommandMetadata.of("db.table.delete", "Delete table", "Delete", "delete", "table").confirmable(); }
    public void execute() {
        removedForeignKeys.clear();
        tableIndex = diagram.getTables().indexOf(table);
        diagram.getForeignKeys().removeIf(fk -> {
            boolean match = fk.getSourceTableId().equals(table.getId()) || fk.getTargetTableId().equals(table.getId());
            if (match) removedForeignKeys.add(fk);
            return match;
        });
        diagram.getTables().remove(table);
    }
    public void undo() {
        if (tableIndex >= 0 && tableIndex <= diagram.getTables().size()) {
            diagram.getTables().add(tableIndex, table);
        } else {
            diagram.getTables().add(table);
        }
        diagram.getForeignKeys().addAll(removedForeignKeys);
    }
}
