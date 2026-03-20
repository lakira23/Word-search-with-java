//________imports______________
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import swiftbot.Button;
import swiftbot.SwiftBotAPI;


//________CLASSES_______________
class Connectors {
	protected int head;
	protected int tail;

	static String LADDER_ASCII_ART =  ""
			+ "o-o\r\n"
			+ "| |\r\n"
			+ "+-+\r\n"
			+ "| |\r\n"
			+ "+-+\r\n"
			+ "| |\r\n"
			+ "o-o\r\n"
			+ "";

	static String SNAKE_ASCII_ART = ""
			+ ",=e\r\n"
			+ " `-.  \r\n"
			+ "_,-'\r\n"
			+ "";

	static Random random = new Random(); //a random class object

	protected int[] head_boundaries; //min, max
	protected int[] tail_boundaries; //min, max

	public Connectors(ArrayList<Connectors> list_connectors, int[] head_bounds, int[] tail_bounds) {
		this.head_boundaries = head_bounds;
		this.tail_boundaries = tail_bounds;

		assign_pos(list_connectors); //generates the integers for the max and the min of the head and tail

	}
	private static boolean pos_taken(int value, ArrayList<Connectors> list_connectors) {
		//this method validates if the pos which has been generated is already taken
		
		for (Connectors c : list_connectors) { //iterate through all the connectors
			if (c.get_head() == value || c.get_tail() == value) {
				return true;
			}
		}
		return false;
	}

	protected boolean check_head_and_tail_pos(int tail) {
		//a method which validates if the head pos is greater than the tail pos, the oriiginal position is for the snake, polymorphism is used to check for the ladder validation in the ladder class
		return this.head > tail; //default is snake
	}

	private int generate_head(ArrayList<Connectors> list_connectors) {
		//a method which generates the head position
		while (true) {
			head = random.nextInt(head_boundaries[1] - head_boundaries[0] + 1) + head_boundaries[0]; //generates a random integer using boundaries, to ensure teh head position dont take certain rows

			if (!pos_taken(head, list_connectors)) {//continues untill spaces are available
				return head;
			}
		}
	}

	private int generate_tail(ArrayList<Connectors> list_connectors) {
		//a method which generates the tail position
		int headrow = (this.head - 1) / 5;

		while (true) {
			tail = random.nextInt(tail_boundaries[1] - tail_boundaries[0] + 1) + tail_boundaries[0]; //generates a random integer using boundaries, to ensure teh head position dont take certain rows
			int tailrow = (this.tail - 1) / 5;

			if (!pos_taken(this.tail, list_connectors) && (tailrow != headrow) && (check_head_and_tail_pos(tail))){ 
				//continues untill if the pos is not taken, the row aint the same for head and tail, checks if the head is greater than tail or the other way around depending on the connective
				return tail;
			}
		}
	}

	private void assign_pos(ArrayList<Connectors> list_connectors) {
		//assigns the positions to the class variables
		this.head = generate_head(list_connectors);
		this.tail = generate_tail(list_connectors);
	}

	public int get_head() {
		//a get method to get head
		return this.head; 
	}

	public int get_tail() {
		//a get method to get tail
		return this.tail;
	}

	public void player_connector_interaction(Players player,String connector_type) {
		//a method when checks if the player lands on a connector
		
		if (player.get_pos() == this.head) {
			
			if (connector_type.equals("snake")) {
				//checks if the connector type is a snake
				System.out.println(SNAKE_ASCII_ART);
			}

			else if (connector_type.equals("ladder")) {
				//the only other option is ladder
				System.out.println(LADDER_ASCII_ART);
			}

			System.out.println(player.get_name() + " has facen a "+connector_type+" in position "+ this.head );
			System.out.println("went from square " + player.get_pos() + " to " + this.tail);
			System.out.println("***************************************");
			player.set_pos(this.tail); //set the player's position to the connector's end
		}
	}
}

class Snakes extends Connectors {
	public Snakes(ArrayList<Connectors> list_connectors) {
		super(list_connectors, new int[] {6,24},new int[] {2,20} ); //inherit the attributes from "connector's" class with the preloaded boundaries for tail and head
	}

	@Override
	protected boolean check_head_and_tail_pos(int tail) {
		return this.head > tail; //the validation for snakes, and it overrides the method in the parent class
	}

	public void player_connector_interaction(Players player) {
		//inheritance where the parent's class is called with the arguments of "snake"
		super.player_connector_interaction(player,"snake");
	}
}

