package see.schemeonyou.command;

import see.schemeonyou.model.DbTable;

public class RenameTableCommand implements Command {
    private final DbTable table;
    private final String newName;
    private String oldName;

    public RenameTableCommand(DbTable table, String newName) {
        this.table = table;
        this.newName = newName;
    }

    public CommandMetadata metadata() {
        return CommandMetadata.of("element.rename", "Rename selected", "Space E", "rename", "edit");
    }

    public void execute() {
        oldName = table.getName();
        table.setName(newName);
    }

    public void undo() {
        if (oldName != null) table.setName(oldName);
    }
}
