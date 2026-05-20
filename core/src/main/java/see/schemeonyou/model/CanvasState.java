package see.schemeonyou.model;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class CanvasState {
    @Setter
    private double viewportX;
    @Setter
    private double viewportY;
    private double zoom = 1.0;
    private final Map<String, Rect> boundsByElementId = new LinkedHashMap<>();

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
}
