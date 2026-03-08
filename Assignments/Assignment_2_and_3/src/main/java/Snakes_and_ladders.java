//________imports______________
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


import swiftbot.Button;
import swiftbot.SwiftBotAPI;


//________CLASSES_______________
class Connectors {
	protected int head;
	protected int tail;

	static Random random = new Random();

	protected int[] head_boundaries; //min, max
	protected int[] tail_boundaries; //min, max

	public Connectors(ArrayList<Connectors> list_connectors, int[] head_bounds, int[] tail_bounds) {
		this.head_boundaries = head_bounds;
		this.tail_boundaries = tail_bounds;

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

		while (true) {
			head = random.nextInt(head_boundaries[1] - head_boundaries[0] + 1) + head_boundaries[0];

			if (!pos_taken(head, list_connectors)) {
				return head;
			}
		}
	}

	private int generate_tail(ArrayList<Connectors> list_connectors) {

		int headrow = (this.head - 1) / 5;

		while (true) {
			tail = random.nextInt(tail_boundaries[1] - tail_boundaries[0] + 1) + tail_boundaries[0];
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

	public void player_connector_interaction(Players player,String connector_type) {
		if (player.get_pos() == this.head) {
			System.out.println(player.get_name() + " has facen a "+connector_type+" in position "+ this.head );
			System.out.println(player.get_name() + " will move to " + this.tail);
			player.set_pos(this.tail);
		}
	}
}
class Snakes extends Connectors {

	public Snakes(ArrayList<Connectors> list_connectors) {
		super(list_connectors, new int[] {6,24},new int[] {2,20} );
	}

	@Override
	protected boolean check_head_and_tail_pos(int tail) {
		return this.head > tail; 
	}

	public void player_connector_interaction(Players player) {
		super.player_connector_interaction(player,"snake");
	}
}

class Ladders extends Connectors {
	public Ladders(ArrayList<Connectors> list_connectors) {
		super(list_connectors, new int[] {2,20},new int[] {6,24});
	}

	@Override
	protected boolean check_head_and_tail_pos(int tail) {
		return tail > this.head ; 
	}

	public void player_connector_interaction(Players player) {
		super.player_connector_interaction(player,"ladder");
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

	public void set_pos(int new_pos) {
		this.pos = new_pos;
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

class Colours {

	public static final String RESET = "\u001B[0m";
	public static final String RED = "\u001B[31m";
	public static final String GREEN = "\u001B[32m";
	public static final String YELLOW = "\u001B[33m";
	public static final String BLUE = "\u001B[34m";
	public static final String CYAN = "\u001B[36m";
	public static final String BOLD = "\u001B[1m";

}

public class Snakes_and_ladders {

	//___object holdings
	static ArrayList<Snakes> snakes_obj;
	static ArrayList<Ladders> ladders_obj;
	static ArrayList<Connectors> connectors_obj;
	static ArrayList<Players> players_obj = new ArrayList<Players>();
	static ArrayList<Users> users_obj = new ArrayList<Users>();
	static SwiftBot_class swiftbot_obj;

	static String current_mode;
	static int num_players = 2;
	static Players current_player;

	static String pressed_button;

	static int swiftbot_physical_pos = 1;
	static int pos_changer;
	static int WHEEL_POWER = 50; //limited power to reduce slipping
	static int TURN_90_TIME = 510;//miliseconds in plywood
	static double POWER_SPEED =  0.4212 * WHEEL_POWER; //motor power cofient for linear callibration.
	static int FORWARD_TIME = 850; //miliseoncds for 25 cm of travel
	static int WHEEL_CALLIBRATION_SYMETRY = -10;
	static String SwiftBot_orientation = "East";

	static boolean game_over = false;

	static String DICE_ASCII_ART = "  "
			+ "____\r\n"
			+ " /\\' .\\    _____\r\n"
			+ "/: \\___\\  / .  /\\\r\n"
			+ "\\' / . / /____/..\\\r\n"
			+ " \\/___/  \\'  '\\  /\r\n"
			+ "          \\'__'\\/\r\n";

	static SwiftBotAPI swiftBot = SwiftBotAPI.INSTANCE;

	static int [][] board = {	
			{21,22,23,24,25},
			{20,19,18,17,16},
			{11,12,13,14,15},
			{10,9,8,7,6},
			{1,2,3,4,5}}; 

	static int[] BLUE = {0,0,255};
	static int[] RED = {0,255,0};
	static int[] ORANGE = {255,165,0};

	//___________menu

	private static void menu() throws InterruptedException {
		System.out.println(
				Colours.CYAN + Colours.BOLD +
				"====================================\n" +
				"     SWIFTBOT SNAKES AND LADDERS    \n" +
				"===================================="
				+ Colours.RESET);

		System.out.println("Press [Y] in the SwiftBot to start the game! ");

		String choice = input_handler(List.of("Y"));
		if (choice.equals("Y")) {
			System.out.println("");
			System.out.println(Colours.BOLD + Colours.CYAN +"Welcome to Snakes and Ladders!" + Colours.RESET);
		}
		else {
			error("invalid option selected, please select [Y]");
		}

		player_setup();
		Board_setup();
		Mode_selection();
		Decide_start_player();



	}

	private static void player_setup() throws InterruptedException {
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
		Thread.sleep(1000);

	}

	private static void Board_setup() {
		int num_of_snakes = 2;
		int num_of_ladders = 2;

		snakes_obj = new ArrayList<Snakes>();
		ladders_obj = new ArrayList<Ladders>();
		connectors_obj = new ArrayList<Connectors>();

		//makes the snakes
		try { 
			System.out.println("");
			System.out.println("Snakes: ");
			for (int i = 0; i < num_of_snakes; i++) {
				Snakes a_snake = new Snakes(connectors_obj);
				snakes_obj.add(a_snake);
				connectors_obj.add(a_snake);
				System.out.println("> Snake "+(i + 1)+" - Head Square " + a_snake.get_head() + "--> Tail Square " + a_snake.get_tail());
			}
			System.out.println("");
			Thread.sleep(1000);

			//makes the ladders
			System.out.println("Ladders: ");

			for (int i = 0; i < num_of_ladders; i++) {
				Ladders a_ladder = new Ladders(connectors_obj);
				ladders_obj.add(a_ladder);
				connectors_obj.add(a_ladder);
				System.out.println("> Ladder "+ (i + 1)+ " - Head Square " + a_ladder.get_head() + "--> Tail Square " + a_ladder.get_tail());
			}	
			System.out.println("");
			Thread.sleep(1000);
		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void Mode_selection() throws InterruptedException {
		System.out.println("Select a game mode by pressing the buttons: ");
		System.out.println("-----------------------------------------------------------------");
		System.out.println("[A] Mode A - Normal Gameplay");
		System.out.println("[B] Mode B - User power to manually override SwiftBot movements");
		System.out.println("");
		System.out.println("Please press the buttons in the swiftBot > ");

		String choice = input_handler(List.of("A","B"));
		if (choice.equals("A")) {
			current_mode = "A";
			System.out.println("Mode A selected");
		}
		else if (choice.equals("B")) {
			current_mode = "B";
			System.out.println("Mode B selcted");
		}



	}

	private static void Decide_start_player() throws InterruptedException {
		ArrayList<Integer> user_roll_results = new ArrayList<Integer>();

		System.out.println("");
		System.out.println("Deciding starting player! :-");
		Thread.sleep(1500);

		System.out.println(" ");

		System.out.println("User's turn: ");
		for (int i = 0; i < num_players - 1; i++) {
			System.out.println(users_obj.get(i).get_name()+" press [A] in the Swiftbot to perform dice roll >");

			String choice = input_handler(List.of("A"));
			if (choice.equals("A")) {
				System.out.println(DICE_ASCII_ART);
				System.out.println(Colours.YELLOW + "DICE ROLL..." + Colours.RESET);
				Thread.sleep(500);

				int each_diceroll = users_obj.get(i).get_dice_num();
				System.out.println(users_obj.get(i).get_name()+" rolled a "+ each_diceroll);
				user_roll_results.add(each_diceroll);
				System.out.println(" ");
			}
		}

		//swiftbot roll

		Thread.sleep(1000);
		System.out.println("SwiftBot's turn: ");
		System.out.println(DICE_ASCII_ART);
		System.out.println(Colours.YELLOW + "DICE ROLL..." + Colours.RESET);

		int swiftbot_dice_roll_num;
		while (true) {
			swiftbot_dice_roll_num = swiftbot_obj.get_dice_num();

			if (!user_roll_results.contains(swiftbot_dice_roll_num)) {
				break;
			}
		}

		System.out.println(swiftbot_obj.get_name() + " rolled a " + swiftbot_dice_roll_num);

		//comparing all the users

		int highest_user_roll = 0;
		int highest_user_array_index = 0; //help to continue from that user if we had multiple users
		for (int i = 0; i < user_roll_results.size(); i++) {
			if (highest_user_roll <= user_roll_results.get(i)) {
				highest_user_roll = user_roll_results.get(i);
				highest_user_array_index = i;
			}
		}

		if (highest_user_roll > swiftbot_dice_roll_num) {
			current_player = users_obj.get(highest_user_array_index);
		}
		else {
			current_player = swiftbot_obj;
		}

		System.out.println(current_player.get_name() + " is the starting player!");

	}
	//_________game
	private static void main_game() throws InterruptedException, IOException {

		int current_player_index = players_obj.indexOf(current_player);

		while (!game_over) {
			if (current_player instanceof SwiftBot_class) {
				//player is the swiftbot
				swiftbot_turn();
			}
			else {
				//current player is a user
				user_turn();
			}

			if (current_player.get_pos() == 5 || current_player.get_pos() == 25) {
				quit_handling();
			}

			current_player_index = (current_player_index + 1) % players_obj.size(); //mimics a cyclical structure
			current_player = players_obj.get(current_player_index);
			System.out.println("");
			System.out.println("The current player is: " + current_player.get_name());
		}

	}

	private static void user_turn() throws InterruptedException {
		System.out.println("");
		System.out.println(current_player.get_name()+" press [A] in the Swiftbot to perform dice roll >");

		String choice = input_handler(List.of("A"));

		if (choice.equals("A")) {
			System.out.println(DICE_ASCII_ART);
			System.out.println(Colours.YELLOW +"DICE ROLL..." + Colours.RESET);
			Thread.sleep(500);

			int user_diceroll = current_player.get_dice_num();

			user_movements(user_diceroll);
		}
	}

	private static void user_movements(int user_dice) {
		int user_last_pos = current_player.get_pos();
		int user_new_pos = user_last_pos + user_dice ;

		System.out.println("");
		System.out.println(current_player.get_name() + " rolled a " + user_dice + " and moved from:");
		System.out.println(user_last_pos + " to " + user_new_pos);
		System.out.println("");

		if (user_new_pos > 25) {
			System.out.println("Please get a number which is less than or equal to 25!");
			System.out.println(current_player.get_name() + " returned to position : "+ user_last_pos);
			return;
		}
		else {
			current_player.set_pos(user_new_pos);
		}

		System.out.println(" ");
		snake_checker();
		ladder_checker();
	}

	private static void swiftbot_turn() throws InterruptedException {
		System.out.println(DICE_ASCII_ART);
		System.out.println(Colours.YELLOW+"DICE ROLL..."+Colours.RESET);
		Thread.sleep(500);
		int swiftbot_dice = current_player.get_dice_num();

		System.out.println(current_player.get_name()+" rolled a " + swiftbot_dice);
		int swiftbot_pos = current_player.get_pos();

		if (current_mode.equals("B")) {
			mode_B(swiftbot_pos);
		}
		else {
			int swiftbot_new_pos = swiftbot_pos + swiftbot_dice;
			if (swiftbot_new_pos > 25) {
				System.out.println("Please get a number which is less than or equal to 25!");
				System.out.println(current_player.get_name() + " returned to position : "+ swiftbot_pos);
			}
			else {
				current_player.set_pos(swiftbot_pos + swiftbot_dice);
			}
		}

		snake_checker();
		ladder_checker();
		swiftbot_movements();
	}

	private static void mode_B(int swiftbot_pos) throws InterruptedException {

		System.out.println("Would the user like to overide the dice? (y/n)");

		while (true) {
			try {
				Scanner text = new Scanner(System.in);
				String user_input = text.nextLine();
				if (user_input.equals("y")) {
					System.out.println("Choose amount of steps in numbers: ");

					while (true) {
						int extra_steps = text.nextInt();

						if (extra_steps > 0 && extra_steps <= 5 && current_player.get_pos() + extra_steps <= 25) {
							current_player.set_pos(swiftbot_pos + extra_steps);
							break;
						}
						else {
							error("Input out of boundaries, ensure the input is within boundaries!");
						}
					}
					return;
				}
				else if (user_input.equals("n")) {
					return;
				}

				else {
					error("Unusual input given, please input either y or n!");
				}
			}

			catch (Exception e) {
				error("input is not a Integer, please input a integer!");
			}
		}
	}

	private static void swiftbot_movements() throws InterruptedException {

		if (current_player.get_pos() > swiftbot_physical_pos) {
			pos_changer = 1;
		}

		else {
			swiftBot.move(WHEEL_POWER + WHEEL_CALLIBRATION_SYMETRY, -WHEEL_POWER, TURN_90_TIME); //clockwise
			swiftBot.move(WHEEL_POWER + WHEEL_CALLIBRATION_SYMETRY, -WHEEL_POWER, TURN_90_TIME); //clockwise

			if (SwiftBot_orientation.equals("west")) {
				SwiftBot_orientation = "east";
			}

			else if (SwiftBot_orientation.equals("east")) {
				SwiftBot_orientation = "west";
			}


			pos_changer = -1;
		}

		while (current_player.get_pos() != swiftbot_physical_pos) {
			System.out.println(swiftbot_physical_pos);
			swiftbot_turn_movement();

		}
	}

	private static void swiftbot_turn_movement() throws InterruptedException {
		Thread.sleep(1000);
		int current_row = (swiftbot_physical_pos - 1) / 5;
		System.out.println("current row : " + current_row);
		System.out.println("the cal : " + current_row % 2);


		if (swiftbot_physical_pos % 5 == 0) {
			System.out.println("Test: end of row reached");

			if ((current_row + 1) % 2 == 0) {
				swiftBot.move(WHEEL_POWER + WHEEL_CALLIBRATION_SYMETRY, -WHEEL_POWER, TURN_90_TIME); //clockwise
				System.out.println("turning clockwise");
				SwiftBot_orientation = "north";
				Thread.sleep(500);
				swiftBot.move(WHEEL_POWER + WHEEL_CALLIBRATION_SYMETRY, WHEEL_POWER, FORWARD_TIME);
				System.out.println("going straight");
				Thread.sleep(500);
				swiftBot.move(WHEEL_POWER + WHEEL_CALLIBRATION_SYMETRY, -WHEEL_POWER, TURN_90_TIME);
				System.out.println("turning clockwise");
				SwiftBot_orientation = "east";
				Thread.sleep(500);

				//turn 90 degress clockwise
				//move forward
				//turn 90 degrees
			}
			else {



				swiftBot.move(-(WHEEL_POWER + WHEEL_CALLIBRATION_SYMETRY), WHEEL_POWER, TURN_90_TIME); //anticlockwise
				System.out.println("turning anticlockwise");
				SwiftBot_orientation = "north";
				Thread.sleep(500);
				swiftBot.move(WHEEL_POWER + WHEEL_CALLIBRATION_SYMETRY, WHEEL_POWER, FORWARD_TIME);
				System.out.println("going straight");
				Thread.sleep(500);
				swiftBot.move(-(WHEEL_POWER + WHEEL_CALLIBRATION_SYMETRY), WHEEL_POWER, TURN_90_TIME);
				System.out.println("turning anticlockwise");
				SwiftBot_orientation = "west";
				Thread.sleep(500);


				//turn 90 anticlockwise
				//move forward
				//turn 90 degrees anticlockwise
			}
			swiftbot_physical_pos += pos_changer;
		}
		else {
			//swiftbot move formard
			swiftBot.move(WHEEL_POWER + WHEEL_CALLIBRATION_SYMETRY, WHEEL_POWER, FORWARD_TIME);
			System.out.println("going straight");
			swiftbot_physical_pos += pos_changer;
		}

	}

	private static void snake_checker() {
		for (Snakes each_snake: snakes_obj) {
			each_snake.player_connector_interaction(current_player);
		}
	}


	private static void ladder_checker() {
		for (Ladders each_ladder: ladders_obj) {
			each_ladder.player_connector_interaction(current_player);
		}
	}

	//_________input handling
	private static String input_handler(List<String> possible_inputs) throws InterruptedException {
		swiftBot.fillButtonLights();
		List<String> all_inputs = List.of("A","B","X","Y");

		while (true) {
			String pressed_button = input();

			if (possible_inputs.contains(pressed_button)){ //checks if its one of the required inputs
				swiftBot.disableButtonLights();
				return pressed_button;
			}

			else if (all_inputs.contains(pressed_button)) { //checks if its one of the possible inputs
				error("invalid option selected, please select "+ possible_inputs +" in the SwiftBot.");
			}
		}
	}

	public static String input() throws InterruptedException {

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
			error("Something has occured");
			return "null";
		}
	}

	private static void error(String message) throws InterruptedException {
		System.out.println(Colours.BOLD +Colours.RED + "[Error!] " + message + Colours.RESET);
		swiftBot.fillUnderlights(RED);
		Thread.sleep(200);
		swiftBot.disableUnderlights();
	}
	//________game end handling
	private static void quit_handling() throws IOException, InterruptedException {
		System.out.println("The current position of " + current_player.get_name() + " is " + current_player.get_pos());
		System.out.println("please press [X] in the SwiftBot if you would like to quit");

		String choice;
		if (current_player.get_pos() == 5) {
			choice = input_handler(List.of("X","Y","A","B"));
		}
		else {
			choice = input_handler(List.of("X"));
		}

		if (choice.equals("X")) {
			game_over = true;
			game_termination_logging();
		}
	}

	private static void game_termination_logging() throws IOException, InterruptedException {
		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm.ss");

		String formatted_datetime = myDateObj.format(myFormatObj);
		String fileName = "Snake_ladders_log_" + formatted_datetime;
		File logfile = new File(fileName);
		FileWriter writer = new FileWriter(logfile);

		for (Players each_player : players_obj) {
			writer.write(each_player.get_name() + " pos > " + each_player.get_pos() + "\n");
		}
		writer.write("------------------------------------------\n");

		for (int i = 0; i < snakes_obj.size(); i++) {
			writer.write("Snake " + i + " head: " + snakes_obj.get(i).get_head() + "\n");
			writer.write("Snake " + i + " tail: " + snakes_obj.get(i).get_tail() + "\n");
		}
		writer.write("------------------------------------------\n");

		for (int i = 0; i < ladders_obj.size(); i++) {
			writer.write("Ladder " + i + " head: " + ladders_obj.get(i).get_head()  + "\n");
			writer.write("Ladder " + i + " tail: " + ladders_obj.get(i).get_tail()  + "\n");
		}
		writer.write("------------------------------------------\n");

		writer.write(formatted_datetime);

		Players game_winner = current_player;
		System.out.println(Colours.YELLOW + Colours.BOLD +game_winner.get_name() + " is the winner!" + Colours.RESET);

		System.out.println("file location is : ");
		System.out.println(logfile.getAbsolutePath());
		writer.close();

		if (game_winner instanceof Users) {
			swiftBot.fillUnderlights(BLUE);
			Thread.sleep(300);
		}
		else if (game_winner instanceof SwiftBot_class) {
			swiftBot.fillUnderlights(ORANGE);
			Thread.sleep(500);
		}
		swiftBot.disableUnderlights();

	}


	public static void main(String[] args) throws InterruptedException, IOException {

		menu();
		main_game();
		System.exit(0);
		//
	}
}




