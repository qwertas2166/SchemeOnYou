package see.schemeonyou.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class CanvasState {
    private double viewportX;
    private double viewportY;
    private double zoom = 1.0;
    private final Map<String, Rect> boundsByElementId = new LinkedHashMap<>();

    public double getViewportX() { return viewportX; }
    public void setViewportX(double viewportX) { this.viewportX = viewportX; }
    public double getViewportY() { return viewportY; }
    public void setViewportY(double viewportY) { this.viewportY = viewportY; }
    public double getZoom() { return zoom; }
    public void setZoom(double zoom) { this.zoom = Math.max(0.25, Math.min(zoom, 3.0)); }
    public void pan(double deltaX, double deltaY) {
        viewportX += deltaX;
        viewportY += deltaY;
    }
    public void actualSize() {
        zoom = 1.0;
        viewportX = 0;
        viewportY = 0;
    }
    public Map<String, Rect> getBoundsByElementId() { return boundsByElementId; }
}
