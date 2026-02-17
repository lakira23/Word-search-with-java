//________imports______________
import java.util.ArrayList;

import swiftbot.Button;
import swiftbot.SwiftBotAPI;


//________CLASSES_______________
class Connectors {
	int head;
	int tail;

	public Connectors() {

	}

	private int assign_pos(ArrayList<Object> list_connectors) {
		return 0;
	}

	public int get_head() {
		return this.head;
	}

	public int get_tail() {
		return this.tail;
	}
}

class Snakes extends Connectors {

	public Snakes() {

	}
}

class Ladders extends Connectors {
	public Ladders() {

	}
}

class Users {
	int pos;
	public Users() {

	}

	public int get_pos() {
		return this.pos;
	}
}

public class Snakes_and_ladders {
	static boolean game_status = true;
	static String current_button = "";
	static SwiftBotAPI swiftBot = SwiftBotAPI.INSTANCE;

	private static void player_setup() {	
	}
	
	private static void Board_setup() {
		
	}
	
	private static void Mode_selection() {
		
	}
	
	private static void Decide_start_player() {
		
	}
	
	
	
	private static void menu() {
		System.out.println("Press [Y] in the SwiftBot to start the game! ");

		while (true) {
			input();

			if (current_button == "Y") {
				System.out.println("");
				System.out.println("Welcome to Snakes and Ladders!");
				
				player_setup();
				Board_setup();
				Mode_selection();
				Decide_start_player();
	
				
				break;
			}
		}	
	}

	public static void input() {
		try {
			swiftBot.disableButton(Button.A);
			swiftBot.disableButton(Button.B);
			swiftBot.disableButton(Button.X);
			swiftBot.disableButton(Button.Y);

			swiftBot.enableButton(Button.A, () -> {System.out.println(" ");System.out.println("A been pressed"); current_button = "A";});
			swiftBot.enableButton(Button.B, () -> {System.out.println(" ");System.out.println("B been pressed"); current_button = "B";});
			swiftBot.enableButton(Button.X, () -> {System.out.println(" ");System.out.println("X been pressed"); current_button = "X";});
			swiftBot.enableButton(Button.Y, () -> {System.out.println(" ");System.out.println("Y been pressed"); current_button = "Y";});


		}

		catch (Exception e) {
			System.out.println("please enter a correct key!!");

		}
	}
	public static void main(String[] args) {

		menu();
		while (game_status) {
			//game
		}
	}
}




