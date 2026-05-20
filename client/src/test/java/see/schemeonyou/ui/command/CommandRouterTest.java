package see.schemeonyou.ui.command;

import org.junit.jupiter.api.Test;
import see.schemeonyou.command.CommandMetadata;
import see.schemeonyou.model.DiagramType;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class CommandRouterTest {
    @Test
    void searchesCommandMetadataWithoutJavaFx() {
        CommandRouter router = new CommandRouter();
        router.register(CommandMetadata.of("project.save", "Save project", "Ctrl+S", "save"), () -> {});
        router.register(CommandMetadata.of("diagram.sequence.new", "New sequence diagram", "", "sequence", "diagram"), () -> {});

        assertEquals("diagram.sequence.new", router.search("seq").getFirst().id());
        assertEquals("project.save", router.search("Ctrl+S").getFirst().id());
    }

    @Test
    void shortcutMapIncludesMvpGlobalShortcutAliases() {
        ShortcutMap shortcuts = new ShortcutMap();

        assertEquals("command.palette", shortcuts.commandFor("Ctrl+K").orElseThrow());
        assertEquals("command.palette", shortcuts.commandFor("Ctrl+Shift+P").orElseThrow());
        assertEquals("project.new", shortcuts.commandFor("Ctrl+N").orElseThrow());
        assertEquals("redo", shortcuts.commandFor("Ctrl+Shift+Z").orElseThrow());
        assertEquals("redo", shortcuts.commandFor("Ctrl+Y").orElseThrow());
    }

    @Test
    void shortcutMapKeepsKeyLogDevOnly() {
        ShortcutMap releaseShortcuts = new ShortcutMap();
        ShortcutMap debugShortcuts = new ShortcutMap(true);

        assertTrue(releaseShortcuts.commandFor("F12").isEmpty(), "release/default help must not advertise key log");
        assertFalse(releaseShortcuts.asMap().containsValue("debug.keyLog"));
        assertEquals("debug.keyLog", debugShortcuts.commandFor("F12").orElseThrow());
    }

    @Test
    void dispatchesRegisteredActionsAndRejectsUnwiredCommand() {
        CommandRouter router = new CommandRouter();
        AtomicInteger calls = new AtomicInteger();
        CommandMetadata wired = CommandMetadata.of("project.save", "Save project", "Ctrl+S", "save");
        CommandMetadata unwired = CommandMetadata.of("project.open", "Open project", "Ctrl+O", "open");

        router.register(wired, calls::incrementAndGet);

        assertTrue(router.canExecute(wired));
        assertTrue(router.execute(wired));
        assertEquals(1, calls.get());
        assertFalse(router.canExecute(unwired));
        assertFalse(router.execute(unwired));
        assertFalse(router.execute("missing.command"));
    }

    @Test
    void filtersCommandAvailabilityByDiagramContext() {
        CommandRouter router = new CommandRouter();
        router.register(CommandMetadata.of("db.table.add", "Add table", "Space A T", "table"), () -> {});
        router.register(CommandMetadata.of("sequence.participant.add", "Add participant", "Space A P", "sequence"), () -> {});
        router.register(CommandMetadata.of("element.find", "Find element", "Ctrl+F", "find"), () -> {});

        assertEquals(List.of("db.table.add", "element.find"), router.search("", DiagramType.DATABASE).stream().map(CommandMetadata::id).sorted().toList());
        assertEquals(List.of("element.find", "sequence.participant.add"), router.search("", DiagramType.SEQUENCE).stream().map(CommandMetadata::id).sorted().toList());
    }

    @Test
    void doesNotExecuteDatabaseCommandInSequenceContext() {
        CommandRouter router = new CommandRouter();
        AtomicInteger tableMutations = new AtomicInteger();
        CommandMetadata addTable = CommandMetadata.of("db.table.add", "Add table", "Space A T", "table");
        router.register(addTable, tableMutations::incrementAndGet);

        assertFalse(router.canExecute(addTable, DiagramType.SEQUENCE));
        assertFalse(router.execute(addTable, DiagramType.SEQUENCE));
        assertEquals(0, tableMutations.get(), "Space A T / Add table must not mutate sequence diagrams");

        assertTrue(router.execute(addTable, DiagramType.DATABASE));
        assertEquals(1, tableMutations.get());
    }

    @Test
    void spaceCommandSheetOnlyAdvertisesCurrentDiagramCommands() {
        SpaceCommandSheet sheet = new SpaceCommandSheet();

        assertTrue(sheet.entries(DiagramType.DATABASE).containsKey("A T"));
        assertFalse(sheet.entries(DiagramType.DATABASE).containsKey("A P"));
        assertTrue(sheet.entries(DiagramType.SEQUENCE).containsKey("A P"));
        assertFalse(sheet.entries(DiagramType.SEQUENCE).containsKey("A T"));
        assertFalse(sheet.entries(DiagramType.SEQUENCE).containsKey("A F"));
    }

    @Test
    void shortcutHelpFiltersSpaceCommandsByDiagramContext() {
        ShortcutHelpModel help = shortcutHelpModel();

        assertTrue(help.entries(DiagramType.DATABASE).containsKey("Space A T"));
        assertFalse(help.entries(DiagramType.DATABASE).containsKey("Space A P"));
        assertTrue(help.entries(DiagramType.SEQUENCE).containsKey("Space A P"));
        assertFalse(help.entries(DiagramType.SEQUENCE).containsKey("Space A T"));
        assertFalse(help.entries(DiagramType.SEQUENCE).containsKey("Space A F"));
        assertFalse(help.entries(DiagramType.DATABASE).containsKey("Space G T"));
        assertFalse(help.entries(DiagramType.SEQUENCE).containsKey("Space G T"));
        assertFalse(help.entries(DiagramType.DATABASE).containsKey("Space L S"));
        assertFalse(help.entries(DiagramType.SEQUENCE).containsKey("Space L S"));
    }

    @Test
    void shortcutHelpKeepsGlobalShortcutsAndHandlesMissingDiagramSafely() {
        ShortcutHelpModel help = shortcutHelpModel();

        assertEquals("project.save", help.entries(DiagramType.SEQUENCE).get("Ctrl+S"));
        assertEquals("help.shortcuts", help.entries(DiagramType.DATABASE).get("F1"));
        assertEquals("undo", help.entries(null).get("Ctrl+Z"));
        assertEquals("element.deleteSelected", help.entries(null).get("Delete"));
        assertFalse(help.entries(null).containsKey("Space A T"));
        assertFalse(help.entries(null).containsKey("Space A P"));
        assertFalse(help.entries(null).containsKey("Space G T"));
        assertFalse(help.entries(null).containsKey("Space L S"));
    }

    @Test
    void shortcutMapDoesNotAdvertiseUnwiredSpaceCommands() {
        ShortcutMap shortcuts = new ShortcutMap();

        assertTrue(shortcuts.commandFor("Space G T").isEmpty());
        assertTrue(shortcuts.commandFor("Space L S").isEmpty());
        assertEquals("element.edit", shortcuts.commandFor("Space E").orElseThrow());
        assertEquals("element.find", shortcuts.commandFor("Space G S").orElseThrow());
    }

    private ShortcutHelpModel shortcutHelpModel() {
        return new ShortcutHelpModel(new ShortcutMap(), new CommandRouter(), new SpaceCommandSheet());
    }
}
