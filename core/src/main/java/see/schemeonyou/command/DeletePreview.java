package see.schemeonyou.command;

import java.util.List;

public record DeletePreview(String commandId,
                            String title,
                            String targetId,
                            String targetName,
                            int tablesRemoved,
                            int columnsRemoved,
                            int foreignKeysRemoved,
                            List<String> affectedForeignKeyIds,
                            int sequenceParticipantsRemoved,
                            int sequenceMessagesRemoved,
                            List<String> affectedSequenceMessageIds) {
    public DeletePreview(String commandId,
                         String title,
                         String targetId,
                         String targetName,
                         int tablesRemoved,
                         int columnsRemoved,
                         int foreignKeysRemoved,
                         List<String> affectedForeignKeyIds) {
        this(commandId, title, targetId, targetName, tablesRemoved, columnsRemoved, foreignKeysRemoved,
                affectedForeignKeyIds, 0, 0, List.of());
    }

    public DeletePreview {
        affectedForeignKeyIds = List.copyOf(affectedForeignKeyIds);
        affectedSequenceMessageIds = List.copyOf(affectedSequenceMessageIds);
    }

    public boolean requiresConfirmation() {
        return tablesRemoved > 0 || columnsRemoved > 0 || foreignKeysRemoved > 0
                || sequenceParticipantsRemoved > 0 || sequenceMessagesRemoved > 0;
    }

    public String contextLine() {
        if (sequenceParticipantsRemoved > 0 || sequenceMessagesRemoved > 0) {
            String messageText = sequenceMessagesRemoved == 0 ? "no message removed" : sequenceMessagesRemoved + " message(s) removed";
            return "Confirm delete: " + targetName + " — " + messageText + " • Enter confirm • Esc cancel";
        }
        String fkText = foreignKeysRemoved == 0 ? "no FK removed" : foreignKeysRemoved + " FK(s) removed";
        return "Confirm delete: " + targetName + " — " + fkText + " • Enter confirm • Esc cancel";
    }
}
