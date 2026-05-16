package see.schemeonyou.command;

import see.schemeonyou.model.DbColumn;
import see.schemeonyou.model.DbTable;
import see.schemeonyou.model.Diagram;
import see.schemeonyou.model.ForeignKey;

import java.util.ArrayList;
import java.util.List;

public class DeleteColumnCommand implements Command {
    private final Diagram diagram;
    private final DbTable table;
    private final DbColumn column;
    private final List<ForeignKey> removedForeignKeys = new ArrayList<>();
    private int columnIndex = -1;

    public DeleteColumnCommand(Diagram diagram, DbTable table, DbColumn column) {
        this.diagram = diagram;
        this.table = table;
        this.column = column;
    }

    public CommandMetadata metadata() {
        return CommandMetadata.of("db.column.delete", "Delete column", "Delete", "delete", "column").confirmable();
    }

    public void execute() {
        removedForeignKeys.clear();
        columnIndex = table.getColumns().indexOf(column);
        diagram.getForeignKeys().removeIf(fk -> {
            boolean match = (fk.getSourceTableId().equals(table.getId()) && fk.getSourceColumnId().equals(column.getId()))
                    || (fk.getTargetTableId().equals(table.getId()) && fk.getTargetColumnId().equals(column.getId()));
            if (match) removedForeignKeys.add(fk);
            return match;
        });
        table.getColumns().remove(column);
    }

    public void undo() {
        if (columnIndex >= 0 && columnIndex <= table.getColumns().size()) {
            table.getColumns().add(columnIndex, column);
        } else {
            table.getColumns().add(column);
        }
        diagram.getForeignKeys().addAll(removedForeignKeys);
    }
}
