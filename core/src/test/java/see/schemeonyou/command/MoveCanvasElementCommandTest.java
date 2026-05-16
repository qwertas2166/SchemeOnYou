package see.schemeonyou.command;

import org.junit.jupiter.api.Test;
import see.schemeonyou.model.CanvasState;
import see.schemeonyou.model.Rect;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoveCanvasElementCommandTest {
    @Test
    void moveIsOneUndoableCanvasMutation() {
        CanvasState canvas = new CanvasState();
        Rect oldRect = new Rect(10, 20, 220, 120);
        Rect newRect = new Rect(80, 90, 220, 120);
        canvas.getBoundsByElementId().put("table-1", oldRect);
        UndoRedoStack stack = new UndoRedoStack();

        stack.run(new MoveCanvasElementCommand(canvas, "table-1", oldRect, newRect));

        assertEquals(newRect, canvas.getBoundsByElementId().get("table-1"));
        stack.undo();
        assertEquals(oldRect, canvas.getBoundsByElementId().get("table-1"));
        stack.redo();
        assertEquals(newRect, canvas.getBoundsByElementId().get("table-1"));
    }
}
