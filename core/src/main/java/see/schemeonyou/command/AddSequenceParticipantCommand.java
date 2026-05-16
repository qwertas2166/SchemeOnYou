package see.schemeonyou.command;

import see.schemeonyou.model.Diagram;
import see.schemeonyou.model.SequenceParticipant;

public class AddSequenceParticipantCommand implements Command {
    private final Diagram diagram;
    private final SequenceParticipant participant;
    public AddSequenceParticipantCommand(Diagram diagram, SequenceParticipant participant) { this.diagram = diagram; this.participant = participant; }
    public CommandMetadata metadata() { return CommandMetadata.of("sequence.participant.add", "Add participant", "Space A P", "sequence", "participant"); }
    public void execute() { diagram.getParticipants().add(participant); }
    public void undo() { diagram.getParticipants().remove(participant); }
}
