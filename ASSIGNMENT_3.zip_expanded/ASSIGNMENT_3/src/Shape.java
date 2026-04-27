public abstract class Shape {
    private final String type;

    protected Shape(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public abstract void validate();
    public abstract double getArea();
    public abstract String getSizeText();
    public abstract String getAngleText();
}
