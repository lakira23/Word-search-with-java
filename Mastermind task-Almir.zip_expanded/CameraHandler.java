import swiftbot.*;
import java.awt.image.BufferedImage;

public class CameraHandler {

    // reference to the SwiftBot API
    private SwiftBotAPI swiftBot;

    // constructor to pass in the robot instance
    public CameraHandler(SwiftBotAPI swiftBot) {
        this.swiftBot = swiftBot;
    }

    // captures an image using the robot camera
    public BufferedImage captureImage() {
        try {
            System.out.println("Capturing image...");

            // take a square image (smaller size for speed)
            BufferedImage img = swiftBot.takeStill(ImageSize.SQUARE_240x240);

            // basic check to make sure an image was captured
            if (img == null) {
                System.out.println("Image is NULL!");
            } else {
                System.out.println("Image captured successfully");
            }

            return img;

        } catch (Exception e) {
            // handles camera errors (e.g. camera disabled or not working, as mine was for a short while)
            System.out.println("Camera error:");
            e.printStackTrace();
            return null;
        }
    }
}