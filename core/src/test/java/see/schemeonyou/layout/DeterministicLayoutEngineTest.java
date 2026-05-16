package see.schemeonyou.layout;

import org.junit.jupiter.api.Test;
import see.schemeonyou.model.DbTable;
import see.schemeonyou.model.Diagram;
import see.schemeonyou.model.DiagramType;
import see.schemeonyou.model.Rect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class DeterministicLayoutEngineTest {
    @Test
    void layoutPreservesManualBoundsUntilExplicitReset() {
        Diagram diagram = new Diagram("diagram-1", "Database", DiagramType.DATABASE);
        DbTable users = new DbTable("table-1", "users");
        diagram.getTables().add(users);
        DeterministicLayoutEngine engine = new DeterministicLayoutEngine();
        Rect manual = new Rect(300, 250, 220, 92);

        engine.layout(diagram);
        Rect deterministic = diagram.getCanvasState().getBoundsByElementId().get(users.getId());
        diagram.getCanvasState().getBoundsByElementId().put(users.getId(), manual);

        engine.layout(diagram);
        assertEquals(manual, diagram.getCanvasState().getBoundsByElementId().get(users.getId()));

        engine.resetLayout(diagram);
        assertNotEquals(manual, diagram.getCanvasState().getBoundsByElementId().get(users.getId()));
        assertEquals(deterministic, diagram.getCanvasState().getBoundsByElementId().get(users.getId()));
    }
}
