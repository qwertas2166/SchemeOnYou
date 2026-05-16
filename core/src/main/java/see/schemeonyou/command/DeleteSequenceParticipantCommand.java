package see.schemeonyou.command;

import see.schemeonyou.model.Diagram;
import see.schemeonyou.model.SequenceMessage;
import see.schemeonyou.model.SequenceParticipant;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DeleteSequenceParticipantCommand implements Command {
    private final Diagram diagram;
    private final SequenceParticipant participant;
    private int participantIndex = -1;
    private List<IndexedMessage> removedMessages = List.of();

    public DeleteSequenceParticipantCommand(Diagram diagram, SequenceParticipant participant) {
        this.diagram = diagram;
        this.participant = participant;
    }

    public CommandMetadata metadata() {
        return CommandMetadata.of("sequence.participant.delete", "Delete participant", "Delete", "sequence", "delete");
    }

    public void execute() {
        participantIndex = diagram.getParticipants().indexOf(participant);
        removedMessages = affectedMessages();
        diagram.getMessages().removeIf(message -> message.getFromParticipantId().equals(participant.getId())
                || message.getToParticipantId().equals(participant.getId()));
        diagram.getParticipants().remove(participant);
    }

    public void undo() {
        if (!diagram.getParticipants().contains(participant)) {
            int index = participantIndex < 0 ? diagram.getParticipants().size() : Math.min(participantIndex, diagram.getParticipants().size());
            diagram.getParticipants().add(index, participant);
        }
        removedMessages.stream()
                .sorted(Comparator.comparingInt(IndexedMessage::index))
                .forEach(removed -> {
                    if (!diagram.getMessages().contains(removed.message())) {
                        int index = Math.min(removed.index(), diagram.getMessages().size());
                        diagram.getMessages().add(index, removed.message());
                    }
                });
    }

    private List<IndexedMessage> affectedMessages() {
        List<IndexedMessage> messages = new ArrayList<>();
        for (int i = 0; i < diagram.getMessages().size(); i++) {
            SequenceMessage message = diagram.getMessages().get(i);
            if (message.getFromParticipantId().equals(participant.getId()) || message.getToParticipantId().equals(participant.getId())) {
                messages.add(new IndexedMessage(i, message));
            }
        }
        return messages;
    }

    private record IndexedMessage(int index, SequenceMessage message) { }
}
