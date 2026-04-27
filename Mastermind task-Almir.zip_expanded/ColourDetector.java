import java.awt.image.BufferedImage;
import java.awt.Color;

public class ColourDetector {

    // takes an image and determines the main colour between red, green, blue, yellow, orange, pink
    public char detectColour(BufferedImage image) {

        int width = image.getWidth();
        int height = image.getHeight();

        long totalR = 0, totalG = 0, totalB = 0;
        int count = 0;

        // sample pixels across the image (skips some for speed)
        for (int x = 0; x < width; x += 10) {
            for (int y = 0; y < height; y += 10) {

                Color c = new Color(image.getRGB(x, y));

                totalR += c.getRed();
                totalG += c.getGreen();
                totalB += c.getBlue();

                count++;
            }
        }

        //calculate average RGB values
        int avgR = (int)(totalR / count);
        int avgG = (int)(totalG / count);
        int avgB = (int)(totalB / count);

        // print values for debugging / testing
        System.out.println("RGB: " + avgR + ", " + avgG + ", " + avgB);

        return mapToColour(avgR, avgG, avgB);
    }

    //maps RGB values to one of the game colours
    private char mapToColour(int r, int g, int b) {

        // ignore very dark values (likely no card detected)
        if (r + g + b < 100) return '?';

        // red and orange detection
        if (r > 180 && b < 100) {

            double ratio = (double) g / r;

            // if green is close to red it returns orange
            if (ratio > 0.65) return 'O';

            return 'R';
        }

        // green and yellow detection
        if (g > b) {

            // if red and green are close, treat as yellow
            if (Math.abs(r - g) < 50 && r > 150 && g > 150) return 'Y';

            return 'G';
        }

        // pink detection (red + blue)
        if (r > 180 && b > 120) return 'P';

        // blue detection
        if (b > r && b > g) return 'B';

        return '?'; // fall back if unclear
    }
}