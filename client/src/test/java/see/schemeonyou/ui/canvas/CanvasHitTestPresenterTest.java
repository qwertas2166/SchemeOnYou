package see.schemeonyou.ui.canvas;

import org.junit.jupiter.api.Test;
import see.schemeonyou.model.Diagram;
import see.schemeonyou.model.DiagramType;
import see.schemeonyou.model.DbTable;
import see.schemeonyou.model.ForeignKey;
import see.schemeonyou.model.Rect;
import see.schemeonyou.model.SequenceMessage;
import see.schemeonyou.model.SequenceParticipant;

import static org.junit.jupiter.api.Assertions.*;

class CanvasHitTestPresenterTest {
    private final CanvasHitTestPresenter presenter = new CanvasHitTestPresenter();

    @Test
    void databaseHitPrefersTableBeforeForeignKey() {
        Diagram diagram = databaseDiagram();

        CanvasHitTestPresenter.Hit hit = presenter.hitAtModel(diagram, 25, 25).orElseThrow();

        assertInstanceOf(CanvasHitTestPresenter.TableHit.class, hit);
        assertEquals("users", hit.elementId());
        assertEquals("users", presenter.draggableElementAtModel(diagram, 25, 25).orElseThrow());
    }

    @Test
    void databaseHitResolvesForeignKeyNearEdge() {
        Diagram diagram = databaseDiagram();

        CanvasHitTestPresenter.Hit hit = presenter.hitAtModel(diagram, 150, 50).orElseThrow();

        assertInstanceOf(CanvasHitTestPresenter.ForeignKeyHit.class, hit);
        assertEquals("fk.orders.users", hit.elementId());
        assertTrue(presenter.draggableElementAtModel(diagram, 150, 50).isEmpty());
    }

    @Test
    void canvasCoordinatesRespectViewportAndZoom() {
        Diagram diagram = databaseDiagram();
        diagram.getCanvasState().setViewportX(10);
        diagram.getCanvasState().setViewportY(20);
        diagram.getCanvasState().setZoom(2.0);

        CanvasHitTestPresenter.Hit hit = presenter.hitAt(diagram, 30, 10).orElseThrow();

        assertInstanceOf(CanvasHitTestPresenter.TableHit.class, hit);
        assertEquals("users", hit.elementId());
    }

    @Test
    void sequenceHitPrefersMessageBeforeParticipant() {
        Diagram diagram = sequenceDiagram();

        CanvasHitTestPresenter.Hit hit = presenter.hitAtModel(diagram, 150, 150).orElseThrow();

        assertInstanceOf(CanvasHitTestPresenter.MessageHit.class, hit);
        assertEquals("m1", hit.elementId());
        CanvasHitTestPresenter.MessageHit message = (CanvasHitTestPresenter.MessageHit) hit;
        assertEquals("web", message.fromParticipantId());
    }

    @Test
    void sequenceParticipantIsDraggableButMessageIsNot() {
        Diagram diagram = sequenceDiagram();

        CanvasHitTestPresenter.Hit hit = presenter.hitAtModel(diagram, 25, 25).orElseThrow();

        assertInstanceOf(CanvasHitTestPresenter.ParticipantHit.class, hit);
        assertEquals("web", hit.elementId());
        assertEquals("web", presenter.draggableElementAtModel(diagram, 25, 25).orElseThrow());
        assertTrue(presenter.draggableElementAtModel(diagram, 150, 150).isEmpty());
    }

    private Diagram databaseDiagram() {
        Diagram diagram = new Diagram("db", "DB", DiagramType.DATABASE);
        diagram.getTables().add(new DbTable("users", "Users"));
        diagram.getTables().add(new DbTable("orders", "Orders"));
        diagram.getCanvasState().getBoundsByElementId().put("users", new Rect(0, 0, 100, 100));
        diagram.getCanvasState().getBoundsByElementId().put("orders", new Rect(200, 0, 100, 100));
        diagram.getForeignKeys().add(new ForeignKey("fk.orders.users", "orders", "orders.user_id", "users", "users.id"));
        return diagram;
    }

    private Diagram sequenceDiagram() {
        Diagram diagram = new Diagram("seq", "Sequence", DiagramType.SEQUENCE);
        diagram.getParticipants().add(new SequenceParticipant("web", "Web"));
        diagram.getParticipants().add(new SequenceParticipant("api", "API"));
        diagram.getCanvasState().getBoundsByElementId().put("web", new Rect(0, 0, 100, 220));
        diagram.getCanvasState().getBoundsByElementId().put("api", new Rect(200, 0, 100, 220));
        diagram.getMessages().add(new SequenceMessage("m1", "web", "api", "GET /orders"));
        return diagram;
    }
}
