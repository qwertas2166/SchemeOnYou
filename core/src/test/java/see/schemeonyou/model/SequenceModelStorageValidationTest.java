package see.schemeonyou.model;

import org.junit.jupiter.api.Test;
import see.schemeonyou.command.AddSequenceMessageCommand;
import see.schemeonyou.storage.SchemeProjectJsonReader;
import see.schemeonyou.storage.SchemeProjectJsonWriter;
import see.schemeonyou.validation.DiagramValidator;
import see.schemeonyou.validation.ValidationIssue;
import see.schemeonyou.validation.ValidationResult;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SequenceModelStorageValidationTest {
    @Test
    void roundTripsParticipantAndMessageTypesAndOrder() throws Exception {
        SchemeProject project = new SchemeProject("project-1", "Demo");
        Diagram sequence = new Diagram("sequence-1", "Checkout", DiagramType.SEQUENCE);
        sequence.getParticipants().add(new SequenceParticipant("actor-1", "User", SequenceParticipantType.ACTOR));
        sequence.getParticipants().add(new SequenceParticipant("db-1", "Orders", SequenceParticipantType.DATABASE));
        SequenceMessage message = new SequenceMessage("message-1", "actor-1", "db-1", "save", SequenceMessageType.ASYNC, 7);
        message.setActivation(true);
        sequence.getMessages().add(message);
        project.getDiagrams().add(sequence);

        SchemeProjectJsonWriter writer = new SchemeProjectJsonWriter();
        SchemeProject loaded = new SchemeProjectJsonReader().read(writer.write(project));
        Diagram loadedSequence = loaded.getDiagrams().getFirst();

        assertEquals(SequenceParticipantType.ACTOR, loadedSequence.getParticipants().getFirst().getType());
        assertEquals(SequenceMessageType.ASYNC, loadedSequence.getMessages().getFirst().getType());
        assertEquals(7, loadedSequence.getMessages().getFirst().getOrder());
        assertEquals(writer.write(project), writer.write(loaded));
    }

    @Test
    void oldJsonGetsSafeSequenceDefaults() throws Exception {
        SchemeProject project = new SchemeProjectJsonReader().read("""
                {"version": 1, "id": "p", "name": "Demo", "createdAt": "2026-01-01T00:00:00Z", "updatedAt": "2026-01-01T00:00:00Z", "diagrams": [
                  {"id":"s","name":"Sequence","type":"SEQUENCE","canvas":{"viewportX":0,"viewportY":0,"zoom":1,"bounds":{}},"tables":[],"foreignKeys":[],"participants":[
                    {"id":"client","name":"Client"}, {"id":"api","name":"API"}
                  ],"messages":[
                    {"id":"m1","from":"client","to":"api","label":"request","activation":false},
                    {"id":"m2","from":"api","to":"client","label":"response","activation":false}
                  ]}
                ]}
                """);

        Diagram sequence = project.getDiagrams().getFirst();
        assertEquals(SequenceParticipantType.SERVICE, sequence.getParticipants().getFirst().getType());
        assertEquals(SequenceMessageType.SYNC, sequence.getMessages().getFirst().getType());
        assertEquals(1, sequence.getMessages().getFirst().getOrder());
        assertEquals(2, sequence.getMessages().get(1).getOrder());
    }

    @Test
    void rejectsUnknownSequenceTypeValues() {
        IOException error = assertThrows(IOException.class, () -> new SchemeProjectJsonReader().read("""
                {"version": 1, "id": "p", "name": "Demo", "createdAt": "2026-01-01T00:00:00Z", "updatedAt": "2026-01-01T00:00:00Z", "diagrams": [
                  {"id":"s","name":"Sequence","type":"SEQUENCE","canvas":{"viewportX":0,"viewportY":0,"zoom":1,"bounds":{}},"tables":[],"foreignKeys":[],"participants":[
                    {"id":"client","name":"Client","type":"robot"}
                  ],"messages":[]}
                ]}
                """));

        assertTrue(error.getMessage().contains("Invalid sequence participant type"));
    }

    @Test
    void validatesMissingEndpointsDuplicateOrderAndSelfCallRules() {
        Diagram sequence = new Diagram("sequence-1", "Broken", DiagramType.SEQUENCE);
        sequence.getParticipants().add(new SequenceParticipant("client", "Client"));
        sequence.getMessages().add(new SequenceMessage("missing", "client", "api", "call", SequenceMessageType.SYNC, 1));
        sequence.getMessages().add(new SequenceMessage("duplicate", "client", "client", "self", SequenceMessageType.SYNC, 1));
        sequence.getMessages().add(new SequenceMessage("wrong-self", "client", "api", "self", SequenceMessageType.SELF_CALL, 2));

        ValidationResult result = new DiagramValidator().validate(sequence);

        assertTrue(result.hasErrors());
        assertIssue(result, "sequence.message.endpoint.missing", "missing");
        assertIssue(result, "sequence.message.order.invalid", "duplicate");
        assertIssue(result, "sequence.message.self_call.endpoint", "wrong-self");
        assertIssue(result, "sequence.message.self_endpoint.type", "duplicate");
    }

    @Test
    void addMessageAssignsNextOrderForRuntimeCreatedMessages() {
        Diagram sequence = new Diagram("sequence-1", "Runtime", DiagramType.SEQUENCE);
        SequenceMessage first = new SequenceMessage("m1", "a", "b", "first", SequenceMessageType.SYNC, 3);
        sequence.getMessages().add(first);
        SequenceMessage next = new SequenceMessage("m2", "a", "b", "next");

        new AddSequenceMessageCommand(sequence, next).execute();

        assertEquals(4, next.getOrder());
    }

    private static void assertIssue(ValidationResult result, String code, String elementId) {
        assertTrue(result.issues().stream().anyMatch(issue -> code.equals(issue.code()) && elementId.equals(issue.elementId())),
                () -> "Expected issue " + code + " for " + elementId + ", got " + result.issues().stream().map(ValidationIssue::code).toList());
    }
}
