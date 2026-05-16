package see.schemeonyou.command;

import see.schemeonyou.model.Diagram;
import see.schemeonyou.model.SequenceMessage;

public class DeleteSequenceMessageCommand implements Command {
    private final Diagram diagram;
    private final SequenceMessage message;
    private int originalIndex = -1;

    public DeleteSequenceMessageCommand(Diagram diagram, SequenceMessage message) {
        this.diagram = diagram;
        this.message = message;
    }

    public CommandMetadata metadata() {
        return CommandMetadata.of("sequence.message.delete", "Delete message", "Delete", "sequence", "delete");
    }

    public void execute() {
        originalIndex = diagram.getMessages().indexOf(message);
        diagram.getMessages().remove(message);
    }

    public void undo() {
        if (diagram.getMessages().contains(message)) return;
        int index = originalIndex < 0 ? diagram.getMessages().size() : Math.min(originalIndex, diagram.getMessages().size());
        diagram.getMessages().add(index, message);
    }
}
