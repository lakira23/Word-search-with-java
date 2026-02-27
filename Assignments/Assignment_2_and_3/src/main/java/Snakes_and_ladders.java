//________imports______________
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import swiftbot.Button;
import swiftbot.SwiftBotAPI;


//________CLASSES_______________
class Connectors {
	protected int head;
	protected int tail;
	static Random random = new Random();

	public Connectors(ArrayList<Connectors> list_connectors) {
		assign_pos(list_connectors);

	}
	private static boolean pos_taken(int value, ArrayList<Connectors> list_connectors) {
		for (Connectors c : list_connectors) {
			if (c.get_head() == value || c.get_tail() == value) {
				return true;
			}
		}
		return false;
	}

	protected boolean check_head_and_tail_pos(int tail) {
		return this.head > tail; //default is snake
	}

	private int generate_head(ArrayList<Connectors> list_connectors) {

		int head_min = 2;
		int head_max = 24;

		while (true) {
			head = random.nextInt(head_max - head_min + 1) + head_min;

			if (!pos_taken(head, list_connectors)) {
				return head;
			}
		}
	}

	private int generate_tail(ArrayList<Connectors> list_connectors) {
		int tail_min = 2;
		int tail_max = 24;

		int headrow = (this.head - 1) / 5;

		while (true) {
			tail = random.nextInt(tail_max - tail_min + 1) + tail_min;
			int tailrow = (this.tail - 1) / 5;

			if (!pos_taken(this.tail, list_connectors) && (tailrow != headrow) && (check_head_and_tail_pos(tail))){
				return tail;
			}
		}
	}

	private void assign_pos(ArrayList<Connectors> list_connectors) {
		this.head = generate_head(list_connectors);
		this.tail = generate_tail(list_connectors);
	}

	public int get_head() {
		return this.head;
	}

	public int get_tail() {
		return this.tail;
	}
}

class Snakes extends Connectors {

	public Snakes(ArrayList<Connectors> list_connectors) {
		super(list_connectors);
	}

	@Override
	protected boolean check_head_and_tail_pos(int tail) {
		return this.head > tail; 
	}
}

class Ladders extends Connectors {
	public Ladders(ArrayList<Connectors> list_connectors) {
		super(list_connectors);
	}

	@Override
	protected boolean check_head_and_tail_pos(int tail) {
		return tail > this.head ; 
	}
}

class Players {
	static Random random = new Random();
	protected int pos;
	private String name;

	public Players(String player_name) {
		pos = 1;
		this.name = player_name;
	}

	public String get_name() {
		return this.name;
	}

	public int get_pos() {
		return this.pos;
	}

	public int get_dice_num() {
		int dice_min = 1;
		int dice_max = 6;

		return random.nextInt(dice_max - dice_min + 1) + dice_min;
	}
}

class Users extends Players{
	public Users(String player_name){
		super(player_name);
	}

}

class SwiftBot_class extends Players{

	public SwiftBot_class(String player_name) {
		super(player_name);
	}

}

public class Snakes_and_ladders {

	static ArrayList<Snakes> snakes_obj;
	static ArrayList<Ladders> ladders_obj;
	static ArrayList<Connectors> connectors_obj;
	static ArrayList<Players> players_obj = new ArrayList<Players>();
	static ArrayList<Users> users_obj = new ArrayList<Users>();
	static SwiftBot_class swiftbot_obj;

	static String pressed_button;

	static int num_players = 2;

	static String DICE_ASCII_ART = "  "
			+ "____\r\n"
			+ " /\\' .\\    _____\r\n"
			+ "/: \\___\\  / .  /\\\r\n"
			+ "\\' / . / /____/..\\\r\n"
			+ " \\/___/  \\'  '\\  /\r\n"
			+ "          \\'__'\\/\r\n";

	static SwiftBotAPI swiftBot = SwiftBotAPI.INSTANCE;
	static HashMap<String,Boolean> current_screen = new HashMap<>(Map.of(
			"Menu", true,
			"Game", false));
	static int [][] board = {	
			{21,22,23,24,25},
			{20,19,18,17,16},
			{11,12,13,14,15},
			{10,9,8,7,6},
			{1,2,3,4,5}}; 

	private static void player_setup() {
		Scanner text = new Scanner(System.in);

		//makes the users
		for (int i = 0; i < num_players - 1; i++) { //-1 cos to not include the swiftbot
			System.out.println("Please enter your name player "+ (i+1) +" >> ");
			Users a_user = new Users(text.nextLine());

			users_obj.add(a_user);
			players_obj.add(a_user);

			System.out.println(" ");	
		}
		System.out.println("The SwiftBot has been assigned the following name:");

		//makes the swiftbot
		swiftbot_obj = new SwiftBot_class("The SwiftBot");
		players_obj.add(swiftbot_obj);
		System.out.println("> " + swiftbot_obj.get_name());

	}

