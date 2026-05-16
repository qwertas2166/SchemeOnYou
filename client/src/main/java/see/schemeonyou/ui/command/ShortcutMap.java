package see.schemeonyou.ui.command;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class ShortcutMap {
    private final Map<String, String> commandByShortcut = new LinkedHashMap<>();

    public ShortcutMap() {
        this(false);
    }

    public ShortcutMap(boolean keyLogEnabled) {
        bind("Ctrl+S", "project.save"); bind("Ctrl+O", "project.open"); bind("Ctrl+F", "element.find");
        bind("Ctrl+Z", "undo"); bind("Ctrl+Shift+Z", "redo"); bind("Ctrl+Y", "redo");
        bind("0", "focus.topMenu"); bind("1", "focus.leftMenu"); bind("2", "focus.canvas"); bind("3", "focus.inspector");
        bind("Ctrl+K", "command.palette"); bind("Ctrl+Shift+P", "command.palette"); bind("Ctrl+N", "project.new");
        if (keyLogEnabled) bind("F12", "debug.keyLog");
        bind("F1", "help.shortcuts"); bind("Space A T", "db.table.add"); bind("Space A C", "db.column.add");
        bind("Space A P", "sequence.participant.add"); bind("Space A M", "sequence.message.add");
        bind("Space A F", "db.fk.add"); bind("Space A J", "db.joinTable.add"); bind("Space P", "db.relation.pin"); bind("Space U", "db.relation.clearPin");
        bind("Delete", "element.deleteSelected"); bind("Space D", "element.deleteSelected");
        bind("Space E", "element.edit"); bind("Space G T", "go.table"); bind("Space G S", "go.search");
        bind("Space L D", "layout.diagram"); bind("Space L S", "layout.selection");
    }
    public void bind(String shortcut, String commandId) { commandByShortcut.put(shortcut, commandId); }
    public Optional<String> commandFor(String shortcut) { return Optional.ofNullable(commandByShortcut.get(shortcut)); }
    public Map<String, String> asMap() { return Map.copyOf(commandByShortcut); }
}
