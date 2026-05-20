package see.schemeonyou.ui.command;

import see.schemeonyou.model.DiagramType;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Headless shortcut-help model so JavaFX help rendering cannot drift from
 * command/space availability rules.
 */
public class ShortcutHelpModel {
    private final ShortcutMap shortcuts;
    private final CommandRouter commandRouter;
    private final SpaceCommandSheet spaceCommandSheet;

    public ShortcutHelpModel(ShortcutMap shortcuts, CommandRouter commandRouter, SpaceCommandSheet spaceCommandSheet) {
        this.shortcuts = shortcuts;
        this.commandRouter = commandRouter;
        this.spaceCommandSheet = spaceCommandSheet;
    }

    public Map<String, String> entries(DiagramType activeDiagramType) {
        LinkedHashMap<String, String> scoped = new LinkedHashMap<>();
        shortcuts.asMap().forEach((shortcut, commandId) -> {
            if (isVisible(shortcut, commandId, activeDiagramType)) scoped.put(shortcut, commandId);
        });
        return Map.copyOf(scoped);
    }

    private boolean isVisible(String shortcut, String commandId, DiagramType activeDiagramType) {
        if (!commandRouter.isAvailable(commandId, activeDiagramType)) return false;
        String spaceChord = spaceChord(shortcut);
        return spaceChord == null || spaceCommandSheet.isAvailable(spaceChord, activeDiagramType);
    }

    private String spaceChord(String shortcut) {
        if (shortcut == null || !shortcut.startsWith("Space ")) return null;
        return shortcut.substring("Space ".length());
    }
}