class Ladders extends Connectors {
	public Ladders(ArrayList<Connectors> list_connectors) {
		//inheritance where the constructor of the parent class is called with the ladder's tail and head boundaries.
		super(list_connectors, new int[] {2,20},new int[] {6,24});
	}

	@Override
	protected boolean check_head_and_tail_pos(int tail) {
		//overide with the ladder's condition where tail must be greater than head
		return tail > this.head ; 
	}

	public void player_connector_interaction(Players player) {
		//inheritance where the parent's class is called with the arguments of "ladder"
		super.player_connector_interaction(player,"ladder");
	}
}

class Players {
	static Random random = new Random();
	protected int pos; //a variable which is open to be changed by the daughter classed
	private String name;

	public Players(String player_name) {
		pos = 1;
		this.name = player_name;
		//assigns the postion and the player name
	}

	public String get_name() {
		//a get method to get the player's name
		return this.name;
	}

	public int get_pos() {
		//a get method to get the player's position
		return this.pos;
	}

	public int get_dice_num() {
		//the dice roll method which randomly returns a number between 1 to 5 inclusively
		int dice_min = 1;
		int dice_max = 6;

		return random.nextInt(dice_max - dice_min + 1) + dice_min;
	}

	public void set_pos(int new_pos) {
		//a set method to set the new position
		this.pos = new_pos;
	}
}

class Users extends Players{
	public Users(String player_name){
		//user class which inherits the attributes from the parent class 
		super(player_name);
	}

}

class SwiftBot_class extends Players{
	public SwiftBot_class(String player_name) {
		//swiftbot class which inherits the attributes from the parent class
		super(player_name);
	}

}

class Colours {
	//ANSI colour codes for a engaging UI design
	public static final String RESET = "\u001B[0m";
	
	public static final String RED = "\u001B[31m";
	public static final String GREEN = "\u001B[32m";
	public static final String YELLOW = "\u001B[33m";
	public static final String BLUE = "\u001B[34m";
	public static final String CYAN = "\u001B[36m";
	public static final String BLACK = "\u001B[30m";
    public static final String PURPLE = "\u001B[35m";
    public static final String WHITE = "\u001B[37m";
    public static final String ORANGE = "\u001B[38;5;208m";
    public static final String PINK = "\u001B[38;5;213m";
    public static final String TEAL = "\u001B[38;5;37m";
    public static final String LIME = "\u001B[38;5;118m";
    public static final String GOLD = "\u001B[38;5;220m";
    public static final String SKY_BLUE = "\u001B[38;5;117m";
    
    public static final String BRIGHT_BLACK = "\u001B[90m";
    public static final String BRIGHT_RED = "\u001B[91m";
    public static final String BRIGHT_GREEN = "\u001B[92m";
    public static final String BRIGHT_YELLOW = "\u001B[93m";
    public static final String BRIGHT_BLUE = "\u001B[94m";
    public static final String BRIGHT_PURPLE = "\u001B[95m";
    public static final String BRIGHT_CYAN = "\u001B[96m";
    public static final String BRIGHT_WHITE = "\u001B[97m";
	
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

	static int time_straight = 920;
	static int time_turn = 525;

	static int wheel_offset = -5;
	static String SwiftBot_orientation = "East";

	static boolean game_over = false;

	static String DICE_ASCII_ART = "  "
			+ "____\r\n"
			+ " /\\' .\\    _____\r\n"
			+ "/: \\___\\  / .  /\\\r\n"
			+ "\\' / . / /____/..\\\r\n"
			+ " \\/___/  \\'  '\\  /\r\n"
			+ "          \\'__'\\/\r\n";

	static String FIREWORKS_ASCII_ART = 
			"    .              .   .'.     \\   /\r\n"
					+ "  \\   /      .'. .' '.'   '  -=  o  =-\r\n"
					+ "-=  o  =-  .'   '              /   \\\r\n"
					+ "  /   \\                          '\r\n"
					+ "    '\r\n"
					+ "";

	static SwiftBotAPI swiftBot = SwiftBotAPI.INSTANCE;

	static int [][] board = {	
			{21,22,23,24,25},
			{20,19,18,17,16},
			{11,12,13,14,15},
			{10,9,8,7,6},
			{1,2,3,4,5}}; 

	static int[] BLUE = {0,0,255};
	static int[] RED = {255,0,0};
	static int[] ORANGE = {255,165,0};

	//___________menu

