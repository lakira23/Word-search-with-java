import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import swiftbot.Button;
import swiftbot.ButtonFunction;
import swiftbot.SwiftBotAPI;

public class DrawShapeApp {

    public static void main(String[] args) {
        SwiftBotAPI bot = SwiftBotAPI.INSTANCE;

        QRParser parser = new QRParser();
        MovementConfig config = new MovementConfig(10.0, 850, 30, 25);
        ShapeDrawer drawer = new ShapeDrawer(bot, config);
        LogService logService = new LogService();

        List<ShapeResult> history = new ArrayList<>();

        printBanner();

        // X button exits program
        bot.enableButton(Button.X, new ButtonFunction() {
            @Override
            public void run() {
                System.out.println("X button pressed. Exiting...");
                ExitHandler.requestExit();
            }
        });

        try {
            while (!ExitHandler.isExitRequested()) {
                System.out.println("\nShow a QR code to the camera...");
                System.out.println("Examples: S:16  |  T:16:30:24  |  S:16&T:16:30:24");

                BufferedImage qrImage = bot.getQRImage();

                if (qrImage == null) {
                    System.out.println("Could not capture QR image. Try again.");
                    Thread.sleep(1000);
                    continue;
                }

                String qrText = bot.decodeQRImage(qrImage);

                if (qrText == null || qrText.trim().isEmpty()) {
                    System.out.println("No valid QR code detected. Try again.");
                    Thread.sleep(1000);
                    continue;
                }

                System.out.println("QR detected: " + qrText);

                try {
                    List<Shape> shapes = parser.parseShapes(qrText);

                    for (int i = 0; i < shapes.size(); i++) {
                        if (ExitHandler.isExitRequested()) {
                            break;
                        }

                        Shape shape = shapes.get(i);

                        System.out.println("Shape: " + shape.getType());
                        System.out.println("Size: " + shape.getSizeText());
                        System.out.println("Angles: " + shape.getAngleText());

                        ShapeResult result = drawer.drawShape(shape);
                        history.add(result);

                        if (i < shapes.size() - 1) {
                            System.out.println("Moving back 15 cm before next shape...");
                            drawer.moveBack15Cm();
                        }

                        System.out.println("----------------------");
                    }

                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid QR input: " + e.getMessage());
                }

                Thread.sleep(1000);
            }

            bot.disableAllButtons();

            System.out.println("\nWriting log file...");
            System.out.println("Saved to: " + logService.writeLog(history).getAbsolutePath());

        } catch (Exception e) {
            System.out.println("Fatal error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void printBanner() {
        System.out.println("======================================");
        System.out.println("         SWIFTBOT DRAW SHAPE");
        System.out.println("======================================");
        System.out.println("Press X on the SwiftBot to quit.");
    }
}
