package see.schemeonyou.command;

import see.di.See;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

@See
public class UndoRedoStack {
    private final Deque<Command> undo = new ArrayDeque<>();
    private final Deque<Command> redo = new ArrayDeque<>();

    public void run(Command command) {
        command.execute();
        undo.push(command);
        redo.clear();
    }
    public Optional<CommandMetadata> undo() {
        if (undo.isEmpty()) return Optional.empty();
        Command command = undo.pop();
        command.undo();
        redo.push(command);
        return Optional.of(command.metadata());
    }
    public Optional<CommandMetadata> redo() {
        if (redo.isEmpty()) return Optional.empty();
        Command command = redo.pop();
        command.execute();
        undo.push(command);
        return Optional.of(command.metadata());
    }
    public void clear() {
        undo.clear();
        redo.clear();
    }
    public boolean canUndo() { return !undo.isEmpty(); }
    public boolean canRedo() { return !redo.isEmpty(); }
}
