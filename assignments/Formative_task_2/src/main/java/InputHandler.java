

import java.util.ArrayList;
import swiftbot.Button;
import swiftbot.SwiftBotAPI;

public class InputHandler {
	static SwiftBotAPI swiftBot = SwiftBotAPI.INSTANCE;
	static ArrayList<String> userinput = new ArrayList<String>();

	public static void start_InputHandle() {

		try {
			swiftBot.disableButton(Button.A);
			swiftBot.disableButton(Button.B);
			swiftBot.disableButton(Button.X);
			swiftBot.disableButton(Button.Y);

			swiftBot.enableButton(Button.A, () -> {userinput.add("Red"); System.out.println("A been pressed");});
			swiftBot.enableButton(Button.B, () -> {userinput.add("Blue"); System.out.println("B been pressed");});
			swiftBot.enableButton(Button.X, () -> {userinput.add("Green"); System.out.println("X been pressed");});
			swiftBot.enableButton(Button.Y, () -> {userinput.add("White"); System.out.println("Y been pressed");});


		}

		catch (Exception e) {
			System.out.println("please enter a correct key!!");
			
		}
		
	}
	
	public static void reset_input_handler() {
		userinput.clear();
	}
	
	public static ArrayList<String> get_userinput() {
		return userinput;
	}

	}
	