	private static void menu() throws InterruptedException {
		//menu screen
		System.out.println(
				Colours.BRIGHT_CYAN + Colours.BOLD +
				"====================================\n" +
				"     SWIFTBOT SNAKES AND LADDERS    \n" +
				"===================================="
				+ Colours.RESET);

		System.out.println("Press [Y] in the SwiftBot to start the game! ");

		//calls upon a method which handles swiftbot inputs
		String choice = input_handler(List.of("Y"));
		
		if (choice.equals("Y")) {
			//if only the button pressed is "Y", the user is welcomed to the game
			System.out.println("");
			System.out.println(Colours.BOLD + Colours.CYAN +"Welcome to Snakes and Ladders!" + Colours.RESET);
		}
		else {
			//produces a error with a pre-written message
			error("invalid option selected, please select [Y]");
		}

		//set up the players
		player_setup();
		
		//set up the connectors and the board
		Board_setup();
		
		//get the mode for the playthrough selected
		Mode_selection();
		
		//a subroutine which decides who starts the game
		Decide_start_player();



	}

	private static void player_setup() throws InterruptedException {
		//generated the players
		Scanner text = new Scanner(System.in);

		//makes the users
		for (int i = 0; i < num_players - 1; i++) { //-1 cos to not include the swiftbot, easily scalable due to the use of a for-loop
			
			while (true) {
				//stuck the user until a correct name is given
				System.out.println("Please enter your name player "+ (i+1) +" >> ");
				//get input
				String username = text.nextLine();
				
				if (username.strip().equals("")) {
					//removes all white spacing in the username and see if its empty
					error("Player name cannot be blank, please enter a name");
				}
				else {
					//initiates a new object for the user player with the username
					Users a_user = new Users(username);

					//appends the user object to the arrays of users and players
					users_obj.add(a_user);
					players_obj.add(a_user);

					System.out.println(" ");	
					break;
				}
			}
		}
		
		System.out.println("The SwiftBot has been assigned the following name:");

		//makes the swiftbot
		swiftbot_obj = new SwiftBot_class("SwiftBot"); //initiates a new swiftbot object using the class with the username "SwiftBot"
		
		//adds the Swiftbot object to the player class
		players_obj.add(swiftbot_obj);
		
		System.out.println("> " + swiftbot_obj.get_name());
		Thread.sleep(1000);

	}

