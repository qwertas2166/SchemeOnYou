package see.schemeonyou.command;

public interface Command {
    CommandMetadata metadata();
    void execute();
    void undo();
}
