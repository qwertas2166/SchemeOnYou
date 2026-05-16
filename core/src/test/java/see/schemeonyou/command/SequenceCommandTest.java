package see.schemeonyou.command;

import org.junit.jupiter.api.Test;
import see.schemeonyou.model.Diagram;
import see.schemeonyou.model.DiagramType;
import see.schemeonyou.model.SequenceMessage;
import see.schemeonyou.model.SequenceParticipant;

import static org.junit.jupiter.api.Assertions.*;

class SequenceCommandTest {
    @Test
    void addsParticipantAndMessageWithUndoRedo() {
        Diagram diagram = new Diagram("seq", "Checkout", DiagramType.SEQUENCE);
        SequenceParticipant web = new SequenceParticipant("web", "Web");
        SequenceParticipant api = new SequenceParticipant("api", "API");
        SequenceMessage message = new SequenceMessage("m1", web.getId(), api.getId(), "POST /checkout");
        UndoRedoStack undo = new UndoRedoStack();

        undo.run(new AddSequenceParticipantCommand(diagram, web));
        undo.run(new AddSequenceParticipantCommand(diagram, api));
        undo.run(new AddSequenceMessageCommand(diagram, message));

        assertEquals(2, diagram.getParticipants().size());
        assertEquals(message, diagram.getMessages().getFirst());

        assertEquals("sequence.message.add", undo.undo().orElseThrow().id());
        assertTrue(diagram.getMessages().isEmpty());
        assertEquals("sequence.participant.add", undo.undo().orElseThrow().id());
        assertEquals(1, diagram.getParticipants().size());

        undo.redo();
        undo.redo();
        assertEquals(2, diagram.getParticipants().size());
        assertEquals("m1", diagram.getMessages().getFirst().getId());
    }

    @Test
    void editsSequenceFieldsUndoably() {
        SequenceParticipant participant = new SequenceParticipant("api", "API");
        SequenceMessage message = new SequenceMessage("m1", "web", "api", "old");
        UndoRedoStack undo = new UndoRedoStack();

        undo.run(new EditValueCommand<>(participant, "sequence.participant.rename", "Rename participant", SequenceParticipant::getName, SequenceParticipant::setName, "Backend"));
        undo.run(new EditValueCommand<>(message, "sequence.message.label", "Edit message label", SequenceMessage::getLabel, SequenceMessage::setLabel, "new"));
        undo.run(new EditValueCommand<>(message, "sequence.message.activation", "Set message activation", SequenceMessage::isActivation, SequenceMessage::setActivation, true));

        assertEquals("Backend", participant.getName());
        assertEquals("new", message.getLabel());
        assertTrue(message.isActivation());

        undo.undo();
        assertFalse(message.isActivation());
        undo.undo();
        assertEquals("old", message.getLabel());
        undo.undo();
        assertEquals("API", participant.getName());
    }
}
