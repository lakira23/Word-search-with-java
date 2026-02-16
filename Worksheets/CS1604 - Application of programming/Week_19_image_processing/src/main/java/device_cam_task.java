import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import nu.pattern.OpenCV;

public class device_cam_task 
{
	public static void main(String[] args) 
	{
		OpenCV.loadLocally();
		VideoCapture camera = new VideoCapture(0); //the zero is for the first available cam
		
		
		//Do we have a camera?
		if(!camera.isOpened())
		{
			System.out.println("+++Cannot load camera.");
			return;
		}
 
		//Take a picture!
		Mat frame = new Mat();
		camera.read(frame);
 
		//Save the picture
		Imgcodecs.imwrite("c:\\temp\\image.png", frame);
	}
}
