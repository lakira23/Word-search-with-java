public class InputValidator {
    private InputValidator() {
    }

    public static void validateSide(int side) {
        if (side < 15 || side > 85) {
            throw new IllegalArgumentException(
                "Side length " + side + " is invalid. It must be between 15 and 85 cm."
            );
        }
    }
}
