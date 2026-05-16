package see.schemeonyou.command;

import java.util.List;

public record CommandMetadata(String id, String title, String shortcut, List<String> keywords, boolean requiresConfirmation) {
    public static CommandMetadata of(String id, String title, String shortcut, String... keywords) {
        return new CommandMetadata(id, title, shortcut, List.of(keywords), false);
    }
    public CommandMetadata confirmable() {
        return new CommandMetadata(id, title, shortcut, keywords, true);
    }
}
