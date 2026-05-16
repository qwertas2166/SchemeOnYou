package see.schemeonyou.command;

import see.schemeonyou.model.CanvasState;
import see.schemeonyou.model.Rect;

public class MoveCanvasElementCommand implements Command {
    private final CanvasState canvasState;
    private final String elementId;
    private final Rect oldRect;
    private final Rect newRect;

    public MoveCanvasElementCommand(CanvasState canvasState, String elementId, Rect oldRect, Rect newRect) {
        this.canvasState = canvasState;
        this.elementId = elementId;
        this.oldRect = oldRect;
        this.newRect = newRect;
    }

    @Override
    public CommandMetadata metadata() {
        return CommandMetadata.of("canvas.element.move", "Move canvas element", "", "canvas", "move", "drag");
    }

    @Override
    public void execute() {
        canvasState.getBoundsByElementId().put(elementId, newRect);
    }

    @Override
    public void undo() {
        canvasState.getBoundsByElementId().put(elementId, oldRect);
    }
}
