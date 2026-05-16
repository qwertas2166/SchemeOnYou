package see.schemeonyou.ui.search;

import see.schemeonyou.model.DbColumn;
import see.schemeonyou.model.DbTable;
import see.schemeonyou.model.Diagram;
import see.schemeonyou.model.ForeignKey;
import see.schemeonyou.model.SequenceMessage;
import see.schemeonyou.model.SequenceParticipant;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Headless search index for Ctrl+F / Space G S.
 * Keeps element discovery and result-to-selection mapping outside JavaFX dialogs.
 */
public class FindElementPresenter {
    public enum ElementKind { DB_TABLE, DB_COLUMN, FOREIGN_KEY, SEQUENCE_PARTICIPANT, SEQUENCE_MESSAGE }

    public record SearchResult(
            ElementKind kind,
            String id,
            String label,
            String contextLabel,
            SelectionTarget selection
    ) {}

    public record SelectionTarget(
            String tableId,
            String columnId,
            String foreignKeyId,
            String participantId,
            String messageId
    ) {
        public static SelectionTarget table(String tableId) {
            return new SelectionTarget(tableId, null, null, null, null);
        }

        public static SelectionTarget column(String tableId, String columnId) {
            return new SelectionTarget(tableId, columnId, null, null, null);
        }

        public static SelectionTarget foreignKey(String foreignKeyId) {
            return new SelectionTarget(null, null, foreignKeyId, null, null);
        }

        public static SelectionTarget participant(String participantId) {
            return new SelectionTarget(null, null, null, participantId, null);
        }

        public static SelectionTarget message(String messageId, String preferredParticipantId) {
            return new SelectionTarget(null, null, null, preferredParticipantId, messageId);
        }
    }

    public List<SearchResult> search(Diagram diagram, String query) {
        String normalized = normalize(query);
        if (normalized.isBlank()) return List.of();
        return index(diagram).stream()
                .filter(result -> matches(result, normalized))
                .sorted(Comparator
                        .comparingInt((SearchResult result) -> rank(result, normalized))
                        .thenComparing(SearchResult::label, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(SearchResult::id))
                .toList();
    }

    public List<SearchResult> index(Diagram diagram) {
        List<SearchResult> results = new ArrayList<>();
        for (DbTable table : diagram.getTables()) {
            results.add(new SearchResult(
                    ElementKind.DB_TABLE,
                    table.getId(),
                    table.getName(),
                    "table " + table.getName(),
                    SelectionTarget.table(table.getId())));
            for (DbColumn column : table.getColumns()) {
                results.add(new SearchResult(
                        ElementKind.DB_COLUMN,
                        column.getId(),
                        table.getName() + "." + column.getName(),
                        "column " + column.getName() + " in " + table.getName(),
                        SelectionTarget.column(table.getId(), column.getId())));
            }
        }
        for (ForeignKey fk : diagram.getForeignKeys()) {
            String source = columnLabel(diagram, fk.getSourceTableId(), fk.getSourceColumnId());
            String target = columnLabel(diagram, fk.getTargetTableId(), fk.getTargetColumnId());
            results.add(new SearchResult(
                    ElementKind.FOREIGN_KEY,
                    fk.getId(),
                    source + " -> " + target,
                    "foreign key " + fk.getId(),
                    SelectionTarget.foreignKey(fk.getId())));
        }
        for (SequenceParticipant participant : diagram.getParticipants()) {
            results.add(new SearchResult(
                    ElementKind.SEQUENCE_PARTICIPANT,
                    participant.getId(),
                    participant.getName(),
                    "participant " + participant.getName(),
                    SelectionTarget.participant(participant.getId())));
        }
        for (SequenceMessage message : diagram.getMessages()) {
            String from = participantName(diagram, message.getFromParticipantId()).orElse(message.getFromParticipantId());
            String to = participantName(diagram, message.getToParticipantId()).orElse(message.getToParticipantId());
            results.add(new SearchResult(
                    ElementKind.SEQUENCE_MESSAGE,
                    message.getId(),
                    message.getLabel(),
                    "message " + from + " -> " + to,
                    SelectionTarget.message(message.getId(), message.getFromParticipantId())));
        }
        return results;
    }

    private boolean matches(SearchResult result, String normalizedQuery) {
        return normalize(result.label()).contains(normalizedQuery)
                || normalize(result.contextLabel()).contains(normalizedQuery)
                || normalize(result.id()).contains(normalizedQuery);
    }

    private int rank(SearchResult result, String normalizedQuery) {
        String label = normalize(result.label());
        if (label.equals(normalizedQuery)) return 0;
        if (label.startsWith(normalizedQuery)) return 1;
        if (label.contains(normalizedQuery)) return 2;
        return 3;
    }

    private String columnLabel(Diagram diagram, String tableId, String columnId) {
        return diagram.findTable(tableId)
                .flatMap(table -> table.findColumn(columnId).map(column -> table.getName() + "." + column.getName()))
                .orElse(tableId + "." + columnId);
    }

    private Optional<String> participantName(Diagram diagram, String participantId) {
        return diagram.getParticipants().stream()
                .filter(participant -> participant.getId().equals(participantId))
                .map(SequenceParticipant::getName)
                .findFirst();
    }

    private String normalize(String value) {
        return value == null ? "" : value.toLowerCase(Locale.ROOT).trim();
    }
}
