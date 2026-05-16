package see.schemeonyou.ui.search;

import org.junit.jupiter.api.Test;
import see.schemeonyou.model.DbColumn;
import see.schemeonyou.model.DbTable;
import see.schemeonyou.model.Diagram;
import see.schemeonyou.model.DiagramType;
import see.schemeonyou.model.ForeignKey;
import see.schemeonyou.model.SequenceMessage;
import see.schemeonyou.model.SequenceParticipant;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FindElementPresenterTest {
    private final FindElementPresenter presenter = new FindElementPresenter();

    @Test
    void indexesDbTablesColumnsForeignKeysParticipantsAndMessages() {
        Diagram diagram = sampleDiagram();

        assertKinds(presenter.search(diagram, "users"), FindElementPresenter.ElementKind.DB_TABLE);
        assertKinds(presenter.search(diagram, "email"), FindElementPresenter.ElementKind.DB_COLUMN);
        assertKinds(presenter.search(diagram, "fk.orders.user"), FindElementPresenter.ElementKind.FOREIGN_KEY);
        assertKinds(presenter.search(diagram, "browser"), FindElementPresenter.ElementKind.SEQUENCE_PARTICIPANT);
        assertKinds(presenter.search(diagram, "submit order"), FindElementPresenter.ElementKind.SEQUENCE_MESSAGE);
    }

    @Test
    void mapsColumnResultToTableAndColumnSelectionDepth() {
        FindElementPresenter.SearchResult result = presenter.search(sampleDiagram(), "email").getFirst();

        assertEquals(FindElementPresenter.ElementKind.DB_COLUMN, result.kind());
        assertEquals("users", result.selection().tableId());
        assertEquals("users.email", result.selection().columnId());
        assertNull(result.selection().foreignKeyId());
        assertNull(result.selection().participantId());
    }

    @Test
    void mapsForeignKeyAndSequenceResultsToAvailableSelectionTargets() {
        Diagram diagram = sampleDiagram();

        FindElementPresenter.SearchResult fk = presenter.search(diagram, "fk.orders.user").getFirst();
        assertEquals("fk.orders.user", fk.selection().foreignKeyId());
        assertNull(fk.selection().tableId());

        FindElementPresenter.SearchResult message = presenter.search(diagram, "submit order").getFirst();
        assertEquals("msg.submit", message.selection().messageId());
        assertEquals("browser", message.selection().participantId());
    }

    private void assertKinds(List<FindElementPresenter.SearchResult> results, FindElementPresenter.ElementKind expected) {
        assertFalse(results.isEmpty());
        assertEquals(expected, results.getFirst().kind());
    }

    private Diagram sampleDiagram() {
        Diagram diagram = new Diagram("d1", "Sample", DiagramType.DATABASE);
        DbTable users = new DbTable("users", "Users");
        users.getColumns().add(new DbColumn("users.id", "id", "uuid"));
        users.getColumns().add(new DbColumn("users.email", "email", "text"));
        DbTable orders = new DbTable("orders", "Orders");
        orders.getColumns().add(new DbColumn("orders.user_id", "user_id", "uuid"));
        diagram.getTables().add(users);
        diagram.getTables().add(orders);
        diagram.getForeignKeys().add(new ForeignKey("fk.orders.user", "orders", "orders.user_id", "users", "users.id"));

        diagram.getParticipants().add(new SequenceParticipant("browser", "Browser"));
        diagram.getParticipants().add(new SequenceParticipant("api", "API"));
        diagram.getMessages().add(new SequenceMessage("msg.submit", "browser", "api", "Submit order"));
        return diagram;
    }
}
