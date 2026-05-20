package see.schemeonyou.ui;

import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Centralizes shell sizing so side functional areas stay pinned to the window
 * edges while the canvas consumes the remaining adaptive space.
 */
public final class ApplicationLayoutConstraints {
    public static final double LEFT_PANEL_MIN_WIDTH = 220.0;
    public static final double LEFT_PANEL_PREF_WIDTH = 260.0;
    public static final double LEFT_PANEL_MAX_WIDTH = 360.0;
    public static final double INSPECTOR_MIN_WIDTH = 260.0;
    public static final double INSPECTOR_PREF_WIDTH = 320.0;
    public static final double INSPECTOR_MAX_WIDTH = 420.0;
    public static final double CANVAS_MIN_WIDTH = 360.0;
    public static final double CANVAS_MIN_HEIGHT = 320.0;
    public static final double SIDE_PANEL_VIEW_ORDER = 0.0;
    public static final double CANVAS_VIEW_ORDER = 10.0;
    public static final double CANVAS_CONTENT_PADDING = 0.0;

    private ApplicationLayoutConstraints() {
    }

    public static void configureLeftPanel(Region region) {
        configureSidePanel(region, LEFT_PANEL_MIN_WIDTH, LEFT_PANEL_PREF_WIDTH, LEFT_PANEL_MAX_WIDTH);
    }

    public static void configureInspector(Region region) {
        configureSidePanel(region, INSPECTOR_MIN_WIDTH, INSPECTOR_PREF_WIDTH, INSPECTOR_MAX_WIDTH);
    }

    public static void configureCanvasArea(Region region) {
        region.setMinSize(CANVAS_MIN_WIDTH, CANVAS_MIN_HEIGHT);
        region.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        region.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    }

    public static void bindCanvasToArea(Canvas canvas, Region area) {
        canvas.widthProperty().bind(area.widthProperty());
        canvas.heightProperty().bind(area.heightProperty());
    }

    public static void configureFunctionalAreaLayering(Node leftPanel, Node canvasArea, Node inspector) {
        leftPanel.setViewOrder(SIDE_PANEL_VIEW_ORDER);
        inspector.setViewOrder(SIDE_PANEL_VIEW_ORDER);
        canvasArea.setViewOrder(CANVAS_VIEW_ORDER);
    }

    public static void fillVertically(Region region) {
        region.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(region, Priority.ALWAYS);
    }

    private static void configureSidePanel(Region region, double minWidth, double prefWidth, double maxWidth) {
        region.setMinWidth(minWidth);
        region.setPrefWidth(prefWidth);
        region.setMaxWidth(maxWidth);
        region.setMaxHeight(Double.MAX_VALUE);
    }
}
