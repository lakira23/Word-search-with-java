public class Square extends Shape {
    private final int side;

    public Square(int side) {
        super("Square");
        this.side = side;
    }

    public int getSide() {
        return side;
    }

    @Override
    public void validate() {
        InputValidator.validateSide(side);
    }

    @Override
    public double getArea() {
        return side * side;
    }

    @Override
    public String getSizeText() {
        return String.valueOf(side);
    }

    @Override
    public String getAngleText() {
        return "90, 90, 90, 90";
    }
}
