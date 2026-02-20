//________imports______________
import java.util.ArrayList;
import java.util.HashMap;
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
		
		int head_min = 6;
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
		int tail_max = 20;
		
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

class Users {
	int pos;
	public Users() {

	}

	public int get_pos() {
		return this.pos;
	}
}

public class Snakes_and_ladders {

	static ArrayList<Snakes> snakes_obj;
	static ArrayList<Ladders> ladders_obj;
	static ArrayList<Connectors> connectors_obj;

	static String current_button = "";
	static SwiftBotAPI swiftBot = SwiftBotAPI.INSTANCE;
	static HashMap<String,Boolean> current_screen = new HashMap<>(Map.of(
			"Menu", true,
			"Game", false));
	static String user_name;
	static int [][] board = {	
			{21,22,23,24,25},
			{20,19,18,17,16},
			{11,12,13,14,15},
			{10,9,8,7,6},
			{1,2,3,4,5}}; 

	private static void player_setup() {
		Scanner text = new Scanner(System.in);
		System.out.println("Please enter your name >>  ");
		user_name = text.nextLine();
		System.out.println("");
		System.out.println("The SwiftBot has been assigned the following name:");
		System.out.println("> SwiftBot");
		text.close();
	}

	private static void Board_setup() {
		snakes_obj = new ArrayList<Snakes>();
		ladders_obj = new ArrayList<Ladders>();
		connectors_obj = new ArrayList<Connectors>();


	}

	private static void Mode_selection() {

	}

	private static void Decide_start_player() {

	}



	private static void menu() {
		System.out.println("Press [Y] in the SwiftBot to start the game! ");

		while (current_screen.get("Menu")) {
			input();

			if (current_button.equals("Y")) {
				System.out.println("");
				System.out.println("Welcome to Snakes and Ladders!");
				current_screen.replace("Menu", false);
				current_screen.replace("Game", true);

			}
		}
		player_setup();
		Board_setup();
		Mode_selection();
		Decide_start_player();
		//return back to the main code


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
		while (current_screen.get("Game")) {
			//game
		}
	}
}




