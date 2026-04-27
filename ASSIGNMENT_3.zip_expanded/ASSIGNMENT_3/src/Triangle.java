public class Triangle extends Shape {
    private final int a;
    private final int b;
    private final int c;

    private double angleA;
    private double angleB;
    private double angleC;

    public Triangle(int a, int b, int c) {
        super("Triangle");
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }

    public int getC() {
        return c;
    }

    @Override
    public void validate() {
        InputValidator.validateSide(a);
        InputValidator.validateSide(b);
        InputValidator.validateSide(c);

        if (a + b <= c || a + c <= b || b + c <= a) {
            throw new IllegalArgumentException(
                "Invalid triangle: sides " + a + ", " + b + ", " + c + " cannot form a triangle."
            );
        }

        calculateAngles();
    }

    private void calculateAngles() {
        angleA = Math.toDegrees(Math.acos((b * b + c * c - a * a) / (2.0 * b * c)));
        angleB = Math.toDegrees(Math.acos((a * a + c * c - b * b) / (2.0 * a * c)));
        angleC = 180.0 - angleA - angleB;
    }

    public double getAngleA() {
        return round2(angleA);
    }

    public double getAngleB() {
        return round2(angleB);
    }

    public double getAngleC() {
        return round2(angleC);
    }

    @Override
    public double getArea() {
        double s = (a + b + c) / 2.0;
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }

    @Override
    public String getSizeText() {
        return a + ", " + b + ", " + c;
    }

    @Override
    public String getAngleText() {
        return getAngleA() + ", " + getAngleB() + ", " + getAngleC();
    }

    private double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
