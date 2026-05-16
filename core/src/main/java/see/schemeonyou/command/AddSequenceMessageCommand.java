package see.schemeonyou.command;

import see.schemeonyou.model.Diagram;
import see.schemeonyou.model.SequenceMessage;

public class AddSequenceMessageCommand implements Command {
    private final Diagram diagram;
    private final SequenceMessage message;
    public AddSequenceMessageCommand(Diagram diagram, SequenceMessage message) { this.diagram = diagram; this.message = message; }
    public CommandMetadata metadata() { return CommandMetadata.of("sequence.message.add", "Add message", "Space A M", "sequence", "message"); }
    public void execute() { diagram.getMessages().add(message); }
    public void undo() { diagram.getMessages().remove(message); }
}