	private static void Board_setup() {
		int num_of_snakes = 2;
		int num_of_ladders = 2;

		snakes_obj = new ArrayList<Snakes>();
		ladders_obj = new ArrayList<Ladders>();
		connectors_obj = new ArrayList<Connectors>();

		//makes the snakes
		System.out.println("Snakes: ");
		for (int i = 0; i < num_of_snakes; i++) {
			Snakes a_snake = new Snakes(connectors_obj);
			snakes_obj.add(a_snake);
			connectors_obj.add(a_snake);
			System.out.println("> Snake "+(i + 1)+" - Head Square " + a_snake.get_head() + "--> Tail Square " + a_snake.get_tail());
		}
		System.out.println("");

		//makes the ladders
		System.out.println("Ladders: ");

		for (int i = 0; i < num_of_ladders; i++) {
			Ladders a_ladder = new Ladders(connectors_obj);
			ladders_obj.add(a_ladder);
			connectors_obj.add(a_ladder);
			System.out.println("> Ladder "+ (i + 1)+ " - Head Square " + a_ladder.get_head() + "--> Tail Square " + a_ladder.get_tail());
		}	

	}

	private static void Mode_selection() {
		System.out.println("Select a game mode by pressing the buttons: ");
		System.out.println("-----------------------------------------------------------------");
		System.out.println("[A] Mode A - Normal Gameplay");
		System.out.println("[B] Mode B - User power to manually override SwiftBot movements");
		System.out.println("");
		System.out.println("Please press the buttons in the swiftBot > ");

		String choice = input_handler(List.of("A","B"));
		if (choice.equals("A")) {
			System.out.println("Normal mode selected.");
		}
		else if (choice.equals("B")) {
			System.out.println("Override mode selected.");
		}



	}

	private static void Decide_start_player() {
		ArrayList<Integer> roll_results = new ArrayList<Integer>();

		System.out.println("");
		System.out.println("Deciding starting player :-");

		for (int i = 0; i < num_players - 1; i++) {
			System.out.println(users_obj.get(i).get_name()+" press [A] in the Swiftbot to perform dice roll >");

			String choice = input_handler(List.of("A"));
			if (choice.equals("Y")) {
				System.out.println(DICE_ASCII_ART);
				System.out.println("DICE ROLL...");

				int each_diceroll = users_obj.get(i).get_dice_num();
				System.out.println(users_obj.get(i).get_name()+" rolled a "+ each_diceroll);
				roll_results.add(each_diceroll);
				System.out.println(" ");
			}
		}

		//swiftbot roll

		System.out.println(DICE_ASCII_ART);


	}



	private static void menu() {
		System.out.println("Press [Y] in the SwiftBot to start the game! ");

		while (current_screen.get("Menu")) {

			String choice = input_handler(List.of("Y"));
			if (choice.equals("Y")) {
				System.out.println("");
				System.out.println("Welcome to Snakes and Ladders!");
				current_screen.replace("Menu", false);
				current_screen.replace("Game", true);
			}
			else {
				System.out.println("Error! invalid option selected");
			}
		}
		player_setup();
		Board_setup();
		Mode_selection();
		Decide_start_player();
		//return back to the main code


	}

	private static String input_handler(List<String> possible_inputs) {

		List<String> all_inputs = List.of("A","B","X","Y");

		while (true) {
			String pressed_button = input();

			if (possible_inputs.contains(pressed_button)){ //checks if its one of the required inputs
				return pressed_button;
			}

			else if (all_inputs.contains(pressed_button)) { //checks if its one of the possible inputs
				System.out.println("[Error!] invalid option selected, please select "+possible_inputs+ "in the SwiftBot.");
			}
		}
	}

	public static String input() {

		pressed_button = "";
		try {
			swiftBot.disableButton(Button.A);
			swiftBot.disableButton(Button.B);
			swiftBot.disableButton(Button.X);
			swiftBot.disableButton(Button.Y);

			swiftBot.enableButton(Button.A, () -> {System.out.println(" ");System.out.println("A been pressed"); pressed_button = "A";});
			swiftBot.enableButton(Button.B, () -> {System.out.println(" ");System.out.println("B been pressed"); pressed_button = "B";});
			swiftBot.enableButton(Button.X, () -> {System.out.println(" ");System.out.println("X been pressed"); pressed_button = "X";});
			swiftBot.enableButton(Button.Y, () -> {System.out.println(" ");System.out.println("Y been pressed"); pressed_button = "Y";});

			//not enough time so sometimes the previous input in given out
			while (pressed_button.equals("")) {
				Thread.sleep(100);

			}
			return pressed_button;
		}

		catch (Exception e) {
			System.out.println("Error has occured!!");
			return "null";
		}
	}

	public static void main(String[] args) {

		menu();
		while (current_screen.get("Game")) {
			//game
		}
	}
}




