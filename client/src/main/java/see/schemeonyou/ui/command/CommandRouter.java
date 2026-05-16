package see.schemeonyou.ui.command;

import see.schemeonyou.command.CommandMetadata;
import see.schemeonyou.model.DiagramType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Headless command routing slice: owns command metadata, palette search, and id -> action dispatch.
 * JavaFX views only wire Runnable actions and render the selected CommandMetadata.
 */
public class CommandRouter {
    private final CommandPalette palette = new CommandPalette();
    private final Map<String, Runnable> actionsById = new LinkedHashMap<>();

    public void register(CommandMetadata metadata, Runnable action) {
        palette.register(metadata);
        actionsById.put(metadata.id(), action);
    }

    public List<CommandMetadata> search(String query) {
        return palette.search(query == null ? "" : query);
    }

    public List<CommandMetadata> search(String query, DiagramType activeDiagramType) {
        return search(query).stream()
                .filter(command -> isAvailable(command, activeDiagramType))
                .toList();
    }

    public boolean canExecute(CommandMetadata command) {
        return command != null && actionsById.containsKey(command.id());
    }

    public boolean canExecute(CommandMetadata command, DiagramType activeDiagramType) {
        return canExecute(command) && isAvailable(command, activeDiagramType);
    }

    public boolean execute(CommandMetadata command) {
        if (command == null) return false;
        return execute(command.id());
    }

    public boolean execute(CommandMetadata command, DiagramType activeDiagramType) {
        if (!canExecute(command, activeDiagramType)) return false;
        return execute(command.id());
    }

    public boolean execute(String commandId) {
        Runnable action = actionsById.get(commandId);
        if (action == null) return false;
        action.run();
        return true;
    }

    public boolean isAvailable(CommandMetadata command, DiagramType activeDiagramType) {
        if (command == null) return false;
        return isAvailable(command.id(), activeDiagramType);
    }

    public boolean isAvailable(String commandId, DiagramType activeDiagramType) {
        if (commandId == null) return false;
        if (commandId.startsWith("db.")) return activeDiagramType == DiagramType.DATABASE;
        if (commandId.startsWith("sequence.")) return activeDiagramType == DiagramType.SEQUENCE;
        return true;
    }

    public Optional<Runnable> actionFor(String commandId) {
        return Optional.ofNullable(actionsById.get(commandId));
    }
}
