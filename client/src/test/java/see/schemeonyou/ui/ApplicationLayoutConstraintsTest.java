package see.schemeonyou.ui;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApplicationLayoutConstraintsTest {
    @Test
    void leftAndInspectorUseBoundedAdaptiveWidths() {
        Pane left = new Pane();
        Pane inspector = new Pane();

        ApplicationLayoutConstraints.configureLeftPanel(left);
        ApplicationLayoutConstraints.configureInspector(inspector);

        assertEquals(ApplicationLayoutConstraints.LEFT_PANEL_MIN_WIDTH, left.getMinWidth());
        assertEquals(ApplicationLayoutConstraints.LEFT_PANEL_PREF_WIDTH, left.getPrefWidth());
        assertEquals(ApplicationLayoutConstraints.LEFT_PANEL_MAX_WIDTH, left.getMaxWidth());
        assertEquals(ApplicationLayoutConstraints.INSPECTOR_MIN_WIDTH, inspector.getMinWidth());
        assertEquals(ApplicationLayoutConstraints.INSPECTOR_PREF_WIDTH, inspector.getPrefWidth());
        assertEquals(ApplicationLayoutConstraints.INSPECTOR_MAX_WIDTH, inspector.getMaxWidth());
    }

    @Test
    void scrollableChildrenGrowVerticallyInsteadOfOverflowingSidePanels() {
        Pane child = new Pane();

        ApplicationLayoutConstraints.fillVertically(child);

        assertEquals(Double.MAX_VALUE, child.getMaxHeight());
        assertEquals(Priority.ALWAYS, VBox.getVgrow(child));
    }

    @Test
    void canvasKeepsMinimumSmokeSizeAndConsumesRemainingSpace() {
        Pane canvasArea = new Pane();

        ApplicationLayoutConstraints.configureCanvasArea(canvasArea);

        assertEquals(ApplicationLayoutConstraints.CANVAS_MIN_WIDTH, canvasArea.getMinWidth());
        assertEquals(ApplicationLayoutConstraints.CANVAS_MIN_HEIGHT, canvasArea.getMinHeight());
        assertEquals(Double.MAX_VALUE, canvasArea.getMaxWidth());
        assertEquals(Double.MAX_VALUE, canvasArea.getMaxHeight());
    }
}
