package see.schemeonyou.ui.command;

import see.schemeonyou.command.CommandMetadata;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CommandPalette {
    private final List<CommandMetadata> commands = new ArrayList<>();
    public void register(CommandMetadata command) { commands.add(command); }
    public List<CommandMetadata> search(String query) {
        String q = query.toLowerCase();
        return commands.stream()
                .filter(c -> haystack(c).contains(q) || fuzzy(c.title().toLowerCase(), q))
                .sorted(Comparator.comparing(CommandMetadata::title))
                .toList();
    }
    private String haystack(CommandMetadata c) { return (c.id()+" "+c.title()+" "+c.shortcut()+" "+String.join(" ", c.keywords())).toLowerCase(); }
    private boolean fuzzy(String source, String query) {
        int pos = 0;
        for (char ch : source.toCharArray()) if (pos < query.length() && ch == query.charAt(pos)) pos++;
        return pos == query.length();
    }
}
