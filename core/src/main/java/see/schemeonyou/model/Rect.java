package see.schemeonyou.model;

public record Rect(double x, double y, double width, double height) {
    public Point center() {
        return new Point(x + width / 2.0, y + height / 2.0);
    }
}
