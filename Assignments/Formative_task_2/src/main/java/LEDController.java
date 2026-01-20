import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import swiftbot.SwiftBotAPI;

public class LEDController {
	static SwiftBotAPI swiftBot = SwiftBotAPI.INSTANCE;

	static int[][] colours_int = {
			{ 255, 0, 0 }, // Red
			{ 0, 255, 0 }, // Green
			{ 0, 0, 255 }, // Blue
			{ 255, 255, 255 } // White
	};

	
	static HashMap<String, int[]> colour_map = new HashMap<>(Map.of(
		    "Red",   new int[]{255, 0, 0},
		    "Green", new int[]{0, 255, 0},
		    "Blue",  new int[]{0, 0, 255},
		    "White", new int[]{255, 255, 255}
		));

	public static void display_sequence(ArrayList<String> colour_Sequence) throws InterruptedException {

		for (int i= 0; i < colour_Sequence.size(); i++) {
			
			int[] current_colour = colour_map.get(colour_Sequence.get(i));
			
			swiftBot.fillUnderlights(current_colour);
			
			Thread.sleep(1000);
		}
		swiftBot.disableUnderlights();
		System.out.println("all lights are shown!");//test
	};

	public static void celebration_lights() throws InterruptedException { 
		for (int i = 0; i < 4; i++) {
			Random rand = new Random();
			int randomInt = rand.nextInt(4);
			swiftBot.fillUnderlights(colours_int[randomInt]);
			Thread.sleep(300);
		}
		swiftBot.disableUnderlights();


	}

}
