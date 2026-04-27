import java.util.ArrayList;
import java.util.List;

public class QRParser {

    public List<Shape> parseShapes(String qrText) {
        if (qrText == null || qrText.trim().isEmpty()) {
            throw new IllegalArgumentException("QR input cannot be empty.");
        }

        String[] commands = qrText.trim().split("&");

        if (commands.length > 5) {
            throw new IllegalArgumentException("Only up to 5 shapes are allowed in one QR code.");
        }

        List<Shape> shapes = new ArrayList<>();

        for (String command : commands) {
            String[] parts = command.trim().split(":");
            String code = parts[0].trim().toUpperCase();

            try {
                switch (code) {
                    case "S":
                        if (parts.length != 2) {
                            throw new IllegalArgumentException("Square format must be S:length");
                        }
                        int side = Integer.parseInt(parts[1].trim());
                        Square square = new Square(side);
                        square.validate();
                        shapes.add(square);
                        break;

                    case "T":
                        if (parts.length != 4) {
                            throw new IllegalArgumentException("Triangle format must be T:a:b:c");
                        }
                        int a = Integer.parseInt(parts[1].trim());
                        int b = Integer.parseInt(parts[2].trim());
                        int c = Integer.parseInt(parts[3].trim());
                        Triangle triangle = new Triangle(a, b, c);
                        triangle.validate();
                        shapes.add(triangle);
                        break;

                    default:
                        throw new IllegalArgumentException("Unknown shape type: " + code);
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("All side lengths must be whole numbers.");
            }
        }

        return shapes;
    }
}
