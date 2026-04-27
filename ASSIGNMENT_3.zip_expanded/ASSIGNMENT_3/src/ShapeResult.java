public class ShapeResult {
    private final String type;
    private final String sizeText;
    private final String angleText;
    private final long timeMs;
    private final double area;

    public ShapeResult(String type, String sizeText, String angleText, long timeMs, double area) {
        this.type = type;
        this.sizeText = sizeText;
        this.angleText = angleText;
        this.timeMs = timeMs;
        this.area = area;
    }

    public String getType() {
        return type;
    }

    public String getSizeText() {
        return sizeText;
    }

    public String getAngleText() {
        return angleText;
    }

    public long getTimeMs() {
        return timeMs;
    }

    public double getArea() {
        return area;
    }
}
