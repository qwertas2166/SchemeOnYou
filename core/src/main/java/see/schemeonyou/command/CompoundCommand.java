package see.schemeonyou.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompoundCommand implements Command {
    private final CommandMetadata metadata;
    private final List<Command> commands;

    public CompoundCommand(CommandMetadata metadata, List<Command> commands) {
        this.metadata = metadata;
        this.commands = List.copyOf(commands);
    }
    public CommandMetadata metadata() { return metadata; }
    public void execute() { commands.forEach(Command::execute); }
    public void undo() {
        ArrayList<Command> reversed = new ArrayList<>(commands);
        Collections.reverse(reversed);
        reversed.forEach(Command::undo);
    }
}
