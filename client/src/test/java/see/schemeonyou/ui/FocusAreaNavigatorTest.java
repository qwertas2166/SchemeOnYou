package see.schemeonyou.ui;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FocusAreaNavigatorTest {
    @Test
    void firstFocusableInAreaPicksFirstDescendantBeforeAreaRoot() {
        Pane area = focusablePane();
        Pane first = focusablePane();
        Pane second = focusablePane();
        area.getChildren().addAll(first, second);

        assertSame(first, FocusAreaNavigator.firstFocusableInArea(area).orElseThrow());
    }

    @Test
    void firstFocusableInAreaFallsBackToRootForSingleFocusableArea() {
        Pane canvasLikeArea = focusablePane();

        assertSame(canvasLikeArea, FocusAreaNavigator.firstFocusableInArea(canvasLikeArea).orElseThrow());
    }

    @Test
    void firstFocusableInAreaReturnsEmptyForEmptyNonFocusableArea() {
        assertTrue(FocusAreaNavigator.firstFocusableInArea(new Pane()).isEmpty());
    }

    @Test
    void focusableNodesSkipHiddenDisabledAndNonFocusableNodes() {
        Pane root = new Pane();
        Pane hidden = focusablePane();
        hidden.setVisible(false);
        Pane disabled = focusablePane();
        disabled.setDisable(true);
        Pane nonFocusable = new Pane();
        Pane first = focusablePane();
        Pane nested = new Pane(first);
        Pane second = focusablePane();
        root.getChildren().addAll(hidden, disabled, nonFocusable, nested, second);

        List<Node> nodes = FocusAreaNavigator.focusableNodes(root);

        assertEquals(List.of(first, second), nodes);
    }

    private static Pane focusablePane() {
        Pane pane = new Pane();
        pane.setFocusTraversable(true);
        return pane;
    }
}
