
import java.util.Scanner;

import swiftbot.SwiftBotAPI;

public class SimionSwift {

	static SwiftBotAPI swiftBot = SwiftBotAPI.INSTANCE;
	static int lives = 3;
	static boolean game_status = true;
	static int score = 0;
	static int level = 0;

	//ANSI colour codes
	static final String RESET = "\u001B[0m";
	static final String CYAN = "\u001B[36m";
	static final String YELLOW = "\u001B[33m";
	static final String GREEN = "\u001B[32m";
	static final String WHITE = "\u001B[37m";
	static final String BOLD = "\u001B[1m";
	static final String RED    = "\u001B[31m";

	public static void main(String[] args) throws InterruptedException {

		try {
			Scanner input = new Scanner(System.in);

			System.out.printf("""

					%1$s%2$s***************************************************************%3$s
					%1$s%2$s*                         SIMON SWIFT                         *%3$s
					%1$s%2$s***************************************************************%3$s

					""", CYAN, BOLD, RESET);

			while (lives > 0 && game_status) {
				level ++;

				//				System.out.println("current score > "+ score);
				//				System.out.println("currrent level > "+ level);
				//				System.out.println("cuurent lives > "+ lives);


				System.out.printf(
						"%sGame Stats:%s\n\n" +
								"%s Current score: %d %s\n" +
								"%s Current Level: %d %s\n" +
								"%s Current Lives: %d %s\n" +
								"%s------------------------------------------%s\n" +
								"%s RED : A %s\n" +
								"%s BLUE : B %s\n" +
								"%s GREEN : X %s\n" +
								"%s WHITE : Y %s\n" +
								"%s------------------------------------------%s\n",

								WHITE, RESET,
								CYAN, score, RESET,
								CYAN, level, RESET,
								CYAN, lives, RESET,
								WHITE, RESET,
								RED, RESET,
								CYAN, RESET,
								GREEN, RESET,
								WHITE, RESET,
								WHITE, RESET
						);


				if (score % 5 == 0 && level != 1) {

					//System.out.println("Enter 'quit' if required to quit else 'continue'> ");
					System.out.printf("%s Enter 'quit' if required to quit else 'continue'> %s",
							WHITE,RESET);
					String user_quit_input = input.next();

					if (user_quit_input.equals("quit")) {
						game_status = false;

						//System.out.println("performing celebration dive! ");
						System.out.printf("%s performing celebration dive! %s",
								WHITE,RESET);
						MovementController.celebration_movement(score);
						LEDController.celebration_lights();
						break;
					}

				} 

				Thread.sleep(250);
				System.out.println("1..");
				Thread.sleep(250);
				System.out.println("2..");
				Thread.sleep(250);
				System.out.println("3..");
				Thread.sleep(250);

				System.out.println("starting game!");

				SequenceManager.AddRandomColour(); //add a random colour

				System.out.println("the sequence" + SequenceManager.Get_Sequence()); //prints the colour, test

				LEDController.display_sequence(SequenceManager.Get_Sequence());

				System.out.println("");

				while (InputHandler.get_userinput().size() < level) {
					InputHandler.start_InputHandle();

				}

				if (InputHandler.get_userinput().equals(SequenceManager.Get_Sequence())) {
					score ++;
					System.out.println("Score incremented!!");

				}
				else {
					lives = lives - 1;
					System.out.println("wrong sequnce");
				}

				InputHandler.reset_input_handler();
			}
			input.close();
			System.exit(5);
		}

		catch (Exception e) {
			System.out.println("not working!");
			System.exit(5);
		}


	}

}
