import java.util.Scanner;

import swiftbot.Button;
import swiftbot.ButtonFunction;
import swiftbot.SwiftBotAPI;
public class CalibrateSwiftBot {
	
	static SwiftBotAPI swiftbot = SwiftBotAPI.INSTANCE;
	final static int time = 2; // time in seconds
	final static int[] speedPercentages = {30, 40, 50, 60, 70, 80, 90, 100};
	static int i = 0;
	
	public static void main(String[] args) {
		//Measure_gradient();
		test_gradient();
	
		
		
	}
	
	public static void Measure_gradient() {
		System.out.println("Calibrate Example");
		System.out.println("\nPlease place your robot at the start point");
		System.out.println("Ready to start moving at " + speedPercentages[i] + "% speed.");
		swiftbot.enableButton(Button.A, () -> {
			int currentSpeed = speedPercentages[i];
			System.out.println("Moving at (" + currentSpeed + ", " +
					currentSpeed + ")...");
			swiftbot.move(currentSpeed, currentSpeed, time*1000);
			
			i++;		
			
			if (i < speedPercentages.length) {
				System.out.println("\nReady to start moving at " +
						speedPercentages[i] + "% speed.");
			} else {
				System.out.println("\nFinished testing!");
				swiftbot.disableButton(Button.X);
				System.exit(0);
			}
		});
		
	}
	
	public static void test_gradient() {
	
		Scanner scan = new Scanner(System.in);
		
		
		while (true) {
			int the_input = scan.nextInt();
			if (the_input == 0) {
				break;
			}
			
			System.out.println("moving at " + the_input + "% percent");
			swiftbot.move(the_input, the_input, time * 1000);
			
		}
		
		scan.close();
		
	}	
		
	
}
