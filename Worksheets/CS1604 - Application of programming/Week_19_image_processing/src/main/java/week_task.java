
import swiftbot.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class week_task {
	public static void main(String args[]) throws Exception
    {
            SwiftBotAPI sb = swiftbot.SwiftBotAPI.INSTANCE;
            BufferedImage img = sb.takeStill(ImageSize.SQUARE_48x48);
            ImageIO.write(img, "jpg", new File("/data/home/pi/TestImage1.jpg"));

            System.exit(1);
    }

}