	private static void Board_setup() throws InterruptedException {
		//set up the board
		
		//variables with the number of connectors, can be easily changed to scale up
		int num_of_snakes = 2;
		int num_of_ladders = 2;

		//creates the Arraylists for the snakes, ladders and connectors. So that their objects can be stored
		snakes_obj = new ArrayList<Snakes>();
		ladders_obj = new ArrayList<Ladders>();
		connectors_obj = new ArrayList<Connectors>();


		//display the board
		System.out.println("");
		System.out.println(Colours.BOLD + Colours.CYAN +"The board will look like the following : " + Colours.RESET);
		
		//displays the layout of the board
		for (int[] each_row : board) {
			System.out.println(Arrays.toString(each_row));
		}
		System.out.println("");
		Thread.sleep(500);
		System.out.println("");

		//makes the snakes
		try { 
			System.out.println("");
			System.out.println(Colours.BOLD + Colours.YELLOW+ "Snakes: " + Colours.RESET);
			
			for (int i = 0; i < num_of_snakes; i++) {
				//creates a new instance of a snake and adds it to the Array lists as a object
				Snakes a_snake = new Snakes(connectors_obj);
				snakes_obj.add(a_snake);
				connectors_obj.add(a_snake);
				//outputs the location of the snake
				System.out.println("> Snake "+(i + 1)+" - Head Square " + a_snake.get_head() + "--> Tail Square " + a_snake.get_tail());
			}
			System.out.println("");
			Thread.sleep(1000);

			//makes the ladders
			System.out.println(Colours.BOLD + Colours.GREEN + "Ladders: " + Colours.RESET);

			for (int i = 0; i < num_of_ladders; i++) {
				//creates a new instance of a ladder using the constructor and appends it to the Array lists as objects
				Ladders a_ladder = new Ladders(connectors_obj);
				ladders_obj.add(a_ladder);
				connectors_obj.add(a_ladder);
				//outputs the location of the ladder
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
		//select the mode for the game mode
		System.out.println("Select a game mode by pressing the buttons: ");
		System.out.println("-----------------------------------------------------------------");
		System.out.println("[A] Mode A - Normal Gameplay");
		System.out.println("[B] Mode B - User power to manually override SwiftBot movements");
		System.out.println("");
		System.out.println("Please press the buttons in the swiftBot > ");

		//handles the input and the required input are either A or B in the swiftbot
		String choice = input_handler(List.of("A","B"));
		
		//selection depending on which button was pressed
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
		//a method which decides the starting player
		
		//creates a new arraylist which is used to store the dice rolls inside so that the dice roll of the user wouldn't be the same as the Swiftbots
		ArrayList<Integer> user_roll_results = new ArrayList<Integer>();

		System.out.println("");
		System.out.println("Deciding starting player! :-");
		Thread.sleep(1500);

		System.out.println(" ");

		System.out.println("User's turn: ");
		for (int i = 0; i < num_players - 1; i++) {
			//perform dice rolls for the users
			System.out.println(Colours.BLUE +users_obj.get(i).get_name()+" press [A] in the Swiftbot to roll the dice >" + Colours.RESET);
			
			//Swiftbot input for A
			String choice = input_handler(List.of("A"));
			if (choice.equals("A")) {
				//performs dicerolls if A been pressed
				System.out.println(DICE_ASCII_ART);
				System.out.println(Colours.YELLOW + "DICE ROLL..." + Colours.RESET);
				Thread.sleep(500);
				
				//calls upon the dice roll method in the object stored inside the user_obj array list 
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
		System.out.println(Colours.BRIGHT_YELLOW + "DICE ROLL..." + Colours.RESET);

		int swiftbot_dice_roll_num;
		while (true) {
			//calls the dice roll method in the swiftbot class
			//continues untill the rolled results are not the same as any user's roll
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
		//selection depending on who rolls the highest
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
			//continues until the game is not finished
			
			if (current_player instanceof SwiftBot_class) {
				//player is the swiftbot
				swiftbot_turn();
			}
			else {
				//current player is a user
				user_turn();
			}

			if (current_player.get_pos() == 5) {
				quit_handling();
				//checks if the current position is 5 then calls the quit handling method to handle the scenario if the user want to quit or not
			}

			if (current_player.get_pos() == 25) {
				//calls the method to handle logging progress and calling the winner
				game_termination_logging();
				break;
			}

			//gets the current player, the +1 is for the next player
			current_player_index = (current_player_index + 1) % players_obj.size(); //mimics a cyclical structure
			current_player = players_obj.get(current_player_index);
			System.out.println("");
			System.out.println("The current player is: " + current_player.get_name());
		}

	}

	private static void user_turn() throws InterruptedException {
		System.out.println("");
		
		//gets the user to do the diceroll
		System.out.println(Colours.BLUE + current_player.get_name()+" press [A] in the Swiftbot to perform dice roll >" + Colours.RESET);

		String choice = input_handler(List.of("A"));

		if (choice.equals("A")) {
			//if the user input is A then perform the diceroll
			System.out.println(DICE_ASCII_ART);
			System.out.println(Colours.BRIGHT_YELLOW +"DICE ROLL..." + Colours.RESET);
			Thread.sleep(500);

			int user_diceroll = current_player.get_dice_num();
			//performs the user movements with the dice_roll
			user_movements(user_diceroll);
		}
	}

	private static void user_movements(int user_dice) {
		//handles the user movements
		
		int user_last_pos = current_player.get_pos();
		int user_new_pos = user_last_pos + user_dice ;
		
		//outputs changes done to the positon
		System.out.println("");
		System.out.println(current_player.get_name() + " rolled a " + user_dice + " and moved from:");
		System.out.println(user_last_pos + " to " + user_new_pos);
		System.out.println("");

		if (user_new_pos > 25) {
			//bounds the user to the board, the position of the user is not updated if exceeds 25
			System.out.println("Please get a number which is less than or equal to 25!");
			System.out.println(current_player.get_name() + " returned to position : "+ user_last_pos);
			return;
		}
		else {
			//calls upon the class method to update the position
			current_player.set_pos(user_new_pos);
		}

		System.out.println(" ");
		//checks the snakes and ladders in the game
		snake_checker();
		ladder_checker();
	}

	private static void swiftbot_turn() throws InterruptedException {
		//manages swiftbot's turn
		
		System.out.println(DICE_ASCII_ART);
		System.out.println(Colours.BRIGHT_YELLOW+"DICE ROLL..."+Colours.RESET);
		Thread.sleep(500);
		//performs a dice roll
		int swiftbot_dice = current_player.get_dice_num();

		//output of the results of the diceroll
		System.out.println(current_player.get_name()+" rolled a " + swiftbot_dice);
		int swiftbot_pos = current_player.get_pos();

		if (current_mode.equals("B")) {
			//calls the method mode b to manage that mode if its that mode
			mode_B(swiftbot_pos,swiftbot_dice);
		}
		else {
			//if its mode A
			//find the new position and checks if exceed 25
			int swiftbot_new_pos = swiftbot_pos + swiftbot_dice;
			if (swiftbot_new_pos > 25) {
				//if the position goes out of bounds, then the user is notified of the game rule and sent back
				System.out.println("Please get a number which is less than or equal to 25!");
				System.out.println(current_player.get_name() + " returned to position : "+ swiftbot_pos);
			}
			else {
				current_player.set_pos(swiftbot_pos + swiftbot_dice);
			}

		}

		//output the updates to the swiftbot movements done by the dice roll
		System.out.println("");
		System.out.println("The SwiftBot moved from:");
		System.out.println(swiftbot_pos + " to " + current_player.get_pos());
		System.out.println("");

		
		//performs the physical movement of the swiftbot
		swiftbot_movements();
		
		//checks if the swiftbot interacts with either a snake or ladder
		snake_checker();
		ladder_checker();
		
		swiftbot_movements(); //re-check again if the robot has been moved by the snakes or ladders
		
		
	}

	private static void mode_B(int swiftbot_pos,int swiftbot_dice) throws InterruptedException {
		//asks if the user would like to overide the dice roll, gets input
		System.out.println("Would the user like to overide the dice? please type (y/n)");
		Scanner text = new Scanner(System.in);
		
		while (true) {
			//while loop for validation and give a sense of loop
			try {
				String user_input = text.nextLine();
				
				if (user_input.equals("y")) {
					//if the input entered by the user is "y"
					System.out.println("Enter a number between 1 and 5 >>: ");

					while (true) {
						//validates the input dice roll and sets the pos if its correct
						int extra_steps = Integer.parseInt(text.nextLine());

						if (extra_steps > 0 && extra_steps <= 5 && current_player.get_pos() + extra_steps <= 25) {
							current_player.set_pos(swiftbot_pos + extra_steps);
							break;
						}
						else {
							//error handling if the given input is out of bounds
							error("Input out of boundaries, ensure the input is within boundaries!");
						}
					}
					return;
				}
				else if (user_input.equals("n")) {
					//if the user wish to continue without obstructing
					int swiftbot_new_pos = swiftbot_pos + swiftbot_dice;
					if (swiftbot_new_pos > 25) {
						//boundary handling
						System.out.println("Please get a number which is less than or equal to 25!");
						System.out.println(current_player.get_name() + " returned to position : "+ swiftbot_pos);
					}
					else {
						//sets the new position of the current player
						current_player.set_pos(swiftbot_pos + swiftbot_dice);
					}
					return;
				}

				else {
					//error message if the invalid input is gievn
					error("Invalid input given, please input either y or n!");
				}
			}

			catch (Exception e) {
				error("input is not a Integer, please input a integer!");
				text.nextLine();
			}
		}
	}

	private static void swiftbot_movements() throws InterruptedException {
		//a method which manages the physical movements of the swiftbot
		if (current_player.get_pos() >= swiftbot_physical_pos) {
			//if the new position is of higher than the current one
			pos_changer = 1;
		}

		else {
			//if the swiftbot has to move backwards
			//turn the swiftbot 180
			Thread.sleep(1000);
			swiftbot_turn_left();
			Thread.sleep(1000);
			swiftbot_turn_left();
			
			//change oreintation of the swiftbot
			if (SwiftBot_orientation.equals("west")) {
				SwiftBot_orientation = "east";
			}

			else if (SwiftBot_orientation.equals("east")) {
				SwiftBot_orientation = "west";
			}

			pos_changer = -1;
		}

		while (current_player.get_pos() != swiftbot_physical_pos) {
			//continuesly call to move the bot untill the current posion is equal to the required position
			swiftbot_turn_movement();

		}
		
		if (pos_changer == -1) {
			//turn the swiftbot back to its original position when if done backtracking
			
			//do a 180
			Thread.sleep(1000);
			swiftbot_turn_left();
			Thread.sleep(1000);
			swiftbot_turn_left();
			pos_changer = 1;

			//change orientation
			if (SwiftBot_orientation.equals("west")) {
				SwiftBot_orientation = "east";
			}
			else if (SwiftBot_orientation.equals("east")) {
				SwiftBot_orientation = "west";
			}
		}
	}

	private static void swiftbot_turn_movement() throws InterruptedException {
		//the method which handles mathematics behind the turning and logic
		Thread.sleep(1000);
		
		//gets the current row
		int current_row = (swiftbot_physical_pos - 1) / 5;


		if (swiftbot_physical_pos % 5 == 0 && pos_changer == 1) { //if end of the row normal scenario
			
			if ((current_row + 1) % 2 == 0) {
				swiftbot_turn_clockwise();
				//turn 90 degress clockwise
				//move forward
				//turn 90 degrees
			}
			else {
				swiftbot_turn_anticlockwise();
				//turn 90 anticlockwise
				//move forward
				//turn 90 degrees anticlockwise
			}
			swiftbot_physical_pos += pos_changer;
		}
		
		else if ((swiftbot_physical_pos -1) % 5 == 0 && pos_changer == -1) {
			//if the end of the row has been met and in the going back position
			
			//if the row is 1 or 3 or 5
			if ((current_row + 1) % 2 == 0) {
				swiftbot_turn_clockwise();
			}
			else {
				//if the row is 2 or 4 
				swiftbot_turn_anticlockwise();
			}
			//change the physical position of the pos
			swiftbot_physical_pos += pos_changer;

		}
		else {
			//swiftbot move formard
			swiftbot_move_straight();
			swiftbot_physical_pos += pos_changer;
		}

	}

	private static void swiftbot_move_straight() throws InterruptedException {
		swiftBot.move(WHEEL_POWER + wheel_offset, WHEEL_POWER, time_straight);
		Thread.sleep(40);
	}

	private static void swiftbot_turn_clockwise() throws InterruptedException {
		swiftbot_turn_right();
		SwiftBot_orientation = "north";

		Thread.sleep(400);
		swiftbot_move_straight();

		Thread.sleep(400);
		swiftbot_turn_right();
		SwiftBot_orientation = "east";
		Thread.sleep(400);
	}

	private static void swiftbot_turn_anticlockwise() throws InterruptedException {
		swiftbot_turn_left();
		SwiftBot_orientation = "north";
		Thread.sleep(400);

		swiftbot_move_straight();

		Thread.sleep(400);
		swiftbot_turn_left();
		
		SwiftBot_orientation = "west";
		Thread.sleep(400);
	}

	private static void swiftbot_turn_left() throws InterruptedException {
			swiftBot.move(-(WHEEL_POWER + wheel_offset), WHEEL_POWER, time_turn);
			Thread.sleep(40);
	}

	private static void swiftbot_turn_right() throws InterruptedException {
			swiftBot.move(WHEEL_POWER + wheel_offset, -WHEEL_POWER, time_turn);
			Thread.sleep(40);
	}

	private static void snake_checker() {
		//checks if the player is interacting with the snake
		for (Snakes each_snake: snakes_obj) {
			each_snake.player_connector_interaction(current_player);
		}
	}


	private static void ladder_checker() {
		//checks if the player is interacting with the ladder
		for (Ladders each_ladder: ladders_obj) {
			each_ladder.player_connector_interaction(current_player);
		}
	}

	//_________input handling
	private static String input_handler(List<String> possible_inputs) throws InterruptedException {
		//a private method which handles inputs
		
		swiftBot.fillButtonLights(); //lights up all the button lights
		List<String> all_inputs = List.of("A","B","X","Y"); //all the possible inputs in the swiftbot

		while (true) {
			//stuck the user untill the required button is pressed
			//calls upon a method which gets which buttons has been pressed
			String pressed_button = input();

			if (possible_inputs.contains(pressed_button)){ //checks if its one of the required inputs
				swiftBot.disableButtonLights();
				return pressed_button;
			}

			else if (all_inputs.contains(pressed_button)) { //checks if its one of the possible inputs
				//calls upon the error subroutine with the following message
				error("invalid option selected, please select "+ possible_inputs +" in the SwiftBot and try again!");
			}
		}
	}

	public static String input() throws InterruptedException {
		//similar to Dr Zear's DoesMYSwiftBot work file

		pressed_button = "";
		try {
			//first disabled all the buttons
			swiftBot.disableButton(Button.A);
			swiftBot.disableButton(Button.B);
			swiftBot.disableButton(Button.X);
			swiftBot.disableButton(Button.Y);

			//have procedures which print up which button has been pressed
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
		//a method which handles the output of error messages, to follow a standard
		System.out.println(Colours.BOLD +Colours.BRIGHT_RED + "[Error!] " + message + Colours.RESET);
		
		//fill the underlights red
		swiftBot.fillUnderlights(RED);
		Thread.sleep(200);
		swiftBot.disableUnderlights();
	}
	//________game end handling
	private static void quit_handling() throws IOException, InterruptedException {
		//a method which handles if the user want to finish the game
		
		System.out.println("The current position of " + current_player.get_name() + " is " + current_player.get_pos());
		System.out.println(Colours.ORANGE+"please press [X] in the SwiftBot if you would like to quit" + Colours.RESET);

		String choice;
		if (current_player.get_pos() == 5) {
			//if the current position is 5, then the option to quit is given
			System.out.println("If you would like to continue the game, pleae press either [Y], [A] or [B]");
			choice = input_handler(List.of("X","Y","A","B"));
		}
		else {
			//since this method is else called when position is 25, which would only require the input of "X"
			choice = input_handler(List.of("X"));
		}

		if (choice.equals("X")) {
			//if the user press x, the game ends even if the pos is 25 or 5
			
			//turns the game looping flag off
			game_over = true;
			
			//calls upon the method to finish the game and log the game results
			game_termination_logging();
		}
	}

	private static void game_termination_logging() throws IOException, InterruptedException {
		//handles logging the progress of the game
		
		//Gets the date and time using the Localdatetime library
		LocalDateTime myDateObj = LocalDateTime.now();
		//produces a template to format the date and time
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm.ss");

		//formats the time and date using the template
		String formatted_datetime = myDateObj.format(myFormatObj);
		
		//creates the file name using the date to avoid duplicacy.
		String fileName = "Snake_ladders_log_" + formatted_datetime;
		//initiate a new log file object with the file name
		File logfile = new File(fileName);
		//initiate a new object using the logfile, which enable writing
		FileWriter writer = new FileWriter(logfile);

		//writing the position of each player in the Array list
		for (Players each_player : players_obj) {
			writer.write(each_player.get_name() + " pos > " + each_player.get_pos() + "\n");
		}
		writer.write("------------------------------------------\n");

		//writing the positions of all the snakes
		for (int i = 0; i < snakes_obj.size(); i++) {
			writer.write("Snake " + (i + 1) + " head: " + snakes_obj.get(i).get_head() + "\n");
			writer.write("Snake " + (i + 1) + " tail: " + snakes_obj.get(i).get_tail() + "\n");
		}
		writer.write("------------------------------------------\n");

		//writing the positions of all the ladders
		for (int i = 0; i < ladders_obj.size(); i++) {
			writer.write("Ladder " + (i + 1) + " head: " + ladders_obj.get(i).get_head()  + "\n");
			writer.write("Ladder " + (i + 1) + " tail: " + ladders_obj.get(i).get_tail()  + "\n");
		}
		writer.write("------------------------------------------\n");

		//writing the date and time in the format at the end
		writer.write(formatted_datetime);

		//outputs the winner
		Players game_winner = current_player;
		System.out.println(Colours.BRIGHT_PURPLE + Colours.BOLD +game_winner.get_name() + " is the winner!" + Colours.RESET);

		System.out.println(FIREWORKS_ASCII_ART);
		System.out.println("file location is : ");
		System.out.println(logfile.getAbsolutePath());
		writer.close();

		if (game_winner instanceof Users) {
			//a swiftbot celebration if the winner is user
			swiftBot.fillUnderlights(BLUE);
			Thread.sleep(300);
		}
		else if (game_winner instanceof SwiftBot_class) {
			//a swiftbot celebration if the winner is the swiftbot
			swiftBot.fillUnderlights(ORANGE);
			Thread.sleep(500);
		}
		swiftBot.disableUnderlights();

	}

	//_______calibration
	private static  void calibration() throws InterruptedException {
		//a method used to calibrate the SwiftBot during the VIVA exam so that the Bot moves accordingly to the floor material
		
		while (true) {
			//this shall continue untill the correct timing is comfirmed by the user
			try {
				//try used for if any error occur

				System.out.println("");
				System.out.println(Colours.CYAN + Colours.BOLD + "Lets start by calibrating the Swiftbot's movemets! " + Colours.RESET);
				System.out.println(" ");

				//perform the calibration on the vehical wheel imbalance
				calibrate_motor_offset();

				//perform the calibration for the 25 cm
				calibrate_straightline();

				//perform the calibration for the turns
				calibrate_turns();

				//highlights that the calibration is done with the final results
				System.out.println(Colours.BOLD + Colours.YELLOW + "calibration is done!" + Colours.RESET);
				System.out.println(" ");

				System.out.println(Colours.BOLD + "Final calibrated settings > " + Colours.RESET);
				System.out.println("Offset: " + wheel_offset);
				System.out.println("Straight time: " + time_straight);
				System.out.println("Turn time: " + time_turn);

				break;
			}

			catch (Exception e) {
				error(e + ".Please redo it!");
			}
		}
	}
	private static void calibrate_motor_offset() throws InterruptedException {
		while (true) {
			//continues untill comfirmed
			try {
				System.out.println(Colours.BOLD + Colours.GREEN +"Calibrating motor offset" + Colours.RESET);

				//gets input from the user 
				Scanner input = new Scanner(System.in);
				System.out.println("press anybutton to start");
				String temp = input.nextLine();

				System.out.println("current motor offset : " + wheel_offset);
				
				//moves the robot straight with the current variables values
				swiftbot_move_straight();
				
				//allows new input for the variables
				System.out.println("enter the new motor offset for the straight line or enter 0 to comfirm: ");
				int user_input = input.nextInt();

				//selection if the user required to finish this calibration or continue
				if (user_input == 0) {
					System.out.println("The final motor offset is : " + wheel_offset);
					return;
				}

				else if (WHEEL_POWER + user_input > 100) {
					error("Max power is 100, please enter a lower offset");

				}
				else {
					wheel_offset = user_input;
				}
			}

			catch (Exception e) {
				error("Invalid data type used, please enter a integer!");
			}
		}
	}

	private static void calibrate_straightline() throws InterruptedException {
		//calibration for straight line
		while (true) {
			try {
				//continues untill required by the user
				System.out.println(Colours.BOLD + Colours.GREEN +"Calibrating straight line" + Colours.RESET);

				//gets input
				Scanner input = new Scanner(System.in);
				System.out.println("press any key to start");
				String temp = input.nextLine();


				//outputs original variable values
				System.out.println("current time : " + time_straight);

				//moves the SwiftBot straight
				swiftbot_move_straight();

				System.out.println("enter the new time for the straight line or enter 0 to comfirm: ");
				//input capture
				int user_input = input.nextInt();
				
				//selection to check if the calibration is to be comfirmed
				if (user_input == 0) {
					System.out.println("The final pulse is : " + time_straight);
					return;

				}

				else if (user_input < 0) {
					error("entered number is invalid, please enter a number greater than 0");

				}

				else if (user_input > 1000) {
					//thought 1000 was the upperbound and it wouldn't take 1000 miliseconds for the bot to go 25 cm
					error("entered number is invalid, please enter a number less than 60");

				}
				else {
					time_straight = user_input;
				}
			}

			catch (Exception e) {
				error("Invalid data type used, please enter a integer!");
			}
		}
	}

	private static void calibrate_turns() throws InterruptedException {
		//calibrate turning straight
		while (true) {
			//continues untill required by the user
			try {
				System.out.println(" ");
				System.out.println(Colours.BOLD+Colours.GREEN +"Calibrating turn" + Colours.RESET);
				
				//input capture
				Scanner input = new Scanner(System.in);
				System.out.println("press any key to start");
				String temp = input.nextLine();


				System.out.println("current time : " + time_turn);

				swiftbot_turn_right();

				System.out.println("enter the new time for the turn or enter 0 to comfirm: ");
				int user_input = input.nextInt();

				//selection to get comfirmation and end this calibration
				if (user_input == 0) {
					System.out.println("The final pulse is : " + time_turn);
					return;
				}

				else if (user_input > 1000) {
					//Assumed 1000 is the upper bound for any material
					error("entered number is invalid, please enter a number greater than 0");

				}
				else if (user_input < 0) {
					error("entered number is invalid, please enter a number greater than 60");
				}

				else {
					time_turn = user_input;
				}
			}

			catch (Exception e) {
				error("Invalid data type used, please enter a integer!");
			}
		}
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		//calibrate the swiftbot for the floor material
		calibration();
		
		//the menu 
		menu();
		
		//starting the main game
		main_game();
		
		//system finish and exits
		System.exit(0);
		
	}
}




